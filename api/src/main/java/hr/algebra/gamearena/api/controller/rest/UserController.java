package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserViewDto>> getMe() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ApiResponse<UserViewDto>());
    }

}
