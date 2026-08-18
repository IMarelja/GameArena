package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberHighPrivilegeAddRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.InvalidVariableException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService implements ITournamentService {

    private final ITournamentRepo tournamentRepo;
    private final IGamesRepo gamesRepo;
    private final IUserRepo userRepo;

    private static String tournamentNotFoundByIdOutput(Long id) {
        return "Tournament not found with id: " + id;
    }

    private static String gameNotFoundByIdOutput(Long id) {
        return "Game not found with id: " + id;
    }

    private static String userNotFoundByIdOutput(Long id) {
        return "User not found with id: " + id;
    }

    private static String tournamentMemberNotFoundByIdOutput(Long id) {
        return "Tournament member not found with id: " + id;
    }

    private static String onlyOrganizerCannotRemoveSelfOutput() {
        return "You are the only Organizer in this Tournament, you can not remove yourself. "
                + "Assign someone else to be the Organizer for this Tournament";
    }

    private static String onlyOrganizerCannotDemoteSelfOutput() {
        return "You are the only Organizer in this Tournament, you can not demote yourself. "
                + "Assign someone else to be the Organizer for this Tournament";
    }

    public TournamentService(ITournamentRepo tournamentRepo, IGamesRepo gamesRepo, IUserRepo userRepo) {
        this.tournamentRepo = tournamentRepo;
        this.gamesRepo = gamesRepo;
        this.userRepo = userRepo;
    }

    // Tournament

    @Override
    public List<TournamentFullView> getAllTournaments() {
        return tournamentRepo.getAllTournament()
                .stream()
                .map(tournament -> TournamentFullView.fromTournamentAndGame(tournament, gameOf(tournament)))
                .toList();
    }

    @Override
    public Optional<TournamentFullView> getTournament(Long id) {
        return tournamentRepo.getTournamentById(id)
                .map(tournament -> TournamentFullView.fromTournamentAndGame(tournament, gameOf(tournament)));
    }

    @Override
    public TournamentFullView createTournament(TournamentCreateRequest request) {
        var game = gamesRepo.getById(request.getGameId())
                .orElseThrow(() -> new NotFoundException(gameNotFoundByIdOutput(request.getGameId())));

        var tournamentSave = new TournamentSave();
        tournamentSave.setName(request.getName());
        tournamentSave.setDescription(request.getDescription());
        tournamentSave.setGameId(request.getGameId());
        tournamentSave.setStatus(TournamentStatus.SCHEDULED);
        tournamentSave.setStartsAt(request.getStartsAt());
        tournamentSave.setEndsAt(request.getEndsAt());

        var created = tournamentRepo.createTournament(tournamentSave);
        return TournamentFullView.fromTournamentAndGame(created, game);
    }

    @Override
    public TournamentFullView updateTournament(Long id, TournamentEditRequest request) {
        var game = gamesRepo.getById(request.getGameId())
                .orElseThrow(() -> new NotFoundException(gameNotFoundByIdOutput(request.getGameId())));

        var tournamentSave = new TournamentSave();
        tournamentSave.setName(request.getName());
        tournamentSave.setDescription(request.getDescription());
        tournamentSave.setGameId(request.getGameId());
        tournamentSave.setStatus(request.getStatus());
        tournamentSave.setStartsAt(request.getStartsAt());
        tournamentSave.setEndsAt(request.getEndsAt());

        var updated = tournamentRepo.updateTournament(id, tournamentSave)
                .orElseThrow(() -> new NotFoundException(tournamentNotFoundByIdOutput(id)));

        return TournamentFullView.fromTournamentAndGame(updated, game);
    }

    @Override
    public void deleteTournament(Long id) {
        if (!tournamentRepo.tournamentExistsById(id)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(id));
        }

        tournamentRepo.deleteTournament(id);
    }

    // Tournament member

    @Override
    public List<TournamentMemberView> getTournamentsMembers(Long tournamentId) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        return tournamentRepo.getAllTournamentMembersFromTournamentId(tournamentId)
                .stream()
                .map(TournamentMemberView::fromTournamentMember)
                .toList();
    }

    @Override
    public void joinAsRegularTournamentMemberAndPay(Long calledId) {

    }

    @Override
    public TournamentMemberView addTournamentMemberAsAHighPrivilege(TournamentMemberHighPrivilegeAddRequest request) {
        if (!tournamentRepo.tournamentExistsById(request.getTournamentId())) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(request.getTournamentId()));
        }

        if (!userRepo.existsByIdAndIsActive(request.getUserId())) {
            throw new NotFoundException(userNotFoundByIdOutput(request.getUserId()));
        }

        if (tournamentRepo.isUserPartOfTournament(request.getUserId(), request.getTournamentId())) {
            throw new ConflictException("User is already a member of this tournament");
        }

        var tournamentMemberSave = new TournamentMemberSave();
        tournamentMemberSave.setUserId(request.getUserId());
        tournamentMemberSave.setTournamentId(request.getTournamentId());
        tournamentMemberSave.setRole(request.getRole());

        var created = tournamentRepo.addTournamentMember(tournamentMemberSave);
        return TournamentMemberView.fromTournamentMember(created);
    }

    @Override
    public TournamentMemberView editTournamentMember(Long callerId, Long tournamentMemberId, TournamentMemberEditRequest request) {
        var member = tournamentRepo.getTournamentMemberById(tournamentMemberId)
                .orElseThrow(() -> new NotFoundException(tournamentMemberNotFoundByIdOutput(tournamentMemberId)));

        if (!member.tournamentId().equals(request.getTournamentId())) {
            throw new NotFoundException(tournamentMemberNotFoundByIdOutput(tournamentMemberId));
        }

        boolean isSelf = member.userId().equals(callerId);
        boolean isOrganizer = member.role() == TournamentMemberRole.ORGANIZER;
        boolean demotingFromOrganizer = request.getRole() != TournamentMemberRole.ORGANIZER;

        if (isSelf && isOrganizer && demotingFromOrganizer && onlyOneOrganizer(member.tournamentId())) {
            throw new InvalidVariableException(onlyOrganizerCannotDemoteSelfOutput());
        }

        var tournamentMemberUpdate = new TournamentMemberUpdate();
        tournamentMemberUpdate.setRole(request.getRole());

        var updated = tournamentRepo.updateTournamentMember(tournamentMemberId, tournamentMemberUpdate)
                .orElseThrow(() -> new NotFoundException(tournamentMemberNotFoundByIdOutput(tournamentMemberId)));

        return TournamentMemberView.fromTournamentMember(updated);
    }

    @Override
    public void removeTournamentMember(Long callerId, Long tournamentMemberId) {
        var member = tournamentRepo.getTournamentMemberById(tournamentMemberId)
                .orElseThrow(() -> new NotFoundException(tournamentMemberNotFoundByIdOutput(tournamentMemberId)));

        boolean isSelf = member.userId().equals(callerId);
        boolean isOrganizer = member.role() == TournamentMemberRole.ORGANIZER;

        if (isSelf && isOrganizer && onlyOneOrganizer(member.tournamentId())) {
            throw new InvalidVariableException(onlyOrganizerCannotRemoveSelfOutput());
        }

        tournamentRepo.deleteTournamentMember(tournamentMemberId);
    }

    private boolean onlyOneOrganizer(Long tournamentId) {
        return tournamentRepo.countTournamentMembersByRole(tournamentId, TournamentMemberRole.ORGANIZER) <= 1;
    }

    private Games gameOf(Tournament tournament) {
        return gamesRepo.getById(tournament.gameId())
                .orElseThrow(() -> new NotFoundException(gameNotFoundByIdOutput(tournament.gameId())));
    }
}
