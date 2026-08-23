package hr.algebra.gamearena.api.service.authentication;

import hr.algebra.gamearena.api.dto.authentication.LoginRequest;
import hr.algebra.gamearena.api.dto.authentication.RegisterRequest;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.dto.jwt.JwtTokenRequest;
import hr.algebra.gamearena.api.dto.loginlog.LoginLogCreate;
import hr.algebra.gamearena.api.dto.loginlog.LoginLogTypeView;
import hr.algebra.gamearena.api.dto.user.RoleView;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UnauthorizedAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.UserNotFoundException;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.model.user.UserSave;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.service.jwt.IJwtService;
import hr.algebra.gamearena.api.service.loginlog.ILoginLoggingService;
import hr.algebra.gamearena.api.utils.NetworkUtilities;
import hr.algebra.gamearena.api.utils.SecurityUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserRepo userRepo;
    private final IJwtService jwtService;
    private final ILoginLoggingService loginLoggingService;
    private final HttpServletRequest httpServletRequest;

    public AuthenticationService(
            IUserRepo userRepo,
            IJwtService jwtService,
            ILoginLoggingService loginLoggingService,
            HttpServletRequest httpServletRequest) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.loginLoggingService = loginLoggingService;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public TokenDto login(LoginRequest loginRequest) {
        var usernameOrEmail = loginRequest.getUsernameOrEmail();

        try {
            var fetchedUser = userRepo.findByUsernameOrEmail(usernameOrEmail);

            if(fetchedUser.isEmpty()) {
                logLoginAttempt(usernameOrEmail, LoginLogType.BAD_CREDENTIALS);
                throw new UserNotFoundException("User not found");
            }

            if(!fetchedUser.get().isActive()) {
                logLoginAttempt(usernameOrEmail, LoginLogType.DISABLED_ACCOUNT);
                throw new ForbiddenAccessException("This account is suspended, contact moderators or administrators");
            }

            var loginHashedPassword = SecurityUtilities.hashPasswordWithSalt(
                    loginRequest.getPassword(),
                    fetchedUser
                            .get()
                            .passwordSalt()
            );

            if(!fetchedUser.get().passwordHash().equals(loginHashedPassword)) {
                logLoginAttempt(usernameOrEmail, LoginLogType.BAD_PASSWORD);
                throw new UnauthorizedAccessException("The password is incorrect");
            }

            /*
            * [Chorus]
            * Baby, I'm preyin' on you tonight
            * Hunt you down, eat you alive
            * Just like animals, animals
            * Like animals-mals
            * Maybe you think that you can hide
            * I can smell your scent for miles
            * Just like animals, animals
            * Like animals-mals
            * Baby, I'm (Hey)
            * */

            var tokenAttributes = new JwtTokenRequest(
                    fetchedUser.get().id(),
                    RoleView.fromRole(fetchedUser.get().role()),
                    loginRequest.getRememberMe()
            );

            var tokenDto = new TokenDto();
            tokenDto.setToken(
                    jwtService.generateToken(
                            tokenAttributes
                    )
            );

            logLoginAttempt(usernameOrEmail, LoginLogType.SUCCESS);

            return tokenDto;
        } catch (UserNotFoundException | ForbiddenAccessException | UnauthorizedAccessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            logLoginAttempt(usernameOrEmail, LoginLogType.UNEXPECTED_FAILURE);
            throw ex;
        }
    }

    @Override
    public TokenDto register(RegisterRequest registerRequest) {

        if(userRepo.existsByEmail(registerRequest.getEmail()))
            throw new ConflictException("Email is already in use");

        if(userRepo.existsByUsername(registerRequest.getUsername()))
            throw new ConflictException("Username is already in use");

        var passwordSalt = SecurityUtilities.saltForPassword();
        var passwordHash = SecurityUtilities.hashPasswordWithSalt(registerRequest.getPassword(), passwordSalt);

        var userSave = new UserSave();
        userSave.setUsername(registerRequest.getUsername());
        userSave.setEmail(registerRequest.getEmail());
        userSave.setPasswordHash(passwordHash);
        userSave.setPasswordSalt(passwordSalt);
        userSave.setRole(Role.USER);

        var savedUser = userRepo.save(userSave);

        var tokenAttributes = new JwtTokenRequest(
                savedUser.id(),
                RoleView.fromRole(savedUser.role()),
                registerRequest.getRememberMe()
        );

        var tokenDto = new TokenDto();
        tokenDto.setToken(
                jwtService.generateToken(
                        tokenAttributes
                )
        );

        return tokenDto;
    }

    private void logLoginAttempt(String credential, LoginLogType type) {
        var loginLogCreate = new LoginLogCreate();
        var clientAddress = NetworkUtilities.resolveClientAddress(httpServletRequest);

        loginLogCreate.setCredential(credential);
        loginLogCreate.setType(LoginLogTypeView.fromLoginLogType(type));

        if (NetworkUtilities.isIpv6(clientAddress))
            loginLogCreate.setIpv6(clientAddress);
        else
            loginLogCreate.setIpv4(clientAddress);

        loginLoggingService.log(loginLogCreate);
    }
}
