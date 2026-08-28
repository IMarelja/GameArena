package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

public record TeamMemberRemoveViewData(
        Long teamId,
        Long memberId,
        String memberUsername
) {
}
