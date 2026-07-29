package hr.algebra.gamearena.api.dto.user;

import hr.algebra.gamearena.api.model.user.User;

import java.time.LocalDateTime;

public record UserViewDto(Long id, String username, LocalDateTime createdAt) {

    public static UserViewDto fromUser(User user){
        return new UserViewDto(
                user.id(),
                user.username(),
                user.createdAt()
        );
    }
}
