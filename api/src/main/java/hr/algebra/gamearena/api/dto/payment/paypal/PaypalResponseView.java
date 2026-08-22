package hr.algebra.gamearena.api.dto.payment.paypal;

import hr.algebra.gamearena.api.model.payment.paypal.PaypalPayment;

public record PaypalResponseView(
        String paypalOrderId,
        String approveUrl,
        String status
) {
    public static PaypalResponseView fromPaypalPaymentAndApprovalUrl(PaypalPayment paypalPayment, String approveUrl) {
        return new PaypalResponseView(
                paypalPayment.paypalOrderId(),
                approveUrl,
                paypalPayment.status()
        );
    }
}
