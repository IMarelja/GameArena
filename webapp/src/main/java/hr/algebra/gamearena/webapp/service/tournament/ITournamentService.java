package hr.algebra.gamearena.webapp.service.tournament;

import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface ITournamentService {
    ApiResult<List<TournamentFullViewDecereal>> getAllTournaments();
}
