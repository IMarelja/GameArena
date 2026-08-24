package hr.algebra.gamearena.webapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GameArenaServiceException extends Exception {
    private final HttpStatus status;
    private final String view;

    protected GameArenaServiceException(HttpStatus status, String view, String message) {
        super(message);
        this.status = status;
        this.view = view;
    }

}
