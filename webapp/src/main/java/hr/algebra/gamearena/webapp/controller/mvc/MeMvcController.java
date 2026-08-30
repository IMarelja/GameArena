package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.leaderboard.TournamentStatsEntryViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.MeProfileViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserFullViewData;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import hr.algebra.gamearena.webapp.service.leaderboard.ILeaderboardService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.team.ITeamService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import hr.algebra.gamearena.webapp.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MeMvcController {

    private final IUserService userService;
    private final ILeaderboardService leaderboardService;
    private final ITournamentService tournamentService;
    private final IMatchService matchService;
    private final ITeamService teamService;
    private final IJwtService jwtService;

    private static final String ME_VIEW = "me";

    public MeMvcController(
            IUserService userService,
            ILeaderboardService leaderboardService,
            ITournamentService tournamentService,
            IMatchService matchService, ITeamService teamService,
            IJwtService jwtService)
    {
        this.userService = userService;
        this.leaderboardService = leaderboardService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView me() throws UnauthorizedException, UnexpectedApiErrorException, NotFoundException {
        var user = UserFullViewData.from(userService.getMe());

        Optional<List<TournamentStatsEntryViewData>> leaderboard;
        try {
            leaderboard = Optional.of(leaderboardService.getMyStats()
                    .stream()
                    .map(TournamentStatsEntryViewData::fromTournamentStatsEntryViewDecereal)
                    .toList());
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            leaderboard = Optional.empty();
        }

        Optional<List<TournamentViewData>> tournaments;
        try {
            tournaments = Optional.of(tournamentService.getMyTournaments()
                    .stream()
                    .map(TournamentViewData::fromTournamentFullViewDecereal)
                    .toList()
            );
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            tournaments = Optional.empty();
        }

        Optional<List<MatchViewData>> matches;
        try {
            matches = Optional.of(matchService.getMyMatches()
                    .stream()
                    .map(MatchViewData::fromMatchDetailFullViewDecereal)
                    .toList()
            );
        } catch (NotFoundException | UnexpectedApiErrorException ex) {
            matches = Optional.empty();
        }

        Optional<List<TeamInviteViewData>> invitations;
        try {
            invitations = Optional.of(teamService.getAllMyTeamInvitations()
                    .stream()
                    .map(TeamInviteViewData::fromTeamInvitationDecereal)
                    .toList()
            );
        } catch (NotFoundException | ForbiddenException | UnexpectedApiErrorException ex) {
            invitations = Optional.empty();
        }

        return MvcResponse.success(
                HttpStatus.OK,
                ME_VIEW,
                new MeProfileViewData(
                        user,
                        leaderboard,
                        tournaments,
                        matches,
                        invitations)
        ).toModelAndView();
    }

    @GetMapping("/me/delete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteAccountConfirm() {
        return MvcResponse.success(
                HttpStatus.OK,
                "delete-account",
                null)
        .toModelAndView();
    }

    @PostMapping("/me/delete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteAccount() throws UnauthorizedException {
        userService.deleteMyAccount();
        jwtService.clearToken();

        return MvcResponse.redirect("/");
    }
}
