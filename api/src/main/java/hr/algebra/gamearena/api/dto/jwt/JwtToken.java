package hr.algebra.gamearena.api.dto.jwt;

import java.time.LocalDateTime;

public record JwtToken(
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime expiration,
        boolean rememberMe
) {
}
