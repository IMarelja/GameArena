package hr.algebra.gamearena.webapp.models.cereal.user;

import com.gamearena.client.model.UserJustUsernameView;

public record UserJustUsernameDecereal(
        Long id,
        String username
) {
    private static final UserJustUsernameDecereal UNAVAILABLE = new UserJustUsernameDecereal(-1L, "Unknown User");

    public static UserJustUsernameDecereal fromUserJustUsernameClient(UserJustUsernameView userJustUsernameView) {
        return new UserJustUsernameDecereal(
                userJustUsernameView.getId(),
                userJustUsernameView.getUsername()
        );
    }

    public static UserJustUsernameDecereal fromUserJustUsernameClientOrUnavailableGarbage(UserJustUsernameView userJustUsernameView) {
        return userJustUsernameView != null ? fromUserJustUsernameClient(userJustUsernameView) : UNAVAILABLE;
    }
}
