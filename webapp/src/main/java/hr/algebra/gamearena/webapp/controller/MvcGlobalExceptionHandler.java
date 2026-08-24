package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.mvc")
public class MvcGlobalExceptionHandler {

    // GameArena site exceptions

    @ExceptionHandler(GameArenaServiceException.class)
    public ModelAndView handleGameArenaSiteException(GameArenaServiceException ex) {
        return MvcResponse.error(ex.getStatus(), ex.getView(), new MvcError(ex.getMessage())).toModelAndView();
    }

    // Third party

    @ExceptionHandler(RestClientResponseException.class)
    public ModelAndView handleRestClientResponseException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return MvcResponse.error(status, "error", new MvcError(ex.getMessage())).toModelAndView();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleResourceAccessException(ResourceAccessException ex) {
        return MvcResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE,
                "error",
                new MvcError("Could not reach the GameArena API: " + ex.getMessage())
        ).toModelAndView();
    }

    // Generic

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return MvcResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "error", new MvcError(ex.getMessage())).toModelAndView();
    }
}
