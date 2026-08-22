package hr.algebra.gamearena.api.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NotNull(message = "Payment type is required")
    private PaymentTypes paymentType;

    @NotNull(message = "Billing details are required")
    private BillingDetailsRequest paymentDetails;
}
