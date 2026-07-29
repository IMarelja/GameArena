package hr.algebra.gamearena.api.utils;

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
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private static String secret;

    @Value("${jwt.expiration.default}")
    private static long expirationDefault;

    @Value("${jwt.expiration.rememberMe}")
    private static long expirationRememberMe;

    private static SecretKey signingKey;

    @PostConstruct
    private void init() {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public static String generateToken(JwtTokenRequest request) {
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

    public static JwtTokenClaim extractClaimFromToken(String token) {
        Claims claims = parseClaims(token);

        return new JwtTokenClaim(
                claims.get("userId", Long.class),
                Role.valueOf(claims.get("role", String.class)),
                toLocalDateTime(claims.getIssuedAt()),
                toLocalDateTime(claims.getExpiration())
        );
    }

    public static boolean isTokenValidSigningAndExpiration(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private static Claims parseClaims(String token) {
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
