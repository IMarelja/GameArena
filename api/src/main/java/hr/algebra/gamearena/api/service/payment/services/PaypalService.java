package hr.algebra.gamearena.api.service.payment.services;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.payment.responce.providers.PaypalResponseView;
import hr.algebra.gamearena.api.service.payment.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaypalService implements IPaymentService<PaypalResponseView> {


    @Override
    public PaypalResponseView pay(Long paymentId, PaymentRequest paymentRequest) {
        return null;
    }
}
