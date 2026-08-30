package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.team.member.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.games.GameViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.*;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberEditFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberEditPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberMinimalViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberRemoveViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.member.TeamMemberRoleViewEnum;
import hr.algebra.gamearena.webapp.service.games.IGamesService;
import hr.algebra.gamearena.webapp.service.team.ITeamService;
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
public class TeamMvcController {

    private static final String TEAM_VIEW = "team";
    private static final String TEAMS_VIEW = "teams";
    private static final String TEAM_ADD_VIEW = "team-add";
    private static final String TEAM_EDIT_VIEW = "team-edit";

    private static final String TEAM_MEMBER_EDIT_VIEW = "team-member-edit";
    private static final String TEAM_MEMBER_REMOVE_VIEW = "team-member-remove";

    private final ITeamService teamService;
    private final IGamesService gamesService;

    public TeamMvcController(ITeamService teamService, IGamesService gamesService) {
        this.teamService = teamService;
        this.gamesService = gamesService;
    }

    @GetMapping("/teams")
    @PreAuthorize("permitAll()")
    public ModelAndView listTeams() {
        try {
            return MvcResponse.success(
                    HttpStatus.OK,
                    TEAMS_VIEW,
                    teamService.getAllTeams().stream().map(TeamMinimalViewData::from).toList()
            ).toModelAndView();
        } catch (NotFoundException | UnexpectedApiErrorException ex){
            return MvcResponse.errors(
                    ex.getStatus(),
                    TEAMS_VIEW,
                    ex.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }
    }

    @GetMapping("/team/{teamId}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewTeam(@PathVariable Long teamId) throws NotFoundException, UnexpectedApiErrorException {
        var team = TeamMinimalViewData.from(teamService.getTeamById(teamId));

        Optional<List<TeamMemberMinimalViewData>> members;
        try {
            members = Optional.of(teamService.getTeamMembers(teamId).stream().map(TeamMemberMinimalViewData::from).toList());
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            members = Optional.empty();
        }

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_VIEW,
                new TeamDetailViewData(
                        team,
                        members
                )
        ).toModelAndView();
    }

    @GetMapping("/team/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addTeamForm() {
        var activeGames = loadActiveGames();
        var form = new TeamAddPostViewModel(
                null,
                null
        );

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_ADD_VIEW,
                new TeamAddFormViewData(
                        form,
                        activeGames)
        ).toModelAndView();
    }

    @PostMapping("/team/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addTeam(
            @Valid @ModelAttribute("form") TeamAddPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException, UnauthorizedException {
        var activeGames = loadActiveGames();

        if (bindingResult.hasErrors()) {
            var errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(error -> new MvcError(error.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TEAM_ADD_VIEW,
                    new TeamAddFormViewData(
                            form,
                            activeGames
                    ),
                    errors
            ).toModelAndView();
        }

        try {
            var created = teamService.addTeam(form.toTeamAddCereal());
            return MvcResponse.redirect("/" + TEAM_VIEW + "/" + created.id());
        } catch (NotFoundException | BadRequestedExceptions | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TEAM_ADD_VIEW,
                    new TeamAddFormViewData(
                            form,
                            activeGames
                    ),
                    errors
            ).toModelAndView();
        }
    }

    @GetMapping("/team/{teamId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTeamForm(@PathVariable Long teamId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can edit it"));
        }

        var team = teamService.getTeamById(teamId);
        var activeGames = loadActiveGames();
        var form = TeamEditPostViewModel.fromTeamMinimalViewDecereal(team);

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_EDIT_VIEW,
                new TeamEditFormViewData(
                        teamId,
                        form,
                        activeGames)
        ).toModelAndView();
    }

    @PostMapping("/team/{teamId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTeam(
            @PathVariable Long teamId,
            @Valid @ModelAttribute("form") TeamEditPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can edit it"));
        }

        var activeGames = loadActiveGames();

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream().map(error -> new MvcError(error.getDefaultMessage())).toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TEAM_EDIT_VIEW,
                    new TeamEditFormViewData(
                            teamId,
                            form,
                            activeGames
                    ),
                    errors
            ).toModelAndView();
        }

        try {
            teamService.editTeam(teamId, form.toTeamEditCereal());
            return MvcResponse.redirect("/" + TEAM_VIEW + "/" + teamId);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TEAM_EDIT_VIEW,
                    new TeamEditFormViewData(
                            teamId,
                            form,
                            activeGames),
                    errors
            ).toModelAndView();
        }
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

    @GetMapping("/team/{teamId}/member/{memberId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTeamMemberForm(@PathVariable Long teamId, @PathVariable Long memberId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can edit members"));
        }

        var member = findMember(teamId, memberId);
        var form = new TeamMemberEditPostViewModel(TeamMemberRoleViewEnum.fromDecereal(member.role()));

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_MEMBER_EDIT_VIEW,
                new TeamMemberEditFormViewData(
                        teamId,
                        memberId,
                        member.username(),
                        form)
        ).toModelAndView();
    }

    @PostMapping("/team/{teamId}/member/{memberId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @Valid @ModelAttribute("form") TeamMemberEditPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can edit members"));
        }

        String username;
        try {
            username = findMember(teamId, memberId).username();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    e.getStatus(),
                    TEAM_MEMBER_EDIT_VIEW,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TEAM_MEMBER_EDIT_VIEW,
                    new TeamMemberEditFormViewData(
                            teamId,
                            memberId,
                            username,
                            form),
                    errors
            ).toModelAndView();
        }

        try {
            teamService.editTeamMemberRole(
                    teamId,
                    memberId,
                    form.toTeamMemberEditCereal()
            );
            return MvcResponse.redirect("/" + TEAM_VIEW + "/" + teamId);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TEAM_MEMBER_EDIT_VIEW,
                    new TeamMemberEditFormViewData(
                            teamId,
                            memberId,
                            username,
                            form
                    ),
                    errors
            ).toModelAndView();
        }
    }

    @GetMapping("/team/{teamId}/member/{memberId}/remove")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeTeamMemberForm(@PathVariable Long teamId, @PathVariable Long memberId) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can remove members"));
        }

        var member = findMember(teamId, memberId);
        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_MEMBER_REMOVE_VIEW,
                new TeamMemberRemoveViewData(
                        teamId,
                        memberId,
                        member.username())
        ).toModelAndView();
    }

    @PostMapping("/team/{teamId}/member/{memberId}/remove")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView removeTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId) throws ForbiddenException {
        if (!teamService.isTeamCaptain(teamId)) {
            throw new ForbiddenException(List.of("Only this team's captain can remove members"));
        }

        try {
            teamService.removeTeamMember(teamId, memberId);
            return MvcResponse.redirect("/" + TEAM_VIEW + "/" + teamId);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errors(
                    e.getStatus(),
                    TEAM_MEMBER_REMOVE_VIEW,
                    errors
            ).toModelAndView();
        }
    }

    @PostMapping("/team/{teamId}/leave")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView leaveTeam(@PathVariable Long teamId) {
        try {
            teamService.leaveTeam(teamId);
            return MvcResponse.redirect("/" + TEAMS_VIEW);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | ConflictException | BadRequestedExceptions | UnexpectedApiErrorException e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errors(
                    e.getStatus(),
                    TEAM_VIEW,
                    errors
            ).toModelAndView();
        }
    }

    private TeamMemberMinimalViewDecereal findMember(Long teamId, Long memberId) throws NotFoundException, UnexpectedApiErrorException {
        return teamService.getTeamMembers(teamId).stream()
                .filter(member -> member.memberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(List.of("Team member (" + memberId + ") not found")));
    }
}
