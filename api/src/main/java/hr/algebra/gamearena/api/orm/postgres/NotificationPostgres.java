package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;
import hr.algebra.gamearena.api.model.notification.ReferenceType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "notifications", indexes = @Index(name = "idx_notifications_recipient_user_id", columnList = "recipient_user_id"))
@DynamicInsert
public class NotificationPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", length = 50)
    private ReferenceType referenceType;

    @Column(name = "read_at", nullable = false)
    private boolean read;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public NotificationPostgres fromNotificationSave(NotificationSave notificationSave) {
        this.type = notificationSave.getType();
        this.recipientUserId = notificationSave.getRecipientUserId();
        this.referenceId = notificationSave.getReferenceId();
        this.referenceType = notificationSave.getReferenceType();
        this.read = false;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }

    public NotificationPostgres fromNotificationUpdate(NotificationUpdate notificationUpdate) {
        this.read = notificationUpdate.getRead();
        return this;
    }
}
