package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchDetailViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchEditFormViewData;
import hr.algebra.gamearena.webapp.models.mvc.data.match.MatchEditPostViewModel;
import hr.algebra.gamearena.webapp.models.mvc.data.tournament.member.TournamentMemberViewData;
import hr.algebra.gamearena.webapp.service.match.IMatchService;
import hr.algebra.gamearena.webapp.service.tournament.ITournamentService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class MatchMvcController {

    private static final String MATCH_VIEW = "match";
    private static final String MATCH_EDIT_VIEW = "match-edit";

    private final IMatchService matchService;
    private final ITournamentService tournamentService;

    public MatchMvcController(IMatchService matchService, ITournamentService tournamentService) {
        this.matchService = matchService;
        this.tournamentService = tournamentService;
    }

    @GetMapping("/match/{matchId}")
    @PreAuthorize("permitAll()")
    public ModelAndView viewMatch(@PathVariable Long matchId) throws NotFoundException, UnexpectedApiErrorException {
        var match = matchService.getMatchById(matchId);

        return MvcResponse.success(
                HttpStatus.OK,
                MATCH_VIEW,
                MatchDetailViewData.from(match)
        ).toModelAndView();
    }

    @GetMapping("/match/{matchId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editMatchForm(
            @PathVariable Long matchId
    ) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        var match = matchService.getMatchById(matchId);

        if (!tournamentService.isTournamentOrganizerOrAdmin(match.tournamentId())) {
            throw new ForbiddenException(Collections.singletonList("Only an admin or this tournament's organizer can edit matches on this tournament"));
        }

        var form = MatchEditPostViewModel.fromMatchDetailFullViewDecereal(match);


        var members = loadTournamentMembersOrOptional(match.tournamentId());

        return MvcResponse.success(
                HttpStatus.OK,
                MATCH_EDIT_VIEW,
                new MatchEditFormViewData(
                        match.tournamentId(),
                        matchId,
                        form,
                        members
                )
        ).toModelAndView();
    }

    @PostMapping("/match/{matchId}/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editMatch(
            @PathVariable Long matchId,
            @Valid @ModelAttribute("form") MatchEditPostViewModel form,
            BindingResult bindingResult
    ) throws ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        var match = matchService.getMatchById(matchId);
        Long tournamentId = match.tournamentId();

        if (!tournamentService.isTournamentOrganizerOrAdmin(tournamentId)) {
            throw new ForbiddenException(Collections.singletonList("Only an admin or this tournament's organizer can edit matches on this tournament"));
        }

        var members = loadTournamentMembersOrOptional(tournamentId);

        if (form.playerOneId() != null && form.playerOneId().equals(form.playerTwoId())) {
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    MATCH_EDIT_VIEW,
                    new MatchEditFormViewData(
                            tournamentId,
                            matchId,
                            form,
                            members
                    ),
                    MvcError.toListMvcErrorFromMvcError(new MvcError("Player One and player Two can't be the same player"))
            ).toModelAndView();
        }

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> new MvcError(error.getDefaultMessage()))
                    .toList();
            return MvcResponse.errorsWithData(
                    HttpStatus.BAD_REQUEST,
                    MATCH_EDIT_VIEW,
                    new MatchEditFormViewData(
                            tournamentId,
                            matchId,
                            form,
                            members
                    ),
                    errors
            ).toModelAndView();
        }

        try {
            matchService.editMatch(matchId, form.toMatchEditCereal());
            return MvcResponse.redirect("/" + MATCH_VIEW + "/" + matchId);
        } catch (UnauthorizedException | ForbiddenException | NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errorsWithData(
                    e.getStatus(),
                    MATCH_EDIT_VIEW,
                    new MatchEditFormViewData(
                            tournamentId,
                            matchId,
                            form,
                            members),
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }
    }

    private Optional<List<TournamentMemberViewData>> loadTournamentMembersOrOptional(Long tournamentId) {
        try {
            return Optional.of(tournamentService.getTournamentMembers(tournamentId)
                    .stream()
                    .map(TournamentMemberViewData::from)
                    .toList()
            );
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return Optional.empty();
        }
    }
}
