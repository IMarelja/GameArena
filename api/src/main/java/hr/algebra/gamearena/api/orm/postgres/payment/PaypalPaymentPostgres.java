package hr.algebra.gamearena.api.orm.postgres.payment;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(name = "paypal_payments", indexes = {
        @Index(name = "idx_paypal_payments_capture_id", columnList = "capture_id"),
        @Index(name = "idx_paypal_payments_paypal_payer_id", columnList = "paypal_payer_id")
})
@DynamicInsert
public class PaypalPaymentPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column(name = "paypal_order_id", nullable = false, unique = true, length = 64)
    private String paypalOrderId;

    @Column(name = "paypal_payer_id", length = 64)
    private String paypalPayerId;

    @Column(name = "capture_id", length = 64)
    private String captureId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
