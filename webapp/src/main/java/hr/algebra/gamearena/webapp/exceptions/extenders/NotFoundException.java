package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GameArenaServiceException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "notfound", message);
    }
}
