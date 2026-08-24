package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.models.rest.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.rest")
public class RestExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<RestResponse<Void>> handleRestClientResponseException(RestClientResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(RestResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<RestResponse<Void>> handleResourceAccessException(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(RestResponse.error("Could not reach the GameArena API: " + ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RestResponse.error(ex.getMessage()));
    }
}
