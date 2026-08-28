package hr.algebra.gamearena.api.event.notification;

public record MatchCreatedEvent(Long recipientUserId, Long matchId) {
}
