package hr.algebra.gamearena.api.orm.postgres.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogType;

public enum LoginLogTypePostgres {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static LoginLogTypePostgres fromLoginLogType(LoginLogType type) {
        return switch (type) {
            case SUCCESS -> LoginLogTypePostgres.SUCCESS;
            case BAD_PASSWORD -> LoginLogTypePostgres.BAD_PASSWORD;
            case BAD_CREDENTIALS -> LoginLogTypePostgres.BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> LoginLogTypePostgres.DISABLED_ACCOUNT;
            case UNEXPECTED_FAILURE -> LoginLogTypePostgres.UNEXPECTED_FAILURE;
        };
    }
}
