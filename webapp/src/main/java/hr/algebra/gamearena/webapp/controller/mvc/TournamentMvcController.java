package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.TournamentDetailViewData;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TournamentMvcController {

    private final ITournamentService tournamentService;
    private final IMatchService matchService;

    public TournamentMvcController(ITournamentService tournamentService, IMatchService matchService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    @GetMapping("/tournaments")
    @PreAuthorize("permitAll()")
    public ModelAndView listTournaments() {
        try {
            return MvcResponse.fromApiResult(
                    "tournaments",
                    tournamentService.getAllTournaments(),
                            data -> data
            ).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "tournaments", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }

    @GetMapping("/tournaments/{id}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewTournament(@PathVariable Long id) {
        try {
            var tournament = tournamentService.getTournamentById(id).data();
            var members = tournamentService.getTournamentMembers(id).data();
            var matches = matchService.getMatchesByTournamentId(id).data();

            return MvcResponse.success(
                    HttpStatus.OK,
                    "tournament",
                    new TournamentDetailViewData(
                            tournament,
                            members,
                            matches
                    )
            ).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "tournament", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }
}
