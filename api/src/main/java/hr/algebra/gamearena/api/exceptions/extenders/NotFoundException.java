package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class NotFoundException extends GameArenaApiException {
    public NotFoundException(String message) {
        super(message);
    }
}
