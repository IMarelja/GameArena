package hr.algebra.gamearena.api.dto.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private T data;
    private List<ApiError> errors;

    private ApiResponse() {}

    private ApiResponse(T data) {
        this.data = data;
        this.errors = null;
    }

    private ApiResponse(List<ApiError> errors) {
        this.errors = errors;
        this.data = null;
    }

    private ApiResponse(T data, List<ApiError> errors) {
        this.data = data;
        this.errors = errors;
    }

    private ApiResponse(T data, ApiError error) {
        this.data = data;
        this.errors = List.of(error);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(List.of(error));
    }

    public static <T> ApiResponse<T> errors(List<ApiError> errors) {
        return new ApiResponse<>(errors);
    }

    public static <T> ApiResponse<T> errorDataOnly(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> errorsWithData(T data, List<ApiError> errors) {
        return new ApiResponse<>(data, errors);
    }

    public static <T> ApiResponse<T> errorWithData(T data, List<ApiError> errors) {
        return new ApiResponse<>(data, errors);
    }
}

