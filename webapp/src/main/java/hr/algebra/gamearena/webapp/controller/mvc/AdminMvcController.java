package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.AdminQueryGetViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.AdminQueryViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;
import hr.algebra.gamearena.webapp.service.admin.IAdminService;
import hr.algebra.gamearena.webapp.service.games.IGamesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminMvcController {

    /**
     * 🦀
     * Love your grandma
     * */

    private static final String ADMIN_VIEW = "admin";
    private static final String ADMIN_GAME_CREATE_VIEW = "admin-game-create";
    private static final String ADMIN_GAME_EDIT_VIEW = "admin-game-edit";

    private final IAdminService adminService;
    private final IGamesService gameService;

    public AdminMvcController(IAdminService adminService, IGamesService gameService) {
        this.adminService = adminService;
        this.gameService = gameService;
    }

    @GetMapping("/admin")
    public ModelAndView adminPage() throws ForbiddenException, UnauthorizedException {
        var form = new AdminQueryGetViewModel(
                null,
                false,
                null,
                false
        );

        return buildAdminQueryView(form);
    }

    @GetMapping("/admin/query")
    public ModelAndView queryAdmin(
        @Valid AdminQueryGetViewModel query
    ) throws ForbiddenException, UnauthorizedException {
        return buildAdminQueryView(query);
    }

    private ModelAndView buildAdminQueryView(AdminQueryGetViewModel form) throws ForbiddenException, UnauthorizedException {
        List<MvcError> errors = new ArrayList<>();

        Optional<List<GameViewData>> gamesAll;

        try {
            gamesAll = Optional.of(gameService.getGames()
                    .stream()
                    .map(GameViewData::fromGamesViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException ex) {

            gamesAll = Optional.empty();
        }

        Optional<List<TournamentViewData>> tournaments;

        try {
            tournaments = Optional.of(adminService.queryTournaments(form.toTournamentMatchQueryCereal())
                    .stream()
                    .map(TournamentViewData::fromTournamentFullViewDecereal)
                    .toList());
        } catch (NotFoundException | BadRequestedExceptions | UnexpectedApiErrorException ex) {

            tournaments = Optional.empty();
        }

        Optional<List<MatchViewData>> matches;

        try {
            matches = Optional.of(adminService.queryMatches(form.toMatchQueryCereal())
                    .stream()
                    .map(MatchViewData::fromMatchDetailFullViewDecereal)
                    .toList());
        } catch (NotFoundException | BadRequestedExceptions | UnexpectedApiErrorException ex) {

            matches = Optional.empty();
        }

        return MvcResponse.dataWithErrors(
                HttpStatus.OK,
                ADMIN_VIEW,
                new AdminQueryViewData(
                        form,
                        gamesAll,
                        tournaments,
                        matches
                ),
                errors
        ).toModelAndView();
    }

}
