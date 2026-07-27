package hr.algebra.gamearena.api.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String usernameOrEmail;
    private String password;
    private boolean rememberMe;
}
