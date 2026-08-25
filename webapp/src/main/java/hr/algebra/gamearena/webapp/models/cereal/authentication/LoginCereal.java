package hr.algebra.gamearena.webapp.models.cereal.authentication;

import com.gamearena.client.model.LoginRequest;

public record LoginCereal(
        String usernameOrEmail,
        String password,
        Boolean rememberMe
) {
    public LoginRequest toLoginRequestClient() {
        return new LoginRequest()
                .usernameOrEmail(usernameOrEmail)
                .password(password)
                .rememberMe(rememberMe);
    }
}
