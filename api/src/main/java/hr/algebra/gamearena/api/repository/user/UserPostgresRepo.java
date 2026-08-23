package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.model.user.UserActiveStatusUpdate;
import hr.algebra.gamearena.api.model.user.UserSave;
import hr.algebra.gamearena.api.model.user.UserSelfDeleteUpdate;
import hr.algebra.gamearena.api.orm.postgres.user.UserPostgres;
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
    public Optional<User> findById(Long id) {
        return sqlUserRepository.findById(id)
                .map(User::fromPostgres);
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
    public boolean existsByIdAndIsActiveAndNotDeleted(Long id) {
        return sqlUserRepository.existsByIdAndIsActiveAndNotDeleted(id);
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
    public User save(UserSave userSave) {
        var userPostgres = new UserPostgres().fromUserSave(userSave);
        var savedUser = sqlUserRepository.save(userPostgres);
        return User.fromPostgres(savedUser);
    }

    @Override
    public Optional<User> selfDelete(Long id, UserSelfDeleteUpdate update) {
        return sqlUserRepository.findById(id)
                .map(existing -> existing.fromUserSelfDeleteUpdate(update))
                .map(sqlUserRepository::save)
                .map(User::fromPostgres);
    }

    @Override
    public Optional<User> updateActiveStatus(Long id, UserActiveStatusUpdate update) {
        return sqlUserRepository.findById(id)
                .map(existing -> existing.fromUserActiveStatusUpdate(update))
                .map(sqlUserRepository::save)
                .map(User::fromPostgres);
    }
}
