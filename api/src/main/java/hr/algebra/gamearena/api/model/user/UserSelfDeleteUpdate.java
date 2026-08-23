package hr.algebra.gamearena.api.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSelfDeleteUpdate {
    private String username;
    private String email;
    private String passwordHash;
    private String passwordSalt;
}
