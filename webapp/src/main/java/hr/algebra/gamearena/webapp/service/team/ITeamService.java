package hr.algebra.gamearena.webapp.service.team;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface ITeamService {
    ApiResult<List<TeamMinimalViewDecereal>> getAllTeams() throws NotFoundException;
}
