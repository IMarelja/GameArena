package hr.algebra.gamearena.webapp.models.mvc.data.match;

import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;

public record MatchDetailViewData(
        MatchViewData match,
        boolean canEdit
) {
    public static MatchDetailViewData from(MatchDetailFullViewDecereal match, boolean canEdit) {
        return new MatchDetailViewData(MatchViewData.fromMatchDetailFullViewDecereal(match), canEdit);
    }
}
