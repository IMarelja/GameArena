package hr.algebra.gamearena.webapp.models.rest.notification;

import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationDecereal;

import java.time.OffsetDateTime;
import java.util.Optional;

public record NotificationDto(
    Long id,
    NotificationTypeDto type,
    Optional<Long> referenceId,
    Optional<NotificationReferenceTypeDto> referenceType,
    Boolean read,
    OffsetDateTime createdAt
) {
    public static NotificationDto fromNotificationDecereal(NotificationDecereal decereal) {
        return new NotificationDto(
                decereal.id(),
                NotificationTypeDto.fromNotificationTypeDecereal(decereal.type()),
                decereal.referenceId(),
                NotificationReferenceTypeDto.fromNotificationTypeDecereal(decereal.referenceType()),
                decereal.read(),
                decereal.createdAt()
        );
    }
}
