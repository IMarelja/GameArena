package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamEditRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberEditRequest;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberFullView;
import hr.algebra.gamearena.api.dto.team.member.TeamMemberMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.team.ITeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    /** Team */
    // BEGIN
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamMinimalView>>> getAllTeams(){
        return ResponseEntity.ok(ApiResponse.success(teamService.getAll()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/for/me")
    public ResponseEntity<ApiResponse<TeamMinimalView>> createTeam(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @Valid @RequestBody TeamCreateRequest team
    ){
        var createdTeam = teamService.createTeamByUsersRequest(caller.userId(), team);

        return ResponseEntity.ok(ApiResponse.success(createdTeam));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TeamMinimalView>>> getTeamsForMe(@AuthenticationPrincipal JwtTokenClaim caller){
        return ResponseEntity.ok(ApiResponse.success(teamService.getTeamsForUser(caller.userId())));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMinimalView>> getTeamById(@PathVariable Long id){
        var team = teamService.getTeamById(id);

        return team.map(teamMinimalView -> ResponseEntity.ok(ApiResponse.success(teamMinimalView)))
                .orElseThrow(() -> new NotFoundException("Team not found with id: " + id));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMinimalView>> editTeam(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody TeamEditRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(teamService.editTeam(caller.userId(), id, request)));
    }
    // END

    /** Team Member */

    // BEGIN
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{teamId}/member/me")
    public ResponseEntity<ApiResponse<TeamMemberFullView>> getMeTeamMember(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long teamId
    ){
        return teamService.getTeamMemberByUserIdAndTeamId(caller.userId(), teamId)
                .map(member -> ResponseEntity.ok(ApiResponse.success(member)))
                .orElseThrow(() -> new NotFoundException("You are not a member of team with id: " + teamId));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{teamId}/member/{memberId}")
    public ResponseEntity<ApiResponse<TeamMemberFullView>> getTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId
    ){
        return teamService.getTeamMemberByIdAndTeamId(memberId, teamId)
                .map(member -> ResponseEntity.ok(ApiResponse.success(member)))
                .orElseThrow(() -> new NotFoundException("Team member (" + memberId + ") not found in team with id: " + teamId));
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberMinimalView>>> getTeamMembers(@PathVariable Long teamId){
        return ResponseEntity.ok(ApiResponse.success(teamService.getTeamMembers(teamId)));
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{teamId}/member/{memberId}")
    public ResponseEntity<ApiResponse<TeamMemberFullView>> editTeamMemberRole(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @Valid @RequestBody TeamMemberEditRequest request
    ){
        return ResponseEntity.ok(ApiResponse.success(teamService.editTeamMemberRole(caller.userId(), teamId, memberId, request)));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{teamId}/member/{memberId}")
    public ResponseEntity<ApiResponse<Void>> removeTeamMember(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long teamId,
            @PathVariable Long memberId
    ){
        teamService.removeTeamMember(caller.userId(), teamId, memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(null));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveTeam(
            @AuthenticationPrincipal JwtTokenClaim claim,
            @PathVariable Long teamId
    ){
        teamService.leaveTeam(claim.userId(), teamId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }
    // END

    /** Team Invitation */

    // BEGIN
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invitation/me")
    public ResponseEntity<ApiResponse<List<TeamInvitationView>>> getMyInvitations(
            @AuthenticationPrincipal JwtTokenClaim caller
    ){
        return ResponseEntity.ok(ApiResponse.success(teamService.getInvitationsForUser(caller.userId())));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/invitation/{id}")
    public ResponseEntity<ApiResponse<TeamInvitationView>> getInvitationById(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id
    ){
        var invitation = teamService.getInvitationById(caller.userId(), id);

        return invitation.map(view -> ResponseEntity.ok(ApiResponse.success(view)))
                .orElseThrow(() -> new NotFoundException("Team invitation not found with id: " + id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{teamId}/invite/user/{userId}")
    public ResponseEntity<ApiResponse<TeamInvitationView>> inviteUser(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long teamId,
            @PathVariable Long userId
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(teamService.createInvitationAndPushNotification(caller.userId(), teamId, userId)));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/invitation/{id}/responce")
    public ResponseEntity<ApiResponse<TeamInvitationView>> invitationResponse(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody TeamInvitationResponseEditRequest invitedRequest
    ){
        // Only the invited user
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(teamService.respondInvitationAndPushNotification(id, caller.userId(), invitedRequest)));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/invitation/{id}/update")
    public ResponseEntity<ApiResponse<TeamInvitationView>> updateInvitation(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody InviterTeamInvitationEditRequest inviterRequest
    ){
        // Only the inviter
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(teamService.updateInvitationAndPushNotification(id, caller.userId(), inviterRequest)));
    }
    // END
}
