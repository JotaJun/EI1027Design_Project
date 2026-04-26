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

        // Comprobamos si no hay NADIE logueado (ni cuenta normal ni técnico)
        if (session.getAttribute("account") == null && session.getAttribute("technician") == null) {

            // Reconstruimos la URL a la que intentaban ir
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();

            // Si la URL tenía parámetros (ej: /perfil?id=5), los añadimos también
            String fullUrl = (queryString == null) ? requestURI : requestURI + "?" + queryString;

            // Guardamos la URL en la sesión (variable para LoginController)
            session.setAttribute("nextUrl", fullUrl);

            // Redirigimos al login y bloqueamos el acceso
            response.sendRedirect("/login");
            return false; // Retornar false significa "detener la petición aquí"
        }

        // Si hay alguien en la sesión, dejamos que la petición continúe
        return true;
    }
}
