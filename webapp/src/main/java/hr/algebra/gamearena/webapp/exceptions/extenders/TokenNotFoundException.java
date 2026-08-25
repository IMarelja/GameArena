package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends GameArenaApiServiceException {
    public TokenNotFoundException() {
        super(HttpStatus.BAD_REQUEST,"error", "Token not found, you need to log in again");
    }
}
