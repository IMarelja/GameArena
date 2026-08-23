package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.leaderboard.TournamentMemberStatsEntryView;
import hr.algebra.gamearena.api.dto.leaderboard.TournamentStatsEntryView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.service.leaderboard.ILeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final ILeaderboardService leaderboardService;

    public LeaderboardController(ILeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/tournament/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentMemberStatsEntryView>>> getTournamentLeaderboard(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getTournamentLeaderboard(id)));
    }

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TournamentStatsEntryView>>> getMyStats(@AuthenticationPrincipal JwtTokenClaim caller) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getStatsFromUserId(caller.userId())));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<TournamentStatsEntryView>>> getStatsForUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getStatsFromUserId(id)));
    }
}
