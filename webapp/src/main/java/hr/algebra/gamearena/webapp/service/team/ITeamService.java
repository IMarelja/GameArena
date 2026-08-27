package hr.algebra.gamearena.webapp.service.team;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    ApiResult<List<TeamMinimalViewDecereal>> getAllTeams() throws NotFoundException;
    ApiResult<TeamMinimalViewDecereal> getTeamById(Long id) throws NotFoundException;
    ApiResult<List<TeamMemberMinimalViewDecereal>> getTeamMembers(Long teamId) throws NotFoundException;
    ApiResult<TeamMemberFullViewDecereal> getMyTeamMembership(Long teamId) throws UnauthorizedException, NotFoundException;
    Optional<TeamMemberFullViewDecereal> getMyTeamMembershipOrEmpty(Long teamId);
}
