package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaServiceException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GameArenaServiceException {
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, "unauthorized", message);
    }
}
