package hr.algebra.gamearena.api.repository.user;

import hr.algebra.gamearena.api.orm.postgres.UserPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserPostgreSQLRepo extends JpaRepository<UserPostgres, Long> {
    Optional<UserPostgres> findByEmail(String email);

    Optional<UserPostgres> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserPostgres u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<UserPostgres> findByUsernameOrEmail(
            @Param("usernameOrEmail") String usernameOrEmail
    );
}
