package hr.algebra.gamearena.api.service.match;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
import hr.algebra.gamearena.api.dto.match.MatchCreateRequest;
import hr.algebra.gamearena.api.dto.match.MatchDetailedFullView;
import hr.algebra.gamearena.api.dto.match.MatchEditRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationCreateRequest;
import hr.algebra.gamearena.api.dto.notification.NotificationTypeView;
import hr.algebra.gamearena.api.dto.notification.ReferenceTypeView;
import hr.algebra.gamearena.api.dto.user.RoleView;
import hr.algebra.gamearena.api.exceptions.extenders.BadRequestedException;
import hr.algebra.gamearena.api.exceptions.extenders.ForbiddenAccessException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.match.Match;
import hr.algebra.gamearena.api.model.match.MatchSave;
import hr.algebra.gamearena.api.model.match.MatchStatus;
import hr.algebra.gamearena.api.model.match.MatchUpdate;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import hr.algebra.gamearena.api.repository.match.IMatchRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.service.notification.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService implements IMatchService {

    private final IMatchRepo matchRepo;
    private final ITournamentRepo tournamentRepo;
    private final IUserRepo userRepo;
    private final IGamesRepo gamesRepo;
    private final INotificationService notificationService;

    public MatchService(
            IMatchRepo matchRepo,
            ITournamentRepo tournamentRepo,
            IUserRepo userRepo,
            IGamesRepo gamesRepo,
            INotificationService notificationService)
    {
        this.matchRepo = matchRepo;
        this.tournamentRepo = tournamentRepo;
        this.userRepo = userRepo;
        this.gamesRepo = gamesRepo;
        this.notificationService = notificationService;
    }

    @Override
    public Optional<MatchDetailedFullView> getById(Long id) {
        return matchRepo.getById(id)
                .map(this::toDetailedFullView);
    }

    @Override
    public List<MatchDetailedFullView> getMatchesByUserId(Long userId) {
        return matchRepo.getAllByPlayerId(userId)
                .stream()
                .map(this::toDetailedFullView)
                .toList();
    }

    @Override
    public List<MatchDetailedFullView> getMatchesTournamentId(Long tournamentId) {
        return matchRepo.getAllByTournamentId(tournamentId)
                .stream()
                .map(this::toDetailedFullView)
                .toList();
    }

    @Override
    public MatchDetailedFullView createMatchAndPushNotification(JwtTokenClaim caller, MatchCreateRequest request) {
        var tournament = tournamentRepo.getTournamentById(request.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Tournament not found with id: " + request.getTournamentId()));

        if (callerIsNotAdminOrOrganizerOfTournament(caller, tournament.id())) {
            throw new ForbiddenAccessException("Only an admin or the organizer of the tournament can create matches for it");
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

        return toDetailedFullView(created);
    }

    @Override
    public MatchDetailedFullView editMatch(JwtTokenClaim caller, Long id, MatchEditRequest request) {
        var match = matchRepo.getById(id)
                .orElseThrow(() -> new NotFoundException("Match with id: " + id + " not found"));

        if (callerIsNotAdminOrOrganizerOfTournament(caller, match.tournamentId())) {
            throw new ForbiddenAccessException("Only an admin or the organizer of the tournament can edit this match");
        }

        if (request.getPlayerOneId().equals(request.getPlayerTwoId())) {
            throw new BadRequestedException("A match must have two different players");
        }

        var matchUpdate = new MatchUpdate();
        matchUpdate.setPlayerOneId(request.getPlayerOneId());
        matchUpdate.setPlayerTwoId(request.getPlayerTwoId());
        matchUpdate.setPlayerOneScore(request.getPlayerOneScore());
        matchUpdate.setPlayerTwoScore(request.getPlayerTwoScore());
        matchUpdate.setWinnerId(request.getWinnerId());
        matchUpdate.setStatus(request.getStatus().toMatchStatus());
        matchUpdate.setScheduledAt(request.getScheduledAt());
        matchUpdate.setPlayedAt(request.getPlayedAt());

        var updated = matchRepo.update(id, matchUpdate)
                .orElseThrow(() -> new NotFoundException("Match with id: " + id + " not found"));

        return toDetailedFullView(updated);
    }

    private boolean callerIsNotAdminOrOrganizerOfTournament(JwtTokenClaim caller, Long tournamentId) {
        if (caller.role() == RoleView.ADMIN) {
            return false;
        }

        return tournamentRepo.getAllTournamentMembersFromTournamentId(tournamentId)
                .stream()
                .noneMatch(member -> member.userId().equals(caller.userId()) && member.role() == TournamentMemberRole.ORGANIZER);
    }

    private MatchDetailedFullView toDetailedFullView(Match match) {
        return MatchDetailedFullView.fromMatchUserOneUserTwoAndGame(
                match,
                userRepo.findById(match.playerOneId()),
                userRepo.findById(match.playerTwoId()),
                userRepo.findById(match.winnerId()),
                gamesRepo.getById(match.gameId())
        );
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
