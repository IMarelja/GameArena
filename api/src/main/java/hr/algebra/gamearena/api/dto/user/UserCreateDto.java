package hr.algebra.gamearena.api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDto {
    private String username;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    //private Role role;
}
