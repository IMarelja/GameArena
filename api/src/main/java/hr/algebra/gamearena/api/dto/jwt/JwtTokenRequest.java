package hr.algebra.gamearena.api.dto.jwt;

import hr.algebra.gamearena.api.dto.user.RoleView;

/*Imagine (dragons)*/

public record JwtTokenRequest(
        Long userId,
        RoleView role,
        boolean rememberMe) {
}
