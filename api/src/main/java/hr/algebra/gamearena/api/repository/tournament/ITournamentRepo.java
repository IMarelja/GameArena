package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentUpdate;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;

import java.util.List;
import java.util.Optional;

public interface ITournamentRepo {
    // Tournament
    List<Tournament> getAllTournament();
    Optional<Tournament> getTournamentById(Long id);
    Tournament createTournament(TournamentSave tournamentSave);
    Optional<Tournament> updateTournament(Long id, TournamentUpdate tournamentUpdate);
    void deleteTournament(Long id);
    boolean tournamentExistsById(Long id);

    // Tournament member
    List<TournamentMember> getAllTournamentMembersFromTournamentId(Long tournamentId);
    Optional<TournamentMember> getTournamentMemberById(Long tournamentMemberId);
    TournamentMember addTournamentMember(TournamentMemberSave tournamentMemberSave);
    Optional<TournamentMember> updateTournamentMember(Long id, TournamentMemberUpdate tournamentMemberUpdate);
    void deleteTournamentMember(Long id);
    boolean tournamentMemberExistsById(Long id);
    boolean isUserPartOfTournament(Long userId, Long tournamentId);
    boolean isUserPartOfTournamentAndActive(Long userId, Long tournamentId);
    boolean isUserPaymentPending(Long userId, Long tournamentId);
    long countTournamentMembersByRole(Long tournamentId, TournamentMemberRole role);
}
