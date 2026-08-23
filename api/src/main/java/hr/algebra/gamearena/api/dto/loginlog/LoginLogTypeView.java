package hr.algebra.gamearena.api.dto.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogType;

public enum LoginLogTypeView {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static LoginLogTypeView fromLoginLogType(LoginLogType type) {
        return switch (type) {
            case SUCCESS -> SUCCESS;
            case BAD_PASSWORD -> BAD_PASSWORD;
            case BAD_CREDENTIALS -> BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> UNEXPECTED_FAILURE;
        };
    }

    public LoginLogType toLoginLogType() {
        return switch (this) {
            case SUCCESS -> LoginLogType.SUCCESS;
            case BAD_PASSWORD -> LoginLogType.BAD_PASSWORD;
            case BAD_CREDENTIALS -> LoginLogType.BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> LoginLogType.DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> LoginLogType.UNEXPECTED_FAILURE;
        };
    }
}
