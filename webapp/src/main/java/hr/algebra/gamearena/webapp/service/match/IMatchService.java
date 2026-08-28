package hr.algebra.gamearena.webapp.service.match;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchEditCereal;

import java.util.List;

public interface IMatchService {
    List<MatchDetailFullViewDecereal> getMyMatches() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException;
    List<MatchDetailFullViewDecereal> getMatchesByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException;
    List<MatchDetailFullViewDecereal> getMatchesByTournamentId(Long id) throws NotFoundException, UnexpectedApiErrorException;
    MatchDetailFullViewDecereal getMatchById(Long id) throws NotFoundException, UnexpectedApiErrorException;
    MatchDetailFullViewDecereal createMatch(MatchCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException, BadRequestedExceptions;
    MatchDetailFullViewDecereal editMatch(Long id, MatchEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
}
