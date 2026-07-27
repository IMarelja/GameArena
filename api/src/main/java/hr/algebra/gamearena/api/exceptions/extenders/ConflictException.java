package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class ConflictException extends GameArenaApiException {
    public ConflictException(String message) {
        super(message);
    }
}
