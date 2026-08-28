package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationTypeView;
import hr.algebra.gamearena.api.dto.notification.ReferenceTypeView;
import hr.algebra.gamearena.api.event.notification.MatchCreatedEvent;
import hr.algebra.gamearena.api.event.notification.TeamInvitationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final INotificationService notificationService;

    public NotificationEventListener(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onMatchCreated(MatchCreatedEvent event) {
        notificationService.createAndPush(new NotificationCreateRequest(
                NotificationTypeView.CREATED_MATCH,
                event.recipientUserId(),
                event.matchId(),
                ReferenceTypeView.MATCH));
    }

    @EventListener
    public void onTeamInvitation(TeamInvitationEvent event) {
        notificationService.createAndPush(new NotificationCreateRequest(
                NotificationTypeView.fromNotificationType(event.type()),
                event.recipientUserId(),
                event.invitationId(),
                ReferenceTypeView.TEAM_INVITATION));
    }
}
