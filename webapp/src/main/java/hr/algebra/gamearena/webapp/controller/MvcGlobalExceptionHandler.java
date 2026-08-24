package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Last resort for hr.algebra.gamearena.webapp.controller.mvc controllers: expected,
 * recoverable failures should be returned as an MvcResponse.error(...) from the
 * controller itself. This only catches what actually escapes - typically the
 * generated OpenAPI client blowing up further down the call stack.
 */
@ControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.mvc")
public class MvcGlobalExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ModelAndView handleRestClientResponseException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return MvcResponse.error(
                status,
                "error",
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleResourceAccessException(ResourceAccessException ex) {
        return MvcResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE,
                "error",
                new MvcError("Could not reach the GameArena API: " + ex.getMessage())
        ).toModelAndView();
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return MvcResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "error",
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }
}
