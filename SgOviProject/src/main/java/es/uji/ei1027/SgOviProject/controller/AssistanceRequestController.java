package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.AssistanceRequestComparator;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.enums.Status;
import es.uji.ei1027.SgOviProject.filters.StatusFilter;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.OviUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/assistanceRequest")
public class AssistanceRequestController {

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    @GetMapping("/add")
    public String showAddAssistanceForm(Model model) {
        model.addAttribute("assistanceRequest", new AssistanceRequest());
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

        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");


        // Asignar los datos que faltan
        assistanceRequest.setDniOviUser(currentUser.getDni());

        assistanceRequestDao.addAssistanceRequest(assistanceRequest);

        return "redirect:/assistanceRequest/done";
    }

    // Número de peticiones que queremos mostrar al usuario
    private int pageLength = 5;

    @GetMapping({"/list", "/list/{status}"}) // Acepta la ruta base o con filtro
    public String showList(Model model, HttpSession session,
                           @PathVariable(required = false) String status,
                           @RequestParam("page") Optional<Integer> page) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Si no viene estado en la URL, por defecto mostramos "Totes"
        if (status == null) status = "Totes";

        // Obtener la lista filtrada
        List<AssistanceRequest> requests = assistanceRequestDao.getAssistanceRequestsByDniAndStatus(currentUser.getDni(), status);

        requests.sort(new AssistanceRequestComparator());   // ordenar la lista completa

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
        model.addAttribute("selectedPage", currentPage);
        // PASAMOS LOS DATOS TOTALES PARA EL CONTADOR
        model.addAttribute("totalRequests", requests.size());
        model.addAttribute("pageLength", pageLength);

        // Preparar el objeto del filtro para la vista
        StatusFilter filter = new StatusFilter();
        filter.setStatusSel(status);

        model.addAttribute("statusFilter", filter);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("requestsPaged", requestsPaged);


        // Guardamos la URL exacta (con su estado y página) para el botón de volver de assistanceRequest/details
        String exactUrl = "/assistanceRequest/list/" + status + "?page=" + currentPage;
        session.setAttribute("lastRequestListUrl", exactUrl);

        return "assistanceRequest/list";
    }

    @PostMapping("/list")
    public String processFilter(@ModelAttribute("statusFilter") StatusFilter filter) {
        return "redirect:/assistanceRequest/list/" + filter.getStatusSel();
    }

    @GetMapping(value="/details/{idApRequest}")
    public String showDetails(Model model, @PathVariable int idApRequest, HttpSession session) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Comprobar que el id de la ap request pertenece al usuario que la ha pedido
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null || ! request.getDniOviUser().equals(currentUser.getDni())) {
            return "redirect:/assistanceRequest/list";
        }

        model.addAttribute("req", request);

        return "assistanceRequest/details";
    }

    @GetMapping(value="/update/{idApRequest}")
    public String editApRequest(Model model, @PathVariable int idApRequest, HttpSession session) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Comprobar que el id de la ap request pertenece al usuario que la ha pedido
        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (request == null || ! request.getDniOviUser().equals(currentUser.getDni())) {
            return "redirect:/assistanceRequest/list";
        }

        model.addAttribute("assistanceRequest", request);
        return "assistanceRequest/update";
    }

    @PostMapping(value="/update")
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
        AssistanceRequest originalRequest = assistanceRequestDao.getAssistanceRequest(assistanceRequest.getIdApRequest());

        if (originalRequest == null || !originalRequest.getDniOviUser().equals(currentUser.getDni())) {
            return "redirect:/assistanceRequest/list";
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

        return "redirect:/assistanceRequest/done";
    }

    @GetMapping(value="/delete/{idApRequest}")
    public String deleteApRequest(Model model, @PathVariable int idApRequest, HttpSession session) {
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(idApRequest);

        // Comprobar que la solicitud existe, pertenece al usuario actual y está en estado PENDING
        if (request == null || !request.getDniOviUser().equals(currentUser.getDni()) || request.getStatus() != Status.PENDING) {
            return "redirect:/assistanceRequest/list";
        }

        assistanceRequestDao.deleteAssistanceRequest(idApRequest);

        return "redirect:/assistanceRequest/list";
    }

    @RequestMapping("/done")
    public String apRequestDone() {
        return "assistanceRequest/done";
    }
}
