package hr.algebra.gamearena.webapp.models.mvc;

import java.util.List;

public record MvcError(String message) {
    public static MvcError fromString(String message) {
        return new MvcError(message);
    }

    public static List<MvcError> fromListString(List<String> message) {
        return message.stream().map(MvcError::fromString).toList();
    }
}
