package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class UnexpectedApiErrorException extends GameArenaApiServiceException {
    public UnexpectedApiErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "error", message);
    }

    public UnexpectedApiErrorException(List<String> messages) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "error", messages);
    }
}
