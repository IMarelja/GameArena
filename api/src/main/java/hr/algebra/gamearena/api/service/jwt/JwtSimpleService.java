package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtToken;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtSimpleService implements IJwtService {
    @Override
    public JwtToken getToken(String token) {
        return null;
    }

    @Override
    public String generateToken(JwtTokenRequest attributes) {
        return "Good job";
    }
}
