package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import jakarta.validation.constraints.NotNull;

public record TeamInviterUpdatePostView(
        @NotNull(message = "Status is required")
        UpdateInviterStatusView status
) {
}
