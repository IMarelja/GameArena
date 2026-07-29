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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
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
    public Optional<JwtTokenClaim> parseToken(String token) {
        try {
            Claims claims = parseClaims(token);

            return Optional.of(new JwtTokenClaim(
                    claims.get("userId", Long.class),
                    Role.valueOf(claims.get("role", String.class)),
                    toLocalDateTime(claims.getIssuedAt()),
                    toLocalDateTime(claims.getExpiration())
            ));
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            log.debug("JwtService parseToken(): rejected token: {}", e.getMessage());
            return Optional.empty();
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
