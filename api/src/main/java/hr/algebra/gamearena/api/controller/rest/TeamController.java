package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.team.TeamCreateRequest;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.team.ITeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamMinimalView>>> getAllTeams(){
        return ResponseEntity.ok(ApiResponse.success(teamService.getAll()));
    }

    @PostMapping("/for/me")
    public ResponseEntity<ApiResponse<TeamMinimalView>> createTeam(@AuthenticationPrincipal JwtTokenClaim caller, @Valid @RequestBody TeamCreateRequest team){
        var createdTeam = teamService.createTeamByUsersRequest(caller.userId(), team);

        return ResponseEntity.ok(ApiResponse.success(createdTeam));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMinimalView>> getTeamById(@PathVariable Long id){
        var team = teamService.getTeamById(id);

        return team.map(teamMinimalView -> ResponseEntity.ok(ApiResponse.success(teamMinimalView)))
                .orElseThrow(() -> new NotFoundException("Team not found with id: " + id));
    }
}
