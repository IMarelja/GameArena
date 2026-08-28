package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentStagesView;
import hr.algebra.gamearena.api.dto.payment.responce.PaymentResponseView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberRoleEdit;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.exceptions.extenders.*;
import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentStatus;
import hr.algebra.gamearena.api.model.tournament.TournamentUpdate;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.repository.games.IGamesRepo;
import hr.algebra.gamearena.api.repository.invoice.IInvoiceRepo;
import hr.algebra.gamearena.api.repository.payment.IPaymentRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
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

    private static final String USER_NOT_FOUND_OR_NOT_ACTIVE = "User not found or not active";
    private static final String NOT_AN_ORGANIZER_OF_THIS_TOURNAMENT = "You are not an organizer of this tournament";

    private final ITournamentRepo tournamentRepo;
    private final IGamesRepo gamesRepo;
    private final IUserRepo userRepo;
    private final IInvoiceRepo invoiceRepo;
    private final IPaymentRepo paymentRepo;

    private static String tournamentNotFoundByIdOutput(Long id) {
        return "Tournament not found with id: " + id;
    }

    private static String gameNotFoundByIdOutput(Long id) {
        return "Game not found with id: " + id;
    }

    private static String tournamentMemberNotFoundByIdOutput(Long id) {
        return "Tournament member not found with id: " + id;
    }

    public TournamentService(
            ITournamentRepo tournamentRepo,
            IGamesRepo gamesRepo,
            IUserRepo userRepo,
            IInvoiceRepo invoiceRepo,
            IPaymentRepo paymentRepo) {
        this.tournamentRepo = tournamentRepo;
        this.gamesRepo = gamesRepo;
        this.userRepo = userRepo;
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
    }

    /** Tournament */

    @Override
    public List<TournamentFullView> getAllTournaments() {
        return tournamentRepo.getAllTournament()
                .stream()
                .map(tournament -> TournamentFullView.fromTournamentAndGame(tournament, gamesRepo.getById(tournament.gameId())))
                .toList();
    }

    @Override
    public Optional<TournamentFullView> getTournament(Long id) {
        return tournamentRepo.getTournamentById(id)
                .map(tournament -> TournamentFullView.fromTournamentAndGame(tournament, gamesRepo.getById(tournament.gameId())));
    }

    @Override
    public List<TournamentFullView> getTournamentsFromUserId(Long userId) {
        return tournamentRepo.getAllTournamentMembersForUser(userId)
                .stream()
                .map(TournamentMember::tournamentId)
                .distinct()
                .map(tournamentRepo::getTournamentById)
                .flatMap(Optional::stream)
                .map(tournament -> TournamentFullView.fromTournamentAndGame(tournament, gamesRepo.getById(tournament.gameId())))
                .toList();
    } // getTournamentsForUser

    @Override
    public TournamentFullView createTournament(Long callerId, TournamentCreateRequest request) {
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

        var organizerSave = new TournamentMemberSave();
        organizerSave.setUserId(callerId);
        organizerSave.setRole(TournamentMemberRole.ORGANIZER);
        organizerSave.setConfirmed(true);

        var created = tournamentRepo.createTournamentAndTournamentMemberTransactional(tournamentSave, organizerSave);

        return TournamentFullView.fromTournamentAndGame(created, Optional.of(game));
    } // createTournament

    @Override
    public TournamentFullView editTournamentAsOrganizerOrAdmin(Long callerId, Long tournamentId, TournamentEditRequest request) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        if (callerIsNotAdminOrOrganizerOfTournament(callerId, tournamentId)) {
            throw new ForbiddenAccessException(NOT_AN_ORGANIZER_OF_THIS_TOURNAMENT);
        }

        var game = gamesRepo.getById(request.getGameId())
                .orElseThrow(() -> new NotFoundException(gameNotFoundByIdOutput(request.getGameId())));

        var tournamentUpdate = new TournamentUpdate();

        tournamentUpdate.setName(request.getName());
        tournamentUpdate.setDescription(request.getDescription());
        tournamentUpdate.setGameId(request.getGameId());
        tournamentUpdate.setStatus(request.getStatus().toTournamentStatus());

        tournamentUpdate.setPriceSolo(request.getPrice().getSoloPrice());
        tournamentUpdate.setPriceGroup(request.getPrice().getGroupPrice());
        tournamentUpdate.setCurrency(request.getPrice().getCurrency());

        tournamentUpdate.setStartsAt(request.getStartsAt());
        tournamentUpdate.setEndsAt(request.getEndsAt());

        var updated = tournamentRepo.updateTournament(tournamentId, tournamentUpdate)
                .orElseThrow(() -> new NotFoundException(tournamentNotFoundByIdOutput(tournamentId)));

        return TournamentFullView.fromTournamentAndGame(updated, Optional.of(game));
    } // editTournamentAsOrganizerOrAdmin

    /** Tournament member */

    @Override
    public Optional<TournamentMemberView> getTournamentMemberByUserIdAndTournamentId(Long userId, Long tournamentId) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        return tournamentRepo.getTournamentMember(tournamentId, userId)
                .map(member -> TournamentMemberView.fromTournamentMember(
                        member,
                        userRepo.findById(member.userId())));
    }

    @Override
    public Optional<TournamentMemberView> getTournamentMemberByIdAndTournamentId(Long id, Long tournamentId) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        return tournamentRepo.getTournamentMemberById(id)
                .filter(member -> member.tournamentId().equals(tournamentId))
                .map(member -> TournamentMemberView.fromTournamentMember(
                        member,
                        userRepo.findById(member.userId())));
    }

    @Override
    public List<TournamentMemberView> getTournamentsMembers(Long tournamentId) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        return tournamentRepo.getAllTournamentMembersFromTournamentId(tournamentId)
                .stream()
                .map(member -> TournamentMemberView.fromTournamentMember(
                        member,
                        userRepo.findById(member.userId()))
                )
                .toList();
    }

    @Override
    public TournamentMemberView addTournamentMemberAsOrganizerOrAdmin(Long callerId, Long tournamentId, TournamentMemberCreateRequest request) {
        if (!tournamentRepo.tournamentExistsById(tournamentId)) {
            throw new NotFoundException(tournamentNotFoundByIdOutput(tournamentId));
        }

        if (callerIsNotAdminOrOrganizerOfTournament(callerId, tournamentId)) {
            throw new ForbiddenAccessException(NOT_AN_ORGANIZER_OF_THIS_TOURNAMENT);
        }

        return applyAddTournamentMember(tournamentId, request);
    } // addTournamentMemberAsOrganizerOrAdmin

    private TournamentMemberView applyAddTournamentMember(Long tournamentId, TournamentMemberCreateRequest request) {
        var user = userRepo.findById(request.getUserId())
                .filter(u -> Boolean.TRUE.equals(u.isActive()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_OR_NOT_ACTIVE));

        if (tournamentRepo.isUserPartOfTournament(request.getUserId(), tournamentId)) {
            throw new ConflictException("User is already a member of this tournament");
        }

        var tournamentMemberSave = new TournamentMemberSave();
        tournamentMemberSave.setUserId(request.getUserId());
        tournamentMemberSave.setTournamentId(tournamentId);
        tournamentMemberSave.setRole(request.getRole().toTournamentMemberRole());
        tournamentMemberSave.setConfirmed(true);

        var created = tournamentRepo.addTournamentMember(tournamentMemberSave);
        return TournamentMemberView.fromTournamentMember(created, Optional.of(user));
    } // applyAddTournamentMember

    @Override
    public TournamentMemberView editTournamentMemberAsOrganizerOrAdmin(Long callerId, Long tournamentMemberId, TournamentMemberEditRequest request) {
        var member = tournamentRepo.getTournamentMemberById(tournamentMemberId)
                .orElseThrow(() -> new NotFoundException(tournamentMemberNotFoundByIdOutput(tournamentMemberId)));

        if (callerIsNotAdminOrOrganizerOfTournament(callerId, member.tournamentId())) {
            throw new ForbiddenAccessException(NOT_AN_ORGANIZER_OF_THIS_TOURNAMENT);
        }

        boolean demotingTheOnlyOrganizer =
                member.role() == TournamentMemberRole.ORGANIZER
                        && request.getRole() != TournamentMemberRoleEdit.ORGANIZER
                        && tournamentRepo.countTournamentMembersByRole(member.tournamentId(), TournamentMemberRole.ORGANIZER) <= 1;

        if (demotingTheOnlyOrganizer) {
            throw new InvalidVariableException("This member is the only Organizer in this Tournament, their role can not be changed. "
                    + "Assign someone else to be the Organizer for this Tournament first");
        }

        var tournamentMemberUpdate = new TournamentMemberUpdate();
        tournamentMemberUpdate.setRole(request.getRole().toTournamentMemberRole());

        var updated = tournamentRepo.updateTournamentMember(member.id(), tournamentMemberUpdate)
                .orElseThrow(() -> new NotFoundException(tournamentMemberNotFoundByIdOutput(member.id())));

        return TournamentMemberView.fromTournamentMember(
                updated,
                userRepo.findById(updated.userId())
        );
    } // editTournamentMemberAsOrganizerOrAdmin


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
    } // createPaymentRecords

    private boolean callerIsNotAdminOrOrganizerOfTournament(Long callerId, Long tournamentId) {
        boolean isAdmin = userRepo.findById(callerId)
                .map(user -> user.role() == Role.ADMIN)
                .orElse(false);

        if (isAdmin) {
            return false;
        }

        return !tournamentRepo.isUserPartOfTournamentActiveAndOrganizer(callerId, tournamentId);
    }

}
