package hr.algebra.gamearena.webapp.service.notification;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface INotificationService {
    void setReadStatus(Long id, boolean read) throws UnauthorizedException, ForbiddenException, NotFoundException;
    SseEmitter streamUnread() throws UnauthorizedException;
    SseEmitter streamAll() throws UnauthorizedException;
}
