package hr.algebra.gamearena.api.service.payment;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.model.payment.Payment;

public interface IPaymentService<T> {
    // Returns whatever the user needs to complete the transaction (e.g. an approval URL),
    // or null if this payment type settles synchronously with nothing further required.
    T pay(PaymentRequest paymentRequest, Payment payment);
}
