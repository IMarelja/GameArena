package hr.algebra.gamearena.webapp.models.mvc.data.admin.games;

public record GameEditFormViewData(
        Long gameId,
        GameEditPostViewModel form
) {
}
