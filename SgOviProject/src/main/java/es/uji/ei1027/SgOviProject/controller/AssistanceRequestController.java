package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.AssistanceRequestComparator;
import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.PapPatiDao;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.enums.*;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
import es.uji.ei1027.SgOviProject.filters.StatusFilter;
import es.uji.ei1027.SgOviProject.model.*;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/assistanceRequest")
public class AssistanceRequestController {

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private CandidacyService candidacyService;

    @Autowired
    private PapPatiDao papPatiDao;

    @Autowired
    private CandidacyDao candidacyDao;

    @ModelAttribute("genderList")
    public List<Gender> genderList() {
        return Arrays.asList(Gender.values());
    }

    @ModelAttribute("staffTypeList")
    public List<StaffType> staffTypeList() {
        return Arrays.asList(StaffType.values());
    }

    @GetMapping({"/add", "/add/{dni}"})
    public String showAddAssistanceForm(Model model, @PathVariable(required = false) String dni, HttpSession session) {
        AssistanceRequest assistanceRequest = new AssistanceRequest();
        
        // Si se pasa un DNI por la URL (caso del tutor legal)
        if (dni != null) {
            assistanceRequest.setDniOviUser(dni);
        }
        
        model.addAttribute("assistanceRequest", assistanceRequest);
        return "assistanceRequest/add";
    }

    @PostMapping("/add")
    public String processAddAssistanceForm(@ModelAttribute("assistanceRequest") AssistanceRequest assistanceRequest,
            BindingResult bindingResult,
            HttpSession session) {
        AssistanceRequestValidator assistanceRequestValidator = new AssistanceRequestValidator();
        assistanceRequestValidator.validate(assistanceRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            return "assistanceRequest/add";
        }

        String userRole = (String) session.getAttribute("userRole");

        if (AccountType.LEGALGUARDIAN.name().equals(userRole)){
            LegalGuardian currentUser = (LegalGuardian) session.getAttribute("specificAccount");
            assistanceRequest.setDniLegalGuardian(currentUser.getDni());
            // El dniOviUser ya vendrá en el objeto assistanceRequest (desde el campo oculto del formulario)
        } else {
            OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
            assistanceRequest.setDniOviUser(currentUser.getDni());
        }

        assistanceRequestDao.addAssistanceRequest(assistanceRequest);

        return "redirect:/assistanceRequest/done/" + assistanceRequest.getIdApRequest();
    }

    // Número de peticiones que queremos mostrar al usuario
    private int pageLength = 5;

    @GetMapping({ "/list", "/list/{status}" }) // Acepta la ruta base o con filtro
    public String showList(Model model, HttpSession session,
            @PathVariable(required = false) String status,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("nova") Optional<Integer> nova) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Si no viene estado en la URL, por defecto mostramos "Totes"
        if (status == null)
            status = "Totes";

        // Obtener la lista filtrada
        List<AssistanceRequest> requests = assistanceRequestDao
                .getAssistanceRequestsByDniAndStatus(currentUser.getDni(), status);

        requests.sort(new AssistanceRequestComparator()); // ordenar la lista completa

        // Crear la lista paginada (una lista de listas)
        ArrayList<ArrayList<AssistanceRequest>> requestsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= requests.size()) {
            requestsPaged.add(new ArrayList<>(requests.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        // Añadir los elementos sobrantes si la división no es exacta
        if (ini < requests.size()) {
            requestsPaged.add(new ArrayList<>(requests.subList(ini, requests.size())));
        }

        model.addAttribute("requestsPaged", requestsPaged);

        // Crear la lista de números de página para la barra de navegación
        int totalPages = requestsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        // Determinar la página seleccionada (por defecto la 0 si no se indica)
        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }
        int novaReqId = nova.orElse(-1);

        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("nova", novaReqId);
        // PASAMOS LOS DATOS TOTALES PARA EL CONTADOR
        model.addAttribute("totalRequests", requests.size());
        model.addAttribute("pageLength", pageLength);

        // Preparar el objeto del filtro para la vista
        StatusFilter filter = new StatusFilter();
        filter.setStatusSel(status);

        model.addAttribute("statusFilter", filter);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("requestsPaged", requestsPaged);

        // Guardamos la URL exacta (con su estado y página) para el botón de volver de
        // assistanceRequest/details
        String exactUrl = "/assistanceRequest/list/" + status + "?page=" + currentPage;
        session.setAttribute("lastRequestListUrl", exactUrl);

        return "assistanceRequest/list";
    }

    @PostMapping("/list")
    public String processFilter(@ModelAttribute("statusFilter") StatusFilter filter) {
        return "redirect:/assistanceRequest/list/" + filter.getStatusSel();
    }

    @GetMapping(value = "/details/{idApRequest}")
    public String showDetails(Model model, @PathVariable int idApRequest, HttpSession session) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Comprobar que el id de la ap request pertenece al usuario que la ha pedido
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició sol·licitada", "Error 404 - No trobat");
        }

        if (!request.getDniOviUser().equals(currentUser.getDni())) {
            throw new SgOviException("No tens permisos per veure aquesta petició", "Error 403 - Sense permisos");
        }

        // Si la solicitud fue creada por un tutor, recuperamos sus datos
        if (request.getDniLegalGuardian() != null && !request.getDniLegalGuardian().trim().isEmpty()) {
            Account guardianAccount = accountDao.getAccount(request.getDniLegalGuardian());
            model.addAttribute("guardianAccount", guardianAccount);
        }

        // guardar datos usuario ovi
        Account oviUserAccount = accountDao.getAccount(request.getDniOviUser());
        model.addAttribute("oviUserAccount", oviUserAccount);

        model.addAttribute("req", request);

        return "assistanceRequest/details";
    }

    @GetMapping(value = "/update/{idApRequest}")
    public String editApRequest(Model model, @PathVariable int idApRequest, HttpSession session) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Comprobar que el id de la ap request pertenece al usuario que la ha pedido
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició sol·licitada", "Error 404 - No trobat");
        }

        if (!request.getDniOviUser().equals(currentUser.getDni())) {
            throw new SgOviException("No tens permisos per editar aquesta petició", "Error 403 - Sense permisos");
        }

        model.addAttribute("assistanceRequest", request);
        return "assistanceRequest/update";
    }

    @PostMapping(value = "/update")
    public String processUpdateApRequest(@ModelAttribute("assistanceRequest") AssistanceRequest assistanceRequest,
            BindingResult bindingResult,
            HttpSession session) {
        AssistanceRequestValidator assistanceRequestValidator = new AssistanceRequestValidator();
        assistanceRequestValidator.validate(assistanceRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            return "assistanceRequest/update";
        }

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // RECUPERAMOS LA PETICIÓN ORIGINAL DE LA BASE DE DATOS
        AssistanceRequest originalRequest = assistanceRequestDao
                .getAssistanceRequest(assistanceRequest.getIdApRequest());

        if (originalRequest == null) {
            throw new SgOviException("No s'ha trobat la petició original", "Error 404 - No trobat");
        }
        
        if (!originalRequest.getDniOviUser().equals(currentUser.getDni())) {
            throw new SgOviException("No tens permisos per editar aquesta petició", "Error 403 - Sense permisos");
        }

        // Copiamos SOLO los campos que el usuario tiene permitido editar
        originalRequest.setAssistantType(assistanceRequest.getAssistantType());
        originalRequest.setInitialDateRequired(assistanceRequest.getInitialDateRequired());
        originalRequest.setMonthsRequired(assistanceRequest.getMonthsRequired());
        originalRequest.setCity(assistanceRequest.getCity());
        originalRequest.setGender(assistanceRequest.getGender());
        originalRequest.setYearsOfExperience(assistanceRequest.getYearsOfExperience());
        originalRequest.setDrivingLicense(assistanceRequest.getDrivingLicense());
        originalRequest.setDescription(assistanceRequest.getDescription());

        assistanceRequestDao.updateAssistanceRequest(originalRequest);

        return "redirect:/assistanceRequest/done/" + assistanceRequest.getIdApRequest();
    }

    @GetMapping(value = "/delete/{idApRequest}")
    public String deleteApRequest(Model model, @PathVariable int idApRequest, HttpSession session) {
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        // Comprobar que la solicitud existe, pertenece al usuario actual y está en
        // estado PENDING
        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició sol·licitada", "Error 404 - No trobat");
        }

        if (!request.getDniOviUser().equals(currentUser.getDni())) {
            throw new SgOviException("No tens permisos per esborrar aquesta petició", "Error 403 - Sense permisos");
        }

        if (request.getStatus() != Status.PENDING) {
            throw new SgOviException("Només es poden esborrar les peticions en estat PENDING", "Error de Validació");
        }

        assistanceRequestDao.deleteAssistanceRequest(idApRequest);

        return "redirect:/assistanceRequest/list";
    }

    @GetMapping("/done/{idApRequest}")
    public String apRequestDone(Model model, @PathVariable int idApRequest) {
        // Pasamos el ID a la vista done.html para que pueda usarlo en el botón de
        // "Volver"
        model.addAttribute("idApRequest", idApRequest);
        return "assistanceRequest/done";
    }

    @GetMapping("/manage/list") // Acepta la ruta base o con filtro
    public String showManageList(Model model, HttpSession session,
            @RequestParam("page") Optional<Integer> page) {

        // Obtener la lista de pendientes
        List<AssistanceRequest> requests = assistanceRequestDao.getPendingRequests();

        requests.sort(new AssistanceRequestComparator()); // ordenar la lista

        // Preparamos nombre+apellidos por DNI para la vista
        Map<String, String> userNameByDni = new HashMap<>();
        List<String> dnis = requests.stream()
                .map(AssistanceRequest::getDniOviUser)
                .distinct()
                .collect(Collectors.toList());
        for (String dni : dnis) {
            Account acc = accountDao.getAccount(dni);
            if (acc != null) {
                userNameByDni.put(dni, acc.getName() + " " + acc.getSurname());
            } else {
                userNameByDni.put(dni, "");
            }
        }
        model.addAttribute("userNameByDni", userNameByDni);

        // Crear la lista paginada (una lista de listas)
        ArrayList<ArrayList<AssistanceRequest>> requestsPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= requests.size()) {
            requestsPaged.add(new ArrayList<>(requests.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        // Añadir los elementos sobrantes si la división no es exacta
        if (ini < requests.size()) {
            requestsPaged.add(new ArrayList<>(requests.subList(ini, requests.size())));
        }

        model.addAttribute("requestsPaged", requestsPaged);

        // Crear la lista de números de página para la barra de navegación
        int totalPages = requestsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        // Determinar la página seleccionada (por defecto la 0 si no se indica)
        int currentPage = page.orElse(0);
        if (totalPages > 0) {
            if (currentPage < 0) currentPage = 0;
            if (currentPage >= totalPages) currentPage = totalPages - 1;
        } else {
            currentPage = 0;
        }

        model.addAttribute("selectedPage", currentPage);
        // PASAMOS LOS DATOS TOTALES PARA EL CONTADOR
        model.addAttribute("totalRequests", requests.size());
        model.addAttribute("pageLength", pageLength);

        model.addAttribute("requestsPaged", requestsPaged);

        // Guardamos la URL exacta (con su estado y página) para el botón de volver de
        // assistanceRequest/manage/details
        String exactUrl = "/assistanceRequest/manage/list" + "?page=" + currentPage;
        session.setAttribute("lastRequestListUrl", exactUrl);

        return "assistanceRequest/manage/list";
    }

    @GetMapping(value = "/manage/details/{idApRequest}")
    public String showManageDetails(Model model, @PathVariable int idApRequest, HttpSession session) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició per gestionar", "Error 404 - No trobat");
        }

        model.addAttribute("req", request);
        Account requester = accountDao.getAccount(request.getDniOviUser());
        if (requester != null) {
            model.addAttribute("requesterFullName", requester.getName() + " " + requester.getSurname());
        } else {
            model.addAttribute("requesterFullName", "");
        }

        // Si la solicitud fue creada por un tutor, recuperamos sus datos
        if (request.getDniLegalGuardian() != null && !request.getDniLegalGuardian().trim().isEmpty()) {
            Account guardianAccount = accountDao.getAccount(request.getDniLegalGuardian());
            model.addAttribute("guardianAccount", guardianAccount);
        }

        return "assistanceRequest/manage/details";
    }

    @PostMapping(value = "/manage/approve/{idApRequest}")
    public String manageDoApprove(Model model, @PathVariable int idApRequest, HttpSession session) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició per aprovar", "Error 404 - No trobat");
        }

        request.setStatus(Status.ACCEPTED);
        assistanceRequestDao.updateAssistanceRequest(request);

        // Generar candidaturas automáticamente con los PapPati compatibles
        candidacyService.generateCandidacies(request);

        return "redirect:/candidacy/listCandidates/" + idApRequest;
    }

    @GetMapping(value = "/manage/rejectReason/{idApRequest}")
    public String showRejectReason(Model model, @PathVariable int idApRequest) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició per rebutjar", "Error 404 - No trobat");
        }

        model.addAttribute("req", request);
        return "assistanceRequest/manage/rejectReason";
    }

    @PostMapping(value = "/manage/rejectReason")
    public String doRejectReason(@ModelAttribute("req") AssistanceRequest request, BindingResult bindingResult) {

        if (request.getDeniedReason() == null) {
            bindingResult.rejectValue("deniedReason", "required", "El motiu del rebuig és obligatori");
            return "assistanceRequest/manage/rejectReason";
        } else if (request.getDeniedReason().length() > 255) {
            bindingResult.rejectValue("deniedReason", "tooLong",
                    "El motiu del rebuig no pot superar els 255 caràcters");
            return "assistanceRequest/manage/rejectReason";
        }

        AssistanceRequest original = assistanceRequestDao.getAssistanceRequest(request.getIdApRequest());
        if (original == null) {
            throw new SgOviException("No s'ha trobat la petició per rebutjar", "Error 404 - No trobat");
        }
        original.setDeniedReason(request.getDeniedReason());
        original.setStatus(Status.REJECTED);
        assistanceRequestDao.updateAssistanceRequest(original);

        return "redirect:/assistanceRequest/manage/done?result=rejected";
    }

    @GetMapping("/manage/done")
    public String manageApDone(Model model, @RequestParam(value = "result", required = false) String result) {
        model.addAttribute("result", result);
        return "assistanceRequest/manage/done";
    }

    // ---- REVISIÓ DE CANDIDATS (tecnic selecciona quins s'oferiran a l'OVI user)
    // ----

    /**
     * Mostra els candidats potencials (PapPati compatibles) sense crear cap
     * Candidacy.
     * El tècnic selecciona quins vol confirmar.
     */
    @GetMapping("/manage/reviewCandidates/{idApRequest}")
    public String showReviewCandidates(Model model, @PathVariable int idApRequest, HttpSession session) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició a revisar", "Error 404 - No trobat");
        }
        
        if (request.getStatus() != Status.PENDING) {
            throw new SgOviException("Només es poden revisar peticions en estat PENDING", "Error d'Estat");
        }

        // Obtenir candidats potencials sense crear Candidacy (consulta filtrada a la
        // BD)
        List<PapPati> candidates = papPatiDao.getCandidatePapPatis(request);

        // Construir llista de DTOs (sense Candidacy) per passar a la vista
        List<CandidacyDTO> candidatePreviews = new ArrayList<>();
        for (PapPati papPati : candidates) {
            Account account = accountDao.getAccount(papPati.getDni());
            candidatePreviews.add(new CandidacyDTO(null, papPati, account));
        }

        // Guardar URL de revisió per al botó de tornar del detall de candidat
        session.setAttribute("lastReviewUrl", "/assistanceRequest/manage/reviewCandidates/" + idApRequest);

        model.addAttribute("candidatePreviews", candidatePreviews);
        model.addAttribute("idApRequest", idApRequest);
        model.addAttribute("req", request);

        return "assistanceRequest/manage/reviewCandidates";
    }

    /**
     * Processa la selecció del tècnic: crea Candidacy per als PapPati seleccionats,
     * marca la AssistanceRequest com a ACCEPTED i redirigeix a la pantalla de fet.
     */
    @PostMapping("/manage/confirmCandidates/{idApRequest}")
    public String confirmCandidates(@PathVariable int idApRequest,
            @RequestParam(value = "selectedDnis", required = false) List<String> selectedDnis) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició per confirmar candidats", "Error 404 - No trobat");
        }

        // Marcar la sol·licitud com a acceptada
        request.setStatus(Status.ACCEPTED);
        assistanceRequestDao.updateAssistanceRequest(request);

        // Crear Candidacy només per als PapPati seleccionats pel tècnic
        if (selectedDnis != null && !selectedDnis.isEmpty()) {
            for (String dni : selectedDnis) {
                Candidacy candidacy = new Candidacy();
                candidacy.setIdApRequest(idApRequest);
                candidacy.setDniPapPati(dni);
                candidacy.setCandidacyStatus(CandidacyStatus.TALKSNOTSTARTED);
                candidacy.setDateLastModified(java.time.LocalDate.now());
                candidacyDao.addCandidacy(candidacy);
            }
        }

        return "redirect:/assistanceRequest/manage/done?result=approved";
    }

    /**
     * Mostra el perfil complet d'un candidat (PapPati + Account) per al tècnic.
     */
    @GetMapping("/manage/candidateDetails/{dni}")
    public String showCandidateDetails(Model model, @PathVariable String dni, HttpSession session) {
        PapPati papPati = papPatiDao.getPapPati(dni);
        Account account = accountDao.getAccount(dni);

        if (papPati == null || account == null) {
            throw new SgOviException("No s'ha trobat el candidat sol·licitat", "Error 404 - No trobat");
        }

        // Guardar la URL de revisió a la sessió per al botó de tornar
        String referer = (String) session.getAttribute("lastReviewUrl");
        if (referer == null) {
            session.setAttribute("lastReviewUrl", "/assistanceRequest/manage/list");
        }

        model.addAttribute("papPati", papPati);
        model.addAttribute("account", account);

        return "assistanceRequest/manage/candidateDetails";
    }

    // ===== DETALL D'AR EN MODE LECTURA PER AL TÈCNIC =====

    @GetMapping(value = "/technician/details/{idApRequest}")
    public String showTechnicianDetails(Model model, @PathVariable int idApRequest, HttpSession session) {
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició sol·licitada", "Error 404 - No trobat");
        }

        // Si la solicitud fue creada por un tutor, recuperamos sus datos
        if (request.getDniLegalGuardian() != null && !request.getDniLegalGuardian().trim().isEmpty()) {
            Account guardianAccount = accountDao.getAccount(request.getDniLegalGuardian());
            model.addAttribute("guardianAccount", guardianAccount);
        }

        model.addAttribute("req", request);

        // Guardar URL per a la tornada des de la llista de candidatures
        session.setAttribute("lastRequestListUrl", "/assistanceRequest/technician/details/" + idApRequest);

        return "assistanceRequest/technicianDetails";
    }
}
