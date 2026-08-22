package hr.algebra.gamearena.api.dto.payment.responce.providers;

import hr.algebra.gamearena.api.model.payment.paypal.PaypalPayment;

public record PaypalResponseView(
        String paypalOrderId,
        String approveUrl,
        String status
) implements PaymentProviderResponse {
    public static PaypalResponseView fromPaypalPaymentAndApprovalUrl(PaypalPayment paypalPayment, String approveUrl) {
        return new PaypalResponseView(
                paypalPayment.paypalOrderId(),
                approveUrl,
                paypalPayment.status()
        );
    }
}
