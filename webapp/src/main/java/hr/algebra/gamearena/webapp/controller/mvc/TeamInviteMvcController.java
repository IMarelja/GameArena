package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.JwtClaimDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.InviteStatusDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationInviteeRespondCereal;
import hr.algebra.gamearena.webapp.models.cereal.team.invitation.TeamInvitationInviterUpdateCereal;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteeResponsePostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviteeResponseViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviterUpdatePostView;
import hr.algebra.gamearena.webapp.models.mvc.data.team.invite.TeamInviterUpdateViewData;
import hr.algebra.gamearena.webapp.service.authentication.user.IAuthenticatedUserService;
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

@Controller
public class TeamInviteMvcController {

    private static final String TEAM_INVITE_VIEW = "/team-invite/";
    private static final String TEAM_INVITE_V = "/view";
    private static final String TEAM_INVITE_VIEW_VIEW = "team-invite-view";
    private static final String TEAM_INVITE_RESPOND_VIEW = "team-invite-respond";
    private static final String TEAM_INVITE_UPDATE_VIEW = "team-invite-update";

    private final ITeamService teamService;
    private final IAuthenticatedUserService authenticatedUserService;

    public TeamInviteMvcController(ITeamService teamService, IAuthenticatedUserService authenticatedUserService) {
        this.teamService = teamService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/team-invite/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView redirectInvite(@PathVariable Long id) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {
        var invitation = teamService.getInvitationById(id);
        var invitationView = TeamInviteViewData.fromTeamInvitationDecereal(invitation);

        Long currentUserId = currentUserId();
        boolean isInvitee = currentUserId != null && currentUserId.equals(invitationView.invitee().id());
        boolean isInviter = currentUserId != null && currentUserId.equals(invitationView.inviter().id());

        if (!isInvitee && !isInviter) {
            throw new ForbiddenException(List.of("You are not part of this invitation"));
        }

        if (invitation.status() != InviteStatusDecereal.PENDING) {
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id + TEAM_INVITE_V);
        }

        if (isInvitee) {
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id + "/respond");
        }

        return MvcResponse.redirect(TEAM_INVITE_VIEW + id + "/update");
    }

    @GetMapping("/team-invite/{id}/view")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView viewViewPage(
            @PathVariable Long id
    ) throws ForbiddenException, UnauthorizedException, UnexpectedApiErrorException, NotFoundException {
        var invitationView = TeamInviteViewData.fromTeamInvitationDecereal(teamService.getInvitationById(id));

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_INVITE_VIEW_VIEW,
                invitationView
        ).toModelAndView();
    }

    @GetMapping("/team-invite/{id}/respond")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView respondForm(@PathVariable Long id) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {
        var invitation = teamService.getInvitationById(id);
        var invite = TeamInviteViewData.fromTeamInvitationDecereal(invitation);

        Long currentUserId = currentUserId();
        if (currentUserId == null || !currentUserId.equals(invite.invitee().id())) {
            throw new ForbiddenException(List.of("Only the invitee can respond to this invitation"));
        }

        if (invitation.status() != InviteStatusDecereal.PENDING) {
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id + TEAM_INVITE_V);
        }

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_INVITE_RESPOND_VIEW,
                new TeamInviteeResponseViewData(
                        invite,
                        new TeamInviteeResponsePostViewModel(null))
        ).toModelAndView();
    }

    @GetMapping("/team-invite/{id}/update")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView updateForm(@PathVariable Long id) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {
        var invitation = teamService.getInvitationById(id);
        var invite = TeamInviteViewData.fromTeamInvitationDecereal(invitation);

        Long currentUserId = currentUserId();
        if (currentUserId == null || !currentUserId.equals(invite.inviter().id())) {
            throw new ForbiddenException(List.of("Only the inviter can update this invitation"));
        }

        if (invitation.status() != InviteStatusDecereal.PENDING) {
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id + TEAM_INVITE_V);
        }

        return MvcResponse.success(
                HttpStatus.OK,
                TEAM_INVITE_UPDATE_VIEW,
                new TeamInviterUpdateViewData(invite, new TeamInviterUpdatePostView(null))
        ).toModelAndView();
    }

    @PostMapping("/team-invite/{id}/respond")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView respond(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") TeamInviteeResponsePostViewModel form,
            BindingResult bindingResult
    ) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {
        var invite = TeamInviteViewData.fromTeamInvitationDecereal(teamService.getInvitationById(id));

        Long currentUserId = currentUserId();
        if (currentUserId == null || !currentUserId.equals(invite.invitee().id())) {
            throw new ForbiddenException(List.of("Only the invitee can respond to this invitation"));
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TEAM_INVITE_RESPOND_VIEW,
                    new TeamInviteeResponseViewData(
                            invite,
                            form),
                    errors
            ).toModelAndView();
        }

        try {

            var cereal = new TeamInvitationInviteeRespondCereal(form.status().toStatusInviteeCereal());
            teamService.respondInvitation(id, cereal);
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id);
        } catch (ConflictException | BadRequestedExceptions e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TEAM_INVITE_RESPOND_VIEW,
                    new TeamInviteeResponseViewData(
                            invite,
                            form),
                    errors
            ).toModelAndView();
        }
    }

    @PostMapping("/team-invite/{id}/update")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") TeamInviterUpdatePostView form,
            BindingResult bindingResult
    ) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {
        var invite = TeamInviteViewData.fromTeamInvitationDecereal(teamService.getInvitationById(id));

        Long currentUserId = currentUserId();
        if (currentUserId == null || !currentUserId.equals(invite.inviter().id())) {
            throw new ForbiddenException(List.of("Only the inviter can update this invitation"));
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult
                    .getAllErrors()
                    .stream()
                    .map(err -> new MvcError(err.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    TEAM_INVITE_UPDATE_VIEW,
                    new TeamInviterUpdateViewData(
                            invite,
                            form),
                    errors
            ).toModelAndView();
        }

        try {
            var cereal = new TeamInvitationInviterUpdateCereal(form.status().toCereal());
            teamService.updateInvitation(id, cereal);
            return MvcResponse.redirect(TEAM_INVITE_VIEW + id);
        } catch (ConflictException | BadRequestedExceptions e) {
            var errors = e.getMessages()
                    .stream()
                    .map(MvcError::new)
                    .toList();
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    TEAM_INVITE_UPDATE_VIEW,
                    new TeamInviterUpdateViewData(
                            invite,
                            form),
                    errors
            ).toModelAndView();
        }
    }

    private Long currentUserId() {
        return authenticatedUserService
                .current()
                .map(JwtClaimDecereal::userId)
                .orElse(null);
    }
}
