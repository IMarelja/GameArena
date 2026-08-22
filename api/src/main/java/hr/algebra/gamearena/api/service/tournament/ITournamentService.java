package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentResponseView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberHighPrivilegeAddRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface ITournamentService {
    // Tournament
    List<TournamentFullView> getAllTournaments();
    Optional<TournamentFullView> getTournament(Long id);
    TournamentFullView createTournament(TournamentCreateRequest request);
    TournamentFullView updateTournament(Long id, TournamentEditRequest request);
    void deleteTournament(Long id);

    // Tournament member
    List<TournamentMemberView> getTournamentsMembers(Long tournamentId);
    Flux<PaymentResponseView<?>> joinAsRegularTournamentMemberAndPay(Long callerId, Long tournamentId, PaymentRequest paymentRequest);
    TournamentMemberView addTournamentMemberAsAHighPrivilege(TournamentMemberHighPrivilegeAddRequest request);
    TournamentMemberView editTournamentMember(Long callerId, Long tournamentMemberId, TournamentMemberEditRequest request);
    void removeTournamentMember(Long callerId, Long tournamentMemberId);
}
