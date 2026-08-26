package hr.algebra.gamearena.webapp.models.cereal.authentication;

import com.gamearena.client.model.RegisterRequest;

public record RegisterCereal(
        String username,
        String email,
        String password,
        Boolean rememberMe
) {
    public RegisterRequest toRegisterRequestClient() {
        return new RegisterRequest()
                .username(username)
                .email(email)
                .password(password)
                .rememberMe(rememberMe);
    }
}
