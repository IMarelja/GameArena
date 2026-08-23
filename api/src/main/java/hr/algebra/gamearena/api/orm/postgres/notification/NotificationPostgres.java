package hr.algebra.gamearena.api.orm.postgres.notification;

import hr.algebra.gamearena.api.model.notification.NotificationSave;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.NotificationUpdate;
import hr.algebra.gamearena.api.model.notification.ReferenceType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "notifications", indexes = @Index(name = "idx_notifications_recipient_user_id", columnList = "recipient_user_id"))
@DynamicInsert
public class NotificationPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "read_at", nullable = false)
    private boolean read;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public NotificationPostgres fromNotificationSave(NotificationSave notificationSave) {
        this.type = notificationSave.getType().toString();
        this.recipientUserId = notificationSave.getRecipientUserId();
        this.referenceId = notificationSave.getReferenceId();
        this.referenceType = notificationSave.getReferenceType().toString();
        this.read = false;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }

    public NotificationPostgres fromNotificationUpdate(NotificationUpdate notificationUpdate) {
        this.read = notificationUpdate.getRead();
        return this;
    }
}
