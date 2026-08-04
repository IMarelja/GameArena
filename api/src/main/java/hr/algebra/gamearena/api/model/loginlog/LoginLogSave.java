package hr.algebra.gamearena.api.model.loginlog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginLogSave {
    private String credential;
    private String ipv4;
    private String ipv6;
    private LoginLogType type;
}
