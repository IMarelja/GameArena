package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.ReferenceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Recipient user is required")
    private Long recipientUserId;

    private Long referenceId;

    private ReferenceType referenceType;
}
