package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.security.AuthenticatedUser;
import hr.algebra.gamearena.webapp.service.authentication.IAuthenticationApiService;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PreAuthorize("permitAll()")
public class AuthenticationMvcController {

    private static final String LOGIN_VIEW = "login";

    private final IAuthenticationApiService authenticationService;
    private final IJwtService jwtService;

    public AuthenticationMvcController(IAuthenticationApiService authenticationService, IJwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        if (AuthenticatedUser.isAuthenticated()) {
            return new ModelAndView("redirect:/");
        }
        return MvcResponse.success(HttpStatus.OK, LOGIN_VIEW, null).toModelAndView();
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam String usernameOrEmail,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "false") boolean rememberMe
    ) throws UnexpectedApiErrorException {
        LoginCereal loginCereal = new LoginCereal(usernameOrEmail, password, rememberMe);
        ApiResult<TokenDecereal> apiResult;

        try {
            apiResult = authenticationService.login(loginCereal);
        } catch (UnauthorizedException | BadRequestedExceptions | NotFoundException | ForbiddenException ex) {
            List<MvcError> errors = ex.getMessages().stream().map(MvcError::new).toList();
            return MvcResponse.errors(ex.getStatus(), LOGIN_VIEW, errors).toModelAndView();
        }

        if (apiResult.data() == null){
            var error = new MvcError("Unexpected error with storing your session");
            return MvcResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, LOGIN_VIEW, error).toModelAndView();
        }

        jwtService.storeToken(apiResult.data().token());
        return new ModelAndView("redirect:/");
    }
}
