package hr.algebra.gamearena.api.repository.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import hr.algebra.gamearena.api.model.loginlog.LoginLogs;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class LoginLoggingMemoryRepo implements ILoginLoggingRepo {

    private final List<LoginLogs> loginLogs = new CopyOnWriteArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public LoginLogs save(LoginLogSave loginLogSave) {
        var loginLog = new LoginLogs(
                idSequence.getAndIncrement(),
                loginLogSave.getCredential(),
                Optional.ofNullable(loginLogSave.getIpv4()),
                Optional.ofNullable(loginLogSave.getIpv6()),
                loginLogSave.getType(),
                LocalDateTime.now(ZoneId.of("UTC"))
        );

        loginLogs.add(loginLog);
        return loginLog;
    }

    @Override
    public List<LoginLogs> getAll() {
        return List.copyOf(loginLogs);
    }

    @Override
    public List<LoginLogs> getByCredential(String credential) {
        return loginLogs.stream()
                .filter(loginLogsFilter -> loginLogsFilter.credential().equals(credential))
                .toList();
    }

    @Override
    public List<LoginLogs> getByType(LoginLogType type) {
        return loginLogs.stream()
                .filter(loginLogsFilter -> loginLogsFilter.type().equals(type))
                .toList();
    }
}
