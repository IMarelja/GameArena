package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenAttributes;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UnauthorizedAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UserNotFoundException;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.service.jwt.IJwtService;
import hr.algebra.gamearena.api.utils.SecurityUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepo userRepo;
    private final IJwtService jwtService;

    public AuthenticationService(IUserRepo userRepo, IJwtService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @Override
    public TokenDto login(LoginDto loginDto) {
        var fetchedUser = userRepo.findByUsernameOrEmail(loginDto.getUsernameOrEmail());

        if(fetchedUser.isEmpty())
            throw new UserNotFoundException("User not found");

        if(fetchedUser.get().getIsActive())
            throw new ForbiddenAccessException("This account is suspended, contact moderators or administrators");

        var loginHashedPassword = SecurityUtilities.hashPasswordWithSalt(
                loginDto.getPassword(),
                fetchedUser
                        .get()
                        .getPasswordSalt()
        );

        if(!fetchedUser.get().getPasswordHash().equals(loginHashedPassword))
            throw new UnauthorizedAccessException("The password is incorrect");



        var tokenAttributes = new JwtTokenAttributes();
        tokenAttributes.setUserId(fetchedUser.get().getId());
        tokenAttributes.setRememberMe(loginDto.isRememberMe());

        var tokenDto = new TokenDto();
        tokenDto.setToken(
                jwtService.generateToken(
                        tokenAttributes
                )
        );

        return tokenDto;
    }

    @Override
    public TokenDto register(RegisterDto registerDto) {
        return null;
    }
}
