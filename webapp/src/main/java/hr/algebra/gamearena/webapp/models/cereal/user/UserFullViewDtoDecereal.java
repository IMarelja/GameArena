package hr.algebra.gamearena.webapp.models.cereal.user;

import com.gamearena.client.model.UserFullViewDto;

import java.time.OffsetDateTime;

public record UserFullViewDtoDecereal(
        Long id,
        String username,
        OffsetDateTime createdAt,
        String email,
        String role,
        Boolean isActive,
        Boolean isDeleted
) {
    public static UserFullViewDtoDecereal fromUserFullViewDtoClient(UserFullViewDto user) {
        return new UserFullViewDtoDecereal(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().getValue() : null,
                user.getIsActive(),
                user.getIsDeleted()
        );
    }
}
