package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.team.TeamDetailViewData;
import hr.algebra.gamearena.webapp.service.team.ITeamService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams")
    @PreAuthorize("permitAll()")
    public ModelAndView listTeams() {
        try {
            return MvcResponse.fromApiResult("teams", teamService.getAllTeams(), data -> data).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "teams", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }

    @GetMapping("/teams/{id}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewTeam(@PathVariable Long id) {
        try {
            var team = teamService.getTeamById(id).data();
            var members = teamService.getTeamMembers(id).data();

            return MvcResponse.success(
                    HttpStatus.OK,
                    "team",
                    new TeamDetailViewData(team, members)
            ).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(HttpStatus.NOT_FOUND, "team", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }
}
