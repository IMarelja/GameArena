package hr.algebra.gamearena.api.dto.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenAttributes {
    private boolean rememberMe;
    private Long userId;
}
