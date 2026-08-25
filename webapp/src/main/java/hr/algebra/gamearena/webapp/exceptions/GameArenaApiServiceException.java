package hr.algebra.gamearena.webapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GameArenaApiServiceException extends Exception {
    private final HttpStatus status;
    private final String view;

    protected GameArenaApiServiceException(HttpStatus status, String view, String message) {
        super(message);
        this.status = status;
        this.view = view;
    }

}
