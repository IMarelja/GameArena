package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UnauthorizedAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UserNotFoundException;
import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.model.user.UserSave;
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

        if(!fetchedUser.get().isActive())
            throw new ForbiddenAccessException("This account is suspended, contact moderators or administrators");

        var loginHashedPassword = SecurityUtilities.hashPasswordWithSalt(
                loginDto.getPassword(),
                fetchedUser
                        .get()
                        .passwordSalt()
        );

        if(!fetchedUser.get().passwordHash().equals(loginHashedPassword))
            throw new UnauthorizedAccessException("The password is incorrect");



        var tokenAttributes = new JwtTokenRequest(
                fetchedUser.get().id(),
                fetchedUser.get().role(),
                loginDto.isRememberMe()
        );

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

        if(userRepo.existsByEmail(registerDto.getEmail()))
            throw new ConflictException("Email is already in use");

        if(userRepo.existsByUsername(registerDto.getUsername()))
            throw new ConflictException("Username is already in use");

        var passwordSalt = SecurityUtilities.saltForPassword();
        var passwordHash = SecurityUtilities.hashPasswordWithSalt(registerDto.getPassword(), passwordSalt);

        var userSave = new UserSave();
        userSave.setUsername(registerDto.getUsername());
        userSave.setEmail(registerDto.getEmail());
        userSave.setPasswordHash(passwordHash);
        userSave.setPasswordSalt(passwordSalt);
        userSave.setRole(Role.USER);

        var savedUser = userRepo.save(userSave);

        var tokenAttributes = new JwtTokenRequest(
                savedUser.id(),
                savedUser.role(),
                registerDto.getRememberMe()
        );

        var tokenDto = new TokenDto();
        tokenDto.setToken(
                jwtService.generateToken(
                        tokenAttributes
                )
        );

        return tokenDto;
    }
}
