package hr.algebra.gamearena.api.repository.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.model.loginlog.LoginLogs;

import java.util.List;

public interface ILoginLoggingRepo {
    LoginLogs save(LoginLogSave loginLogSave);
    List<LoginLogs> getAll();
    List<LoginLogs> getByCredential(String credential);
    List<LoginLogs> getByType(LoginLogType type);
}
