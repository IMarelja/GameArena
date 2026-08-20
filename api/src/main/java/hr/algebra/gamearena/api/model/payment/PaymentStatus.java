package hr.algebra.gamearena.api.model.payment;

import hr.algebra.gamearena.api.orm.postgres.payment.PaymentStatusPostgres;

public enum PaymentStatus {
    PENDING,
    PAID,
    FAILED,
    REFUNDED,
    CANCELLED;

    public static PaymentStatus fromPaymentStatusPostgres(PaymentStatusPostgres paymentStatusPostgres) {
        return PaymentStatus.valueOf(paymentStatusPostgres.name());
    }
}
