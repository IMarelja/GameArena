package hr.algebra.gamearena.webapp.models.cereal.user;

import com.gamearena.client.model.UserViewDto;

import java.time.OffsetDateTime;

public record UserViewDtoDecereal(
        Long id,
        String username,
        OffsetDateTime createdAt,
        Boolean isDeleted
) {
    private static final UserViewDtoDecereal UNAVAILABLE = new UserViewDtoDecereal(-1L, "Unknown User", null, true);

    public static UserViewDtoDecereal fromUserViewDtoClient(UserViewDto user) {
        return new UserViewDtoDecereal(user.getId(), user.getUsername(), user.getCreatedAt(), user.getIsDeleted());
    }

    /*
    * RELEASE ME FROM THIS HELL!!!
    * */

    public static UserViewDtoDecereal fromUserViewDtoClientOrUnavailableGarbage(UserViewDto user) {
        return user != null ? fromUserViewDtoClient(user) : UNAVAILABLE;
    }
}
