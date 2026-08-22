package hr.algebra.gamearena.api.service.payment;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;

public interface IPaymentService<T> {
    T pay(Long paymentId, PaymentRequest paymentRequest);
}
