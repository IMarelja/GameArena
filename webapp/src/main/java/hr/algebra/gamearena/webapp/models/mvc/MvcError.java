package hr.algebra.gamearena.webapp.models.mvc;

import java.util.List;

public record MvcError(String message) {
    public static List<MvcError> toListMvcErrorFromMvcError(MvcError mvcError) {
        return List.of(mvcError);
    }
}
