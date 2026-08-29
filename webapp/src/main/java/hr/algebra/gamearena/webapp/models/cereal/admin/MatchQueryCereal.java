package hr.algebra.gamearena.webapp.models.cereal.admin;

import com.gamearena.client.model.MatchQueryDto;

public record MatchQueryCereal(
        Long gameId,
        Boolean ascending
) {
    public static MatchQueryDto toMatchQueryDto(MatchQueryCereal queryCereal) {
        MatchQueryDto matchQueryDto = new MatchQueryDto();
        matchQueryDto.setGameId(queryCereal.gameId);
        matchQueryDto.setAscending(queryCereal.ascending);

        return matchQueryDto;
    }
}
