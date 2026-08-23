package hr.algebra.gamearena.api.orm.postgres.payment;

import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.model.payment.PaymentUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "payments")
@DynamicInsert
public class PaymentPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "payment_status")
    private payment_status status;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public PaymentPostgres fromPaymentSave(PaymentSave save) {
        this.status = payment_status.PENDING;
        this.amount = save.getAmount();
        this.currency = save.getCurrency();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }

    public PaymentPostgres fromPaymentUpdate(PaymentUpdate update) {
        this.status = update.getStatus().toPaymentStatus();
        return this;
    }
}
