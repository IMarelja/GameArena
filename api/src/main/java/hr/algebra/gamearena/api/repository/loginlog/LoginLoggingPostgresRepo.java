package hr.algebra.gamearena.api.repository.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.model.loginlog.LoginLogs;
import hr.algebra.gamearena.api.orm.postgres.loginlog.LoginLogPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginLoggingPostgresRepo implements ILoginLoggingRepo{

    private final ILoginLogPostgreSQLRepo sqlLoginLogPostgresRepo;

    public LoginLoggingPostgresRepo(ILoginLogPostgreSQLRepo sqlLoginLogPostgresRepo) {
        this.sqlLoginLogPostgresRepo = sqlLoginLogPostgresRepo;
    }

    @Override
    public LoginLogs save(LoginLogSave loginLogSave) {
        var loginLogsPostgres = new LoginLogPostgres().fromLoginLogSave(loginLogSave);
        var savedLoginLog = sqlLoginLogPostgresRepo.save(loginLogsPostgres);
        return LoginLogs.fromLoginLogPostgres(savedLoginLog);
    }

    @Override
    public List<LoginLogs> getAll() {
        return sqlLoginLogPostgresRepo.findAll()
                .stream()
                .map(LoginLogs::fromLoginLogPostgres)
                .toList();
    }

    @Override
    public List<LoginLogs> getByCredential(String credential) {
        return sqlLoginLogPostgresRepo.findByCredential(credential)
                .stream()
                .map(LoginLogs::fromLoginLogPostgres)
                .toList();
    }

    @Override
    public List<LoginLogs> getByType(LoginLogType type) {
        return sqlLoginLogPostgresRepo.findByType(type)
                .stream()
                .map(LoginLogs::fromLoginLogPostgres)
                .toList();
    }
}
