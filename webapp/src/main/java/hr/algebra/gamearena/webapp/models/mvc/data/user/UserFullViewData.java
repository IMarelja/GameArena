package hr.algebra.gamearena.webapp.models.mvc.data.user;

import hr.algebra.gamearena.webapp.models.cereal.user.UserFullViewDtoDecereal;

import java.time.OffsetDateTime;

public record UserFullViewData(
        Long id,
        String username,
        OffsetDateTime createdAt,
        String email,
        String role,
        Boolean isActive,
        Boolean isDeleted
) {
    public static UserFullViewData from(UserFullViewDtoDecereal user) {
        return new UserFullViewData(
                user.id(),
                user.username(),
                user.createdAt(),
                user.email(),
                user.role(),
                user.isActive(),
                user.isDeleted()
        );
    }
}