package hr.algebra.gamearena.webapp.models.cereal.loginog;

import com.gamearena.client.model.LoginLogsFullView;

import java.time.OffsetDateTime;
import java.util.Optional;

public record LoginLogDecereal(
        Long id,
        String ipv4,
        String ipv6,
        Optional<LogLoginTypeDecereal> type,
        String credential,
        OffsetDateTime createdAt) {

    public static LoginLogDecereal fromLoginLogsFullViewClient(LoginLogsFullView loginLog){
        return new LoginLogDecereal(
                loginLog.getId(),
                loginLog.getIpv4(),
                loginLog.getIpv6(),
                LogLoginTypeDecereal.fromTypeEnumClient(loginLog.getType()),
                loginLog.getCredential(),
                loginLog.getCreatedAt()
        );
    }
}
