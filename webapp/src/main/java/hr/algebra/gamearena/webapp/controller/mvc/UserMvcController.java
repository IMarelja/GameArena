package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.user.UserSuspendCereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.leaderboard.TournamentStatsEntryViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.TeamMinimalViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteCreatePostView;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteCreateViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberAddFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberAddPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserFullViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserJustUsernameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserProfileViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserViewData;
import hr.algebra.gamearena.webapp.service.authentication.user.IAuthenticatedUserService;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import hr.algebra.gamearena.webapp.service.leaderboard.ILeaderboardService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.team.ITeamService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import hr.algebra.gamearena.webapp.service.user.IUserService;
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
public class UserMvcController {

    private final IUserService userService;
    private final IMatchService matchService;
    private final ITournamentService tournamentService;
    private final ILeaderboardService leaderboardService;
    private final IJwtService jwtService;
    private final IAuthenticatedUserService authenticatedUserService;
    private final ITeamService teamService;

    private static final String ME_VIEW = "me";

    private static final String USER_VIEW = "user";
    private static final String USERS_VIEW = "users";

    private static final String USER_SUSPEND_VIEW = "user-suspend";
    private static final String USER_TOURNAMENT_ADD_VIEW = "user-tournament-add";
    private static final String USER_TEAM_INVITE_ADD_VIEW = "user-team-invite-add";

    public UserMvcController(
            IUserService userService,
            IMatchService matchService,
            ITournamentService tournamentService,
            ILeaderboardService leaderboardService,
            IJwtService jwtService,
            IAuthenticatedUserService authenticatedUserService,
            ITeamService teamService)
    {
        this.userService = userService;
        this.matchService = matchService;
        this.tournamentService = tournamentService;
        this.leaderboardService = leaderboardService;
        this.jwtService = jwtService;
        this.authenticatedUserService = authenticatedUserService;
        this.teamService = teamService;
    }

    @GetMapping("/users")
    @PreAuthorize("permitAll()")
    public ModelAndView listUsers() throws NotFoundException, UnexpectedApiErrorException {
        return MvcResponse.success(
                HttpStatus.OK,
                USERS_VIEW,
                userService.getAllUsers().stream().map(UserViewData::from).toList()
        ).toModelAndView();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewUser(@PathVariable Long id) throws NotFoundException, UnexpectedApiErrorException {
        var claims = jwtService.getTokenClaimsAndValidateOrNull();
        if (claims != null && claims.userId().equals(id)) {
            return MvcResponse.redirect("/" + ME_VIEW);
        }

        var user = UserViewData.from(userService.getUserById(id));

        Optional<List<TournamentStatsEntryViewData>> leaderboard;
        try {
            leaderboard = Optional.of(leaderboardService.getStatsByUserId(id)
                    .stream()
                    .map(TournamentStatsEntryViewData::fromTournamentStatsEntryViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            leaderboard = Optional.empty();
        }

        Optional<List<TournamentViewData>> tournaments;
        try {
            tournaments = Optional.of(tournamentService.getTournamentsByUserId(id)
                    .stream()
                    .map(TournamentViewData::fromTournamentFullViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            tournaments = Optional.empty();
        }

        Optional<List<MatchViewData>> matches;
        try {
            matches = Optional.of(matchService.getMatchesByUserId(id)
                    .stream()
                    .map(MatchViewData::fromMatchDetailFullViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            matches = Optional.empty();
        }

        return MvcResponse.success(
                HttpStatus.OK,
                USER_VIEW,
                new UserProfileViewData(user, leaderboard, tournaments, matches, canAddToTournament())
        ).toModelAndView();
    }

    @GetMapping("/user/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView suspendConfirm(@PathVariable Long id) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        var user = UserFullViewData.from(userService.getUserFullById(id));
        return MvcResponse.success(
                HttpStatus.OK,
                USER_SUSPEND_VIEW,
                user
        ).toModelAndView();
    }

    @PostMapping("/user/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView suspend(@PathVariable Long id) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        var current = userService.getUserFullById(id);
        boolean newIsActive = !Boolean.TRUE.equals(current.isActive());

        try {
            userService.suspendAccount(new UserSuspendCereal(id, newIsActive));
            return MvcResponse.redirect("/" + USER_VIEW + "/" + id);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    e.getStatus(),
                    USER_SUSPEND_VIEW,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }
    }

    @GetMapping("/user/{id}/tournaments/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToTournamentForm(@PathVariable Long id) throws NotFoundException, UnexpectedApiErrorException {
        var username = userService.getUserById(id).username();
        var tournaments = tournamentsAddableByCaller();
        var form = new TournamentMemberAddPostViewModel(null, null);

        return MvcResponse.success(
                HttpStatus.OK,
                USER_TOURNAMENT_ADD_VIEW,
                new TournamentMemberAddFormViewData(
                        id,
                        username,
                        form,
                        tournaments)
        ).toModelAndView();
    }

    @PostMapping("/user/{id}/tournaments/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addToTournament(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") TournamentMemberAddPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException {
        String username;
        try {
            username = userService.getUserById(id).username();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    e.getStatus(),
                    USER_TOURNAMENT_ADD_VIEW,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }
        var tournaments = tournamentsAddableByCaller();

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    USER_TOURNAMENT_ADD_VIEW,
                    new TournamentMemberAddFormViewData(
                            id,
                            username,
                            form,
                            tournaments),
                    errors
            ).toModelAndView();
        }

        try {
            tournamentService.addTournamentMember(form.tournamentId(), form.toTournamentMemberCreateCereal(id));

            return MvcResponse.redirect("/" + USER_VIEW + "/" + id);
        } catch (NotFoundException | BadRequestedExceptions | ConflictException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    USER_TOURNAMENT_ADD_VIEW,
                    new TournamentMemberAddFormViewData(
                            id,
                            username,
                            form,
                            tournaments),
                    errors
            ).toModelAndView();
        }
    }

    @GetMapping("/user/{id}/team-invite/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addTeamInviteForm(@PathVariable Long id) throws NotFoundException, UnexpectedApiErrorException {
        var user = UserJustUsernameViewData.fromUserViewDtoDecereal(userService.getUserById(id));
        var teams = myTeams();
        var form = new TeamInviteCreatePostView(null);

        return MvcResponse.success(
                HttpStatus.OK,
                USER_TEAM_INVITE_ADD_VIEW,
                new TeamInviteCreateViewData(user, form, teams)
        ).toModelAndView();
    }

    @PostMapping("/user/{id}/team-invite/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addTeamInvite(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") TeamInviteCreatePostView form,
            BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException, UnexpectedApiErrorException, NotFoundException {
        UserJustUsernameViewData user = UserJustUsernameViewData.fromUserViewDtoDecereal(userService.getUserById(id));

        var teams = myTeams();

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    USER_TEAM_INVITE_ADD_VIEW,
                    new TeamInviteCreateViewData(
                            user,
                            form,
                            teams),
                    errors
            ).toModelAndView();
        }

        try {
            var created = teamService.createInvitation(id, form.teamId());
            return MvcResponse.redirect("/team-invite/" + created.id());
        } catch (NotFoundException | ConflictException | BadRequestedExceptions | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    USER_TEAM_INVITE_ADD_VIEW,
                    new TeamInviteCreateViewData(
                            user,
                            form,
                            teams),
                    errors
            ).toModelAndView();
        }
    }

    private List<TeamMinimalViewData> myTeams() {
        try {
            return teamService.getAllMyTeam().stream().map(TeamMinimalViewData::from).toList();
        } catch (UnauthorizedException | NotFoundException | ForbiddenException | UnexpectedApiErrorException e) {
            return List.of();
        }
    }

    private boolean canAddToTournament() {
        if (!authenticatedUserService.isAuthenticated()) {
            return false;
        }
        if (authenticatedUserService.isAdmin()) {
            return true;
        }
        return !tournamentService.getOrganizersTournaments(currentUserId()).isEmpty();
    }

    private List<TournamentViewData> tournamentsAddableByCaller() {
        if (authenticatedUserService.isAdmin()) {
            try {
                return tournamentService.getAllTournaments()
                        .stream()
                        .map(TournamentViewData::fromTournamentFullViewDecereal)
                        .toList();
            } catch (NotFoundException | UnexpectedApiErrorException e) {
                return List.of();
            }
        }

        return tournamentService.getOrganizersTournaments(currentUserId())
                .stream()
                .map(TournamentViewData::fromTournamentFullViewDecereal)
                .toList();
    }

    private Long currentUserId() {
        return jwtService.getTokenClaimsAndValidateOrNull() != null
                ? jwtService.getTokenClaimsAndValidateOrNull().userId()
                : null;
    }
}
