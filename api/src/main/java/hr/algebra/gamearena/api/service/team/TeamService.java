package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.repository.team.ITeamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService implements ITeamService {

    private final ITeamRepo teamRepo;

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
}
