package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentCreateFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentCreatePostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentDetailViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentEditFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentEditPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchCreateFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchCreatePostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberEditFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberEditPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberRoleEditViewEnum;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberViewData;
import hr.algebra.gamearena.webapp.service.games.IGamesService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class TournamentMvcController {

    private static final String TOURNAMENT_VIEW = "tournament";
    private static final String TOURNAMENTS_VIEW = "tournaments";
    private static final String TOURNAMENT_ADD_VIEW = "tournament-add";
    private static final String TOURNAMENT_EDIT_VIEW = "tournament-edit";
    private static final String TOURNAMENT_MEMBER_EDIT_VIEW = "tournament-member-edit";
    private static final String TOURNAMENT_MATCH_ADD_VIEW = "tournament-match-add";

    private static final String MATCH_VIEW = "match";

    private static final String GAMES_LOAD_FAILED = "Failed to load available games. Please try again later.";

    private final ITournamentService tournamentService;
    private final IMatchService matchService;
    private final IGamesService gamesService;

    public TournamentMvcController(
            ITournamentService tournamentService,
            IMatchService matchService,
            IGamesService gamesService)
    {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.gamesService = gamesService;
    }

    @GetMapping("/tournaments")
    @PreAuthorize("permitAll()")
    public ModelAndView listTournaments() throws NotFoundException, UnexpectedApiErrorException {
        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENTS_VIEW,
                tournamentService.getAllTournaments()
                        .stream()
                        .map(TournamentViewData::fromTournamentFullViewDecereal)
                        .toList()
        ).toModelAndView();
    }

    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewTournament(@PathVariable Long tournamentId) throws NotFoundException, UnexpectedApiErrorException {
        var tournament = TournamentViewData.fromTournamentFullViewDecereal(tournamentService.getTournamentById(tournamentId));

        Optional<List<TournamentMemberViewData>> members;
        try {
            members = Optional.of(tournamentService.getTournamentMembers(tournamentId).stream().map(TournamentMemberViewData::from).toList());
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            members = Optional.empty();
        }

        Optional<List<MatchViewData>> matches;
        try {
            matches = Optional.of(matchService.getMatchesByTournamentId(tournamentId).stream().map(MatchViewData::fromMatchDetailFullViewDecereal).toList());
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            matches = Optional.empty();
        }

        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENT_VIEW,
                new TournamentDetailViewData(
                        tournament,
                        members,
                        matches
                )
        ).toModelAndView();
    }

    @GetMapping("/tournaments/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addTournamentForm() {
        List<GameViewData> activeGames;

        try {
            activeGames = gamesService
                    .getActiveGames()
                    .stream()
                    .map(GameViewData::fromGamesViewDecereal)
                    .toList();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            activeGames = List.of();
        }

        var form = new TournamentCreatePostViewModel(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENT_ADD_VIEW,
                new TournamentCreateFormViewData(
                        form,
                        activeGames
                )
        ).toModelAndView();
}

    @PostMapping("/tournaments/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView addTournament(
            @Valid @ModelAttribute("form") TournamentCreatePostViewModel form,
            BindingResult bindingResult
    ) {
        List<GameViewData> activeGames;
        try {
            activeGames = gamesService
                    .getActiveGames()
                    .stream()
                    .map(GameViewData::fromGamesViewDecereal)
                    .toList();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    TOURNAMENT_ADD_VIEW,
                    MvcError.toListMvcErrorFromMvcError(new MvcError(GAMES_LOAD_FAILED))
            ).toModelAndView();
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TOURNAMENT_ADD_VIEW,
                    new TournamentCreateFormViewData(
                            form,
                            activeGames),
                    errors
            ).toModelAndView();
        }

        try {
            var created = tournamentService.createTournament(form.toTournamentCreateCereal());
            return MvcResponse.redirect("/" + TOURNAMENT_VIEW + "/" + created.id());
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TOURNAMENT_ADD_VIEW,
                    new TournamentCreateFormViewData(
                            form,
                            activeGames),
                    errors
            ).toModelAndView();
        }
    }

    @GetMapping("/tournament/{tournamentId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTournamentForm(@PathVariable Long tournamentId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can edit it"));
        }

        var tournament = tournamentService.getTournamentById(tournamentId);
        var form = TournamentEditPostViewModel.fromTournamentFullViewDecereal(tournament);
        var activeGames = loadActiveGames();

        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENT_EDIT_VIEW,
                new TournamentEditFormViewData(
                        tournamentId,
                        form,
                        activeGames)
        ).toModelAndView();
    }

    @PostMapping("/tournament/{tournamentId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTournament(
            @PathVariable Long tournamentId,
            @Valid @ModelAttribute("form") TournamentEditPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can edit it"));
        }

        var activeGames = loadActiveGames();

        if (bindingResult.hasErrors()) {
            var errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TOURNAMENT_EDIT_VIEW,
                    new TournamentEditFormViewData(
                            tournamentId,
                            form,
                            activeGames
                    ),
                    errors
            ).toModelAndView();
        }

        try {
            tournamentService.editTournament(tournamentId, form.toTournamentEditCereal());
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TOURNAMENT_EDIT_VIEW,
                    new TournamentEditFormViewData(
                            tournamentId,
                            form,
                            activeGames
                    ),
                    errors
            ).toModelAndView();
        }

        return MvcResponse.redirect("/" + TOURNAMENT_VIEW + "/" + tournamentId);
    }

    private Optional<List<GameViewData>> loadActiveGames() {
        try {
            return Optional.of(gamesService.getActiveGames()
                    .stream()
                    .map(GameViewData::fromGamesViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return Optional.empty();
        }
    }

    @GetMapping("/tournament/{tournamentId}/member/{memberId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTournamentMemberForm(@PathVariable Long tournamentId, @PathVariable Long memberId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can edit members"));
        }

        var member = findMember(tournamentId, memberId);
        var form = new TournamentMemberEditPostViewModel(TournamentMemberRoleEditViewEnum.fromDecereal(member.role()));

        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENT_MEMBER_EDIT_VIEW,
                new TournamentMemberEditFormViewData(
                        tournamentId,
                        memberId,
                        member.user().username(),
                        form)
        ).toModelAndView();
    }

    @PostMapping("/tournament/{tournamentId}/member/{memberId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTournamentMember(
            @PathVariable Long tournamentId,
            @PathVariable Long memberId,
            @Valid @ModelAttribute("form") TournamentMemberEditPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can edit members"));
        }

        String username;
        try {
            username = findMember(tournamentId, memberId).user().username();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    e.getStatus(),
                    TOURNAMENT_MEMBER_EDIT_VIEW,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TOURNAMENT_MEMBER_EDIT_VIEW,
                    new TournamentMemberEditFormViewData(
                            tournamentId,
                            memberId,
                            username,
                            form)
                    , errors
            ).toModelAndView();
        }

        try {
            tournamentService.editTournamentMember(memberId, form.toTournamentMemberEditCereal());
            return MvcResponse.redirect("/" + TOURNAMENT_VIEW + "/" + tournamentId);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | ConflictException | BadRequestedExceptions | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TOURNAMENT_MEMBER_EDIT_VIEW,
                    new TournamentMemberEditFormViewData(
                            tournamentId,
                            memberId, username,
                            form)
                    , errors
            ).toModelAndView();
        }
    }

    private TournamentMemberViewDecereal findMember(Long tournamentId, Long memberId) throws NotFoundException, UnexpectedApiErrorException {
        return tournamentService.getTournamentMembers(tournamentId).stream()
                .filter(member -> member.id().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(List.of("Tournament member (" + memberId + ") not found")));
    }

    @GetMapping("/tournament/{tournamentId}/match/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addMatchForm(@PathVariable Long tournamentId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can add matches"));
        }

        var members = tournamentService.getTournamentMembers(tournamentId).stream().map(TournamentMemberViewData::from).toList();
        var form = new MatchCreatePostViewModel(null, null, null);

        return MvcResponse.success(
                HttpStatus.OK,
                TOURNAMENT_MATCH_ADD_VIEW,
                new MatchCreateFormViewData(
                        tournamentId,
                        form,
                        members)
        ).toModelAndView();
    }

    @PostMapping("/tournament/{tournamentId}/match/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addMatch(
            @PathVariable Long tournamentId,
            @Valid @ModelAttribute("form") MatchCreatePostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(List.of("Only an admin or this tournament's organizer can add matches"));
        }

        var members = tournamentService.getTournamentMembers(tournamentId)
                .stream()
                .map(TournamentMemberViewData::from)
                .toList();

        if (form.playerOneId() != null && form.playerOneId().equals(form.playerTwoId())) {
            bindingResult.reject("samePlayer", "Player one and player two must be different");
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> new MvcError(error.getDefaultMessage())
                    ).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TOURNAMENT_MATCH_ADD_VIEW,
                    new MatchCreateFormViewData(
                            tournamentId,
                            form,
                            members
                    ),
                    errors
            ).toModelAndView();
        }

        try {
            var created = matchService.createMatch(form.toMatchCreateCereal(tournamentId));
            return MvcResponse.redirect("/"+ MATCH_VIEW + "/" + created.id());
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | BadRequestedExceptions | UnexpectedApiErrorException e) {
            var errors = e.getMessages().stream().map(MvcError::new).toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TOURNAMENT_MATCH_ADD_VIEW,
                    new MatchCreateFormViewData(
                            tournamentId,
                            form,
                            members
                    ),
                    errors
            ).toModelAndView();
        }
    }
}
