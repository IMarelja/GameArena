package hr.algebra.gamearena.api.exceptions.extenders;

import hr.algebra.gamearena.api.exceptions.GameArenaApiException;

public class CryptographicOperationException extends GameArenaApiException {
    public CryptographicOperationException(String message) {
        super(message);
    }
}
