package hr.algebra.gamearena.webapp.service.tournament;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentMemberViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;
import java.util.Optional;

public interface ITournamentService {

    /** Tournament */
    ApiResult<List<TournamentFullViewDecereal>> getAllTournaments() throws NotFoundException;
    ApiResult<List<TournamentFullViewDecereal>> getMyTournaments() throws UnauthorizedException, NotFoundException;
    ApiResult<List<TournamentFullViewDecereal>> getTournamentsByUserId(Long id) throws NotFoundException;
    ApiResult<TournamentFullViewDecereal> getTournamentById(Long id) throws NotFoundException;

    /** Tournament members */
    ApiResult<List<TournamentMemberViewDecereal>> getTournamentMembers(Long tournamentId) throws NotFoundException;
    ApiResult<TournamentMemberViewDecereal> getMyTournamentMembership(Long tournamentId) throws UnauthorizedException, NotFoundException;
    Optional<TournamentMemberViewDecereal> getMyTournamentMembershipOrEmpty(Long tournamentId);
}
