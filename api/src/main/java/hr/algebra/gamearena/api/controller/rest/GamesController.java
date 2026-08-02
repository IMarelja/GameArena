package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequest;
import hr.algebra.gamearena.api.dto.games.GamesEditRequest;
import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.service.game.IGameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GamesController {
    private final IGameService gameService;

    public GamesController(IGameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GamesView>>> getAllGames() {
        return ResponseEntity.ok(ApiResponse.success(gameService.getAll()));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<GamesView>>> getAllActiveGames() {
        return ResponseEntity.ok(ApiResponse.success(gameService.getAllActive()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GamesView>> getGames(@PathVariable("id") Long gameId) {
        var game = this.gameService.getById(gameId);

        return game.map(gameViewDto -> ResponseEntity.ok(ApiResponse.success(gameViewDto)))
                .orElseThrow(() -> new NotFoundException("Game with id: " + gameId + " not found"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GamesView>> createGames(@Valid @RequestBody GamesCreateRequest gamesCreateRequest) {
        gamesCreateRequest.setName(gamesCreateRequest.getName().trim());

        if (gamesCreateRequest.getDescription() != null)
            gamesCreateRequest.setDescription(gamesCreateRequest.getDescription().trim());

        return ResponseEntity.ok(ApiResponse.success(this.gameService.create(gamesCreateRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GamesView>> updateGames(@PathVariable("id") Long id, @Valid @RequestBody GamesEditRequest gamesEditRequest) {
        gamesEditRequest.setName(gamesEditRequest.getName().trim());

        if (gamesEditRequest.getDescription() != null)
            gamesEditRequest.setDescription(gamesEditRequest.getDescription().trim());

        return ResponseEntity.ok(ApiResponse.success(this.gameService.update(id, gamesEditRequest)));
    }

}
