package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.Team;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamPostgresRepo implements ITeamRepo{

    private final ITeamPostgreSQLRepo teamPostgresRepo;
    private final ITeamMemberPostgreSQLRepo teamMemberPostgresRepo;

    public TeamPostgresRepo(ITeamPostgreSQLRepo teamPostgresRepo, ITeamMemberPostgreSQLRepo teamMemberPostgresRepo) {
        this.teamPostgresRepo = teamPostgresRepo;
        this.teamMemberPostgresRepo = teamMemberPostgresRepo;
    }

    @Override
    public List<Team> getAll() {
        return teamPostgresRepo.findAll()
                .stream()
                .map(Team::fromPostgresTeam)
                .toList();
    }

    @Override
    public Long memberCountInATeam(Long teamId) {
        return teamMemberPostgresRepo.countByTeamId(teamId);
    }
}
