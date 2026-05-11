package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.CandidacyDTOComparator;
import es.uji.ei1027.SgOviProject.comparator.PapPatiCandidacyDTOComparator;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.dto.PapPatiCandidacyDTO;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
import es.uji.ei1027.SgOviProject.filters.CandidacyStatusFilter;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.OviUser;
import es.uji.ei1027.SgOviProject.model.PapPati;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/candidacy")
public class CandidacyController {

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private CandidacyService candidacyService;

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    @ModelAttribute("candidacyStatuses")
    public List<CandidacyStatus> candidacyStatusList() {
        return Arrays.asList(CandidacyStatus.values());
    }

    // Número de candidatos que queremos mostrar por página
    private int pageLength = 5;

    // Ruta para OVIUSER
    // Aceptamos la ruta con o sin el filtro de estado
    @GetMapping({"/listCandidates/{idApRequest}", "/listCandidates/{idApRequest}/{status}"})
    public String listCandidates(Model model,
                                 @PathVariable int idApRequest,
                                 @PathVariable(required = false) String status,
                                 @RequestParam("page") Optional<Integer> page,
                                 HttpSession session) {

        // Si no viene estado en la URL, por defecto mostramos "Totes"
        if (status == null) status = "Totes";

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");
        AssistanceRequest assistanceRequest = assistanceRequestDao.getAssistanceRequest(idApRequest);

        if (assistanceRequest == null) {
            throw new SgOviException("No s'ha trobat la petició d'assistència", "Error 404 - No trobat");
        }

        if (!assistanceRequest.getDniOviUser().equals(currentUser.getDni())){
            throw new SgOviException("No tens permisos per veure els candidats d'aquesta petició", "Error 403 - Sense permisos");
        }

        List<CandidacyDTO> filteredCandidacies = candidacyService.getCandidaciesWithDetailsByIdApRequestAndStatus(idApRequest, status);

        filteredCandidacies.sort(new CandidacyDTOComparator());

        // Crear la lista paginada sobre la lista YA FILTRADA
        ArrayList<ArrayList<CandidacyDTO>> candidaciesPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= filteredCandidacies.size()) {
            candidaciesPaged.add(new ArrayList<>(filteredCandidacies.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < filteredCandidacies.size()) {
            candidaciesPaged.add(new ArrayList<>(filteredCandidacies.subList(ini, filteredCandidacies.size())));
        }

        // Crear la lista de números de página para la barra de navegación
        int totalPages = candidaciesPaged.size();
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

        // PREPARAR EL FILTRO PARA LA VISTA
        CandidacyStatusFilter filter = new CandidacyStatusFilter();
        filter.setStatusSel(status);

        // PASAR DATOS AL MODELO

        model.addAttribute("candidacyStatusFilter", filter);
        model.addAttribute("candidaciesPaged", candidaciesPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalCandidacies", filteredCandidacies.size());
        model.addAttribute("pageLength", pageLength);
        model.addAttribute("idApRequest", idApRequest);

        // Guardar URL exacta para el botón de volver al listado
        String exactUrl = "/candidacy/listCandidates/" + idApRequest + "/" + status + "?page=" + currentPage;
        session.setAttribute("lastCandidacyListUrl", exactUrl);

        return "candidacy/listCandidates";
    }

    // PostMapping para procesar el formulario del filtro
    @PostMapping("/listCandidates/{idApRequest}")
    public String processFilter(@PathVariable int idApRequest,
                                @ModelAttribute("candidacyStatusFilter") CandidacyStatusFilter filter) {
        // Redirigimos a la ruta GET, inyectando el ID de la petición y el estado seleccionado
        return "redirect:/candidacy/listCandidates/" + idApRequest + "/" + filter.getStatusSel();
    }

    @GetMapping(value="/details/{idCandidacy}")
    public String detailsCandidacy(Model model, @PathVariable int idCandidacy, HttpSession session) {

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        CandidacyDTO candidacyDto = candidacyService.getCandidacyDetailByIdCandidacy(idCandidacy);

        if (candidacyDto == null) {
            throw new SgOviException("No s'ha trobat la candidatura", "Error 404 - No trobat");
        }

        if (!candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser)) {
            throw new SgOviException("No tens permisos per veure els detalls d'aquesta candidatura", "Error 403 - Sense permisos");
        }

        model.addAttribute("candidacyDto", candidacyDto);

        return "candidacy/details";
    }

    @GetMapping("reject/{idCandidacy}")
    public String processDelete(Model model, @PathVariable int idCandidacy, HttpSession session){
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        // Si la candidacy no existe devolver al listado de aprequest
        if (candidacy == null){
            throw new SgOviException("No s'ha trobat la candidatura", "Error 404 - No trobat");
        }

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Si la candidatura no es del usuario actual, devolver a la lista de aprequest
        if (! candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser)){
            throw new SgOviException("No tens permisos per rebutjar aquesta candidatura", "Error 403 - Sense permisos");
        }

        candidacy.setCandidacyStatus(CandidacyStatus.TALKSENDED);
        candidacy.setDateLastModified(LocalDate.now());

        candidacyDao.updateCandidacy(candidacy);

        model.addAttribute("idApRequest", candidacy.getIdApRequest());  // guardar en caso de haber perdido el enlace a la lista con parámetros específicos

        return "candidacy/rejected_done";
    }

    // Ruta para PAPPATI
    @GetMapping({"/listRequests", "/listRequests/{status}"})
    public String listRequests(Model model,
                               @PathVariable(required = false) String status,
                               @RequestParam("page") Optional<Integer> page,
                               HttpSession session) {

        PapPati currentPap = (PapPati) session.getAttribute("specificAccount");
        if (status == null) status = "Totes";

        List<PapPatiCandidacyDTO> filteredCandidacies = candidacyService.getPapPatiCandidaciesWithDetails(currentPap.getDni(), status);

        filteredCandidacies.sort(new PapPatiCandidacyDTOComparator());

        // Crear la lista paginada
        ArrayList<ArrayList<PapPatiCandidacyDTO>> candidaciesPaged = new ArrayList<>();
        int ini = 0;
        int fin = pageLength;

        while (fin <= filteredCandidacies.size()) {
            candidaciesPaged.add(new ArrayList<>(filteredCandidacies.subList(ini, fin)));
            ini += pageLength;
            fin += pageLength;
        }
        if (ini < filteredCandidacies.size()) {
            candidaciesPaged.add(new ArrayList<>(filteredCandidacies.subList(ini, filteredCandidacies.size())));
        }

        // Crear la barra de navegación de páginas
        int totalPages = candidaciesPaged.size();
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

        // Preparar el filtro para la vista
        CandidacyStatusFilter filter = new CandidacyStatusFilter();
        filter.setStatusSel(status);

        // Pasar todos los atributos al modelo
        model.addAttribute("candidacyStatusFilter", filter);
        model.addAttribute("candidaciesPaged", candidaciesPaged);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("totalCandidacies", filteredCandidacies.size());
        model.addAttribute("pageLength", pageLength);

        // Guardar URL exacta para el botón de volver en los detalles
        String exactUrl = "/candidacy/listRequests/" + status + "?page=" + currentPage;
        session.setAttribute("lastCandidacyListUrl", exactUrl);

        return "candidacy/listRequests";
    }

    // PostMapping para procesar el formulario del filtro del PapPati
    @PostMapping("/listRequests")
    public String processPapPatiFilter(@ModelAttribute("candidacyStatusFilter") CandidacyStatusFilter filter) {
        return "redirect:/candidacy/listRequests/" + filter.getStatusSel();
    }

    // Ruta para que el PapPati vea los detalles de la petición a la que aplicó
    @GetMapping(value="/requestDetails/{idCandidacy}")
    public String detailsPapPatiRequest(Model model, @PathVariable int idCandidacy, HttpSession session) {

        PapPati currentPap = (PapPati) session.getAttribute("specificAccount");

        // Obtener el DTO completo con Candidacy, AssistanceRequest y OviUser
        PapPatiCandidacyDTO dto = candidacyService.getPapPatiCandidacyDetail(idCandidacy);

        // Verificar que existe y que pertenece al PapPati logueado
        if (dto == null) {
            throw new SgOviException("No s'ha trobat la petició sol·licitada", "Error 404 - No trobat");
        }

        if (!dto.getCandidacy().getDniPapPati().equals(currentPap.getDni())) {
            throw new SgOviException("No tens permisos per veure els detalls d'aquesta petició", "Error 403 - Sense permisos");
        }

        // Pasamos el DTO a la vista
        model.addAttribute("candidacyDto", dto);

        return "candidacy/requestDetails";
    }
}
