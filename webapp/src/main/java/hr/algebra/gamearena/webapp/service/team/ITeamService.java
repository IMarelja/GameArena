package hr.algebra.gamearena.webapp.service.team;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamAddCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationInviteeRespondCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationInviterUpdateCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;

import java.util.List;
import java.util.Optional;

public interface ITeamService {
    List<TeamMinimalViewDecereal> getAllTeams() throws NotFoundException, UnexpectedApiErrorException;
    TeamMinimalViewDecereal getTeamById(Long id) throws NotFoundException, UnexpectedApiErrorException;
    List<TeamMinimalViewDecereal> getAllMyTeam() throws UnauthorizedException, NotFoundException, ForbiddenException, UnexpectedApiErrorException;
    TeamMinimalViewDecereal addTeam(TeamAddCereal cereal) throws UnauthorizedException, BadRequestedExceptions, NotFoundException, UnexpectedApiErrorException, ForbiddenException;
    TeamMinimalViewDecereal editTeam(Long id, TeamEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;

    List<TeamMemberMinimalViewDecereal> getTeamMembers(Long teamId) throws NotFoundException, UnexpectedApiErrorException;
    TeamMemberFullViewDecereal getMyTeamMembership(Long teamId) throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException;
    Optional<TeamMemberFullViewDecereal> getMyTeamMembershipOrEmpty(Long teamId);
    TeamMemberFullViewDecereal editTeamMemberRole(Long teamId, Long memberId, TeamMemberEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
    void removeTeamMember(Long teamId, Long memberId) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;

    boolean isTeamCaptain(Long teamId);
    boolean isTeamMember(Long teamId);
    boolean isUserPartOfAnyTeam();

    List<TeamInvitationDecereal> getAllMyTeamInvitations() throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException;
    TeamInvitationDecereal getInvitationById(Long invitationId) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException;
    TeamInvitationDecereal createInvitation(Long userId, Long teamId) throws NotFoundException, UnauthorizedException, ForbiddenException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException;
    TeamInvitationDecereal respondInvitation(Long invitationId, TeamInvitationInviteeRespondCereal cereal) throws NotFoundException, UnauthorizedException, ForbiddenException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException;
    TeamInvitationDecereal updateInvitation(Long invitationId, TeamInvitationInviterUpdateCereal cereal) throws NotFoundException, UnauthorizedException, ForbiddenException, ConflictException, BadRequestedExceptions, UnexpectedApiErrorException;
}
