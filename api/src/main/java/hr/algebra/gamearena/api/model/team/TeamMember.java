package hr.algebra.gamearena.api.model.team;

import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;

import java.time.LocalDateTime;

public record TeamMember (
    Long id,
    Long teamId,
    Long userId,
    TeamMemberRole role,
    LocalDateTime joinedAt
){
    public static TeamMember fromTeamMemberPostgres(TeamMemberPostgres teamMemberPostgres){
        return new TeamMember(
                teamMemberPostgres.getTeamMemberId(),
                teamMemberPostgres.getTeamId(),
                teamMemberPostgres.getUserId(),
                teamMemberPostgres.getRole(),
                teamMemberPostgres.getJoinedAt()
        );
    }
}
