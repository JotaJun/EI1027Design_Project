package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dto.CandidacyDTO;
import es.uji.ei1027.SgOviProject.services.CandidacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/candidacy")
public class CandidacyController {

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private CandidacyService candidacyService;

    // Ruta para OVIUSER
    @GetMapping(value="/listCandidates/{idApRequest}")
    public String listCandidates(Model model, @PathVariable int idApRequest) {
        // El controlador pide los datos ya procesados y listos para la vista
        List<CandidacyDTO> candidaciesInfo = candidacyService.getCandidaciesWithDetailsByIdApRequest(idApRequest);

        model.addAttribute("candidacies", candidaciesInfo);
        model.addAttribute("idApRequest", idApRequest);

        return "candidacy/listCandidates";
    }

    @GetMapping(value="/details/{idCandidacy}")
    public String detailsCandidacy(Model model, @PathVariable int idCandidacy) {
        CandidacyDTO candidacyDto = candidacyService.getCandidacyDetailByIdCandidacy(idCandidacy);

        model.addAttribute("candidacyDto", candidacyDto);

        return "candidacy/details";
    }

    // Ruta para PAPPATI
    @GetMapping(value="/listRequests/{dniPapPati}")
    public String listRequests(Model model, @PathVariable String dniPapPati, HttpSession session) {
        // ... lógica para PapPati
        return "candidacy/listRequests";
    }
}
