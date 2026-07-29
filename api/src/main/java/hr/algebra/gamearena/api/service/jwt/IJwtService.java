package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;

public interface IJwtService {
    String generateToken(JwtTokenRequest request);
    JwtTokenClaim extractClaimFromToken(String token);
    boolean isTokenValidSigningAndExpiration(String token);
}
