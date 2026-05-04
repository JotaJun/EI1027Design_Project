package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.ContractComparator;
import es.uji.ei1027.SgOviProject.dao.ContractDao;
import es.uji.ei1027.SgOviProject.model.Contract;
import es.uji.ei1027.SgOviProject.model.OviUser;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/contract")
public class ContractController {

    @Value("${upload.directorio.contratos}")
    private String uploadDirectory;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private CandidacyService candidacyService;

    @GetMapping("/add/{idCandidacy}")
    public String showContractForm(Model model,
                                   HttpSession session,
                                   @PathVariable int idCandidacy) {
        // Comprobar que la idCandidacy corresponde al usuario loggeado
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
        if(! candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser))
            return "redirect:/oviUser/main";

        Contract contract = new Contract();
        contract.setIdCandidacy(idCandidacy);
        model.addAttribute("contract", contract);
        return "contract/add";
    }

    @PostMapping("/add")
    public String processAddContractForm(Model model,
                                         @ModelAttribute("contract") Contract contract,
                                         BindingResult bindingResult,
                                         HttpSession session,
                                         @RequestParam("ficheroPdf") MultipartFile ficheroPdf) {
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
        if(!candidacyService.isCandidacyFromOviUser(contract.getIdCandidacy(), currentUser)) {
            return "redirect:/oviUser/main";
        }

        ContractValidator contractValidator = new ContractValidator();
        contractValidator.validate(contract, bindingResult);

        if (ficheroPdf == null || ficheroPdf.isEmpty()) {
            bindingResult.rejectValue("urlDocument", "required", "Has d'adjuntar el contracte en format PDF");
        } else if (!ficheroPdf.getContentType().equals("application/pdf")) {
            bindingResult.rejectValue("urlDocument", "format", "El document ha de ser un PDF");
        }

        if (bindingResult.hasErrors()) {
            // Por motivos de seguridad este campo si falla algo se limpia, mejor avisar al usuario
            model.addAttribute("avisoPdf", "S'han trobat errors en el formulari. Si us plau, recorda tornar a adjuntar el document PDF.");
            return "contract/add";
        }

        // GUARDAR EL FICHERO EN EL DISCO
        if (ficheroPdf != null && !ficheroPdf.isEmpty()) {
            try {
                String originalFilename = ficheroPdf.getOriginalFilename();

                // Limpiamos la ruta de caracteres extraños o saltos de directorio
                String cleanFilename = StringUtils.cleanPath(originalFilename);

                // milisegundos para hacer nombre único y evitar sobreescribir
                String nombreArchivo = System.currentTimeMillis() + "_" + cleanFilename;

                // Construimos la ruta completa donde se va a guardar
                Path rutaDestino = Paths.get(uploadDirectory, nombreArchivo);

                // Si no existe directorio, lo creamos
                if (!Files.exists(rutaDestino.getParent())) {
                    Files.createDirectories(rutaDestino.getParent());
                }

                // Copiamos el archivo al disco duro.
                // REPLACE_EXISTING hace que si subes otro con el mismo nombre, lo sobreescriba (no debería pasar por el nombre único pero por seguridad)
                Files.copy(ficheroPdf.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

                // Guardamos en el objeto Contract solo la ruta que necesitamos para la web
                contract.setUrlDocument("contracts/" + nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace();
                // Si falla la escritura en disco, le devolvemos un error al usuario
                bindingResult.rejectValue("urlDocument", "error", "S'ha produït un error al guardar el fitxer");
                return "contract/add";
            }
        }

        contractDao.addContract(contract);

        // Cambiar la candidatura de estado
        candidacyService.contractDone(contract);

        return "redirect:/contract/list/" + contract.getIdCandidacy() + "?nou=" + contract.getIdContract();
    }

    // Número de contratos que queremos mostrar por página
    private int pageLength = 5;

    @GetMapping("/list/{idCandidacy}")
    public String listContracts(Model model,
                                @PathVariable int idCandidacy,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("nou") Optional<Integer> nou,
                                HttpSession session) {

        // Seguridad
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
        if (!candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser)) {
            return "redirect:/oviUser/main";
        }

        List<Contract> contracts = contractDao.getContractsByIdCandidacy(idCandidacy);

        contracts.sort(new ContractComparator());

        // Crear la lista paginada
        ArrayList<ArrayList<Contract>> contractsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= contracts.size()) {
            contractsPaged.add(new ArrayList<>(contracts.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < contracts.size()) {
            contractsPaged.add(new ArrayList<>(contracts.subList(ini, contracts.size())));
        }

        // Crear la lista de números de página para la barra de navegación
        int totalPages = contractsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        int currentPage = page.orElse(0);
        Integer nouContracte = nou.orElse(-1);

        // PASAR DATOS AL MODELO
        model.addAttribute("contractsPaged", contractsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalContracts", contracts.size());
        model.addAttribute("pageLength", pageLength);
        model.addAttribute("idCandidacy", idCandidacy);
        model.addAttribute("nou", nouContracte);

        // Guardar URL exacta para el botón de volver (útil cuando vayamos al details del contrato)
        String exactUrl = "/contract/list/" + idCandidacy + "?page=" + currentPage;
        session.setAttribute("lastContractListUrl", exactUrl);

        return "contract/list";
    }

    @GetMapping("/details/{idContract}")
    public String showContractDetails(Model model,
                                      HttpSession session,
                                      @PathVariable int idContract) {

        Contract contract = contractDao.getContract(idContract);

        if (contract == null) {
            return "redirect:/oviUser/main";
        }

        // Comprobar que el contrato pertenece a una candidatura del usuario loggeado
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
        if (!candidacyService.isCandidacyFromOviUser(contract.getIdCandidacy(), currentUser)) {
            return "redirect:/oviUser/main";
        }

        // 3. Recuperar la URL exacta de la lista (con su paginación) para el botón de volver
        String backUrl = (String) session.getAttribute("lastContractListUrl");
        // Si por algún motivo se entra directamente y no hay variable de sesión, ponemos manualmente para evitar errores
        if (backUrl == null) {
            backUrl = "/contract/list/" + contract.getIdCandidacy();
        }

        model.addAttribute("contract", contract);
        model.addAttribute("backUrl", backUrl);

        return "contract/details";
    }


    @RequestMapping("/done")
    public String contractDone() {
        return "contract/done";
    }


}
