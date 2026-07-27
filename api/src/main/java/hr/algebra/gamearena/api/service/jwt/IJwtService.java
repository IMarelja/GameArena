package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtToken;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;

public interface IJwtService {
    JwtToken getToken(String token);
    String generateToken(JwtTokenRequest attributes);
}
