package hr.algebra.gamearena.webapp.models.service;

import com.gamearena.client.model.ApiError;

import java.util.ArrayList;
import java.util.List;

public record ApiWrong(String message) {
    public static List<ApiWrong> fromListApiErrorsOrNull(List<ApiError> errors) {
        List<ApiWrong> list = new ArrayList<>();

        if(errors == null || errors.isEmpty())
            return null;

        for (ApiError error : errors) {
            list.add(new ApiWrong(error.getMessage()));
        }
        return list;
    }
}
