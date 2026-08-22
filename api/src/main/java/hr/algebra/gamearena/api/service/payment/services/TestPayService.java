package hr.algebra.gamearena.api.service.payment.services;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.payment.responce.providers.TestPayResponseView;
import hr.algebra.gamearena.api.service.payment.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class TestPayService implements IPaymentService<TestPayResponseView> {

    @Override
    public TestPayResponseView pay(Long id, PaymentRequest paymentRequest) {
        return null;
    }
}
