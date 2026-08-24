package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TournamentController {

    private final ITournamentService tournamentService;

    public TournamentController(ITournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/tournaments")
    public ModelAndView listTournaments() {
        return MvcResponse.fromApiResult("tournaments", tournamentService.getAllTournaments(), data -> data)
                .toModelAndView();
    }
}
