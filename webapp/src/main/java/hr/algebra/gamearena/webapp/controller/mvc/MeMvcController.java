package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.user.MeProfileViewData;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import hr.algebra.gamearena.webapp.service.leaderboard.ILeaderboardService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import hr.algebra.gamearena.webapp.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MeMvcController {

    private final IUserService userService;
    private final ILeaderboardService leaderboardService;
    private final ITournamentService tournamentService;
    private final IMatchService matchService;
    private final IJwtService jwtService;

    public MeMvcController(
            IUserService userService,
            ILeaderboardService leaderboardService,
            ITournamentService tournamentService,
            IMatchService matchService,
            IJwtService jwtService)
    {
        this.userService = userService;
        this.leaderboardService = leaderboardService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.jwtService = jwtService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView me() throws UnauthorizedException {
        try {
            var user = userService.getMe().data();
            var leaderboard = leaderboardService.getMyStats().data();
            var tournaments = tournamentService.getMyTournaments().data();
            var matches = matchService.getMyMatches().data();

            return MvcResponse.success(
                    HttpStatus.OK,
                    "me",
                    new MeProfileViewData(
                            user,
                            leaderboard,
                            tournaments,
                            matches)
            ).toModelAndView();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            jwtService.clearToken();

            return MvcResponse.errors(
                    e.getStatus(),
                    "me",
                    MvcError.fromListString(e.getMessages())
            ).toModelAndView();
        }
    }
}
