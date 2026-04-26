package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.OviUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public String showList(Model model, HttpSession session) {
        // No hace falta comprobar si es null, ya se encarga interceptor
        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        model.addAttribute("requests", assistanceRequestDao.getAssistanceRequestsByDni(currentUser.getDni()));

        return "assistanceRequest/list";
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

        model.addAttribute("req", assistanceRequestDao.getAssistanceRequest(idApRequest));

        return "assistanceRequest/details";
    }

    @RequestMapping("/done")
    public String apRequestDone() {
        return "assistanceRequest/done";
    }
}
