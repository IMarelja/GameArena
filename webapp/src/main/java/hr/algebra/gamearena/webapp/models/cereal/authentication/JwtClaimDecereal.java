package hr.algebra.gamearena.webapp.models.cereal.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record JwtClaimDecereal(
        Long userId,
        UserRoleDecereal role,
        OffsetDateTime issuedAt,
        OffsetDateTime expiration
) {

    public static JwtClaimDecereal fromJsonNode(JsonNode payload) throws UnexpectedApiErrorException {
        JsonNode userIdNode = payload.path("userId");
        String roleText = payload.path("role").asText(null);
        long issuedAtEpochSeconds = payload.path("iat").asLong(-1);
        long expirationEpochSeconds = payload.path("exp").asLong(-1);

        if (!userIdNode.canConvertToLong() || roleText == null || issuedAtEpochSeconds < 0 || expirationEpochSeconds < 0) {
            throw new UnexpectedApiErrorException("The JWT payload is missing required claims");
        }

        UserRoleDecereal role;
        try {
            role = UserRoleDecereal.valueOf(roleText);
        } catch (IllegalArgumentException e) {
            throw new UnexpectedApiErrorException("The JWT payload contains an unknown role: " + roleText);
        }

        Long userId = userIdNode.asLong();
        OffsetDateTime issuedAt = Instant.ofEpochSecond(issuedAtEpochSeconds).atOffset(ZoneOffset.UTC);
        OffsetDateTime expiration = Instant.ofEpochSecond(expirationEpochSeconds).atOffset(ZoneOffset.UTC);

        return new JwtClaimDecereal(userId, role, issuedAt, expiration);
    }
}
