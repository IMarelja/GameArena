package hr.algebra.gamearena.webapp.controller.rest;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.rest.RestResponse;
import hr.algebra.gamearena.webapp.models.rest.notification.NotificationReadStatusRequest;
import hr.algebra.gamearena.webapp.service.notification.INotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class NotificationRestController {

    private final INotificationService notificationService;

    public NotificationRestController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/stream/unread", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    public SseEmitter streamUnread() throws UnauthorizedException {
        return notificationService.streamUnread();
    }

    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    public SseEmitter streamAll() throws UnauthorizedException {
        return notificationService.streamAll();
    }

    @PatchMapping("/{id}/read-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RestResponse<Void>> setReadStatus(
            @PathVariable Long id,
            @RequestBody NotificationReadStatusRequest request
    ) throws UnauthorizedException, ForbiddenException, NotFoundException {
        notificationService.setReadStatus(id, Boolean.TRUE.equals(request.read()));

        return RestResponse.<Void>success(HttpStatus.NO_CONTENT, null).toResponseEntity();
    }
}
