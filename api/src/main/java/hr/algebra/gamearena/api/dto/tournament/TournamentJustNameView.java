package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.model.tournament.Tournament;

// Please give me a 5

public record TournamentJustNameView(
        Long id,
        String name
) {
    public static TournamentJustNameView fromTournament(Tournament tournament) {
        return new TournamentJustNameView(
                tournament.id(),
                tournament.name()
        );
    }
}
