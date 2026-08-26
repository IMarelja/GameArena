package hr.algebra.gamearena.webapp.models.mvc.data.authentication;

import hr.algebra.gamearena.webapp.models.cereal.authentication.RegisterCereal;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterPostViewModel(
        @NotBlank(message = "Username is required")
        @Size(min = 4, message = "Username must be at least 4 characters long")
        @Pattern(regexp = "^\\S+$", message = "Username must not contain spaces")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{};:'\",.<>?/\\\\|`~]+$",
                message = "Password may only contain letters, digits, and special characters (! @ # $ % ^ & * ( ) _ - = + [ ] { } ; : ' \" , . < > ? / \\ | ` ~), and must not contain spaces"
        )
        String password,

        String confirmPassword,

        Boolean rememberMe
) {
    public RegisterCereal toRegisterCereal() {
        return new RegisterCereal(
                username,
                email,
                password,
                Boolean.TRUE.equals(rememberMe)
        );
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordsMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
