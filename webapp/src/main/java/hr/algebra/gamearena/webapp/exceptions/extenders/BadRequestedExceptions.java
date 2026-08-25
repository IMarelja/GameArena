package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

public class BadRequestedExceptions extends GameArenaApiServiceException {
    public BadRequestedExceptions(String message) {
        super(HttpStatus.BAD_REQUEST, "error", message);
    }
}
