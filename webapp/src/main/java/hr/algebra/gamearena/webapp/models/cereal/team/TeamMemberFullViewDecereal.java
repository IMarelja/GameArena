package hr.algebra.gamearena.webapp.models.cereal.team;

import com.gamearena.client.model.TeamMemberFullView;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;

import java.time.OffsetDateTime;

public record TeamMemberFullViewDecereal(
        Long memberId,
        Long teamId,
        TeamMemberRoleDecereal role,
        UserViewDtoDecereal user,
        OffsetDateTime joinedAt
) {
    public static TeamMemberFullViewDecereal fromTeamMemberFullViewClient(TeamMemberFullView member) {
        return new TeamMemberFullViewDecereal(
                member.getMemberId(),
                member.getTeamId(),
                TeamMemberRoleDecereal.fromTeamMemberFullViewRoleClient(member.getRole()),
                UserViewDtoDecereal.fromUserViewDtoClientOrUnavailableGarbage(member.getUser()),
                member.getJoinedAt()
        );
    }
}
