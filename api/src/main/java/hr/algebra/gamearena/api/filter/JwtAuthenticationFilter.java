package hr.algebra.gamearena.api.filter;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.service.jwt.IJwtService;
import hr.algebra.gamearena.api.service.user.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Identifies the caller, and nothing more.
 * <p>
 * The filter runs on every request. When a bearer token is present and valid the matching
 * {@link JwtTokenClaim} is published as the authenticated principal, so the rest of the request can
 * read it. When no token is present the request simply continues as anonymous. Deciding whether an
 * anonymous or under-privileged caller may reach an endpoint is not this filter's job - that lives
 * in {@code SecurityConfig}.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Request attribute holding why a supplied token was refused. Read by {@code SecurityErrorHandler}
     * so the 401 body can state the actual reason instead of a generic message.
     */
    public static final String AUTH_ERROR_ATTRIBUTE = "jwtAuthenticationError";

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLE_PREFIX = "ROLE_";

    private final IUserService userService;
    private final IJwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX))
            authenticate(request, authHeader.substring(BEARER_PREFIX.length()));

        filterChain.doFilter(request, response);
    }

    /** Publishes the caller's identity, or records why it could not be established. */
    private void authenticate(HttpServletRequest request, String token) {
        Optional<JwtTokenClaim> jwtClaims = jwtService.parseToken(token);

        if (jwtClaims.isEmpty()) {
            refuse(request, "Token is expired or not valid");
            return;
        }

        JwtTokenClaim claim = jwtClaims.get();

        if (!userService.isActiveById(claim.userId())) {
            refuse(request, "User is not allowed to access this resource");
            return;
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                claim,
                null,
                List.of(new SimpleGrantedAuthority(ROLE_PREFIX + claim.role().name())));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * Leaves the request anonymous and notes the reason. No response is written here: a refused token
     * only matters once an endpoint actually demands an identity, and that verdict belongs to the
     * authorization rules. Public endpoints therefore keep working even when a stale token is sent along.
     */
    private void refuse(HttpServletRequest request, String message) {
        log.debug("JwtAuthenticationFilter: refused token: {}", message);

        SecurityContextHolder.clearContext();
        request.setAttribute(AUTH_ERROR_ATTRIBUTE, message);
    }
}
