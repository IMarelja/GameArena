package hr.algebra.gamearena.webapp.service.authentication.user;

import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.UserRoleDecereal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedUserService implements IAuthenticatedUserService {

    @Override
    public Optional<JwtClaimDecereal> current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof JwtClaimDecereal claim
                ? Optional.of(claim)
                : Optional.empty();
    }

    @Override
    public boolean isAuthenticated() {
        return current().isPresent();
    }

    @Override
    public boolean isAdmin() {
        return current().map(claim -> claim.role() == UserRoleDecereal.ADMIN).orElse(false);
    }
}
