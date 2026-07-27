package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class ForbiddenAccessException extends GameArenaApiException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
