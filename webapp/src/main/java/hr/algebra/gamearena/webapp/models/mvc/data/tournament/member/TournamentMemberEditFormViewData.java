package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

public record TournamentMemberEditFormViewData(
        Long tournamentId,
        Long memberId,
        String memberUsername,
        TournamentMemberEditPostViewModel form
) {
}
