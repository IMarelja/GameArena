package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;

import java.util.List;

public record TournamentMemberAddFormViewData(
        Long userId,
        String username,
        TournamentMemberAddPostViewModel form,
        List<TournamentViewData> tournaments
) {
}
