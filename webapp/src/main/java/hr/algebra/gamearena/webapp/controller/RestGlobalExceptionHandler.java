package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import hr.algebra.gamearena.webapp.models.rest.RestError;
import hr.algebra.gamearena.webapp.models.rest.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.rest")
public class RestGlobalExceptionHandler {

    // GameArena site exceptions

    @ExceptionHandler(GameArenaServiceException.class)
    public ResponseEntity<RestResponse<Void>> handleGameArenaSiteException(GameArenaServiceException ex) {
        return RestResponse.<Void>error(ex.getStatus(), new RestError(ex.getMessage())).toResponseEntity();
    }

    // Third party

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<RestResponse<Void>> handleRestClientResponseException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return RestResponse.<Void>error(status, new RestError(ex.getMessage())).toResponseEntity();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<RestResponse<Void>> handleResourceAccessException(ResourceAccessException ex) {
        return RestResponse.<Void>error(
                HttpStatus.SERVICE_UNAVAILABLE,
                new RestError("Could not reach the GameArena API: " + ex.getMessage())
        ).toResponseEntity();
    }

    // Generic

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleException(Exception ex) {
        return RestResponse.<Void>error(HttpStatus.INTERNAL_SERVER_ERROR, new RestError(ex.getMessage())).toResponseEntity();
    }
}
