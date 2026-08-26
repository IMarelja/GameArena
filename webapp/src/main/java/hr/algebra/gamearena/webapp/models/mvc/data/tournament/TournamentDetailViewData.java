package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentMemberViewDecereal;

import java.util.List;

public record TournamentDetailViewData(
        TournamentFullViewDecereal tournament,
        List<TournamentMemberViewDecereal> members
) {
}
