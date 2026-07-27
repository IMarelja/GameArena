package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.dto.user.UserCreateDto;
import hr.algebra.gamearena.api.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserPostgresRepo implements IUserRepo {

    private final IUserPostgreSQLRepo sqlUserRepository;

    public UserPostgresRepo(IUserPostgreSQLRepo sqlRepository) {
        this.sqlUserRepository = sqlRepository;
    }

    @Override
    public List<User> findAll() {
        return sqlUserRepository.findAll()
                .stream()
                .map(User::fromPostgres)
                .toList();
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return sqlUserRepository.findByEmail(email)
                .map(User::fromPostgres);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return sqlUserRepository.findByUsername(username)
                .map(User::fromPostgres);
    }
    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return sqlUserRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(User::fromPostgres);
    }

    @Override
    public boolean existsByUsername(String username) {
        return sqlUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return sqlUserRepository.existsByEmail(email);
    }

    @Override
    public User save(UserCreateDto userCreate) {

        return null;
    }


}
