package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;

public interface IAuthenticationService {
    TokenDto login(LoginDto loginDto);
    TokenDto register(RegisterDto registerDto);
}
