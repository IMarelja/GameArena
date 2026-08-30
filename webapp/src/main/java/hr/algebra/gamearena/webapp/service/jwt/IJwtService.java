package hr.algebra.gamearena.webapp.service.jwt;

import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;

import java.util.Optional;

public interface IJwtService {
    void storeToken(String token);
    void clearToken();
    Optional<String> getTokenPlainAndValidate();
    Optional<JwtClaimDecereal> getTokenClaimsAndValidate();
}
