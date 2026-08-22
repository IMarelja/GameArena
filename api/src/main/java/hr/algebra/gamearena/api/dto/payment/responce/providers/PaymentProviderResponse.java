package hr.algebra.gamearena.api.dto.payment.responce.providers;

public sealed interface PaymentProviderResponse permits PaypalResponseView, TestPayResponseView {
}
