package hr.algebra.gamearena.api.model.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceSave {
    private Long userId;
    private Long billingInfoId;
    private Long paymentId;
}
