package hr.algebra.gamearena.webapp.models.mvc.data.team.member;

public record TeamMemberEditFormViewData(
        Long teamId,
        Long memberId,
        String memberUsername,
        TeamMemberEditPostViewModel form
) {
}
