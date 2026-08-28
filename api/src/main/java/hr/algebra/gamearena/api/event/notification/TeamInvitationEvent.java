package hr.algebra.gamearena.api.event.notification;

import hr.algebra.gamearena.api.model.notification.NotificationType;

public record TeamInvitationEvent(NotificationType type, Long recipientUserId, Long invitationId) {
}
