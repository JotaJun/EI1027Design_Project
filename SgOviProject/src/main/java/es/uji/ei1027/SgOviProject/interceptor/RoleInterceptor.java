package es.uji.ei1027.SgOviProject.interceptor;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class RoleInterceptor implements HandlerInterceptor {

    private final AccountType requiredRole;

    // Pasamos el rol necesario cuando creamos el interceptor
    public RoleInterceptor(AccountType requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session.getAttribute("account") == null) {
            response.sendRedirect("/login");
            return false;
        }

        String currentRole = (String) session.getAttribute("userRole");

        // Comprobamos si el rol coincide con el que requiere este interceptor
        if (currentRole == null || !currentRole.equals(requiredRole.name())) {
            response.sendRedirect("/login");
            return false;
        }

        return true; // Todo correcto, le dejamos pasar
    }
}