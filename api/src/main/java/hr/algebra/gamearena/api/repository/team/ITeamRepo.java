package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.Team;
import hr.algebra.gamearena.api.model.team.TeamSave;

import java.util.List;
import java.util.Optional;

public interface ITeamRepo {
    List<Team> getAll();
    Optional<Team> getTeamById(Long id);
    Long memberCountInATeam(Long teamId);
    Team save(TeamSave team);
}
