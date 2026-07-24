package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.orm.postgres.UserPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserPostgreSQLRepo extends JpaRepository<UserPostgres, Long> {
    Optional<UserPostgres> findByEmail(String email);
}
