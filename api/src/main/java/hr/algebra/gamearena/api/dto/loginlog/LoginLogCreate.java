package hr.algebra.gamearena.api.dto.loginlog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginLogCreate {
    @NotBlank
    private String credential;

    private String ipv4;
    private String ipv6;

    @NotNull
    private LoginLogTypeView type;
}
