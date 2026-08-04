package hr.algebra.gamearena.api.dto.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.model.loginlog.LoginLogs;

import java.time.LocalDateTime;
import java.util.Optional;

public record LoginLogsFullView(
        Long id,
        String credential,
        Optional<String> ipv4,
        Optional<String> ipv6,
        LoginLogType type,
        LocalDateTime createdAt
) {
    public static LoginLogsFullView fromLoginLogsModel(LoginLogs loginLogs) {
        return new LoginLogsFullView(
                loginLogs.id(),
                loginLogs.credential(),
                loginLogs.ipv4(),
                loginLogs.ipv6(),
                loginLogs.type(),
                loginLogs.createdAt()
        );
    }
}
