package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.comparator.CommunicationComparator;
import es.uji.ei1027.SgOviProject.dao.AccountDao;
import es.uji.ei1027.SgOviProject.dao.AssistanceRequestDao;
import es.uji.ei1027.SgOviProject.dao.CandidacyDao;
import es.uji.ei1027.SgOviProject.dao.CommunicationDao;
import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.enums.CandidacyStatus;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
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
        Account currentUser = (Account) session.getAttribute("account");
        AccountType role = AccountType.valueOf((String) session.getAttribute("userRole"));

        // Validamos y obtenemos la request
        AssistanceRequest request = validateChatAccessAndGetRequest(idCandidacy, currentUser, role);
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);

        Account otherUser = null;
        if (role == AccountType.OVIUSER) {
            otherUser = accountDao.getAccount(candidacy.getDniPapPati());
        } else if (role == AccountType.PAPPATI) {
            otherUser = accountDao.getAccount(request.getDniOviUser());
        }

        String otherUserName = "Usuari Desconegut";
        if (otherUser != null) {
            otherUser.setPassword(null);
            otherUserName = otherUser.getName() + " " + otherUser.getSurname();
        }


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

        Account currentUser = (Account) session.getAttribute("account");
        AccountType role = AccountType.valueOf((String) session.getAttribute("userRole"));

        validateChatAccessAndGetRequest(idCandidacy, currentUser, role);

        // Evitar mensajes vacíos (opcional pero recomendado)
        if (newCommunication.getInformation() == null || newCommunication.getInformation().trim().isEmpty()) {
            return "redirect:/communication/chat/" + idCandidacy;
        }

        // Rellenamos los datos faltantes de la comunicación
        newCommunication.setIdCandidacy(idCandidacy);

        // Guardamos quién es el emisor
        newCommunication.setTransmitterDni(currentUser.getDni());

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

    private AssistanceRequest validateChatAccessAndGetRequest(int idCandidacy, Account currentUser, AccountType role) {
        Candidacy candidacy = candidacyDao.getCandidacyById(idCandidacy);
        if (candidacy == null) {
            throw new SgOviException("No s'ha trobat la candidatura", "Error 404 - No trobat");
        }

        AssistanceRequest request = assistanceRequestDao.getAssistanceRequest(candidacy.getIdApRequest());
        if (request == null) {
            throw new SgOviException("No s'ha trobat la petició associada", "Error 404 - No trobat");
        }

        boolean isAuthorized = false;
        if (role == AccountType.OVIUSER && request.getDniOviUser().equals(currentUser.getDni())) {
            isAuthorized = true;
        } else if (role == AccountType.PAPPATI && candidacy.getDniPapPati().equals(currentUser.getDni())) {
            isAuthorized = true;
        }

        if (!isAuthorized) {
            throw new SgOviException("No tens permisos per accedir a aquest xat", "Error 403 - Sense permisos");
        }

        return request;
    }
}
