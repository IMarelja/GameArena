package hr.algebra.gamearena.webapp.service.authentication.user;

import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;

import java.util.Optional;

public interface IAuthenticatedUserService {
    Optional<JwtClaimDecereal> current();
    boolean isAuthenticated();
    boolean isAdmin();
}
