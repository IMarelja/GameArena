package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class UserNotFoundException extends GameArenaApiException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
