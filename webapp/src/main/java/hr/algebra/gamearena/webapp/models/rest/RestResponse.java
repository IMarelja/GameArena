package hr.algebra.gamearena.webapp.models.rest;

import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public class RestResponse<T> {
    private final T data;
    private final List<RestError> errors;

    private RestResponse(T data, List<RestError> errors) {
        this.data = data;
        this.errors = errors;
    }

    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>(data, List.of());
    }

    public static <T> RestResponse<T> error(String message) {
        return new RestResponse<>(null, List.of(new RestError(message)));
    }

    public static <T> RestResponse<T> from(ApiResult<T> apiResult) {
        List<RestError> errors = apiResult.getErrors().stream()
                .map(e -> new RestError(e.message()))
                .toList();
        return new RestResponse<>(apiResult.getData(), errors);
    }

    public T getData() {
        return data;
    }

    public List<RestError> getErrors() {
        return errors;
    }
}
