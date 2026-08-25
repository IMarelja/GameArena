package hr.algebra.gamearena.webapp.exceptions.extenders;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class NotFoundException extends GameArenaApiServiceException {

    public NotFoundException(List<String> messages) {
        super(HttpStatus.NOT_FOUND, "notfound", messages);
    }
}
