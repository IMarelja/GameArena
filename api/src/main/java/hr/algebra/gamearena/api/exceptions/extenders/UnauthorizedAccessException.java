package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class UnauthorizedAccessException extends GameArenaApiException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
