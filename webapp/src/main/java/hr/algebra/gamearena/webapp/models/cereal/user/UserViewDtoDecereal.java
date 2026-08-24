package hr.algebra.gamearena.webapp.models.cereal.user;

import com.gamearena.client.model.UserViewDto;

import java.time.OffsetDateTime;

public record UserViewDtoDecereal(
        Long id,
        String username,
        OffsetDateTime createdAt,
        Boolean isDeleted
) {
    public static UserViewDtoDecereal fromUserViewDtoClient(UserViewDto user) {
        return new UserViewDtoDecereal(user.getId(), user.getUsername(), user.getCreatedAt(), user.getIsDeleted());
    }
}
