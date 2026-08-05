package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.Team;

import java.util.List;

public interface ITeamRepo {
    List<Team> getAll();
    Long memberCountInATeam(Long teamId);
}
