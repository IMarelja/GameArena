package hr.algebra.gamearena.api.dto.user;

import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;

public record UserFullViewDto(Long id, String username, LocalDateTime createdAt, String email, Role role) {
    public static UserFullViewDto fromUser(User user) {
        return new UserFullViewDto(
                user.id(),
                user.username(),
                user.createdAt(),
                user.email(),
                user.role()
        );
    }
}
