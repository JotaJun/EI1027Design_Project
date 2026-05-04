package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.CandidacyDTOComparator;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.filters.CandidacyStatusFilter;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.OviUser;
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

        // Comprobar que la petición de la aprequest es del usuario loggeado
        if (! assistanceRequest.getDniOviUser().equals(currentUser.getDni())){
            return "redirect:/assistanceRequest/list";
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

        // PREPARAR EL FILTRO PARA LA VISTA
        CandidacyStatusFilter filter = new CandidacyStatusFilter();
        filter.setStatusSel(status);

        // PASAR DATOS AL MODELO

        model.addAttribute("candidacyStatusFilter", filter);
        model.addAttribute("candidacyStatuses", CandidacyStatus.values());
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
    public String detailsCandidacy(Model model, @PathVariable int idCandidacy) {
        CandidacyDTO candidacyDto = candidacyService.getCandidacyDetailByIdCandidacy(idCandidacy);

        model.addAttribute("candidacyDto", candidacyDto);

        return "candidacy/details";
    }

    @GetMapping("reject/{idCandidacy}")
    public String processDelete(Model model, @PathVariable int idCandidacy, HttpSession session){
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        // Si la candidacy no existe devolver al listado de aprequest
        if (candidacy == null){
            return "redirect:/assistanceRequest/list";
        }

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        // Si la candidatura no es del usuario actual, devolver a la lista de aprequest
        if (! candidacyService.isCandidacyFromOviUser(idCandidacy, currentUser)){
            return "redirect:/assistanceRequest/list";
        }

        candidacy.setCandidacyStatus(CandidacyStatus.TALKSENDED);
        candidacy.setDateLastModified(LocalDate.now());

        candidacyDao.updateCandidacy(candidacy);

        model.addAttribute("idApRequest", candidacy.getIdApRequest());  // guardar en caso de haber perdido el enlace a la lista con parámetros específicos

        return "candidacy/rejected_done";
    }

    // Ruta para PAPPATI
    @GetMapping(value="/listRequests/{dniPapPati}")
    public String listRequests(Model model, @PathVariable String dniPapPati, HttpSession session) {
        // ... lógica para PapPati
        return "candidacy/listRequests";
    }
}
