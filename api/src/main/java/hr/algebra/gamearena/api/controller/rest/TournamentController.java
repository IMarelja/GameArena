package hr.algebra.gamearena.api.controller.rest;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<TournamentSave>> createTournament(@Valid @RequestBody TournamentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
