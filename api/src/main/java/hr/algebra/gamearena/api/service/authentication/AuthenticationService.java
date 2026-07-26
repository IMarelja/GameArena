package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.exceptions.extenders.UserNotFoundException;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepo userRepo;

    public AuthenticationService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public TokenDto login(LoginDto loginDto) {
        var user = userRepo.findByUsernameOrEmail(loginDto.getUsernameOrEmail());

        if(user.isEmpty())
            throw new UserNotFoundException("User not found");

        TokenDto tokenDto = new TokenDto();

        tokenDto.setToken("Good job");

        return tokenDto;
    }

    @Override
    public TokenDto register(RegisterDto registerDto) {
        return null;
    }
}
