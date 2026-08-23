package hr.algebra.gamearena.api.dto.user;

import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;

public record UserFullViewDto(Long id, String username, LocalDateTime createdAt, String email, RoleView role) {
    public static UserFullViewDto fromUser(User user) {
        return new UserFullViewDto(
                user.id(),
                user.username(),
                user.createdAt(),
                user.email(),
                RoleView.fromRole(user.role())
        );
    }
}
