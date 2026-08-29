package hr.algebra.gamearena.webapp.models.mvc.data.admin;

import hr.algebra.gamearena.webapp.models.cereal.admin.MatchQueryCereal;
import hr.algebra.gamearena.webapp.models.cereal.admin.TournamentQueryCereal;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record AdminQueryGetViewModel(
        @Nullable
        Long matchGameId,

        @NotNull(message = "Order of match creation is required")
        Boolean ascendingMatch,

        @Nullable
        Long tournamentGameId,

        @NotNull(message = "Order of match creation is required")
        Boolean ascendingTournament
) {
        public MatchQueryCereal toMatchQueryCereal() {
                return new MatchQueryCereal(
                        matchGameId,
                        ascendingMatch
                );
        }

        public TournamentQueryCereal toTournamentMatchQueryCereal() {
                return new TournamentQueryCereal(
                        tournamentGameId,
                        ascendingTournament
                );
        }
}
