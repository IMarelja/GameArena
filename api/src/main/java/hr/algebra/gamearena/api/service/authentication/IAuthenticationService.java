package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginRequest;
import hr.algebra.gamearena.api.dto.authentication.RegisterRequest;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;

public interface IAuthenticationService {
    TokenDto login(LoginRequest loginRequest);
    TokenDto register(RegisterRequest registerRequest);
}
