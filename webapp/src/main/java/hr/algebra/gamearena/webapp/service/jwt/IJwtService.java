package hr.algebra.gamearena.webapp.service.jwt;

import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;

public interface IJwtService {
    void storeToken(String token);

    String getTokenPlainAndValidate() throws TokenNotFoundException, TokenNotValidException;

    JwtClaimDecereal getTokenClaimsAndValidate() throws TokenNotFoundException, TokenNotValidException;
}
