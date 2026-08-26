package hr.algebra.gamearena.webapp.models.mvc.data.authentication;

import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;

public record LoginPostViewModel(
        String usernameOrEmail,
        String password,
        Boolean rememberMe
) {
    public LoginCereal toLoginCereal() {
        return new LoginCereal(usernameOrEmail, password, Boolean.TRUE.equals(rememberMe));
    }
}
