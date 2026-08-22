package hr.algebra.gamearena.api.service.payment.services;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.payment.paypal.PaypalResponseView;
import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.service.payment.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaypalService implements IPaymentService<PaypalResponseView> {

    @Override
    public PaypalResponseView pay(PaymentRequest paymentRequest, Payment payment) {
        throw new UnsupportedOperationException("PayPal payments are not implemented yet");
    }
}
