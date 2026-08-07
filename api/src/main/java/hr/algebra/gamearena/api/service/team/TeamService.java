package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.team.TeamSave;
import hr.algebra.gamearena.api.repository.team.ITeamRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService implements ITeamService {

    private final ITeamRepo teamRepo;

    private String teamNotFoundByIdOutput(Long id) {
        return "Team not found with id: " + id;
    }

    public TeamService(ITeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    @Override
    public List<TeamMinimalView> getAll() {
        return teamRepo.getAll()
                .stream()
                .map(team -> TeamMinimalView.fromTeam(team, teamRepo.memberCountInATeam(team.id())))
                .toList();
    }

    @Override
    public Optional<TeamMinimalView> getTeamById(Long id) {
        return Optional.of(teamRepo.getTeamById(id)
                .map( team -> TeamMinimalView.fromTeam(team, teamRepo.memberCountInATeam(team.id())))
                .orElseThrow(() -> new NotFoundException( teamNotFoundByIdOutput(id) )));
    }

    @Override
    public TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team) {
        var teamSave = new TeamSave();
        teamSave.setName(team.getName());
        teamSave.setGameId(team.getGameId());
        teamSave.setCaptainId(userId);

        var savedTeam = teamRepo.save(teamSave);
        return TeamMinimalView.fromTeam(savedTeam, teamRepo.memberCountInATeam(savedTeam.id()));
    }
}
