package es.uji.ei1027.SgOviProject.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session.getAttribute("account") == null && session.getAttribute("technician") == null) {

            String requestURI = request.getRequestURI();

            // Solo guardamos la URL si NO es un recurso estático evidente
            // Evitamos guardar: .ico, .css, .js, .png, .jpg, etc.
            if (!isStaticResource(requestURI)) {
                String queryString = request.getQueryString();
                String fullUrl = (queryString == null) ? requestURI : requestURI + "?" + queryString;
                session.setAttribute("nextUrl", fullUrl);
            }

            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean isStaticResource(String uri) {
        return uri.contains("favicon.ico") ||
                uri.endsWith(".css") ||
                uri.endsWith(".js") ||
                uri.endsWith(".png") ||
                uri.endsWith(".jpg") ||
                uri.endsWith(".jpeg");
    }
}
