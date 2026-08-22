package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentStagesView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentResponseView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberHighPrivilegeAddRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.exceptions.extenders.*;
import hr.algebra.gamearena.api.model.games.Games;
import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;
import hr.algebra.gamearena.api.model.tournament.TournamentUpdate;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import hr.algebra.gamearena.api.repository.invoice.IInvoiceRepo;
import hr.algebra.gamearena.api.repository.payment.IPaymentRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.service.payment.ProcessingPaymentServiceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TournamentService implements ITournamentService {

    private final ITournamentRepo tournamentRepo;
    private final IGamesRepo gamesRepo;
    private final IUserRepo userRepo;
    private final IInvoiceRepo invoiceRepo;
    private final IPaymentRepo paymentRepo;

    private static final String USER_NOT_FOUND_OR_NOT_ACTIVE = "User not found or not active";

    private static String tournamentNotFoundByIdOutput(Long id) {
        return "Tournament not found with id: " + id;
    }

    private static String gameNotFoundByIdOutput(Long id) {
        return "Game not found with id: " + id;
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

    public TournamentService(
            ITournamentRepo tournamentRepo,
            IGamesRepo gamesRepo,
            IUserRepo userRepo,
            IInvoiceRepo invoiceRepo,
            IPaymentRepo paymentRepo,
            ProcessingPaymentServiceStrategy processingPaymentServiceStrategy) {
        this.tournamentRepo = tournamentRepo;
        this.gamesRepo = gamesRepo;
        this.userRepo = userRepo;
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
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

        tournamentSave.setPriceSolo(request.getPrice().getSoloPrice());
        tournamentSave.setPriceGroup(request.getPrice().getGroupPrice());
        tournamentSave.setCurrency(request.getPrice().getCurrency());

        var created = tournamentRepo.createTournament(tournamentSave);
        return TournamentFullView.fromTournamentAndGame(created, game);
    }

    @Override
    public TournamentFullView updateTournament(Long id, TournamentEditRequest request) {
        var game = gamesRepo.getById(request.getGameId())
                .orElseThrow(() -> new NotFoundException(gameNotFoundByIdOutput(request.getGameId())));

        var tournamentUpdate = new TournamentUpdate();

        tournamentUpdate.setName(request.getName());
        tournamentUpdate.setDescription(request.getDescription());
        tournamentUpdate.setGameId(request.getGameId());
        tournamentUpdate.setStatus(request.getStatus());

        tournamentUpdate.setPriceSolo(request.getPrice().getSoloPrice());
        tournamentUpdate.setPriceGroup(request.getPrice().getGroupPrice());
        tournamentUpdate.setCurrency(request.getPrice().getCurrency());

        tournamentUpdate.setStartsAt(request.getStartsAt());
        tournamentUpdate.setEndsAt(request.getEndsAt());

        var updated = tournamentRepo.updateTournament(id, tournamentUpdate)
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
    public Flux<PaymentResponseView> joinAsRegularTournamentMemberAndPay(Long callerId, Long tournamentId, PaymentRequest paymentRequest) {
        return Mono.fromRunnable(() -> validateJoinEligibility(callerId, tournamentId))
                    .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.fromCallable(() -> createPaymentRecords(callerId, tournamentId, paymentRequest))
                        .subscribeOn(Schedulers.boundedElastic()))
                .flatMapMany(records -> {
                    var paymentId = records.payment().id();

                    return Flux.<PaymentResponseView>create(sink -> {
                        try {
                            sink.next(PaymentResponseView.justStatus(PaymentStagesView.PROCESSING_PAYMENT));

                            // Processing stuff

                            sink.complete();
                        } catch (Exception ex) {
                            sink.next(PaymentResponseView.failure(paymentId, "Payment could not be completed: " + ex.getMessage()));
                            sink.complete();
                        }
                    }).subscribeOn(Schedulers.boundedElastic());
                });
    }

    private record TournamentJoinRecords(
            Payment payment,
            TournamentMember member
    ) {
    }

    private void validateJoinEligibility(Long callerId, Long tournamentId) {
        userRepo.findById(callerId)
                .filter(user -> Boolean.TRUE.equals(user.isActive()))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_OR_NOT_ACTIVE));

        var tournament = tournamentRepo.getTournamentById(tournamentId)
                .orElseThrow(() -> new NotFoundException(tournamentNotFoundByIdOutput(tournamentId)));

        if (tournament.status() != TournamentStatus.SCHEDULED) {
            throw new BadRequestedException("Tournament is not open for new members");
        }

        if (tournamentRepo.isUserPaymentPending(callerId, tournamentId)) {
            throw new ConflictException("You already requested another payment to join this tournament, finish it or wait it to timeout");
        }

        if (tournamentRepo.isUserPartOfTournament(callerId, tournamentId)) {
            throw new ConflictException("You are already a member of this tournament");
        }
    }

    private TournamentJoinRecords createPaymentRecords(Long callerId, Long tournamentId, PaymentRequest paymentRequest) {

        var tournament = tournamentRepo.getTournamentById(tournamentId)
                .orElseThrow(() -> new NotFoundException(tournamentNotFoundByIdOutput(tournamentId)));

        var billingDetails = paymentRequest.getPaymentDetails();

        var billingInfoSave = new BillingInfoSave();
        billingInfoSave.setFullName(billingDetails.getFullName());
        billingInfoSave.setEmail(billingDetails.getEmail());
        billingInfoSave.setAddressLine(billingDetails.getAddress());
        billingInfoSave.setCity(billingDetails.getCity());
        billingInfoSave.setState(billingDetails.getState());
        billingInfoSave.setZipCode(billingDetails.getZipCode());
        billingInfoSave.setCountry(billingDetails.getCountryCode());

        var paymentSave = new PaymentSave();
        paymentSave.setAmount(tournament.soloPrice());
        paymentSave.setCurrency(tournament.currency());

        var invoice = invoiceRepo.saveWithBillingInfoAndPaymentTransactional(callerId, billingInfoSave, paymentSave);

        var payment = paymentRepo.findPaymentById(invoice.paymentId())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        var tournamentMemberSave = new TournamentMemberSave();
        tournamentMemberSave.setUserId(callerId);
        tournamentMemberSave.setTournamentId(tournamentId);
        tournamentMemberSave.setRole(TournamentMemberRole.PARTICIPANTS);
        tournamentMemberSave.setPaymentId(payment.id());
        tournamentMemberSave.setConfirmed(false);
        var member = tournamentRepo.addTournamentMember(tournamentMemberSave);

        return new TournamentJoinRecords(payment, member);
    }

    @Override
    public TournamentMemberView addTournamentMemberAsAHighPrivilege(TournamentMemberHighPrivilegeAddRequest request) {
        if (!tournamentRepo.tournamentExistsById(request.getTournamentId())) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(request.getTournamentId()));
        }

        if (!userRepo.existsByIdAndIsActive(request.getUserId())) {
            throw new NotFoundException(USER_NOT_FOUND_OR_NOT_ACTIVE);
        }

        if (tournamentRepo.isUserPartOfTournament(request.getUserId(), request.getTournamentId())) {
            throw new ConflictException("User is already a member of this tournament");
        }

        var tournamentMemberSave = new TournamentMemberSave();
        tournamentMemberSave.setUserId(request.getUserId());
        tournamentMemberSave.setTournamentId(request.getTournamentId());
        tournamentMemberSave.setRole(request.getRole());
        tournamentMemberSave.setConfirmed(true);

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
