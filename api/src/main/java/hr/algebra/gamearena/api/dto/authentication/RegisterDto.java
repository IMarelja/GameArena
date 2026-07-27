package hr.algebra.gamearena.api.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private Boolean rememberMe = true;
}
