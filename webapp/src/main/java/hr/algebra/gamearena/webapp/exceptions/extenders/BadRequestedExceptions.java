package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestedExceptions extends GameArenaApiServiceException {
    public BadRequestedExceptions(List<String> messages) {
        super(HttpStatus.BAD_REQUEST, "error", messages);
    }
}
