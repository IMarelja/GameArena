package hr.algebra.gamearena.webapp.models.mvc.data.user;

import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;

import java.time.OffsetDateTime;

public record UserViewData(
        Long id,
        String username,
        OffsetDateTime createdAt,
        Boolean isDeleted
) {
    public static UserViewData from(UserViewDtoDecereal user) {
        return new UserViewData(
                user.id(),
                user.username(),
                user.createdAt(),
                user.isDeleted()
        );
    }
}