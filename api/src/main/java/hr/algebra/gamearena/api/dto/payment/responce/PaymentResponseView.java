package hr.algebra.gamearena.api.dto.payment.responce;

import hr.algebra.gamearena.api.dto.payment.responce.providers.PaymentProviderResponse;
import hr.algebra.gamearena.api.model.payment.Payment;

import java.util.Optional;

public record PaymentResponseView(
        Long paymentId,
        Optional<PaymentProviderResponse> provider,
        Optional<String> message,
        PaymentStagesView stage
) {
    public static PaymentResponseView justStatus(PaymentStagesView stage) {
        return new PaymentResponseView(null, Optional.empty(), Optional.empty(), stage);
    }

    public static PaymentResponseView actionRequired(Payment payment, PaymentProviderResponse data) {
        return new PaymentResponseView(payment.id(), Optional.ofNullable(data), Optional.empty(), PaymentStagesView.PROCESSING_PAYMENT);
    }

    public static PaymentResponseView success(Payment payment, PaymentProviderResponse data) {
        return new PaymentResponseView(payment.id(), Optional.ofNullable(data), Optional.empty(), PaymentStagesView.COMPLETED);
    }

    public static PaymentResponseView failure(Long paymentId, String message) {
        return new PaymentResponseView(paymentId, Optional.empty(), Optional.ofNullable(message), PaymentStagesView.FAILED);
    }
}
