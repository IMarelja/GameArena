package hr.algebra.gamearena.api.service.loginlog;

import hr.algebra.gamearena.api.dto.loginlog.LoginLogCreate;
import hr.algebra.gamearena.api.dto.loginlog.LoginLogsFullView;
import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.repository.loginlog.ILoginLoggingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LoginLoggingService implements ILoginLoggingService {

    private final ILoginLoggingRepo loginLoggingRepo;

    public LoginLoggingService(ILoginLoggingRepo loginLoggingRepo) {
        this.loginLoggingRepo = loginLoggingRepo;
    }

    @Override
    public LoginLogsFullView log(LoginLogCreate loginLogCreate) {
        var loginLogSave = new LoginLogSave();
        loginLogSave.setCredential(loginLogCreate.getCredential());
        loginLogSave.setIpv4(loginLogCreate.getIpv4());
        loginLogSave.setIpv6(loginLogCreate.getIpv6());
        loginLogSave.setType(loginLogCreate.getType());

        return LoginLogsFullView.fromLoginLogsModel(loginLoggingRepo.save(loginLogSave));
    }

    @Override
    public List<LoginLogsFullView> getAll() {
        return loginLoggingRepo.getAll()
                .stream()
                .map(LoginLogsFullView::fromLoginLogsModel)
                .toList();
    }

    @Override
    public List<LoginLogsFullView> getByCredential(String credential) {
        return loginLoggingRepo.getByCredential(credential)
                .stream()
                .map(LoginLogsFullView::fromLoginLogsModel)
                .toList();
    }
}
