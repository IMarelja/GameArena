package hr.algebra.gamearena.webapp.security;

import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static Optional<JwtClaimDecereal> current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof JwtClaimDecereal claim
                ? Optional.of(claim)
                : Optional.empty();
    }

    public static boolean isAuthenticated() {
        return current().isPresent();
    }
}
