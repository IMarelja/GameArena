package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.user.UserProfileViewData;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import hr.algebra.gamearena.webapp.service.leaderboard.ILeaderboardService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import hr.algebra.gamearena.webapp.service.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserMvcController {

    private final IUserService userService;
    private final IMatchService matchService;
    private final ITournamentService tournamentService;
    private final ILeaderboardService leaderboardService;
    private final IJwtService jwtService;

    public UserMvcController(
            IUserService userService,
            IMatchService matchService,
            ITournamentService tournamentService,
            ILeaderboardService leaderboardService,
            IJwtService jwtService)
    {
        this.userService = userService;
        this.matchService = matchService;
        this.tournamentService = tournamentService;
        this.leaderboardService = leaderboardService;
        this.jwtService = jwtService;
    }

    @GetMapping("/users")
    @PreAuthorize("permitAll()")
    public ModelAndView listUsers() {
        try {
            return MvcResponse.fromApiResult("users", userService.getAllUsers(), data -> data).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "users", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewUser(@PathVariable Long id) {
        var claims = jwtService.getTokenClaimsAndValidateOrNull();
        if (claims != null && claims.userId().equals(id)) {
            return MvcResponse.redirect("/me");
        }

        try {
            var user = userService.getUserById(id).data();
            var leaderboard = leaderboardService.getStatsByUserId(id).data();
            var tournaments = tournamentService.getTournamentsByUserId(id).data();
            var matches = matchService.getMatchesByUserId(id).data();

            return MvcResponse.success(
                    HttpStatus.OK,
                    "user",
                    new UserProfileViewData(user, leaderboard, tournaments, matches)
            ).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "user", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }
}
