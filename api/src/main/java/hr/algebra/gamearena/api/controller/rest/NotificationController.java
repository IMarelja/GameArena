package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.notification.NotificationFullView;
import hr.algebra.gamearena.api.dto.notification.NotificationUnreadCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationMinimalView;
import hr.algebra.gamearena.api.dto.notification.NotificationUnreadAndCountView;
import hr.algebra.gamearena.api.dto.notification.NotificationTypeView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.notification.INotificationService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/stream/unread",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
            schema = @Schema(implementation = NotificationUnreadAndCountView.class)))
    public Flux<ServerSentEvent<NotificationUnreadAndCountView>> notificationStream(@AuthenticationPrincipal JwtTokenClaim caller){
        return notificationService.streamForUserUnreadAndCount(caller.userId())
                .map(snapshot -> ServerSentEvent.builder(snapshot).event("unread-notifications").build());
    }

    @GetMapping(value = "/stream/unread/count/",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
            schema = @Schema(implementation = NotificationUnreadCountView.class)))
    public Flux<ServerSentEvent<NotificationUnreadCountView>> notificationCountStream(@AuthenticationPrincipal JwtTokenClaim caller){
        return notificationService.streamToUserUnreadCount(caller.userId())
                .map(snapshot -> ServerSentEvent.builder(snapshot)
                        .event("unread-count")
                        .build()
                );
    }

    @GetMapping(value = "/stream/all",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("isAuthenticated()")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.TEXT_EVENT_STREAM_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = NotificationMinimalView.class))))
    public Flux<ServerSentEvent<List<NotificationMinimalView>>> notificationAllStream(@AuthenticationPrincipal JwtTokenClaim caller){
        return notificationService.streamForUserAll(caller.userId())
                .map(all -> all
                        .stream()
                        .map(NotificationFullView::toMinimalView)
                        .toList()
                )
                .map(snapshot -> ServerSentEvent.builder(snapshot)
                        .event("all-notifications")
                        .build()
                );
    }

    /*
     * why the fuck do I need to configure so much shit for basic shit, and it doesn't even make any sense. No wonder people Vibe code you
     * */

    @PostMapping("/test/to/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationMinimalView>> createTestNotification(@PathVariable("id") Long userId){
        var createRequest = new NotificationCreateRequest(NotificationTypeView.TEST, userId, null, null);

        return ResponseEntity.ok(ApiResponse.success(notificationService.createAndPush(createRequest)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<NotificationMinimalView>> updateNotification(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable("id") Long notificationId,
            @Valid @RequestBody NotificationEditRequest editRequest){
        if (notificationService.doesUserOwnNotification(caller.userId(), notificationId)) {
            throw new ForbiddenAccessException("You do not own this notification");
        }

        return notificationService.updateAndPush(notificationId, editRequest)
                .map(view -> ResponseEntity.ok(ApiResponse.success(view)))
                .orElseThrow(() -> new NotFoundException("Notification with id: " + notificationId + " not found"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNotification(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable("id") Long notificationId
    ){
        if (notificationService.doesUserOwnNotification(caller.userId(), notificationId)) {
            throw new ForbiddenAccessException("You do not own this notification");
        }

        notificationService.deleteByIdAndPush(notificationId);

        return ResponseEntity.noContent().build();
    }
}
