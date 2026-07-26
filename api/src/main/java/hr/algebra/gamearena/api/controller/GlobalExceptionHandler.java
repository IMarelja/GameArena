package hr.algebra.gamearena.api.controller;

import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.exceptions.GameArenaApiException;
import hr.algebra.gamearena.api.exceptions.extenders.CryptographicOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(CryptographicOperationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCryptographicOperationException(CryptographicOperationException ex) {
        log.error("Cryptographic error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Cryptographic error: " + ex.getMessage())));
    }

    @ExceptionHandler(GameArenaApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleGameArenaApiException(GameArenaApiException ex) {
        log.error("Undefined GameArenaApiException error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new ApiError("Undefined GameArenaApiException error: " + ex.getMessage())));
    }

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
