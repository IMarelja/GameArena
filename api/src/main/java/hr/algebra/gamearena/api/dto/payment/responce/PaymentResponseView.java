package hr.algebra.gamearena.api.dto.payment.responce;

import hr.algebra.gamearena.api.model.payment.Payment;

import java.util.Optional;

public record PaymentResponseView<T>(
        Long paymentId,
        Optional<T> data,
        PaymentStagesView stage
) {
    public static PaymentResponseView<Void> justStatus(PaymentStagesView stage) {
        return new PaymentResponseView<>(null, Optional.empty(), stage);
    }

    public static PaymentResponseView<Void> justStatus(Payment payment, PaymentStagesView stage) {
        return new PaymentResponseView<>(payment.id(), Optional.empty(), stage);
    }

    public static <T> PaymentResponseView<T> actionRequired(Payment payment, T data) {
        return new PaymentResponseView<>(payment.id(), Optional.ofNullable(data), PaymentStagesView.PROCESSING_PAYMENT);
    }

    public static <T> PaymentResponseView<T> success(Payment payment, T data) {
        return new PaymentResponseView<>(payment.id(), Optional.ofNullable(data), PaymentStagesView.COMPLETED);
    }

    public static PaymentResponseView<String> failure(Long paymentId, String message) {
        return new PaymentResponseView<>(paymentId, Optional.ofNullable(message), PaymentStagesView.FAILED);
    }
}
