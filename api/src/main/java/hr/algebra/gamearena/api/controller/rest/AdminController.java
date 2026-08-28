package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.match.MatchDetailedFullView;
import hr.algebra.gamearena.api.dto.match.MatchQueryDto;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.tournament.TournamentQueryDto;
import hr.algebra.gamearena.api.service.match.IMatchService;
import hr.algebra.gamearena.api.service.tournament.ITournamentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final IMatchService matchService;
    private final ITournamentService tournamentService;

    public AdminController(IMatchService matchService, ITournamentService tournamentService) {
        this.matchService = matchService;
        this.tournamentService = tournamentService;
    }

    @GetMapping("/match")
    public ResponseEntity<ApiResponse<List<MatchDetailedFullView>>> queryMatches(@Valid @ModelAttribute MatchQueryDto query) {
        return ResponseEntity.ok(ApiResponse.success(matchService.queryMatches(query)));
    }

    @GetMapping("/tournament")
    public ResponseEntity<ApiResponse<List<TournamentFullView>>> queryTournaments(@Valid @ModelAttribute TournamentQueryDto query) {
        return ResponseEntity.ok(ApiResponse.success(tournamentService.queryTournaments(query)));
    }
}
