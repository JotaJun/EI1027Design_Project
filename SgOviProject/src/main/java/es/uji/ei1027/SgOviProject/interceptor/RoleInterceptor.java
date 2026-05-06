package es.uji.ei1027.SgOviProject.interceptor;

import es.uji.ei1027.SgOviProject.enums.AccountType;
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

        if (session.getAttribute("account") == null) {
            response.sendRedirect("/login");
            return false;
        }

        String currentRole = (String) session.getAttribute("userRole");

        // Verificamos si el currentRole coincide con ALGUNO de los roles permitidos
        boolean hasRole = allowedRoles.stream()
                .anyMatch(role -> role.name().equals(currentRole));

        // Si hay usuario, pero NO tiene NINGUNO de los roles adecuados
        if (currentRole == null || !hasRole) {

            String referer = request.getHeader("Referer");

            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect("/index");
            }
            return false;
        }

        return true; // Todo correcto, le dejamos pasar
    }
}