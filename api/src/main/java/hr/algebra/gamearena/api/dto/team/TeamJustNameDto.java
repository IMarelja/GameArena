package hr.algebra.gamearena.api.dto.team;

import hr.algebra.gamearena.api.model.team.Team;

public record TeamJustNameDto(
        Long id,
        String name
) {
    public static TeamJustNameDto fromTeam(Team team) {
        return new TeamJustNameDto(
                team.id(),
                team.name()
        );
    }

    public static TeamJustNameDto deletedTeam(){
        return new TeamJustNameDto(
                -1L,
                "DELETED_TEAM"
        );
    }
}
