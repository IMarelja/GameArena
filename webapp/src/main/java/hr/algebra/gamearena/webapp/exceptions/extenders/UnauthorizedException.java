package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UnauthorizedException extends GameArenaApiServiceException {

    public UnauthorizedException(List<String> messages) {
        super(HttpStatus.UNAUTHORIZED, "unauthorized", messages);
    }
}
