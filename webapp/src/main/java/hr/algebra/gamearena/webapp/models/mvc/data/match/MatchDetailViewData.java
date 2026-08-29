package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;

public record MatchDetailViewData(
        MatchViewData match
) {
    public static MatchDetailViewData from(MatchDetailFullViewDecereal match) {
        return new MatchDetailViewData(MatchViewData.fromMatchDetailFullViewDecereal(match));
    }
}
