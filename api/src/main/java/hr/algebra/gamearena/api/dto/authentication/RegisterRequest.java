package hr.algebra.gamearena.api.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{};:'\",.<>?/\\\\|`~]+$",
            message = "Password may only contain letters, digits, and special characters (! @ # $ % ^ & * ( ) _ - = + [ ] { } ; : ' \" , . < > ? / \\ | ` ~)"
    )
    private String password;

    @NotNull(message = "rememberMe is required")
    private Boolean rememberMe;
}
