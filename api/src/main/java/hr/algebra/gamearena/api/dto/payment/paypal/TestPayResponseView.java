package hr.algebra.gamearena.api.dto.payment.paypal;

public record TestPayResponseView(
        String testId,
        String webHookBodyName,
        String staus
) {
}
