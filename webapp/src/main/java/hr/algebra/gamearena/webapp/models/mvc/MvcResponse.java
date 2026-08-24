package hr.algebra.gamearena.webapp.models.mvc;

import hr.algebra.gamearena.webapp.models.service.ApiResult;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.function.Function;

@Getter
public class MvcResponse<T> {
    public static final String DATA_ATTRIBUTE = "data";
    public static final String ERRORS_ATTRIBUTE = "errors";

    private final String view;
    private final T data;
    private final List<MvcError> errors;
    private final HttpStatus status;

    private MvcResponse(String view, T data, List<MvcError> errors, HttpStatus status) {
        this.view = view;
        this.data = data;
        this.errors = errors;
        this.status = status;
    }

    public static <T> MvcResponse<T> success(HttpStatus status, String view, T data) {
        return new MvcResponse<>(view, data, List.of(), status);
    }

    public static <T> MvcResponse<T> error(HttpStatus status, String view, MvcError error) {
        return new MvcResponse<>(view, null, List.of(error), status);
    }

    public static <T> MvcResponse<T> errors(HttpStatus status, String view, List<MvcError> errors) {
        return new MvcResponse<>(view, null, errors, status);
    }

    public static <R, T> MvcResponse<T> fromApiResult(String view, ApiResult<R> apiResult, Function<R, T> dataMapper) {
        List<MvcError> errors = apiResult.errors().stream()
                .map(e -> new MvcError(e.message()))
                .toList();
        T data = apiResult.data() != null ? dataMapper.apply(apiResult.data()) : null;
        return new MvcResponse<>(view, data, errors, apiResult.status());
    }

    public ModelAndView toModelAndView() {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.setStatus(status);
        modelAndView.addObject(DATA_ATTRIBUTE, data);
        modelAndView.addObject(ERRORS_ATTRIBUTE, errors);
        return modelAndView;
    }
}
