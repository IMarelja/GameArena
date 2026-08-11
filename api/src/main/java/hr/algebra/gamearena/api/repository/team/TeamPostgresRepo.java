package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.Team;
import hr.algebra.gamearena.api.model.team.TeamSave;
import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamPostgresRepo implements ITeamRepo{

    private final ITeamPostgreSQLRepo teamPostgresSQLRepo;
    private final ITeamMemberPostgreSQLRepo teamMemberPostgresSQLRepo;

    public TeamPostgresRepo(ITeamPostgreSQLRepo teamPostgresRepo, ITeamMemberPostgreSQLRepo teamMemberPostgresRepo) {
        this.teamPostgresSQLRepo = teamPostgresRepo;
        this.teamMemberPostgresSQLRepo = teamMemberPostgresRepo;
    }

    @Override
    public List<Team> getAll() {
        return teamPostgresSQLRepo.findAll()
                .stream()
                .map(Team::fromPostgresTeam)
                .toList();
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamPostgresSQLRepo.findById(id)
                .map(Team::fromPostgresTeam);
    }

    @Override
    public Long memberCountInATeam(Long teamId) {
        return teamMemberPostgresSQLRepo.countByTeamId(teamId);
    }

    @Override
    public Team save(TeamSave team) {
        var teamPostgres = new TeamPostgres().fromTeamSave(team);
        var savedTeam = teamPostgresSQLRepo.save(teamPostgres);
        return Team.fromPostgresTeam(savedTeam);
    }
}
