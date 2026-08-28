package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ConflictException extends GameArenaApiServiceException {
    public ConflictException(List<String> messages) {
        super(HttpStatus.CONFLICT, "error", messages);
    }
}
