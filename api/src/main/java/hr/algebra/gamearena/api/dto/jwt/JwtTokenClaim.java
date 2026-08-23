package hr.algebra.gamearena.api.dto.jwt;

import hr.algebra.gamearena.api.dto.user.RoleView;

import java.time.LocalDateTime;

public record JwtTokenClaim(
        Long userId,
        RoleView role,
        LocalDateTime createdAt,
        LocalDateTime expiration
) {
}
