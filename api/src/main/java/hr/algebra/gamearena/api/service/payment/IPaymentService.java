package hr.algebra.gamearena.api.service.payment;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.payment.responce.providers.PaymentProviderResponse;

public interface IPaymentService {
    PaymentProviderResponse pay(Long paymentId, PaymentRequest paymentRequest);
}
