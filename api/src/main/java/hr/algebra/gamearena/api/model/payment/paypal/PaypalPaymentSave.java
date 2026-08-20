package hr.algebra.gamearena.api.model.payment.paypal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalPaymentSave {
    private Long paymentId;
    private String paypalOrderId;
    private String status;
}
