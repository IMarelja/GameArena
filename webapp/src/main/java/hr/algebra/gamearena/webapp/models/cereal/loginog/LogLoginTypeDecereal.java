package hr.algebra.gamearena.webapp.models.cereal.loginog;

import com.gamearena.client.model.LoginLogsFullView;

import java.util.Optional;

public enum LogLoginTypeDecereal {
    SUCCESS,
    BAD_PASSWORD,
    BAD_CREDENTIALS,
    DISABLED_ACCOUNT,
    UNEXPECTED_FAILURE;

    public static Optional<LogLoginTypeDecereal> fromTypeEnumClient(LoginLogsFullView.TypeEnum type){
        if (type == null) {
            return Optional.empty();
        }

        return Optional.of(switch (type){
            case SUCCESS -> LogLoginTypeDecereal.SUCCESS;
            case BAD_PASSWORD -> LogLoginTypeDecereal.BAD_PASSWORD;
            case BAD_CREDENTIALS -> LogLoginTypeDecereal.BAD_CREDENTIALS;
            case DISABLED_ACCOUNT -> LogLoginTypeDecereal.DISABLED_ACCOUNT;
            case  UNEXPECTED_FAILURE -> LogLoginTypeDecereal.UNEXPECTED_FAILURE;
        });
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
