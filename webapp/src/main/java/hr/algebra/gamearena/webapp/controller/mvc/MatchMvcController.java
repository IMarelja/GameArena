package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MatchMvcController {

    private final IMatchService matchService;

    public MatchMvcController(IMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/match/{id}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewMatch(@PathVariable Long id) {
        try {
            return MvcResponse.fromApiResult("match", matchService.getMatchById(id), data -> data).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "match", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }
}
