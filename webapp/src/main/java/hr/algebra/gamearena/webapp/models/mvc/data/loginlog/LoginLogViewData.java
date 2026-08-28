package hr.algebra.gamearena.webapp.models.mvc.data.loginlog;

import hr.algebra.gamearena.webapp.models.cereal.loginog.LoginLogDecereal;

import java.time.OffsetDateTime;
import java.util.Optional;

public record LoginLogViewData(
        Long id,
        String ipv4,
        String ipv6,
        Optional<LogLoginTypeViewEnum> type,
        String credential,
        OffsetDateTime createdAt
) {
    public static LoginLogViewData from(LoginLogDecereal log) {
        return new LoginLogViewData(
                log.id(),
                log.ipv4(),
                log.ipv6(),
                log.type().map(LogLoginTypeViewEnum::fromDecereal),
                log.credential(),
                log.createdAt()
        );
    }
}