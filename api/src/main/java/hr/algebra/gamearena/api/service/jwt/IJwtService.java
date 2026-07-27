package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenAttributes;

public interface IJwtService {
    String generateToken(JwtTokenAttributes attributes);
}
