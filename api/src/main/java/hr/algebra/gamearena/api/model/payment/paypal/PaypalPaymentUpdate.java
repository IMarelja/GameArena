package hr.algebra.gamearena.api.model.payment.paypal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaypalPaymentUpdate {
    private String paypalPayerId;
    private String captureId;
    private String status;
}
