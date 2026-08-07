package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    List<TeamMinimalView> getAll();
    Optional<TeamMinimalView> getTeamById(@PathVariable Long id);
    TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team);
}
