package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;

import java.util.Optional;

public interface IJwtService {
    String generateToken(JwtTokenRequest request);
    Optional<JwtTokenClaim> parseToken(String token);
}
