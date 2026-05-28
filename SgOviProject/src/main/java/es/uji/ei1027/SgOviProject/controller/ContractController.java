package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.ContractComparator;
import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.ContractDao;
import es.uji.ei1027.SgOviProject.dao.OviUserDao;
import es.uji.ei1027.SgOviProject.dto.ContractListAllDTO;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
import es.uji.ei1027.SgOviProject.filters.WardStatusFilter;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import es.uji.ei1027.SgOviProject.services.ContractService;
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

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    private OviUserDao oviUserDao;

    @GetMapping("/add/{idCandidacy}")
    public String showContractForm(Model model,
                                   HttpSession session,
                                   @PathVariable int idCandidacy) {

        // Comprobar que la idCandidacy corresponde al usuario loggeado o a su tutor
        AccountType role = AccountType.valueOf((String) session.getAttribute("userRole"));
        if(role==AccountType.LEGALGUARDIAN) {
            LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");
            if(! candidacyService.isCandidacyFromWard(idCandidacy, currentUser)) return "redirect:/legalGuardian/main";
        } else {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            if(! candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser)) return "redirect:/oviUser/main";
        }



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
        AccountType role = AccountType.valueOf((String) session.getAttribute("userRole"));
        if(role==AccountType.LEGALGUARDIAN) {
            LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");
            if(! candidacyService.isCandidacyFromWard(contract.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per emplenar aquest contracte", "Error 403 - Sense permisos");
            }
        } else {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            if(! candidacyService.isCandidacyFromOviUser(contract.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per emplenar aquest contracte", "Error 403 - Sense permisos");
            }
        }


        ContractValidator contractValidator = new ContractValidator();
        contractValidator.validate(contract, bindingResult);

        if (ficheroPdf == null || ficheroPdf.isEmpty()) {
            bindingResult.rejectValue("urlDocument", "required", "Has d'adjuntar el contracte en format PDF");
        } else if (!"application/pdf".equals(ficheroPdf.getContentType())) {
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

        return "redirect:/contract/done/" + contract.getIdCandidacy() + "/" + contract.getIdContract();
    }

    // Número de contratos que queremos mostrar por página
    private int pageLength = 5;

    @GetMapping("/list/{idCandidacy}")
    public String listContracts(Model model,
                                @PathVariable int idCandidacy,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("nou") Optional<Integer> nou,
                                HttpSession session) {

        checkAuthorizationOrThrow(session, idCandidacy);

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
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }
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
            throw new SgOviException("No s'ha trobat el contracte", "Error 404 - No trobat");
        }

        checkAuthorizationOrThrow(session, contract.getIdCandidacy());

        // Recuperar la URL exacta de la lista (con su paginación) para el botón de volver
        String backUrl = (String) session.getAttribute("lastContractListUrl");
        // Si por algún motivo se entra directamente y no hay variable de sesión, ponemos manualmente para evitar errores
        if (backUrl == null) {
            backUrl = "/contract/list/" + contract.getIdCandidacy();
        }

        model.addAttribute("contract", contract);
        model.addAttribute("backUrl", backUrl);

        return "contract/details";
    }


    @GetMapping("/done/{idCandidacy}/{idContract}")
    public String contractDone(Model model,
                               @PathVariable int idCandidacy,
                               @PathVariable int idContract) {
        model.addAttribute("idCandidacy", idCandidacy);
        model.addAttribute("idContract", idContract);

        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        if (candidacy == null){
            throw new SgOviException("No s'ha trobar la candidatura", "Error 404 - No trobat");
        }

        Account assistantAccount = accountDao.getAccount(candidacy.getDniPapPati());

        if (assistantAccount == null) {
            throw new SgOviException("No s'ha trobat al candidat", "Error 404 - No trobat");
        }

        String assistantName = assistantAccount.getName() + ' ' + assistantAccount.getSurname();

        model.addAttribute("assistantEmail", assistantAccount.getEmail());
        model.addAttribute("assistantName", assistantName);

        return "contract/done";
    }

    @GetMapping("/update/{idContract}")
    public String showUpdateContractForm(Model model,
                                         HttpSession session,
                                         @PathVariable int idContract) {

        Contract contract = contractDao.getContract(idContract);
        if (contract == null) {
            throw new SgOviException("No s'ha trobat el contracte", "Error 404 - No trobat");
        }

        AccountType userRole = AccountType.valueOf((String) session.getAttribute("userRole"));
        if (userRole == AccountType.LEGALGUARDIAN) {
            LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromWard(contract.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
            }
        } else if (userRole == AccountType.OVIUSER) {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromOviUser(contract.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
            }
        } else {
            throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
        }

        model.addAttribute("contract", contract);
        return "contract/update";
    }

    @PostMapping("/update")
    public String processUpdateContractForm(Model model,
                                            @ModelAttribute("contract") Contract contractModificado,
                                            BindingResult bindingResult,
                                            HttpSession session,
                                            @RequestParam("ficheroPdf") MultipartFile ficheroPdf) {

        // Recuperar el contrato original de la BD para no perder los datos que no queremos cambiar
        Contract contractOriginal = contractDao.getContract(contractModificado.getIdContract());
        if (contractOriginal == null) {
            throw new SgOviException("No s'ha trobat el contracte", "Error 404 - No trobat");
        }

        AccountType userRole = AccountType.valueOf((String) session.getAttribute("userRole"));
        if (userRole == AccountType.LEGALGUARDIAN) {
            LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromWard(contractModificado.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
            }
        } else if (userRole == AccountType.OVIUSER) {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromOviUser(contractModificado.getIdCandidacy(), currentUser)) {
                throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
            }
        } else {
            throw new SgOviException("No tens permisos per actualitzar aquest contracte", "Error 403 - Sense permisos");
        }

        ContractValidator contractValidator = new ContractValidator();
        contractValidator.validate(contractModificado, bindingResult);

        // Obligamos a que suba un nuevo documento sí o sí
        if (ficheroPdf == null || ficheroPdf.isEmpty()) {
            bindingResult.rejectValue("urlDocument", "required", "És obligatori adjuntar un nou document PDF actualitzat");
        } else if (!"application/pdf".equals(ficheroPdf.getContentType())) {
            bindingResult.rejectValue("urlDocument", "format", "El document ha de ser un PDF");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("avisoPdf", "S'han trobat errors. Si us plau, recorda tornar a adjuntar el nou document PDF.");
            return "contract/update";
        }

        try {
            String originalFilename = ficheroPdf.getOriginalFilename();
            String cleanFilename = StringUtils.cleanPath(originalFilename);
            String nombreArchivo = System.currentTimeMillis() + "_update_" + cleanFilename;
            Path rutaDestino = Paths.get(uploadDirectory, nombreArchivo);

            if (!Files.exists(rutaDestino.getParent())) {
                Files.createDirectories(rutaDestino.getParent());
            }

            Files.copy(ficheroPdf.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // SOBRESCRIBIR SOLO LOS CAMPOS PERMITIDOS en el contrato original
            contractOriginal.setEndDate(contractModificado.getEndDate());
            contractOriginal.setHourlySalary(contractModificado.getHourlySalary());
            contractOriginal.setSchedule(contractModificado.getSchedule());
            contractOriginal.setUrlDocument("contracts/" + nombreArchivo);

        } catch (IOException e) {
            bindingResult.rejectValue("urlDocument", "error", "S'ha produït un error al guardar el fitxer");
            return "contract/update";
        }

        contractDao.updateContract(contractOriginal);

        return "redirect:/contract/done/" + contractModificado.getIdCandidacy() + "/" + contractModificado.getIdContract();
    }

    @GetMapping("/listAll")
    public String listAllUserContracts(Model model,
                                       @RequestParam("page") Optional<Integer> page,
                                       HttpSession session) {
        AccountType userRole = AccountType.valueOf((String) session.getAttribute("userRole"));
        List<ContractListAllDTO> contractsDto;

        if (userRole == AccountType.OVIUSER) {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            contractsDto = contractService.listAllContractsFromOviUser(currentUser);
        } else if (userRole == AccountType.LEGALGUARDIAN) {
            return "redirect:/contract/ward/list";
        } else if (userRole == AccountType.PAPPATI) {
            PapPati currentUser = (PapPati) session.getAttribute("specificAccount");
            contractsDto = contractService.listAllContractsFromPapPati(currentUser);
        } else {
            throw new SgOviException("No tens permisos per veure aquest llistat", "Error 403 - Sense permisos");
        }

        if (contractsDto != null && !contractsDto.isEmpty()) {
            contractsDto.sort((dto1, dto2) -> new ContractComparator().compare(dto1.getContract(), dto2.getContract()));
        } else {
            contractsDto = new ArrayList<>();
        }

        ArrayList<ArrayList<ContractListAllDTO>> contractsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= contractsDto.size()) {
            contractsPaged.add(new ArrayList<>(contractsDto.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < contractsDto.size()) {
            contractsPaged.add(new ArrayList<>(contractsDto.subList(ini, contractsDto.size())));
        }

        int totalPages = contractsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }

        model.addAttribute("contractsPaged", contractsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalContracts", contractsDto.size());
        model.addAttribute("pageLength", pageLength);

        // Guardar URL exacta para el botón de volver
        String exactUrl = "/contract/listAll?page=" + currentPage;
        session.setAttribute("lastContractListUrl", exactUrl);

        return "contract/listAll";
    }

    @GetMapping({"/ward/list", "/ward/list/{wardDni}"})
    public String wardContractList(Model model, HttpSession session,
                                   @PathVariable(required = false) String wardDni,
                                   @RequestParam("page") Optional<Integer> page) {
        LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");

        if (wardDni == null)
            wardDni = "Tots";

        List<ContractListAllDTO> contractsDto = contractService.listAllContractsFromLegalGuardian(currentUser, wardDni);

        if (contractsDto != null && !contractsDto.isEmpty()) {
            contractsDto.sort((dto1, dto2) -> new ContractComparator().compare(dto1.getContract(), dto2.getContract()));
        } else {
            contractsDto = new ArrayList<>();
        }

        ArrayList<ArrayList<ContractListAllDTO>> contractsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= contractsDto.size()) {
            contractsPaged.add(new ArrayList<>(contractsDto.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < contractsDto.size()) {
            contractsPaged.add(new ArrayList<>(contractsDto.subList(ini, contractsDto.size())));
        }

        int totalPages = contractsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }

        model.addAttribute("contractsPaged", contractsPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalContracts", contractsDto.size());
        model.addAttribute("pageLength", pageLength);

        WardStatusFilter filter = new WardStatusFilter();
        filter.setWardDni(wardDni);
        model.addAttribute("statusFilter", filter);

        List<OviUser> oviWards = oviUserDao.getWardedOviUsers(currentUser.getDni());
        List<Account> wardAccounts = new ArrayList<>();
        for (OviUser ward : oviWards) {
            Account account = accountDao.getAccount(ward.getDni());
            if (account != null) {
                wardAccounts.add(account);
            }
        }
        model.addAttribute("wards", wardAccounts);

        String exactUrl = "/contract/ward/list/" + wardDni + "?page=" + currentPage;
        session.setAttribute("lastContractListUrl", exactUrl);

        return "contract/ward/list";
    }

    @PostMapping("/ward/list")
    public String processWardContractFilter(@ModelAttribute("statusFilter") WardStatusFilter filter) {
        String wardPath = filter.getWardDni() != null && !filter.getWardDni().isEmpty() ? "/" + filter.getWardDni() : "/Tots";
        return "redirect:/contract/ward/list" + wardPath;
    }

    private void checkAuthorizationOrThrow(HttpSession session, int idCandidacy) {
        AccountType userRole = AccountType.valueOf((String) session.getAttribute("userRole"));

        if (userRole == AccountType.TECHNICIAN) {
            // El tècnic pot veure qualsevol contracte en mode lectura
            return;
        }

        if (userRole == AccountType.OVIUSER) {
            OviUser oviUser = (OviUser) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromOviUser(idCandidacy, oviUser)) {
                throw new SgOviException("No tens permisos per accedir a aquest recurs", "Error 403 - Sense permisos");
            }
        } else if (userRole == AccountType.LEGALGUARDIAN) {
            LegalGuardian legalGuardian = (LegalGuardian) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromWard(idCandidacy, legalGuardian)) {
                throw new SgOviException("No tens permisos per accedir a aquest recurs", "Error 403 - Sense permisos");
            }
        } else if (userRole == AccountType.PAPPATI) {
            PapPati papPati = (PapPati) session.getAttribute("specificAccount");
            if (!candidacyService.isCandidacyFromPapPati(idCandidacy, papPati)) {
                throw new SgOviException("No tens permisos per accedir a aquest recurs", "Error 403 - Sense permisos");
            }
        }
    }

}
