package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.orm.postgres.UserPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserPostgresRepo implements IUserRepo {

    private final IUserPostgreSQLRepo sqlRepository;
    public UserPostgresRepo(IUserPostgreSQLRepo sqlRepository) {
        this.sqlRepository = sqlRepository;
    }

    @Override
    public List<User> findAll() {
        return sqlRepository.findAll()
                .stream()
                .map(this::postgresUserToUser)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return sqlRepository.findByEmail(email)
                .map(this::postgresUserToUser);
    }

    private User postgresUserToUser(UserPostgres postgres) {
        return new User(
                postgres.getId(),
                postgres.getUsername(),
                postgres.getEmail(),
                postgres.getPasswordHash(),
                postgres.getPasswordSalt(),
                postgres.getIsActive(),
                postgres.getCreatedAt()
        );
    }
}
