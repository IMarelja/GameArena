package hr.algebra.gamearena.webapp.service.games;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.games.GameCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.games.GameEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.util.List;

public interface IGamesService {
    GamesViewDecereal getById(Long id) throws NotFoundException, UnauthorizedException, UnexpectedApiErrorException;
    List<GamesViewDecereal> getActiveGames() throws NotFoundException, UnexpectedApiErrorException;
    List<GamesViewDecereal> getGames() throws NotFoundException, UnexpectedApiErrorException;
    GamesViewDecereal createGame(GameCreateCereal cereal) throws NotFoundException, UnexpectedApiErrorException, ForbiddenException, ConflictException, UnauthorizedException, BadRequestedExceptions;
    GamesViewDecereal editGame(Long id, GameEditCereal cereal) throws NotFoundException, UnexpectedApiErrorException, ForbiddenException, ConflictException, UnauthorizedException, BadRequestedExceptions;
}
