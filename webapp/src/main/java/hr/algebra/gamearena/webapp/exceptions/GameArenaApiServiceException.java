package hr.algebra.gamearena.webapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class GameArenaApiServiceException extends Exception {
    private final HttpStatus status;
    private final String view;
    private final List<String> messages;

    protected GameArenaApiServiceException(HttpStatus status, String view, List<String> messages) {
        super(String.join("; ", messages));
        this.status = status;
        this.view = view;
        this.messages = messages;
    }

    protected GameArenaApiServiceException(HttpStatus status, String view, String message) {
        this(status, view, List.of(message));
    }

}
