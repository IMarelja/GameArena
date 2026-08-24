package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends GameArenaServiceException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, "forbidden", message);
    }
}
