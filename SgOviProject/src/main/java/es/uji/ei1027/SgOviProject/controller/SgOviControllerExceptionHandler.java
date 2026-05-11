package es.uji.ei1027.SgOviProject.controller;

import es.uji.ei1027.SgOviProject.exception.SgOviException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SgOviControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("errorName", "Error inesperat");

        // Obtenemos la página de la que viene el usuario
        String referer = request.getHeader("Referer");
        // Si no hay referer (ej. entró directo a la URL), mandamos al inicio por defecto
        mav.addObject("backUrl", (referer != null) ? referer : "/");
        return mav;
    }

    @ExceptionHandler(value = SgOviException.class)
    public ModelAndView handleException(SgOviException ex, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("errorName", ex.getErrorName());

        String referer = request.getHeader("Referer");
        mav.addObject("backUrl", (referer != null) ? referer : "/");
        return mav;
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                              HttpServletRequest request){
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "No es pot esborrar o modificar el registre perquè té dades associades o s'ha violat la integritat de la base de dades.");
        mav.addObject("errorName", "Error d'Integritat de Dades");

        String referer = request.getHeader("Referer");
        mav.addObject("backUrl", (referer != null) ? referer : "/");
        return mav;
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ModelAndView handleDataAccessException(DataAccessException ex,
                                                  HttpServletRequest request){
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "El servei de dades no està disponible actualment o hi ha un error a la base de dades. Disculpeu les molèsties.");
        mav.addObject("errorName", "Error de Base de Dades");

        String referer = request.getHeader("Referer");
        mav.addObject("backUrl", (referer != null) ? referer : "/");
        return mav;
    }
}
