package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentResponseView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface ITournamentService {
    // Tournament
    List<TournamentFullView> getAllTournaments();
    Optional<TournamentFullView> getTournament(Long id);
    List<TournamentFullView> getMyTournaments(Long callerId);
    TournamentFullView createTournament(Long callerId, TournamentCreateRequest request);

    /* Admin only (NO role checker) */
    // BEGIN
    TournamentFullView editTournamentAsAdmin(Long tournamentId, TournamentEditRequest request);
    // END

    /* Organizer only (checker) */
    // BEGIN
    TournamentFullView editTournamentAsOrganizer(Long callerId, Long tournamentId, TournamentEditRequest request);
    // END

    // Tournament member
    List<TournamentMemberView> getTournamentsMembers(Long tournamentId);
    Flux<PaymentResponseView> joinAsRegularTournamentMemberAndPay(Long callerId, Long tournamentId, PaymentRequest paymentRequest);

    /* Admin only (NO role checker) */
    // BEGIN
    TournamentMemberView addTournamentMemberAsAdmin(Long tournamentId, TournamentMemberCreateRequest request);
    TournamentMemberView editTournamentMemberAsAdmin(Long tournamentMemberId, TournamentMemberEditRequest request);
    // END

    /* Organizer only (checker) */
    // BEGIN
    TournamentMemberView addTournamentMemberAsOrganizer(Long callerId, Long tournamentId, TournamentMemberCreateRequest request);
    TournamentMemberView editTournamentMemberAsOrganizer(Long callerId, Long tournamentMemberId, TournamentMemberEditRequest request);
    // END
}
