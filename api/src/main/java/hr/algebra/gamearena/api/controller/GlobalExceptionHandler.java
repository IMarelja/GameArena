package hr.algebra.gamearena.api.controller;

import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.yaml.snakeyaml.nodes.Tag.STR;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity.status(500).body(ApiResponse.error(new ApiError("Internal server error: " + ex.getMessage())));
    }
}
