package hr.algebra.gamearena.api.repository.tournament;

import hr.algebra.gamearena.api.model.tournament.Tournament;
import hr.algebra.gamearena.api.model.tournament.TournamentSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberSave;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberUpdate;
import hr.algebra.gamearena.api.orm.postgres.TournamentMemberPostgres;
import hr.algebra.gamearena.api.orm.postgres.TournamentPostgres;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TournamentPostgresRepo implements ITournamentRepo {

    private final ITournamentPostgreSQLRepo tournamentPostgreSQLRepo;
    private final ITournamentMemberPostgreSQLRepo tournamentMemberPostgreSQLRepo;

    public TournamentPostgresRepo(ITournamentPostgreSQLRepo tournamentPostgreSQLRepo, ITournamentMemberPostgreSQLRepo tournamentMemberPostgreSQLRepo) {
        this.tournamentPostgreSQLRepo = tournamentPostgreSQLRepo;
        this.tournamentMemberPostgreSQLRepo = tournamentMemberPostgreSQLRepo;
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
    public Optional<Tournament> updateTournament(Long id, TournamentSave tournamentUpdate) {
        return tournamentPostgreSQLRepo.findById(id)
                .map(existing -> existing.fromTournamentSave(tournamentUpdate))
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
}
