package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.AdminQueryGetViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.AdminQueryViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.games.GameCreatePostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.games.GameEditFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.admin.games.GameEditPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;
import hr.algebra.gamearena.webapp.service.admin.IAdminService;
import hr.algebra.gamearena.webapp.service.games.IGamesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        @Valid AdminQueryGetViewModel query,
        BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    ADMIN_VIEW,
                    new AdminQueryViewData(query, Optional.empty(), Optional.empty(), Optional.empty()),
                    errors
            ).toModelAndView();
        }

        return buildAdminQueryView(query);
    }

    @GetMapping("/admin/game/add")
    public ModelAndView addGamePage(){
        var form = new GameCreatePostViewModel(
                null,
                null
        );

        return MvcResponse.success(
                HttpStatus.OK,
                ADMIN_GAME_CREATE_VIEW,
                form
        ).toModelAndView();
    }

    @PostMapping("/admin/game/add")
    public ModelAndView addGamePost(
            @Valid GameCreatePostViewModel create,
            BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException{

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    ADMIN_GAME_CREATE_VIEW,
                    create,
                    errors
            ).toModelAndView();
        }

        try{
            gameService.createGame(create.getCereal());
        } catch (UnexpectedApiErrorException | NotFoundException | BadRequestedExceptions | ConflictException e) {
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    ADMIN_GAME_CREATE_VIEW,
                    create,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }


        return buildAdminQueryView(new AdminQueryGetViewModel(
                null,
                false,
                null,
                false
        ));
    }

    @GetMapping("/admin/game/{id}/edit")
    public ModelAndView editGamePage(@PathVariable Long id) throws UnauthorizedException, UnexpectedApiErrorException, NotFoundException {

        var game = gameService.getById(id);


        var page = new GameEditFormViewData(
                id,
                new GameEditPostViewModel(
                        game.name(),
                        game.description(),
                        game.isActive()
                )
        );

        return MvcResponse.success(
                HttpStatus.OK,
                ADMIN_GAME_EDIT_VIEW,
                page
        ).toModelAndView();
    }

    @PostMapping("/admin/game/{id}/edit")
    public ModelAndView editGamePost(
            @PathVariable Long id,
            @Valid GameEditPostViewModel edit,
            BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException{

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    ADMIN_GAME_EDIT_VIEW,
                    new GameEditFormViewData(
                            id,
                            edit),
                    errors
            ).toModelAndView();
        }

        try{
            gameService.editGame(id, edit.getCereal());
        } catch (UnexpectedApiErrorException | NotFoundException | BadRequestedExceptions | ConflictException e) {
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    ADMIN_GAME_EDIT_VIEW,
                    new GameEditFormViewData(
                            id,
                            edit),
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }


        return buildAdminQueryView(new AdminQueryGetViewModel(
                null,
                false,
                null,
                false
        ));
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
