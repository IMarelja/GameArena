package hr.algebra.gamearena.api.dto.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private T data;
    private List<ApiError> errors;

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.data = data;
        this.errors = null;
    }

    public ApiResponse(List<ApiError> errors) {
        this.errors = errors;
        this.data = null;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> errors(List<ApiError> errors) {
        return new ApiResponse<>(errors);
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(List.of(error));
    }
}

