package hr.algebra.gamearena.api.model.payment.paypal;

import hr.algebra.gamearena.api.orm.postgres.payment.PaypalPaymentPostgres;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record PaypalPayment(
        Long id,
        Long paymentId,
        String paypalOrderId,
        String paypalPayerId,
        String captureId,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PaypalPayment fromPaypalPaymentPostgres(PaypalPaymentPostgres paypalPaymentPostgres) {
        return new PaypalPayment(
                paypalPaymentPostgres.getId(),
                paypalPaymentPostgres.getPaymentId(),
                paypalPaymentPostgres.getPaypalOrderId(),
                paypalPaymentPostgres.getPaypalPayerId(),
                paypalPaymentPostgres.getCaptureId(),
                paypalPaymentPostgres.getStatus(),
                paypalPaymentPostgres.getCreatedAt(),
                paypalPaymentPostgres.getUpdatedAt()
        );
    }
}
