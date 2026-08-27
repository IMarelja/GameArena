package hr.algebra.gamearena.webapp.controller.rest;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationDecereal;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationUnreadCountDecereal;
import hr.algebra.gamearena.webapp.models.rest.RestResponse;
import hr.algebra.gamearena.webapp.models.rest.TypedSseEmitter;
import hr.algebra.gamearena.webapp.models.rest.notification.NotificationReadStatusRequest;
import hr.algebra.gamearena.webapp.service.notification.INotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {

    private final INotificationService notificationService;

    public NotificationRestController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/stream/unread", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    public TypedSseEmitter<NotificationUnreadCountDecereal> streamUnreadCount() throws UnauthorizedException {
        return notificationService.streamUnreadCount();
    }

    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    public TypedSseEmitter<List<NotificationDecereal>> streamAll() throws UnauthorizedException {
        return notificationService.streamAll();
    }

    /** This should be illegal */

    @PatchMapping("/{id}/read-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RestResponse<Void>> setReadStatus(
            @PathVariable Long id,
            @RequestBody NotificationReadStatusRequest request
    ) throws UnauthorizedException, ForbiddenException, NotFoundException {
        notificationService.setReadStatus(id, Boolean.TRUE.equals(request.read()));

        return RestResponse.<Void>success(HttpStatus.NO_CONTENT, null).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RestResponse<Void>> deleteNotification(@PathVariable Long id)
            throws UnauthorizedException, ForbiddenException, NotFoundException {
        notificationService.deleteNotification(id);

        return RestResponse.<Void>success(HttpStatus.NO_CONTENT, null).toResponseEntity();
    }
}
