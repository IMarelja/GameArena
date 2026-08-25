package hr.algebra.gamearena.webapp.models.service;

import com.gamearena.client.model.ApiError;
import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiResult<T>(
        T data,
        List<ApiWrong> errors,
        HttpStatus status
) {
    public static <T> ApiResult<T> fromApiResponseClient(HttpStatus status, T data, List<ApiError> errors) {
        List<ApiWrong> wrongs = ApiWrong.fromListApiErrors(errors);
        return new ApiResult<>(
                data,
                wrongs,
                status
        );
    }
}
