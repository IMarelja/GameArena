package hr.algebra.gamearena.webapp.models.mvc.data.team.invite;

import jakarta.validation.constraints.NotNull;

public record TeamInviteCreatePostView(
        @NotNull(message = "Selecting a team is required")
        Long teamId
) {
}
