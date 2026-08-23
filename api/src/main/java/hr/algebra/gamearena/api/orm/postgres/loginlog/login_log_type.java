package hr.algebra.gamearena.api.orm.postgres.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogType;

public enum login_log_type {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static login_log_type fromLoginLogType(LoginLogType type) {
        return switch (type) {
            case SUCCESS -> login_log_type.SUCCESS;
            case BAD_PASSWORD -> login_log_type.BAD_PASSWORD;
            case BAD_CREDENTIALS -> login_log_type.BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> login_log_type.DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> login_log_type.UNEXPECTED_FAILURE;
        };
    }
}
