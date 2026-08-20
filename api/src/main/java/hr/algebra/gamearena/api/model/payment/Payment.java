package hr.algebra.gamearena.api.model.payment;

import hr.algebra.gamearena.api.orm.postgres.payment.PaymentPostgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record Payment(
        Long id,
        PaymentStatus status,
        BigDecimal amount,
        String currency,
        OffsetDateTime createdAt
) {
    public static Payment fromPaymentPostgres(PaymentPostgres paymentPostgres) {
        return new Payment(
                paymentPostgres.getId(),
                PaymentStatus.fromPaymentStatusPostgres(paymentPostgres.getStatus()),
                paymentPostgres.getAmount(),
                paymentPostgres.getCurrency(),
                paymentPostgres.getCreatedAt()
        );
    }
}
