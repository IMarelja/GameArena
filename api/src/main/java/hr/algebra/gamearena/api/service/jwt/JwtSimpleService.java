package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenAttributes;
import org.springframework.stereotype.Service;

@Service
public class JwtSimpleService implements IJwtService {
    @Override
    public String generateToken(JwtTokenAttributes attributes) {
        return "Good job";
    }
}
