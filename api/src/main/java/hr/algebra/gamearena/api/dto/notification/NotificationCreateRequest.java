package hr.algebra.gamearena.api.dto.notification;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
    @NotNull(message = "Notification type is required")
    private NotificationTypeView type;

    @NotNull(message = "Recipient user is required")
    private Long recipientUserId;

    private Long referenceId;

    private ReferenceTypeView referenceType;
}
