package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import hr.algebra.gamearena.webapp.models.rest.RestError;
import hr.algebra.gamearena.webapp.models.rest.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@RestControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.rest")
@Slf4j
public class RestGlobalExceptionHandler {

    // GameArena site exceptions

    @ExceptionHandler(GameArenaApiServiceException.class)
    public ResponseEntity<RestResponse<Void>> handleGameArenaSiteException(GameArenaApiServiceException ex) {
        List<RestError> errors = ex.getMessages().stream().map(RestError::new).toList();
        return RestResponse.<Void>errors(ex.getStatus(), errors).toResponseEntity();
    }

    // Third party

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) throws AccessDeniedException {
        throw ex;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RestResponse<Void>> handleAuthenticationException(AuthenticationException ex) throws AuthenticationException {
        throw ex;
    }

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
    public ResponseEntity<RestResponse<Void>> handleException(Exception ex, HttpServletResponse response) {
        if (response.isCommitted()) {
            log.debug("RestGlobalExceptionHandler: exception on already-committed response - {}", ex.getMessage());
            return null;
        }

        return RestResponse.<Void>error(HttpStatus.INTERNAL_SERVER_ERROR, new RestError(ex.getMessage())).toResponseEntity();
    }
}
