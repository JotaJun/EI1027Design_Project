package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.OviUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        OviUser currentUser = (OviUser) session.getAttribute("specificAccount");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Asignar los datos que faltan
        assistanceRequest.setDniOviUser(currentUser.getDni());

        assistanceRequestDao.addAssistanceRequest(assistanceRequest);

        return "redirect:/assistanceRequest/done";
    }

    @RequestMapping("/done")
    public String apRequestDone() {
        return "assistanceRequest/done";
    }
}
