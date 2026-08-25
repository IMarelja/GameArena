package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.service.authentication.IAuthenticationApiService;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("permitAll()")
public class AuthenticationMvcController {

    private final IAuthenticationApiService authenticationService;
    private final IJwtService jwtService;

    public AuthenticationMvcController(IAuthenticationApiService authenticationService, IJwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return MvcResponse.success(HttpStatus.OK, "login", null).toModelAndView();
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam String usernameOrEmail,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "false") boolean rememberMe
    ) {
        LoginCereal loginCereal = new LoginCereal(usernameOrEmail, password, rememberMe);
        ApiResult<TokenDecereal> apiResult;

        try {
            apiResult = authenticationService.login(loginCereal);
        } catch (BadRequestedExceptions | NotFoundException | ForbiddenException ex ) {
            return MvcResponse.error(ex.getStatus(), "login", new MvcError(ex.getMessage())).toModelAndView();
        }

        if (apiResult.data() == null){
            var error = new MvcError("Unexpected error with storing your session");
            return MvcResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "login", error).toModelAndView();
        }

        jwtService.storeToken(apiResult.data().token());
        return new ModelAndView("redirect:/");
    }
}
