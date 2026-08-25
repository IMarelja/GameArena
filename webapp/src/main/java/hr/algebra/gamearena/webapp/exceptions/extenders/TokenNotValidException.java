package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

public class TokenNotValidException extends GameArenaApiServiceException {
    public TokenNotValidException() {
        super(HttpStatus.UNAUTHORIZED,"unauthorized","Session is expired or invalid, log in again");
    }
}
