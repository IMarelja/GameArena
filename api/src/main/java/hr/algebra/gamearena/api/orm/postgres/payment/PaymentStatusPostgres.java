package hr.algebra.gamearena.api.orm.postgres.payment;

public enum PaymentStatusPostgres {
    PENDING,
    PAID,
    FAILED,
    REFUNDED,
    CANCELLED
}
