package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import jakarta.validation.constraints.NotNull;

public record TeamInviteeResponsePostViewModel(
        @NotNull(message = "Status must not be blank")
        ResponseInviteeStatusView status
) {
}
