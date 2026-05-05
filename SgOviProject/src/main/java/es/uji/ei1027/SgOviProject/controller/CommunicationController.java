package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.CommunicationComparator;
import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.CommunicationDao;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.model.Account;
import es.uji.ei1027.SgOviProject.model.AssistanceRequest;
import es.uji.ei1027.SgOviProject.model.Candidacy;
import es.uji.ei1027.SgOviProject.model.Communication;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/communication")
public class CommunicationController {

    @Autowired
    private CommunicationDao communicationDao;

    @Autowired
    private CandidacyDao candidacyDao;

    @Autowired
    private AssistanceRequestDao assistanceRequestDao;

    @Autowired
    private AccountDao accountDao;

    @GetMapping("/chat/{idCandidacy}")
    public String showChat(@PathVariable int idCandidacy,
                           Model model,
                           HttpSession session) {
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);
        if (candidacy == null) {
            return "redirect:/index"; // Si no existe, devolver al inicio
        }

        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(candidacy.getIdApRequest());

        Account currentUser = (Account) session.getAttribute("account");
        AccountType role = AccountType.valueOf( (String) session.getAttribute("userRole"));
        String otherUserName;

        boolean isAuthorized = false;

        if (role == AccountType.OVIUSER){
            if (request.getDniOviUser().equals(currentUser.getDni())) {
                isAuthorized = true;
            }
        }else if (role == AccountType.PAPPATI){
            if (candidacy.getDniPapPati().equals(currentUser.getDni())) {
                isAuthorized = true;
            }
        }

        if (!isAuthorized){
            return "redirect:/index";
        }

        Account otherUser = accountDao.getAccount(candidacy.getDniPapPati());
        otherUser.setPassword(null);
        otherUserName = otherUser.getName() + " " + otherUser.getSurname();

        // Recuperar el historial de mensajes de esta candidatura
        List<Communication> chatHistory = communicationDao.getCommunicationsByCandidacy(idCandidacy);
        chatHistory.sort(new CommunicationComparator());

        model.addAttribute("candidacy", candidacy);
        model.addAttribute("chatHistory", chatHistory);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userRole", role);
        model.addAttribute("otherUserName", otherUserName);

        // Añadimos un objeto vacío para el formulario de enviar nuevo mensaje
        model.addAttribute("newCommunication", new Communication());

        return "communication/chat";

    }

    @PostMapping("/chat/{idCandidacy}/send")
    public String sendMessage(@PathVariable int idCandidacy,
                              @ModelAttribute("newCommunication") Communication newCommunication,
                              HttpSession session) {

        // Rellenamos los datos faltantes de la comunicación
        newCommunication.setIdCandidacy(idCandidacy);

        Account currentUser = (Account) session.getAttribute("account");

        // Guardamos quién es el emisor
        // Concateno nombre y apellidos para que quede visual en el chat.
        newCommunication.setTransmitterName(currentUser.getName() + " " + currentUser.getSurname());

        communicationDao.addCommunication(newCommunication);

        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        // Cambio de estado
        if (candidacy.getCandidacyStatus() == CandidacyStatus.TALKSNOTSTARTED) {
            candidacy.setCandidacyStatus(CandidacyStatus.INTALKS);
            candidacyDao.updateCandidacy(candidacy);
        }

        // Redirigimos de vuelta a la vista del chat para que recargue los mensajes
        return "redirect:/communication/chat/" + idCandidacy;
    }
}
