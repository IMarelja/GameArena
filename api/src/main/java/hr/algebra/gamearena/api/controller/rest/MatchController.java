package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchDetailedFullView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.exceptions.extenders.BadRequestedException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.match.IMatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    /*
    * 🐟
    * Kill your grandma
    * */

    private final IMatchService matchService;

    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<MatchDetailedFullView>>> getMyMatches(@AuthenticationPrincipal JwtTokenClaim caller) {
        return ResponseEntity.ok(ApiResponse.success(matchService.getMatchesByUserId(caller.userId())));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<MatchDetailedFullView>>> getMatchesForUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(matchService.getMatchesByUserId(id)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<MatchDetailedFullView>> getMatch(@PathVariable Long id) {
        return matchService.getById(id)
                .map(match -> ResponseEntity.ok(ApiResponse.success(match)))
                .orElseThrow(() -> new NotFoundException("Match with id: " + id + " not found"));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    // Tournament Organizer only
    public ResponseEntity<ApiResponse<MatchDetailedFullView>> createMatch(
            @AuthenticationPrincipal JwtTokenClaim caller,
            @Valid @RequestBody MatchCreateRequest request
    ) {
        if(request.getScheduledAt() != null && !request.getScheduledAt().getOffset().equals(ZoneOffset.UTC))
            throw new BadRequestedException("Scheduled at offset is not UTC or 00+00, it is: " + request.getScheduledAt().getOffset());

        return ResponseEntity.ok(ApiResponse.success(matchService.createMatchAndPushNotification(caller.userId(), request)));
    }
}
