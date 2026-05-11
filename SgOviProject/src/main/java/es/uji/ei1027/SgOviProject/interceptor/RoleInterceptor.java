package es.uji.ei1027.SgOviProject.interceptor;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.exception.SgOviException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

public class RoleInterceptor implements HandlerInterceptor {

    private final List<AccountType> allowedRoles;

    public RoleInterceptor(AccountType... allowedRoles) {
        this.allowedRoles = Arrays.asList(allowedRoles);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        String currentRole = (String) session.getAttribute("userRole");

        if (currentRole == null){
            throw new SgOviException(
                    "No s'ha trobat un rol a la teua sessió.",
                    "Error de Sessió"
            );
        }

        // Verificamos si el currentRole coincide con ALGUNO de los roles permitidos
        boolean hasAllowedRole = allowedRoles.stream()
                .anyMatch(role -> role.name().equals(currentRole));

        if (!hasAllowedRole) {
            throw new SgOviException(
                    "No tens permisos per a accedir a aquesta funcionalitat.",
                    "Accés Denegat"
            );
        }


        if (AccountType.TECHNICIAN.name().equals(currentRole)) {
            if (session.getAttribute("technician") == null) {
                throw new SgOviException(
                        "Sessió de tècnic no trobada o caducada.",
                        "Error de Sessió"
                );
            }
        } else {
            if (session.getAttribute("account") == null) {
                throw new SgOviException(
                        "Sessió d'usuari no trobada o caducada.",
                        "Error de Sessió"
                );
            }
        }

        return true; // Todo correcto, le dejamos pasar
    }
}