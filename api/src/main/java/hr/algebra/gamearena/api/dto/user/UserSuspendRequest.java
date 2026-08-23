package hr.algebra.gamearena.api.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSuspendRequest {
    @NotNull(message = "You must select a user")
    private Long userId;

    @NotNull(message = "isActive is required")
    private Boolean isActive;
}
