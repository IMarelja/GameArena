package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.Team;
import hr.algebra.gamearena.api.model.team.TeamSave;
import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Team> getTeamById(Long id) {
        return teamPostgresRepo.findById(id)
                .map(Team::fromPostgresTeam);
    }

    @Override
    public Long memberCountInATeam(Long teamId) {
        return teamMemberPostgresRepo.countByTeamId(teamId);
    }

    @Override
    public Team save(TeamSave team) {
        var teamPostgres = new TeamPostgres().fromTeamSave(team);
        var savedTeam = teamPostgresRepo.save(teamPostgres);
        return Team.fromPostgresTeam(savedTeam);
    }
}
