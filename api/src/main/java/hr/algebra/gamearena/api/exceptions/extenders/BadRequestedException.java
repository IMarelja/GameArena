package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class BadRequestedException extends GameArenaApiException {
    public BadRequestedException(String message) {
        super(message);
    }
}
