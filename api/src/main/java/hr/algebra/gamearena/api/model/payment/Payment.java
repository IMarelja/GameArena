package hr.algebra.gamearena.api.model.payment;

import hr.algebra.gamearena.api.orm.postgres.payment.PaymentPostgres;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record Payment(
        Long id,
        PaymentStatus status,
        BigDecimal amount,
        String currency,
        OffsetDateTime created_at
) {
    public static Payment fromPaymentPostgres(final PaymentPostgres paymentPostgres) {
        return new Payment(
                paymentPostgres.getId(),
                PaymentStatus.fromPaymentStatusPostgres(paymentPostgres.getStatus()),
                paymentPostgres.getAmount(),
                paymentPostgres.getCurrency(),
                paymentPostgres.getCreatedAt()
        );
    }
}
