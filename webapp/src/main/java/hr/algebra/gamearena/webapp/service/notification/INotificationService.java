package hr.algebra.gamearena.webapp.service.notification;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationDecereal;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationUnreadCountDecereal;
import hr.algebra.gamearena.webapp.models.rest.TypedSseEmitter;

import java.util.List;

public interface INotificationService {
    void setReadStatus(Long id, boolean read) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
    void deleteNotification(Long id) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
    TypedSseEmitter<NotificationUnreadCountDecereal> streamUnreadCount() throws UnauthorizedException;
    TypedSseEmitter<List<NotificationDecereal>> streamAll() throws UnauthorizedException;
}
