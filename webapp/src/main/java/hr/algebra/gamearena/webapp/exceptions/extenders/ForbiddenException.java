package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ForbiddenException extends GameArenaApiServiceException {

    public ForbiddenException(List<String> messages) {
        super(HttpStatus.FORBIDDEN, "forbidden", messages);
    }
}
