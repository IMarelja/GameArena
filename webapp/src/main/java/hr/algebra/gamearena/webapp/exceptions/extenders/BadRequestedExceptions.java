package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import org.springframework.http.HttpStatus;

public class BadRequestedExceptions extends GameArenaServiceException {
    public BadRequestedExceptions(String message) {
        super(HttpStatus.BAD_REQUEST, "error", message);
    }
}
