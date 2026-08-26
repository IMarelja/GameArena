package hr.algebra.gamearena.webapp.models.cereal.user;

import com.gamearena.client.model.UserJustUsernameView;

public record UserJustUsernameDecereal(
        Long id,
        String username
) {
    public static UserJustUsernameDecereal fromUserJustUsernameClient(UserJustUsernameView userJustUsernameView) {
        return new UserJustUsernameDecereal(
                userJustUsernameView.getId(),
                userJustUsernameView.getUsername()
        );
    }
}
