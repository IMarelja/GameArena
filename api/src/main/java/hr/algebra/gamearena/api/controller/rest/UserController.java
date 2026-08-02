package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.user.UserFullViewDto;
import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserViewDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(this.userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserViewDto>> getById(@PathVariable Long id) {
        var user = this.userService.findById(id);

        return user.map(userViewDto -> ResponseEntity.ok(ApiResponse.success(userViewDto)))
                .orElseThrow(() -> new ConflictException("User with id: " + id + " not found"));
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<ApiResponse<UserFullViewDto>> getByIdFullInfo(@PathVariable Long id) {
        var user = this.userService.findFullInfoById(id);

        return user.map(userFullViewDto -> ResponseEntity.ok(ApiResponse.success(userFullViewDto)))
                .orElseThrow(() -> new ConflictException("User with id: " + id + " not found"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserFullViewDto>> getMe(@AuthenticationPrincipal JwtTokenClaim caller) {
        var user = this.userService.findFullInfoById(caller.userId());

        return user.map(userFullViewDto -> ResponseEntity.ok(ApiResponse.success(userFullViewDto)))
                .orElseThrow(() -> new ConflictException("This user no longer exists"));
    }

}
