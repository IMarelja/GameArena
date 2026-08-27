package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.jwt.JwtTokenClaim;
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
    List<TournamentFullView> getTournamentsFromUserId(Long userId);
    TournamentFullView createTournament(Long callerId, TournamentCreateRequest request);

    /* Admin or organizer of this tournament */
    TournamentFullView editTournament(JwtTokenClaim caller, Long tournamentId, TournamentEditRequest request);

    // Tournament member
    Optional<TournamentMemberView> getTournamentMemberByUserIdAndTournamentId(Long userId, Long tournamentId);
    Optional<TournamentMemberView> getTournamentMemberByIdAndTournamentId(Long id, Long tournamentId);
    List<TournamentMemberView> getTournamentsMembers(Long tournamentId);
    Flux<PaymentResponseView> joinAsRegularTournamentMemberAndPay(Long callerId, Long tournamentId, PaymentRequest paymentRequest);

    /* Admin only (NO role checker) */
    // BEGIN
    TournamentMemberView addTournamentMemberAsAdmin(Long tournamentId, TournamentMemberCreateRequest request);
    // END

    /* Organizer only (checker) */
    // BEGIN
    TournamentMemberView addTournamentMemberAsOrganizer(Long callerId, Long tournamentId, TournamentMemberCreateRequest request);
    // END

    /* Admin or organizer of this tournament */
    TournamentMemberView editTournamentMember(JwtTokenClaim caller, Long tournamentMemberId, TournamentMemberEditRequest request);
}
