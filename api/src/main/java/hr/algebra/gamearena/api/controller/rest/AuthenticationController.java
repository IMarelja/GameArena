package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.authentication.LoginRequest;
import hr.algebra.gamearena.api.dto.authentication.RegisterRequest;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@Valid @RequestBody LoginRequest loginRequest) {
        loginRequest.setUsernameOrEmail(loginRequest.getUsernameOrEmail().trim());

        return ResponseEntity.ok(ApiResponse.success(this.authenticationService.login(loginRequest)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenDto>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        registerRequest.setEmail(registerRequest.getEmail().trim());
        registerRequest.setUsername(registerRequest.getUsername().trim());

        return ResponseEntity.ok(ApiResponse.success(this.authenticationService.register(registerRequest)));
    }
}
