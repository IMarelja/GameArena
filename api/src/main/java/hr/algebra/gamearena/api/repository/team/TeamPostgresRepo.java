package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.*;
import hr.algebra.gamearena.api.orm.postgres.TeamInvitationPostgres;
import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;
import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamPostgresRepo implements ITeamRepo{

    private final ITeamPostgreSQLRepo teamPostgresSQLRepo;
    private final ITeamMemberPostgreSQLRepo teamMemberPostgresSQLRepo;
    private final ITeamInvitationPostgreSQLRepo teamInvitationPostgresSQLRepo;

    public TeamPostgresRepo(
            ITeamPostgreSQLRepo teamPostgresRepo,
            ITeamMemberPostgreSQLRepo teamMemberPostgresRepo,
            ITeamInvitationPostgreSQLRepo teamInvitationPostgresRepo
    ) {
        this.teamPostgresSQLRepo = teamPostgresRepo;
        this.teamMemberPostgresSQLRepo = teamMemberPostgresRepo;
        this.teamInvitationPostgresSQLRepo = teamInvitationPostgresRepo;
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

    @Override
    public TeamInvitation save(TeamInvitationSave save) {
        var teamInvitationPostgres = new TeamInvitationPostgres().fromTeamInvitationSave(save);
        var savedInvitation = teamInvitationPostgresSQLRepo.save(teamInvitationPostgres);
        return TeamInvitation.fromTeamInvitationPostgres(savedInvitation);
    }

    @Override
    public Optional<TeamInvitation> getInvitationById(Long id) {
        return teamInvitationPostgresSQLRepo.findById(id)
                .map(TeamInvitation::fromTeamInvitationPostgres);
    }

    @Override
    public Optional<TeamInvitation> update(Long id, TeamInvitationUpdate update) {
        return teamInvitationPostgresSQLRepo.findById(id)
                .map(invitation -> invitation.fromTeamInvitationUpdate(update))
                .map(teamInvitationPostgresSQLRepo::save)
                .map(TeamInvitation::fromTeamInvitationPostgres);
    }

    @Override
    public void addMember(TeamMemberSave save) {
        teamMemberPostgresSQLRepo.save(new TeamMemberPostgres().fromTeamMemberSave(save));
    }

    @Override
    @Transactional
    public void deleteMember(Long teamId, Long userId) {
        teamMemberPostgresSQLRepo.deleteByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public List<TeamMember> getTeamMembers(Long teamId) {
        return teamMemberPostgresSQLRepo.findByTeamId(teamId)
                .stream()
                .map(TeamMember::fromTeamMemberPostgres)
                .toList();
    }

    @Override
    public void deleteTeam(Long id) {
        teamPostgresSQLRepo.deleteById(id);
    }

    @Override
    public boolean doesTeamExist(Long id) {
        return teamPostgresSQLRepo.existsById(id);
    }

    @Override
    public boolean doesPendingTeamInvitationExist(Long teamId, Long inviteeId) {
        return teamInvitationPostgresSQLRepo.existsByTeamIdAndInviteeIdAndStatus(teamId, inviteeId, InviteStatus.PENDING);
    }

    @Override
    public boolean isUserIdPartOfTeam(Long userId, Long teamId) {
        return teamPostgresSQLRepo.existsByIdAndCaptainId(teamId, userId)
                || teamMemberPostgresSQLRepo.existsByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public boolean isUserAnInviteeOfInvitation(Long invitationId, Long userId) {
        return teamInvitationPostgresSQLRepo.existsByInvitationIdAndInviteeId(invitationId, userId);
    }

    @Override
    public boolean isUserAnInviterOfInvitation(Long invitationId, Long userId) {
        return teamInvitationPostgresSQLRepo.existsByInvitationIdAndInviterId(invitationId, userId);
    }
}
