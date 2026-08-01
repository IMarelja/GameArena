package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.service.game.IGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GamesController {
    private final IGameService gameService;

    public GamesController(IGameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public ResponseEntity<ApiResponse<List<Games>>> getAllGames() {
        return ResponseEntity.ok(ApiResponse.success(gameService.getAll()));
    }


}
