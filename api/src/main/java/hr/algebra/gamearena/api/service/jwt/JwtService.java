package hr.algebra.gamearena.api.service.jwt;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;
import hr.algebra.gamearena.api.model.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService implements IJwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.default}")
    private long expirationDefault;

    @Value("${jwt.expiration.rememberMe}")
    private long expirationRememberMe;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public String generateToken(JwtTokenRequest request) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(request.rememberMe() ? expirationRememberMe : expirationDefault);

        return Jwts.builder()
                .subject(String.valueOf(request.userId()))
                .claim("userId", request.userId())
                .claim("role", request.role())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public JwtTokenClaim extractClaimFromToken(String token) {
        Claims claims = parseClaims(token);

        return new JwtTokenClaim(
                claims.get("userId", Long.class),
                Role.valueOf(claims.get("role", String.class)),
                toLocalDateTime(claims.getIssuedAt()),
                toLocalDateTime(claims.getExpiration())
        );
    }

    @Override
    public boolean isTokenValidSigningAndExpiration(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
