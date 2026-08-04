package hr.algebra.gamearena.api.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email or Username is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{};:'\",.<>?/\\\\|`~]+$",
            message = "Invalid character in password"
    )
    private String password;

    @NotNull(message = "Remember me is required")
    private Boolean rememberMe;
}
