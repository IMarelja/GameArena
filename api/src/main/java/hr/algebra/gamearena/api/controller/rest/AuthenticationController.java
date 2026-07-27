package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.authentication.LoginDto;
import hr.algebra.gamearena.api.dto.authentication.RegisterDto;
import hr.algebra.gamearena.api.dto.authentication.TokenDto;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.service.authentication.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(ApiResponse.success(this.authenticationService.login(loginDto)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenDto>> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(ApiResponse.success(this.authenticationService.register(registerDto)));
    }
}
