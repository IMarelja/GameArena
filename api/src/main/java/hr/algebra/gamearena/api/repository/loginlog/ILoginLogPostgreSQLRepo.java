package hr.algebra.gamearena.api.repository.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.orm.postgres.LoginLogPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILoginLogPostgreSQLRepo extends JpaRepository<LoginLogPostgres, Long> {
    List<LoginLogPostgres> findByCredential(String credential);
    List<LoginLogPostgres> findByType(LoginLogType type);
}
