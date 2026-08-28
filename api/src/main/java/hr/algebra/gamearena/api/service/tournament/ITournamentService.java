package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.tournament.TournamentQueryDto;
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
    List<TournamentFullView> queryTournaments(TournamentQueryDto query);

    /* Admin or organizer of this tournament */
    TournamentFullView editTournamentAsOrganizerOrAdmin(Long calledId, Long tournamentId, TournamentEditRequest request);

    // Tournament member
    Optional<TournamentMemberView> getTournamentMemberByUserIdAndTournamentId(Long userId, Long tournamentId);
    Optional<TournamentMemberView> getTournamentMemberByIdAndTournamentId(Long id, Long tournamentId);
    List<TournamentMemberView> getTournamentsMembers(Long tournamentId);
    TournamentMemberView addTournamentMemberAsOrganizerOrAdmin(Long callerId, Long tournamentId, TournamentMemberCreateRequest request);
    TournamentMemberView editTournamentMemberAsOrganizerOrAdmin(Long callerId, Long tournamentMemberId, TournamentMemberEditRequest request);
    Flux<PaymentResponseView> joinAsRegularTournamentMemberAndPay(Long callerId, Long tournamentId, PaymentRequest paymentRequest);
}
