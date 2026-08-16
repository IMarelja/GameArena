package hr.algebra.gamearena.api.service.team;

import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.InvalidVariableException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.notification.NotificationType;
import hr.algebra.gamearena.api.model.notification.ReferenceType;
import hr.algebra.gamearena.api.model.team.InviteStatus;
import hr.algebra.gamearena.api.model.team.TeamInvitation;
import hr.algebra.gamearena.api.model.team.TeamInvitationSave;
import hr.algebra.gamearena.api.model.team.TeamInvitationUpdate;
import hr.algebra.gamearena.api.model.team.TeamMemberSave;
import hr.algebra.gamearena.api.model.team.TeamSave;
import hr.algebra.gamearena.api.repository.team.ITeamRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.service.notification.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService implements ITeamService {

    private final ITeamRepo teamRepo;
    private final INotificationService notificationService;
    private final IUserRepo userRepo;

    private String teamNotFoundByIdOutput(Long id) {
        return "Team not found with id: " + id;
    }

    private String invitationNotFoundByIdOutput(Long id) {
        return "Team invitation not found with id: " + id;
    }

    public TeamService(ITeamRepo teamRepo, INotificationService notificationService, IUserRepo userRepo) {
        this.teamRepo = teamRepo;
        this.notificationService = notificationService;
        this.userRepo = userRepo;
    }

    // Team

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

    // Team invitation

    @Override
    public Optional<TeamInvitationView> getInvitationById(Long callerUserId, Long id) {
        var invitation = teamRepo.getInvitationById(id)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(id)));

        if (!invitation.inviterId().equals(callerUserId) && !invitation.inviteeId().equals(callerUserId)) {
            throw new ForbiddenAccessException("You are not part of this invitation");
        }

        return Optional.of(TeamInvitationView.fromTeamInvitation(invitation));
    }

    @Override
    public TeamInvitationView createInvitationAndPushNotification(Long inviterId, Long teamId, Long userId) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        if (!teamRepo.isUserIdPartOfTeam(inviterId, teamId)) {
            throw new ForbiddenAccessException("You need to be part of the team to send invitations for it");
        }

        if (inviterId.equals(userId)) {
            throw new InvalidVariableException("You can't invite yourself to a team");
        }

        if (teamRepo.isUserIdPartOfTeam(userId, teamId)) {
            throw new ConflictException("User is already part of this team");
        }

        if (teamRepo.doesPendingTeamInvitationExist(teamId, userId)) {
            throw new ConflictException("User has already received an invitation for this team");
        }

        var invitationSave = new TeamInvitationSave();
        invitationSave.setTeamId(teamId);
        invitationSave.setInviterId(inviterId);
        invitationSave.setInviteeId(userId);
        invitationSave.setStatus(InviteStatus.PENDING);

        var invitation = teamRepo.save(invitationSave);

        pushInvitationNotification(NotificationType.TEAM_INVITATION, invitation.inviteeId(), invitation.id());

        return TeamInvitationView.fromTeamInvitation(invitation);
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

        var update = new TeamInvitationUpdate();
        update.setStatus(InviteStatus.PENDING.fromInviteResponseStatus(request.getStatus()));

        var updated = teamRepo.update(invitationId, update)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        if (updated.status() == InviteStatus.ACCEPTED) {
            var memberSave = new TeamMemberSave();

            memberSave.setTeamId(updated.teamId());
            memberSave.setUserId(updated.inviteeId());
            teamRepo.addMember(memberSave);
        }

        pushInvitationNotification(NotificationType.TEAM_INVITATION_RESPONSE, updated.inviterId(), updated.id());

        return TeamInvitationView.fromTeamInvitation(updated);
    }

    @Override
    public TeamInvitationView updateInvitationAndPushNotification(Long invitationId, Long callerId, InviterTeamInvitationEditRequest request) {

        var invitation = teamRepo.getInvitationById(invitationId)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        if (!teamRepo.isUserAnInviterOfInvitation(invitationId, callerId)) {
            throw new ForbiddenAccessException("Only the inviter can update this invitation");
        }

        if (invitation.status() != InviteStatus.PENDING) {
            throw new ConflictException("This invitation has already been resolved");
        }

        var update = new TeamInvitationUpdate();
        update.setStatus(InviteStatus.PENDING.fromInvitedInviteStatus(request.getStatus()));

        var updated = teamRepo.update(invitationId, update)
                .orElseThrow(() -> new NotFoundException(invitationNotFoundByIdOutput(invitationId)));

        pushInvitationNotification(NotificationType.TEAM_INVITATION_UPDATE, updated.inviteeId(), updated.id());

        return TeamInvitationView.fromTeamInvitation(updated);
    }

    // Team Member

    @Override
    public List<TeamMemberMinimalView> getTeamMembers(Long teamId) {
        var team = teamRepo.getTeamById(teamId)
                .orElseThrow(() -> new NotFoundException(teamNotFoundByIdOutput(teamId)));

        return teamRepo.getTeamMembers(teamId)
                .stream()
                .map(member -> {
                    var user = userRepo.findById(member.userId())
                            .orElseThrow(() -> new NotFoundException("User not found with id: " + member.userId()));

                    return TeamMemberMinimalView.fromTeamTeamMemberAndUser(team, member, user);
                })
                .toList();
    }

    @Override
    public void removeTeamMember(Long callerId, Long teamId, Long userId) {
        var team = teamRepo.getTeamById(teamId)
                .orElseThrow(() -> new NotFoundException(teamNotFoundByIdOutput(teamId)));

        boolean isCaptain = team.captain_id().equals(callerId);

        if (!isCaptain)
            throw new ForbiddenAccessException("Only the team captain can remove other members");

        boolean isSelf = callerId.equals(userId);

        if(isSelf)
            throw new InvalidVariableException("You can not remove self, you are the captain");

        if (!teamRepo.isUserIdPartOfTeam(userId, teamId))
            throw new NotFoundException("User is not part of this team");

        teamRepo.deleteMember(teamId, userId);
    }

    @Override
    public void leaveTeam(Long callerId, Long teamId) {
        var team = teamRepo.getTeamById(teamId)
                .orElseThrow(() -> new NotFoundException(teamNotFoundByIdOutput(teamId)));

        if (!teamRepo.isUserIdPartOfTeam(callerId, teamId))
            throw new NotFoundException("You are not part of this team");

        boolean isCaptain = team.captain_id().equals(callerId);

        if (isCaptain)
            throw new ForbiddenAccessException("You can not leave your team, you are the captain. Remove the team and end everyone");

        teamRepo.deleteMember(teamId, callerId);
    }

    @Override
    public boolean isUserPartOfTeam(Long callerId, Long teamId) {
        if (!teamRepo.doesTeamExist(teamId)) {
            throw new NotFoundException(teamNotFoundByIdOutput(teamId));
        }

        return teamRepo.isUserIdPartOfTeam(callerId, teamId);
    }

    private void pushInvitationNotification(NotificationType type, Long recipientUserId, Long invitationId) {
        notificationService.createAndPush(new NotificationCreateRequest(
                type,
                recipientUserId,
                invitationId,
                ReferenceType.TEAM_INVITATION));
    }
}
