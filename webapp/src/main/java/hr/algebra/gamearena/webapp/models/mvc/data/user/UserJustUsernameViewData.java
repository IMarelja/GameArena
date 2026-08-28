package hr.algebra.gamearena.webapp.models.mvc.data.user;

import hr.algebra.gamearena.webapp.models.cereal.user.UserJustUsernameDecereal;

public record UserJustUsernameViewData(
        Long id,
        String username
) {
    public static UserJustUsernameViewData from(UserJustUsernameDecereal user) {
        return new UserJustUsernameViewData(
                user.id(),
                user.username()
        );
    }
}
