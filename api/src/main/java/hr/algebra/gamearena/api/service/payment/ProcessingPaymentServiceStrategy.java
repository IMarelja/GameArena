package hr.algebra.gamearena.api.service.payment;

import hr.algebra.gamearena.api.dto.payment.PaymentTypes;
import hr.algebra.gamearena.api.exceptions.extenders.InvalidVariableException;
import hr.algebra.gamearena.api.service.payment.services.PaypalService;
import hr.algebra.gamearena.api.service.payment.services.TestPayService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProcessingPaymentServiceStrategy {

    private final Map<PaymentTypes, IPaymentService> paymentServicesByType;

    public ProcessingPaymentServiceStrategy(
            PaypalService paypalService,
            TestPayService testPayService
    ) {
        this.paymentServicesByType = Map.of(
                PaymentTypes.PAYPAL, paypalService,
                PaymentTypes.TEST, testPayService
        );
    }

    public IPaymentService resolve(PaymentTypes paymentType) {
        var paymentService = paymentServicesByType.get(paymentType);
        if (paymentService == null) {
            throw new InvalidVariableException("Unsupported payment type: " + paymentType);
        }

        return paymentService;
    }
}
