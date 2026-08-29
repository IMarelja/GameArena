package hr.algebra.gamearena.webapp.models.cereal.admin;

import com.gamearena.client.model.TournamentQueryDto;

public record TournamentQueryCereal(
        Long gameId,
        Boolean ascending
) {
    public static TournamentQueryDto toMatchQueryDto(TournamentQueryCereal queryCereal) {
        TournamentQueryDto tournamentQueryDto = new TournamentQueryDto();
        tournamentQueryDto.setGameId(queryCereal.gameId);
        tournamentQueryDto.setAscending(queryCereal.ascending);

        return tournamentQueryDto;
    }
}
