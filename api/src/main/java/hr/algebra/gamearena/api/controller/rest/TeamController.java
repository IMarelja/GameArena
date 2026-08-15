package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationResponseEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.InviterTeamInvitationEditRequest;
import hr.algebra.gamearena.api.dto.team.invitation.TeamInvitationView;
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

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMinimalView>> getTeamById(@PathVariable Long id){
        var team = teamService.getTeamById(id);

        return team.map(teamMinimalView -> ResponseEntity.ok(ApiResponse.success(teamMinimalView)))
                .orElseThrow(() -> new NotFoundException("Team not found with id: " + id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{teamId}/invite/user/{userId}")
    public ResponseEntity<ApiResponse<TeamInvitationView>> inviteUser(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long teamId,
            @PathVariable Long userId
    ){
        if(!teamService.isUserIdPartOfTeam(teamId, caller.userId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(new ApiError("You need to be part of the team to send invitation to that team")));

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(teamService.createInvitationAndPushNotification(caller.userId(), teamId, userId)));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/invitation/{id}/responce")
    public ResponseEntity<ApiResponse<TeamInvitationView>> invitationResponse(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody TeamInvitationResponseEditRequest invitedRequest
    ){
        // Only invited can update
        if(!teamService.isUserAnInviterOfInvitation(id, caller.userId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(new ApiError("Only the inviter can update this request")));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(teamService.respondInvitationAndPushNotification(id, invitedRequest)));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/invitation/{id}/update")
    public ResponseEntity<ApiResponse<TeamInvitationView>> updateInvitation(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @PathVariable Long id,
            @Valid @RequestBody InviterTeamInvitationEditRequest inviterRequest
    ){
        // Only inviter can update
        if(!teamService.isUserAnInviteeOfInvitation(id, caller.userId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(new ApiError("Only the invitee can update this request")));

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(teamService.updateInvitationAndPushNotification(id, inviterRequest)));
    }


}
