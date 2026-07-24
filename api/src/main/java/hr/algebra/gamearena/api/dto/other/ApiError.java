package hr.algebra.gamearena.api.dto.other;

import lombok.Getter;

@Getter
public class ApiError {

    private String message;

    public ApiError() {}

    public ApiError(String message) {
        this.message = message;
    }

}
