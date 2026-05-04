package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.ContractDao;
import es.uji.ei1027.SgOviProject.model.Contract;
import es.uji.ei1027.SgOviProject.model.OviUser;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            return "contract/add";
        }

        // GUARDAR EL FICHERO EN EL DISCO
        if (ficheroPdf != null && !ficheroPdf.isEmpty()) {
            try {
                // milisegundos para hacer nombre único y evitar sobreescribir + Obtenemos el nombre original del archivo
                String nombreArchivo = System.currentTimeMillis() + "_" + ficheroPdf.getOriginalFilename();

                // Construimos la ruta completa donde se va a guardar
                Path rutaDestino = Paths.get(uploadDirectory + nombreArchivo);

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

        return "redirect:/contract/done";
    }


    @RequestMapping("/done")
    public String contractDone() {
        return "contract/done";
    }


}
