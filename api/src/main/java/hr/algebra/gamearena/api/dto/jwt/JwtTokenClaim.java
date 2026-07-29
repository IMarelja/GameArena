package hr.algebra.gamearena.api.dto.jwt;

import hr.algebra.gamearena.api.model.user.Role;

import java.time.LocalDateTime;

public record JwtTokenClaim(
        Long userId,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime expiration
) {
}
