package hr.algebra.gamearena.webapp.models.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {
    private final T data;
    private final List<RestError> errors;
    private final HttpStatus status;

    private RestResponse(T data, List<RestError> errors, HttpStatus status) {
        this.data = data;
        this.errors = errors;
        this.status = status;
    }

    public static <T> RestResponse<T> success(HttpStatus status, T data) {
        return new RestResponse<>(data, List.of(), status);
    }

    public static <T> RestResponse<T> error(HttpStatus status, RestError error) {
        return new RestResponse<>(null, List.of(error), status);
    }

    public static <T> RestResponse<T> errors(HttpStatus status, List<RestError> errors) {
        return new RestResponse<>(null, errors, status);
    }

    public static <T> RestResponse<T> fromApiResult(ApiResult<T> result) {
        List<RestError> errors = result.errors().stream()
                .map(e -> new RestError(e.message()))
                .toList();
        return new RestResponse<>(result.data(), errors, result.status());
    }

    public ResponseEntity<RestResponse<T>> toResponseEntity() {
        return ResponseEntity.status(status).body(this);
    }
}
