package hr.algebra.gamearena.api.dto.payment.providers;

public record TestPayResponseView(
        String testId,
        String webHookBodyName,
        String staus
) {
}
