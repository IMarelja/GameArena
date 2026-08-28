package hr.algebra.gamearena.webapp.models.mvc.data.loginlog;

import hr.algebra.gamearena.webapp.models.cereal.loginog.LogLoginTypeDecereal;

public enum LogLoginTypeViewEnum {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static LogLoginTypeViewEnum fromDecereal(LogLoginTypeDecereal type) {
        return switch (type) {
            case SUCCESS -> SUCCESS;
            case BAD_PASSWORD -> BAD_PASSWORD;
            case BAD_CREDENTIALS -> BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> UNEXPECTED_FAILURE;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case SUCCESS -> "text-bg-success";
            case BAD_PASSWORD, BAD_CREDENTIALS -> "text-bg-warning";
            case DISABLED_ACCOUNT -> "text-bg-secondary";
            case UNEXPECTED_FAILURE -> "text-bg-danger";
        };
    }
}