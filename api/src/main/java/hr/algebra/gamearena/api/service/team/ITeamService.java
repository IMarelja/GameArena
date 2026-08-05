package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamMinimalView;

import java.util.List;

public interface ITeamService {
    List<TeamMinimalView> getAll();
}
