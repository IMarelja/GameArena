package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GameArenaApiServiceException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "notfound", message);
    }
}
