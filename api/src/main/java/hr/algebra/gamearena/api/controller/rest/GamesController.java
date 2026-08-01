package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.games.GamesCreateRequest;
import hr.algebra.gamearena.api.dto.games.GamesView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GamesView>> getGames(@PathVariable("id") Long gameId) {
        var game = this.gameService.getById(gameId);

        return game.map(gameViewDto -> ResponseEntity.ok(ApiResponse.success(gameViewDto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GamesView>> createGames(@Valid @RequestBody GamesCreateRequest gamesCreateRequst) {
        return ResponseEntity.ok(ApiResponse.success(this.gameService.create(gamesCreateRequst)));
    }



}
