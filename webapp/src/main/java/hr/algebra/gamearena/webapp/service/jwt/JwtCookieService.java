package hr.algebra.gamearena.webapp.service.jwt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class JwtCookieService implements IJwtService {

    public static final String COOKIE_NAME = "gamearena_jwt";
    public static final String COOKIE_PATH = "/";

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
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);

        decodeClaims(token)
                .map(claims -> Duration.between(OffsetDateTime.now(ZoneOffset.UTC), claims.expiration()).getSeconds())
                .filter(seconds -> seconds > 0)
                .ifPresent(seconds -> cookie.setMaxAge(seconds.intValue()));

        response.addCookie(cookie);
    }

    @Override
    public void clearToken() {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public Optional<String> getTokenPlainAndValidate() {
        return findCookieValue().filter(token -> decodeClaims(token).isPresent());
    }

    @Override
    public Optional<JwtClaimDecereal> getTokenClaimsAndValidate() {
        return findCookieValue()
                .flatMap(this::decodeClaims)
                .filter(claims -> claims.expiration().isAfter(OffsetDateTime.now(ZoneOffset.UTC)));
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
            String payload = token.split("\\.")[1];

            byte[] decoded = Base64.getUrlDecoder().decode(payload);
            JsonNode node = objectMapper.readTree(decoded);

            return Optional.of(JwtClaimDecereal.fromJsonNode(node));
        } catch (Exception e) {
            log.debug("Failed to decode JWT claims: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
