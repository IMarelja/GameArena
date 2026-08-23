package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.TournamentUpdate;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import hr.algebra.gamearena.api.model.payment.PaymentStatus;
import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentMemberPostgres;
import hr.algebra.gamearena.api.orm.postgres.tournament.TournamentPostgres;
import hr.algebra.gamearena.api.repository.payment.IPaymentRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TournamentPostgresRepo implements ITournamentRepo {

    private final ITournamentPostgreSQLRepo tournamentPostgreSQLRepo;
    private final ITournamentMemberPostgreSQLRepo tournamentMemberPostgreSQLRepo;
    private final IPaymentRepo paymentRepo;

    public TournamentPostgresRepo(
            ITournamentPostgreSQLRepo tournamentPostgreSQLRepo,
            ITournamentMemberPostgreSQLRepo tournamentMemberPostgreSQLRepo,
            IPaymentRepo paymentRepo
    ) {
        this.tournamentPostgreSQLRepo = tournamentPostgreSQLRepo;
        this.tournamentMemberPostgreSQLRepo = tournamentMemberPostgreSQLRepo;
        this.paymentRepo = paymentRepo;
    }

    // Tournament
    @Override
    public List<Tournament> getAllTournament() {
        return tournamentPostgreSQLRepo.findAll()
                .stream()
                .map(Tournament::fromTournamentPostgres)
                .toList();
    }

    @Override
    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentPostgreSQLRepo.findById(id)
                .map(Tournament::fromTournamentPostgres);
    }

    @Override
    public Tournament createTournament(TournamentSave tournamentSave) {
        var tournamentPostgres = new TournamentPostgres().fromTournamentSave(tournamentSave);
        var savedTournament = tournamentPostgreSQLRepo.save(tournamentPostgres);
        return Tournament.fromTournamentPostgres(savedTournament);
    }

    @Override
    public Optional<Tournament> updateTournament(Long id, TournamentUpdate tournamentUpdate) {
        return tournamentPostgreSQLRepo.findById(id)
                .map(existing -> existing.fromTournamentUpdate(tournamentUpdate))
                .map(tournamentPostgreSQLRepo::save)
                .map(Tournament::fromTournamentPostgres);
    }

    @Override
    public void deleteTournament(Long id) {
        tournamentPostgreSQLRepo.deleteById(id);
    }

    @Override
    public boolean tournamentExistsById(Long id) {
        return tournamentPostgreSQLRepo.existsById(id);
    }

    @Override
    @Transactional
    public Tournament createTournamentAndTournamentMemberTransactional(TournamentSave tournamentSave, TournamentMemberSave tournamentMemberSave) {
        var tournamentPostgres = new TournamentPostgres().fromTournamentSave(tournamentSave);
        var savedTournament = tournamentPostgreSQLRepo.save(tournamentPostgres);

        tournamentMemberSave.setTournamentId(savedTournament.getId());
        var tournamentMemberPostgres = new TournamentMemberPostgres().fromTournamentMemberSave(tournamentMemberSave);
        tournamentMemberPostgreSQLRepo.save(tournamentMemberPostgres);

        return Tournament.fromTournamentPostgres(savedTournament);
    }

    // Tournament member

    @Override
    public List<TournamentMember> getAllTournamentMembersFromTournamentId(Long tournamentId) {
        return tournamentMemberPostgreSQLRepo.findByTournamentId(tournamentId)
                .stream()
                .map(TournamentMember::fromTournamentMemberPostgres)
                .toList();
    }

    @Override
    public Optional<TournamentMember> getTournamentMemberById(Long tournamentMemberId) {
        return tournamentMemberPostgreSQLRepo.findById(tournamentMemberId)
                .map(TournamentMember::fromTournamentMemberPostgres);
    }

    @Override
    public TournamentMember addTournamentMember(TournamentMemberSave tournamentMemberSave) {
        var tournamentMemberPostgres = new TournamentMemberPostgres().fromTournamentMemberSave(tournamentMemberSave);
        var savedMember = tournamentMemberPostgreSQLRepo.save(tournamentMemberPostgres);
        return TournamentMember.fromTournamentMemberPostgres(savedMember);
    }

    @Override
    public Optional<TournamentMember> updateTournamentMember(Long id, TournamentMemberUpdate tournamentMemberUpdate) {
        return tournamentMemberPostgreSQLRepo.findById(id)
                .map(existing -> existing.fromTournamentMemberUpdate(tournamentMemberUpdate))
                .map(tournamentMemberPostgreSQLRepo::save)
                .map(TournamentMember::fromTournamentMemberPostgres);
    }

    @Override
    public void deleteTournamentMember(Long id) {
        tournamentMemberPostgreSQLRepo.deleteById(id);
    }

    @Override
    public boolean tournamentMemberExistsById(Long id) {
        return tournamentMemberPostgreSQLRepo.existsById(id);
    }

    @Override
    public boolean isUserPartOfTournament(Long userId, Long tournamentId) {
        return tournamentMemberPostgreSQLRepo.existsByTournamentIdAndUserId(tournamentId, userId);
    }

    @Override
    public boolean isUserPartOfTournamentAndActive(Long userId, Long tournamentId) {
        return tournamentMemberPostgreSQLRepo.existsByTournamentIdAndUserIdAndConfirmedTrue(tournamentId, userId);
    }

    @Override
    public boolean isUserPaymentPending(Long userId, Long tournamentId) {
        return tournamentMemberPostgreSQLRepo.findByTournamentIdAndUserId(tournamentId, userId)
                .stream()
                .map(TournamentMemberPostgres::getPaymentId)
                .filter(Objects::nonNull)
                .map(paymentRepo::findPaymentById)
                .flatMap(Optional::stream)
                .anyMatch(payment -> payment.status() == PaymentStatus.PENDING);
    }

    @Override
    public long countTournamentMembersByRole(Long tournamentId, TournamentMemberRole role) {
        return tournamentMemberPostgreSQLRepo.countByTournamentIdAndRole(tournamentId, role);
    }
}
