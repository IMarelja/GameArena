package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamEditRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberEditRequest;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberFullView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.*;
import hr.algebra.gamearena.api.event.notification.TeamInvitationEvent;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.team.*;
import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import hr.algebra.gamearena.api.repository.team.ITeamRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService implements ITeamService {

    private static final String USER_NOT_FOUND = "User was found";

    private final ITeamRepo teamRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final IUserRepo userRepo;
    private final IGamesRepo gamesRepo;

    private String teamNotFoundByIdOutput(Long id) {
        return "Team not found with id: " + id;
    }

    private String invitationNotFoundByIdOutput(Long id) {
        return "Team invitation not found with id: " + id;
    }

    private String teamMemberNotFoundByIdOutput(Long id) {
        return "Team member not found with id: " + id;
    }

    private String teamMemberNotPartOfTeamOutput(Long teamId){
        return "Team member not part of team with id: " + teamId;
    }

    private String onlyCaptainCannotRemoveSelfOutput() {
        return "You are the only Captain in this Team, you can not remove yourself. "
                + "Assign someone else to be the Captain for this Team";
    }

    public TeamService(ITeamRepo teamRepo, ApplicationEventPublisher eventPublisher, IUserRepo userRepo, IGamesRepo gamesRepo) {
        this.teamRepo = teamRepo;
        this.eventPublisher = eventPublisher;
        this.userRepo = userRepo;
        this.gamesRepo = gamesRepo;
    }

    // Team

    @Override
    public List<TeamMinimalView> getAll() {
        return teamRepo.getAll()
                .stream()
                .map(this::toMinimalView)
                .toList();
    }

    @Override
    public Optional<TeamMinimalView> getTeamById(Long id) {
        return Optional.of(teamRepo.getTeamById(id)
                .map(this::toMinimalView)
                .orElseThrow(() -> new NotFoundException( teamNotFoundByIdOutput(id) )));
    }

    @Override
    public List<TeamMinimalView> getTeamsForUser(Long userId) {
        return teamRepo.getTeamsForUserId(userId)
                .stream()
                .map(this::toMinimalView)
                .toList();
    }

    @Override
    public TeamMinimalView createTeamByUsersRequest(Long userId, TeamCreateRequest team) {
        var teamSave = new TeamSave();
        teamSave.setName(team.getName());
        teamSave.setGameId(team.getGameId());

        var savedTeam = teamRepo.saveTeamInvite(teamSave);

        var captainSave = new TeamMemberSave();
        captainSave.setTeamId(savedTeam.id());
        captainSave.setUserId(userId);
        captainSave.setRole(TeamMemberRole.CAPTAIN);
        teamRepo.addMember(captainSave);

        return toMinimalView(savedTeam);
    }

    @Override
    public TeamMinimalView editTeam(Long callerId, Long teamId, TeamEditRequest request) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        if (!teamRepo.isUserTeamCaptain(callerId, teamId)) {
            throw new ForbiddenAccessException("Only the team captain can edit this team");
        }

        var teamUpdate = new TeamUpdate();
        teamUpdate.setName(request.getName());
        teamUpdate.setGameId(request.getGameId());

        var updated = teamRepo.updateTeam(teamId, teamUpdate)
                .orElseThrow(() -> new NotFoundException(teamNotFoundByIdOutput(teamId)));

        return toMinimalView(updated);
    }

    private TeamMinimalView toMinimalView(Team team) {
        var game = gamesRepo.getById(team.game_id());
        return TeamMinimalView.fromTeamAndGame(
                team,
                game,
                teamRepo.memberCountInATeam(team.id())
        );
    }

    // Team invitation

    @Override
    public Optional<TeamInvitationView> getInvitationById(Long callerUserId, Long id) {
        var invitation = teamRepo.getInvitationById(id)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(id)));

        if (!invitation.inviterId().equals(callerUserId) && !invitation.inviteeId().equals(callerUserId)) {
            throw new ForbiddenAccessException("You are not part of this invitation");
        }

        return Optional.of(toInvitationViewAndGarbage(invitation));
    }

    @Override
    public List<TeamInvitationView> getInvitationsForUser(Long callerUserId) {
        return teamRepo.getInvitationsForUserId(callerUserId)
                .stream()
                .map(this::toInvitationViewAndGarbage)
                .toList();
    }

    private TeamInvitationView toInvitationViewAndGarbage(TeamInvitation invitation) {
        var team = teamRepo.getTeamById(invitation.teamId()).orElseGet(Team::deletedTeam);
        var inviter = userRepo.findById(invitation.inviterId()).orElseGet(User::deletedUser);
        var invitee = userRepo.findById(invitation.inviteeId()).orElseGet(User::deletedUser);

        return TeamInvitationView.fromTeamInvitationTeamUserInviteeAndUserInviter(invitation, team, inviter, invitee);
    }

    @Override
    public TeamInvitationView createInvitationAndPushNotification(Long inviterId, Long teamId, Long userId) {
        var team = teamRepo.getTeamById(teamId)
                .orElseThrow(() -> new NotFoundException(teamNotFoundByIdOutput(teamId)));

        if (!teamRepo.isUserIdPartOfTeam(inviterId, teamId)) {
            throw new ForbiddenAccessException("You need to be part of the team to send invitations for it");
        }

        if (inviterId.equals(userId)) {
            throw new BadRequestedException("You can't invite yourself to a team");
        }

        if (teamRepo.isUserIdPartOfTeam(userId, teamId)) {
            throw new ConflictException("User is already part of this team");
        }

        if (teamRepo.doesPendingTeamInvitationExist(teamId, userId)) {
            throw new ConflictException("User has already received an invitation for this team");
        }

        var inviter = userRepo.findById(inviterId)
                .orElseThrow(() -> new NotFoundException("We can't find YOU???"));

        var invitee = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        var invitationSave = new TeamInvitationSave();
        invitationSave.setTeamId(teamId);
        invitationSave.setInviterId(inviterId);
        invitationSave.setInviteeId(userId);
        invitationSave.setStatus(InviteStatus.PENDING);

        var invitation = teamRepo.saveTeamInvite(invitationSave);

        pushInvitationNotification(NotificationType.TEAM_INVITATION, invitation.inviteeId(), invitation.id());

        return TeamInvitationView.fromTeamInvitationTeamUserInviteeAndUserInviter(invitation, team, inviter, invitee);
    }

    @Override
    public TeamInvitationView respondInvitationAndPushNotification(Long invitationId, Long callerId, TeamInvitationResponseEditRequest request) {

        var invitation = teamRepo.getInvitationById(invitationId)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        if (!teamRepo.isUserAnInviteeOfInvitation(invitationId, callerId)) {
            throw new ForbiddenAccessException("Only the invited user can respond to this invitation");
        }

        if (invitation.status() != InviteStatus.PENDING) {
            throw new ConflictException("This invitation has already been resolved");
        }

        var parties = validateInvitationPartiesOrCancel(invitation);

        var update = new TeamInvitationUpdate();
        update.setStatus(InviteStatus.PENDING.fromInviteResponseStatus(request.getStatus()));

        var updated = teamRepo.updateTeamInvite(invitationId, update)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        if (updated.status() == InviteStatus.ACCEPTED) {
            var memberSave = new TeamMemberSave();

            memberSave.setTeamId(updated.teamId());
            memberSave.setUserId(updated.inviteeId());
            memberSave.setRole(TeamMemberRole.REGULAR);
            teamRepo.addMember(memberSave);
        }

        pushInvitationNotification(NotificationType.TEAM_INVITATION_RESPONSE, updated.inviterId(), updated.id());

        return TeamInvitationView.fromTeamInvitationTeamUserInviteeAndUserInviter(updated, parties.team(), parties.inviter(), parties.invitee());
    }

    @Override
    public TeamInvitationView updateInvitationAndPushNotification(Long invitationId, Long callerId, InviterTeamInvitationEditRequest request) {

        var invitation = teamRepo.getInvitationById(invitationId)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        if (!teamRepo.isUserAnInviterOfInvitation(invitationId, callerId)) {
            throw new ForbiddenAccessException("Only the inviter can updateTeamInvite this invitation");
        }

        if (invitation.status() != InviteStatus.PENDING) {
            throw new ConflictException("This invitation has already been resolved");
        }

        var parties = validateInvitationPartiesOrCancel(invitation);

        var update = new TeamInvitationUpdate();
        update.setStatus(InviteStatus.PENDING.fromInvitedInviteStatus(request.getStatus()));

        var updated = teamRepo.updateTeamInvite(invitationId, update)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        pushInvitationNotification(NotificationType.TEAM_INVITATION_UPDATE, updated.inviteeId(), updated.id());

        return TeamInvitationView.fromTeamInvitationTeamUserInviteeAndUserInviter(updated, parties.team(), parties.inviter(), parties.invitee());
    }

    private record InvitationParties(Team team, User inviter, User invitee) {}

    private InvitationParties validateInvitationPartiesOrCancel(TeamInvitation invitation) {
        var invitee = userRepo.findById(invitation.inviteeId()).orElse(null);
        if (invitee == null || !Boolean.TRUE.equals(invitee.isActive()) || Boolean.TRUE.equals(invitee.isDeleted())) {
            cancelInvitation(invitation.id());
            throw new ForbiddenAccessException("The invited user is no longer able to respond to this invitation");
        }

        var inviter = userRepo.findById(invitation.inviterId()).orElse(null);
        if (inviter == null) {
            cancelInvitation(invitation.id());
            throw new NotFoundException("The user who sent this invitation no longer exists");
        }

        var team = teamRepo.getTeamById(invitation.teamId()).orElse(null);
        if (team == null) {
            cancelInvitation(invitation.id());
            throw new NotFoundException("Team is missing or no longer exists");
        }

        return new InvitationParties(team, inviter, invitee);
    }

    private void cancelInvitation(Long invitationId) {
        var update = new TeamInvitationUpdate();
        update.setStatus(InviteStatus.CANCELLED);
        teamRepo.updateTeamInvite(invitationId, update);
    }

    // Team Member

    @Override
    public Optional<TeamMemberFullView> getTeamMemberByUserIdAndTeamId(Long userId, Long teamId) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        var member = teamRepo.getTeamMemberByTeamIdAndUserId(teamId, userId);

        return member.map(this::toTeamMemberFullView);
    }

    @Override
    public Optional<TeamMemberFullView> getTeamMemberByIdAndTeamId(Long id, Long teamId) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        var member = teamRepo.getTeamMemberById(id);

        return member.map(this::toTeamMemberFullView);
    }

    @Override
    public List<TeamMemberMinimalView> getTeamMembers(Long teamId) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        return teamRepo.getTeamMembers(teamId)
                .stream()
                .map(member -> {
                    var user = userRepo.findById(member.userId())
                            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

                    return TeamMemberMinimalView.fromTeamMemberAndUser(member, user);
                })
                .toList();
    }

    @Override
    public TeamMemberFullView editTeamMemberRole(Long callerId, Long teamId, Long memberId, TeamMemberEditRequest request) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        if (!teamRepo.isUserTeamCaptain(callerId, teamId)) {
            throw new ForbiddenAccessException("Only the team captain can edit member roles");
        }

        var member = teamRepo.getTeamMemberById(memberId)
                .orElseThrow(() -> new NotFoundException(teamMemberNotFoundByIdOutput(memberId)));

        if (!member.teamId().equals(teamId)) {
            throw new NotFoundException(teamMemberNotPartOfTeamOutput(teamId));
        }

        boolean isSelf = member.userId().equals(callerId);
        boolean demotingFromCaptain = member.role() == TeamMemberRole.CAPTAIN && request.getRole().toTeamMemberRole() != TeamMemberRole.CAPTAIN;

        if (isSelf && demotingFromCaptain && onlyOneCaptain(teamId)) {
            throw new InvalidVariableException(onlyCaptainCannotRemoveSelfOutput());
        }

        var teamMemberUpdate = new TeamMemberUpdate();
        teamMemberUpdate.setRole(request.getRole().toTeamMemberRole());

        var updated = teamRepo.updateTeamMember(memberId, teamMemberUpdate)
                .orElseThrow(() -> new NotFoundException(teamMemberNotFoundByIdOutput(memberId)));


        return toTeamMemberFullView(updated);
    }

    private TeamMemberFullView toTeamMemberFullView(TeamMember member) {
        var user = userRepo.findById(member.userId());

        return TeamMemberFullView.fromTeamMemberAndUser(member, user);
    }

    @Override
    public void removeTeamMember(Long callerId, Long teamId, Long teamMemberId) {
        if (!teamRepo.doesTeamExist(teamId))
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));

        boolean isCaptain = teamRepo.isUserTeamCaptain(callerId, teamId);

        if (!isCaptain)
            throw new ForbiddenAccessException("Only the team captain can remove other members");

        var member = teamRepo.getTeamMemberById(teamMemberId)
                .orElseThrow(() -> new NotFoundException(teamMemberNotFoundByIdOutput(teamMemberId)));

        if (!member.teamId().equals(teamId))
            throw new NotFoundException(teamMemberNotPartOfTeamOutput(teamId));

        boolean isSelf = member.userId().equals(callerId);

        if(isSelf && onlyOneCaptain(teamId))
            throw new InvalidVariableException(onlyCaptainCannotRemoveSelfOutput());

        teamRepo.deleteTeamMember(teamMemberId);
    }

    @Override
    public void leaveTeam(Long callerId, Long teamId) {
        if (!teamRepo.doesTeamExist(teamId))
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));

        if (!teamRepo.isUserIdPartOfTeam(callerId, teamId))
            throw new NotFoundException("You are not part of this team");

        boolean isCaptain = teamRepo.isUserTeamCaptain(callerId, teamId);

        if (isCaptain && onlyOneCaptain(teamId))
            throw new InvalidVariableException(onlyCaptainCannotRemoveSelfOutput());

        teamRepo.deleteMember(teamId, callerId);
    }

    private boolean onlyOneCaptain(Long teamId) {
        return teamRepo.countTeamMembersByRole(teamId, TeamMemberRole.CAPTAIN) <= 1;
    }

    private void pushInvitationNotification(NotificationType type, Long recipientUserId, Long invitationId) {
        eventPublisher.publishEvent(new TeamInvitationEvent(type, recipientUserId, invitationId));
    }
}
