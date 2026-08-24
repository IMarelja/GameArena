package hr.algebra.gamearena.webapp.models.service;

import com.gamearena.client.model.ApiError;

import java.util.List;

public class ApiResult<T> {
    private final T data;
    private final List<ApiWrong> errors;

    private ApiResult(T data, List<ApiWrong> errors) {
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data, List.of());
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(null, List.of(new ApiWrong(message)));
    }

    public static <T> ApiResult<T> fromDataApiError(T data, List<ApiError> clientErrors) {
        List<ApiWrong> errors = clientErrors == null
                ? List.of()
                : clientErrors.stream().map(e -> new ApiWrong(e.getMessage())).toList();
        return new ApiResult<>(data, errors);
    }

    public T getData() {
        return data;
    }

    public List<ApiWrong> getErrors() {
        return errors;
    }
}
