package hr.algebra.gamearena.api.dto.user;

import hr.algebra.gamearena.api.model.user.User;

public record UserJustUsernameView(
        Long id,
        String username
) {
    public static UserJustUsernameView fromUser(User user){
        return new UserJustUsernameView(
                user.id(),
                user.username()
        );
    }
}
