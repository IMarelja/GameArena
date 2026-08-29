package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.authentication.LoginPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.authentication.RegisterPostViewModel;
import hr.algebra.gamearena.webapp.service.authentication.user.IAuthenticatedUserService;
import hr.algebra.gamearena.webapp.service.authentication.IAuthenticationService;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PreAuthorize("permitAll()")
public class AuthenticationMvcController {

    private static final String LOGIN_VIEW = "login";
    private static final String REGISTER_VIEW = "register";

    private final IAuthenticationService authenticationService;
    private final IJwtService jwtService;
    private final IAuthenticatedUserService authenticatedUserService;

    public AuthenticationMvcController(
            IAuthenticationService authenticationService,
            IJwtService jwtService,
            IAuthenticatedUserService authenticatedUserService)
    {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        if (authenticatedUserService.isAuthenticated()) {
            return MvcResponse.redirect("/");
        }
        return MvcResponse.success(
                HttpStatus.OK,
                LOGIN_VIEW,
                null
        ).toModelAndView();
    }

    @PostMapping("/login")
    public ModelAndView login(
            @Valid @ModelAttribute LoginPostViewModel loginForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<MvcError> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> new MvcError(error.getDefaultMessage()))
                    .toList();
            return MvcResponse.errors(
                    HttpStatus.BAD_REQUEST,
                    LOGIN_VIEW,
                    errors
            ).toModelAndView();
        }

        TokenDecereal token;

        try {
            token = authenticationService.login(loginForm.toLoginCereal());
        } catch (UnauthorizedException | BadRequestedExceptions | NotFoundException | ForbiddenException | UnexpectedApiErrorException ex) {
            List<MvcError> errors = ex.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errors(
                    ex.getStatus(),
                    LOGIN_VIEW,
                    errors
            ).toModelAndView();
        }

        jwtService.storeToken(token.token());
        return MvcResponse.redirect("/");
    }

    @GetMapping("/register")
    public ModelAndView registerForm() {
        if (authenticatedUserService.isAuthenticated()) {
            return MvcResponse.redirect("/");
        }
        return MvcResponse.success(
                HttpStatus.OK,
                REGISTER_VIEW,
                null
        ).toModelAndView();
    }

    @PostMapping("/register")
    public ModelAndView register(
            @Valid @ModelAttribute RegisterPostViewModel registerForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<MvcError> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> new MvcError(error.getDefaultMessage()))
                    .toList();
            return MvcResponse.errors(
                    HttpStatus.BAD_REQUEST,
                    REGISTER_VIEW,
                    errors
            ).toModelAndView();
        }

        TokenDecereal token;

        try {
            token = authenticationService.register(registerForm.toRegisterCereal());
        } catch (UnauthorizedException | BadRequestedExceptions | NotFoundException | ForbiddenException |
                 ConflictException | UnexpectedApiErrorException ex) {
            List<MvcError> errors = ex.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errors(
                    ex.getStatus(),
                    REGISTER_VIEW,
                    errors
            ).toModelAndView();
        }

        jwtService.storeToken(token.token());
        return MvcResponse.redirect("/");
    }

    @PostMapping("/logout")
    public ModelAndView logout() {
        jwtService.clearToken();
        return MvcResponse.redirect("/");
    }
}
