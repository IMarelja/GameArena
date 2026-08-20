package hr.algebra.gamearena.api.model.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentSave {
    private BigDecimal amount;
    private String currency;
}
