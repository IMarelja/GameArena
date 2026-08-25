package hr.algebra.gamearena.webapp.models.rest;

import java.util.List;

public record RestError(String message) {
    public static RestError fromString(String message) {
        return new RestError(message);
    }

    public static List<RestError> fromListString(List<String> messages) {
        return messages.stream().map(RestError::fromString).toList();
    }
}
