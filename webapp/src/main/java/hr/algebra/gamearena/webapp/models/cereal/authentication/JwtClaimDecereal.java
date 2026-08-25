package hr.algebra.gamearena.webapp.models.cereal.authentication;

import java.time.OffsetDateTime;

public record JwtClaimDecereal(Long userId, String role, OffsetDateTime expiration) {
}
