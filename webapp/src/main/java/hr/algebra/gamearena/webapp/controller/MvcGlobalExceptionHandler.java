package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.mvc")
public class MvcGlobalExceptionHandler {

    private static final String ERROR_VIEW = "error";

    // Shared across every page for the navbar

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return AuthenticatedUser.isAuthenticated();
    }



    // GameArena site exceptions

    @ExceptionHandler(GameArenaApiServiceException.class)
    public ModelAndView handleGameArenaSiteException(GameArenaApiServiceException ex) {
        List<MvcError> errors = ex.getMessages().stream().map(MvcError::new).toList();
        return MvcResponse.errors(ex.getStatus(), ex.getView(), errors).toModelAndView();
    }

    // Third party

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) throws AccessDeniedException {
        throw ex;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException ex) throws AuthenticationException {
        throw ex;
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ModelAndView handleRestClientResponseException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return MvcResponse.error(
                status,
                ERROR_VIEW,
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleResourceAccessException(ResourceAccessException ex) {
        return MvcResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE,
                ERROR_VIEW,
                new MvcError("Could not reach the GameArena API: " + ex.getMessage())
        ).toModelAndView();
    }

    // Generic

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return MvcResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ERROR_VIEW,
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }
}
