package hr.algebra.gamearena.api.model.loginlog;

import hr.algebra.gamearena.api.orm.postgres.loginlog.LoginLogPostgres;

import java.time.LocalDateTime;
import java.util.Optional;

public record LoginLogs(
        Long id,
        String credential,
        Optional<String> ipv4,
        Optional<String> ipv6,
        LoginLogType type,
        LocalDateTime createdAt
) {
    public static LoginLogs fromLoginLogPostgres(LoginLogPostgres loginLogPostgres) {
        return new LoginLogs(
                loginLogPostgres.getId(),
                loginLogPostgres.getCredential(),
                Optional.ofNullable(loginLogPostgres.getIpv4()),
                Optional.ofNullable(loginLogPostgres.getIpv6()),
                LoginLogType.fromLoginLogType(loginLogPostgres.getType()),
                loginLogPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
