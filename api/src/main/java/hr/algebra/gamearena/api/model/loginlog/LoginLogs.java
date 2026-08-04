package hr.algebra.gamearena.api.model.loginlog;

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
}
