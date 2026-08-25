package hr.algebra.gamearena.webapp.service.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class JwtCookieService implements IJwtService {

    public static final String COOKIE_NAME = "gamearena_jwt";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtCookieService(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void storeToken(String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        decodeClaims(token)
                .map(claims -> Duration.between(LocalDateTime.now(), claims.expiration()).getSeconds())
                .filter(seconds -> seconds > 0)
                .ifPresent(seconds -> cookie.setMaxAge(seconds.intValue()));

        response.addCookie(cookie);
    }

    @Override
    public String getTokenPlainAndValidate() throws TokenNotFoundException, TokenNotValidException {
        String token = findCookieValue().orElseThrow(TokenNotFoundException::new);

        if (decodeClaims(token).isEmpty()) {
            throw new TokenNotValidException();
        }

        return token;
    }

    @Override
    public JwtClaimDecereal getTokenClaimsAndValidate() throws TokenNotFoundException, TokenNotValidException {
        String token = findCookieValue().orElseThrow(TokenNotFoundException::new);
        JwtClaimDecereal claims = decodeClaims(token).orElseThrow(TokenNotValidException::new);

        if (claims.expiration().isBefore(LocalDateTime.now())) {
            throw new TokenNotValidException();
        }

        return claims;
    }

    private Optional<String> findCookieValue() {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private Optional<JwtClaimDecereal> decodeClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return Optional.empty();
            }

            JsonNode payload = objectMapper.readTree(decodeBase64Url(parts[1]));

            JsonNode userIdNode = payload.path("userId");
            String role = payload.path("role").asText(null);
            long expirationEpochSeconds = payload.path("exp").asLong(-1);
            if (!userIdNode.canConvertToLong() || role == null || expirationEpochSeconds < 0) {
                return Optional.empty();
            }

            Long userId = userIdNode.asLong();

            LocalDateTime expiration = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(expirationEpochSeconds), ZoneId.systemDefault());

            return Optional.of(new JwtClaimDecereal(userId, role, expiration));
        } catch (Exception e) {
            log.debug("JwtCookieService decodeClaims(): rejected token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static byte[] decodeBase64Url(String segment) {
        String padded = switch (segment.length() % 4) {
            case 2 -> segment + "==";
            case 3 -> segment + "=";
            default -> segment;
        };
        return Base64.getUrlDecoder().decode(padded);
    }
}
