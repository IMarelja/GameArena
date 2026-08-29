package hr.algebra.gamearena.webapp.service.admin;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.admin.MatchQueryCereal;
import hr.algebra.gamearena.webapp.models.cereal.admin.TournamentQueryCereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;

import java.util.List;

public interface IAdminService {
    List<TournamentFullViewDecereal> queryTournaments(TournamentQueryCereal queryCereal) throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestedExceptions, UnexpectedApiErrorException;
    List<MatchDetailFullViewDecereal> queryMatches(MatchQueryCereal queryCereal) throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestedExceptions, UnexpectedApiErrorException;
}
