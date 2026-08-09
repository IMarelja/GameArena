package hr.algebra.gamearena.api.dto.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEditRequest {
    @NotNull
    Boolean read;
}
