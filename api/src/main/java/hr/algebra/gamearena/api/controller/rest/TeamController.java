package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.team.TeamMinimalView;
import hr.algebra.gamearena.api.service.team.ITeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
