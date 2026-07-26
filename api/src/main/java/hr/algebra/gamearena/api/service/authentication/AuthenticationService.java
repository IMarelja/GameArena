package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.exceptions.extenders.UnauthorizedAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UserNotFoundException;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.utils.SecurityUtilities;
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
        var fetchedUser = userRepo.findByUsernameOrEmail(loginDto.getUsernameOrEmail());

        if(fetchedUser.isEmpty())
            throw new UserNotFoundException("User not found");

        var loginHashedPassword = SecurityUtilities.hashPasswordWithSalt(
                loginDto.getPassword(),
                fetchedUser
                        .get()
                        .getPasswordSalt()
        );

        if(!fetchedUser.get().getPasswordHash().equals(loginHashedPassword))
            throw new UnauthorizedAccessException("The password is incorrect");

        TokenDto tokenDto = new TokenDto();

        tokenDto.setToken("Good job");

        return tokenDto;
    }

    @Override
    public TokenDto register(RegisterDto registerDto) {
        return null;
    }
}
