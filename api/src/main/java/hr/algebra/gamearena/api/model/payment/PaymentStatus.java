package hr.algebra.gamearena.api.model.payment;

import hr.algebra.gamearena.api.orm.postgres.payment.payment_status;

public enum PaymentStatus {
    PENDING,
    PAID,
    FAILED,
    REFUNDED,
    CANCELLED;

    public static PaymentStatus fromPaymentStatus(payment_status paymentStatusPostgres) {
        return PaymentStatus.valueOf(paymentStatusPostgres.name());
    }

    public payment_status toPaymentStatus() {
        return payment_status.valueOf(this.name());
    }
}
