package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchFullView;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationTypeView;
import hr.algebra.gamearena.api.dto.notification.ReferenceTypeView;
import hr.algebra.gamearena.api.exceptions.extenders.BadRequestedException;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.match.MatchSave;
import hr.algebra.gamearena.api.model.match.MatchStatus;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.repository.match.IMatchRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.service.notification.INotificationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService implements IMatchService {

    private final IMatchRepo matchRepo;
    private final ITournamentRepo tournamentRepo;
    private final INotificationService notificationService;

    public MatchService(IMatchRepo matchRepo, ITournamentRepo tournamentRepo, INotificationService notificationService) {
        this.matchRepo = matchRepo;
        this.tournamentRepo = tournamentRepo;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<MatchFullView> getById(Long id) {
        return matchRepo.getById(id)
                .map(MatchFullView::fromMatch);
    }

    @Override
    public MatchFullView createMatchAndPushNotification(Long callerId, MatchCreateRequest request) {
        var tournament = tournamentRepo.getTournamentById(request.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Tournament not found with id: " + request.getTournamentId()));

        boolean callerIsOrganizer = tournamentRepo.getAllTournamentMembersFromTournamentId(tournament.id())
                .stream()
                .anyMatch(member -> member.userId().equals(callerId) && member.role() == TournamentMemberRole.ORGANIZER);

        if (!callerIsOrganizer) {
            throw new ForbiddenAccessException("Only the organizer of the tournament can create matches for it");
        }

        if (request.getPlayerOneId().equals(request.getPlayerTwoId())) {
            throw new BadRequestedException("A match must have two different players");
        }

        if (!tournamentRepo.isUserPartOfTournamentAndActive(request.getPlayerOneId(), tournament.id())) {
            throw new BadRequestedException("Player one must be a member of this tournament");
        }

        if (!tournamentRepo.isUserPartOfTournamentAndActive(request.getPlayerTwoId(), tournament.id())) {
            throw new BadRequestedException("Player two must be a member of this tournament");
        }

        var matchSave = new MatchSave();
        matchSave.setTournamentId(tournament.id());
        matchSave.setGameId(tournament.gameId());
        matchSave.setPlayerOneId(request.getPlayerOneId());
        matchSave.setPlayerTwoId(request.getPlayerTwoId());
        matchSave.setStatus(MatchStatus.SCHEDULED);
        matchSave.setScheduledAt(request.getScheduledAt());

        var created = matchRepo.create(matchSave);

        pushMatchCreatedNotification(created.playerOneId(), created.id());
        pushMatchCreatedNotification(created.playerTwoId(), created.id());

        return MatchFullView.fromMatch(created);
    }

    private void pushMatchCreatedNotification(Long recipientUserId, Long matchId) {
        var notificationRequest = new NotificationCreateRequest(
                NotificationTypeView.CREATED_MATCH,
                recipientUserId,
                matchId,
                ReferenceTypeView.MATCH
        );

        notificationService.createAndPush(notificationRequest);
    }
}
