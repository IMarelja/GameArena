package hr.algebra.gamearena.api.filter;

import hr.algebra.gamearena.api.exceptions.extenders.UnauthorizedAccessException;
import hr.algebra.gamearena.api.service.jwt.IJwtService;
import hr.algebra.gamearena.api.service.user.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IUserService userService;
    private final IJwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
    {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedAccessException("Token not provided");

        String token = authHeader.substring(7);

        if(!jwtService.isTokenValidSigningAndExpiration(token))
            throw new UnauthorizedAccessException("Token is expired or not valid");

        var jwtClaims = jwtService.extractClaimFromToken(token);

        if (jwtClaims == null){
            log.error("Somehow token is valid, while not having any claims");
            throw new UnauthorizedAccessException("Token not provided");
        }

        if(!userService.existsById(jwtClaims.userId()))
            throw new UnauthorizedAccessException("User does not exist");

        if(!userService.isActiveById(jwtClaims.userId()))
            throw new UnauthorizedAccessException("User is no longer active");



    }
}
