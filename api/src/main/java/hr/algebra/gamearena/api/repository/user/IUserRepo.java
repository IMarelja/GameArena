package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.dto.user.UserCreateDto;
import hr.algebra.gamearena.api.model.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepo {
    List<User> findAll();
    //Optional<User> findByEmail(String email);
    //Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User save(UserCreateDto userCreate);
}
