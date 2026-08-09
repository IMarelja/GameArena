package hr.algebra.gamearena.api.model.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSave {
    NotificationType type;
    Long recipientUserId;
    Long referenceId;
    ReferenceType referenceType;
}
