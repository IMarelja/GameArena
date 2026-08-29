package hr.algebra.gamearena.webapp.service.games;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;

import java.util.List;

public interface IGamesService {
    List<GamesViewDecereal> getActiveGames() throws NotFoundException, UnexpectedApiErrorException;
    List<GamesViewDecereal> getGames() throws NotFoundException, UnexpectedApiErrorException;
}
