package hr.algebra.gamearena.api.dto.jwt;

public record JwtTokenRequest(
        Long userId,
        boolean rememberMe) {
}
