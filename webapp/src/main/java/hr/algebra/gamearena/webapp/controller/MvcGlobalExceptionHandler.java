package hr.algebra.gamearena.webapp.controller;

import hr.algebra.gamearena.webapp.exceptions.GameArenaApiServiceException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.service.authentication.user.IAuthenticatedUserService;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.team.ITeamService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice(basePackages = "hr.algebra.gamearena.webapp.controller.mvc")
public class MvcGlobalExceptionHandler {

    private static final String ERROR_VIEW = "error";

    private final IJwtService jwtService;
    private final IAuthenticatedUserService authenticatedUserService;
    private final ITeamService teamService;
    private final ITournamentService tournamentService;
    private final IMatchService matchService;

    public MvcGlobalExceptionHandler(
            IJwtService jwtService,
            IAuthenticatedUserService authenticatedUserService,
            ITeamService teamService,
            ITournamentService tournamentService,
            IMatchService matchService)
    {
        this.jwtService = jwtService;
        this.authenticatedUserService = authenticatedUserService;
        this.teamService = teamService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    // Shared across every page

    @ModelAttribute("authenticated")
    public boolean authenticated() {
        return authenticatedUserService.isAuthenticated();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return authenticatedUserService.isAdmin();
    }

    @ModelAttribute("isTeamCaptain")
    public boolean isTeamCaptain(@PathVariable(name = "teamId", required = false) Long teamId) {
        return teamId != null && teamService.isTeamCaptain(teamId);
    }

    @ModelAttribute("isTeamMember")
    public boolean isTeamMember(@PathVariable(name = "teamId", required = false) Long teamId) {
        return teamId != null && teamService.isTeamMember(teamId);
    }

    @ModelAttribute("isTournamentOrganizerOrAdmin")
    public boolean isTournamentOrganizerOrAdmin(
            @PathVariable(name = "tournamentId", required = false) Long tournamentId,
            @PathVariable(name = "matchId", required = false) Long matchId
    ) {
        if (tournamentId != null) {
            return tournamentService.isTournamentOrganizerOrAdmin(tournamentId);
        }

        if (matchId != null) {
            try {
                return tournamentService.isTournamentOrganizerOrAdmin(matchService.getMatchById(matchId).tournamentId());
            } catch (NotFoundException | UnexpectedApiErrorException e) {
                return false;
            }
        }

        return false;
    }

    @ModelAttribute("isPartOfAnyTeam")
    public boolean isPartOfAnyTeam(){
        return teamService.isUserPartOfAnyTeam();
    }


    // GameArena site exceptions

    @ExceptionHandler(GameArenaApiServiceException.class)
    public ModelAndView handleGameArenaSiteException(GameArenaApiServiceException ex) {
        List<MvcError> errors = ex.getMessages().stream().map(MvcError::new).toList();
        return MvcResponse.errors(ex.getStatus(), ex.getView(), errors).toModelAndView();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(UnauthorizedException ex) {
        jwtService.clearToken();
        List<MvcError> errors = ex.getMessages().stream().map(MvcError::new).toList();
        return MvcResponse.errors(ex.getStatus(), ex.getView(), errors).toModelAndView();
    }

    // Third party

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) throws AccessDeniedException {
        throw ex;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException ex) throws AuthenticationException {
        throw ex;
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ModelAndView handleRestClientResponseException(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return MvcResponse.error(
                status,
                ERROR_VIEW,
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ModelAndView handleResourceAccessException(ResourceAccessException ex) {
        return MvcResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE,
                ERROR_VIEW,
                new MvcError("Could not reach the GameArena API: " + ex.getMessage())
        ).toModelAndView();
    }

    // Generic

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return MvcResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ERROR_VIEW,
                new MvcError(ex.getMessage())
        ).toModelAndView();
    }
}
