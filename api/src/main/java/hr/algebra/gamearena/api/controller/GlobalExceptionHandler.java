package hr.algebra.gamearena.api.controller;

import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.exceptions.GameArenaApiException;
import hr.algebra.gamearena.api.exceptions.extenders.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // GameArena custom API exceptions

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(new ApiError("Conflict: " + ex.getMessage())));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(new ApiError(ex.getMessage())));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ApiResponse<ApiError>> forbiddenAccessException(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(new ApiError(ex.getMessage())));
    }

    @ExceptionHandler(InvalidVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidVariableException(InvalidVariableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(new ApiError("Invalid variables: " + ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedAccessException(UnauthorizedAccessException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(new ApiError(e.getMessage())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(new ApiError(e.getMessage())));
    }

    @ExceptionHandler(CryptographicOperationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCryptographicOperationException(CryptographicOperationException ex) {
        log.error("Cryptographic error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Cryptographic error: " + ex.getMessage())));
    }

    @ExceptionHandler(BadRequestedException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestedException(BadRequestedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(new ApiError("Bad request: " + ex.getMessage())));
    }

    @ExceptionHandler(GameArenaApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleGameArenaApiException(GameArenaApiException ex) {
        log.error("Undefined GameArenaApiException error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Undefined GameArenaApiException error: " + ex.getMessage())));
    }

    // Third party exceptions

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex) throws AccessDeniedException {
        // For SecurityErrorHandler to handle if it is 401 or 403 error code
        throw ex;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        List<ApiError> errors = ex.getAllErrors().stream()
                .map(error -> new ApiError(error.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.errors(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ApiError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ApiError(violation.getMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.errors(errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ApiError(error.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.errors(errors));
    }

    // Generic exceptions

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(RuntimeException ex) {
        log.error("Undefined RuntimeException error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Undefined RuntimeException error: " + ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Undefined Exception error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Undefined Exception error: " + ex.getMessage())));
    }
}
