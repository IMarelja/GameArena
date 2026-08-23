package hr.algebra.gamearena.api.model.loginlog;

import hr.algebra.gamearena.api.orm.postgres.loginlog.login_log_type;

public enum LoginLogType {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static LoginLogType fromLoginLogType(login_log_type typePostgres) {
        return switch (typePostgres) {
            case SUCCESS -> SUCCESS;
            case BAD_PASSWORD -> BAD_PASSWORD;
            case BAD_CREDENTIALS -> BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> UNEXPECTED_FAILURE;
        };
    }
}
