package hr.algebra.gamearena.webapp.models.mvc.data.authentication;

import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginPostViewModel(
        @NotBlank(message = "Username or email is required")
        String usernameOrEmail,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9!@#$%^&*()_\\-=+\\[\\]{};:'\",.<>?/\\\\|`~]+$",
                message = "Password may only contain letters, digits, and special characters (! @ # $ % ^ & * ( ) _ - = + [ ] { } ; : ' \" , . < > ? / \\ | ` ~), and must not contain spaces"
        )
        String password,
        Boolean rememberMe
) {
    public LoginCereal toLoginCereal() {
        return new LoginCereal(usernameOrEmail, password, Boolean.TRUE.equals(rememberMe));
    }
}
