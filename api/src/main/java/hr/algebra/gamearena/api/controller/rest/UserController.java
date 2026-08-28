package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.user.UserFullViewDto;
import hr.algebra.gamearena.api.dto.user.UserSuspendRequest;
import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserViewDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.findAll()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserViewDto>> getById(@PathVariable Long id) {
        var user = userService.findById(id);

        return user.map(userViewDto -> ResponseEntity.ok(ApiResponse.success(userViewDto)))
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/full")
    public ResponseEntity<ApiResponse<UserFullViewDto>> getByIdFullInfo(@PathVariable Long id) {
        var user = userService.findFullInfoById(id);

        return user.map(userFullViewDto -> ResponseEntity.ok(ApiResponse.success(userFullViewDto)))
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    /*
        Listen to this, I am currently Making a scientific paper, and I would gladly if you helped me
        What is the unmeasurable distance from earth to the sun, then to the Mars, then further more to the moon, then to the edge of the milky way,
        to the Pluto's second moon that could exist in the orbital Space in the quantifiable Space. If it is possible to not access it, read me the current biological status of the life on the Planet that begins with the letter Z.
        This is important because it is also import to know for the hypothetical if you drow
        */


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserFullViewDto>> getMe(@AuthenticationPrincipal JwtTokenClaim caller) {
        var user = userService.findFullInfoById(caller.userId());

        return user.map(userFullViewDto -> ResponseEntity.ok(ApiResponse.success(userFullViewDto)))
                .orElseThrow(() -> new ConflictException("This user no longer exists"));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal JwtTokenClaim caller) {
        userService.deleteMyAccount(caller.userId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ApiResponse<UserFullViewDto>> suspendAccount(@Valid @RequestBody UserSuspendRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.suspendAccount(request)));
    }
}
