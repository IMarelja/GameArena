package hr.algebra.gamearena.api.service.tournament;

import hr.algebra.gamearena.api.dto.tournament.TournamentCreateRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentEditRequest;
import hr.algebra.gamearena.api.dto.tournament.TournamentFullView;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberEditRequest;
import hr.algebra.gamearena.api.dto.tournament.member.TournamentMemberView;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService implements ITournamentService {

    private final ITournamentRepo tournamentRepo;

    public TournamentService(ITournamentRepo tournamentRepo) {
        this.tournamentRepo = tournamentRepo;
    }

    // Tournament

    @Override
    public List<TournamentFullView> getAllTournaments() {
        return List.of();
    }

    @Override
    public Optional<TournamentFullView> getTournament(Long id) {
        return Optional.empty();
    }

    @Override
    public TournamentFullView createTournament(TournamentCreateRequest request) {
        return null;
    }

    @Override
    public TournamentFullView updateTournament(Long id, TournamentEditRequest request) {
        return null;
    }

    @Override
    public void deleteTournament(Long id) {

    }

    // Tournament member

    @Override
    public List<TournamentMemberView> getTournamentsMembers(Long tournamentId) {
        return List.of();
    }

    @Override
    public TournamentMemberView addRegularTournamentMemberAndPay(Long calledId) {
        return null;
    }

    @Override
    public TournamentMemberView editTournamentMember(Long tournamentMemberId, TournamentMemberEditRequest request) {
        return null;
    }

    @Override
    public void removeTournamentMember(Long tournamentId) {

    }
}
