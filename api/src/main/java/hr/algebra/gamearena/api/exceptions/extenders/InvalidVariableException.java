package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class InvalidVariableException extends GameArenaApiException {
    public InvalidVariableException(String message) {
        super(message);
    }
}
