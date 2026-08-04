package hr.algebra.gamearena.api.service.loginlog;

import hr.algebra.gamearena.api.dto.loginlog.LoginLogCreate;
import hr.algebra.gamearena.api.dto.loginlog.LoginLogsFullView;

import java.util.List;

public interface ILoginLoggingService {
    LoginLogsFullView log(LoginLogCreate loginLogCreate);
    List<LoginLogsFullView> getAll();
    List<LoginLogsFullView> getByCredential(String credential);
}
