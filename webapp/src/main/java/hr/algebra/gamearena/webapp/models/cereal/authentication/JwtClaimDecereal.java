package hr.algebra.gamearena.webapp.models.cereal.authentication;

import java.time.LocalDateTime;

public record JwtClaimDecereal(Long userId, String role, LocalDateTime expiration) {
}
