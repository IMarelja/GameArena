package hr.algebra.gamearena.api.dto.jwt;

import hr.algebra.gamearena.api.model.user.Role;

/*Imagine (dragons)*/

public record JwtTokenRequest(
        Long userId,
        Role role,
        boolean rememberMe) {
}
