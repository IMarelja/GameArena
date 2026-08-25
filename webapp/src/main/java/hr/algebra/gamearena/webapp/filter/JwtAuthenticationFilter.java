package hr.algebra.gamearena.webapp.filter;

import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ROLE_PREFIX = "ROLE_";

    private final IJwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        authenticate();
        filterChain.doFilter(request, response);
    }

    private void authenticate() {
        JwtClaimDecereal claim;

        try {
            claim = jwtService.getTokenClaimsAndValidate();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            log.debug("JwtAuthenticationFilter: no valid session token - {}", e.getMessage());
            SecurityContextHolder.clearContext();
            return;
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                claim,
                null,
                List.of(new SimpleGrantedAuthority(ROLE_PREFIX + claim.role())));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
