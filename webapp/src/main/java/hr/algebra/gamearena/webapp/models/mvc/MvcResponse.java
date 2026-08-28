package hr.algebra.gamearena.webapp.models.mvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;

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

    public static <T> MvcResponse<T> errorsWithData(HttpStatus status, String view, T data, List<MvcError> errors) {
        return new MvcResponse<>(view, data, errors, status);
    }

    public static ModelAndView redirect(String location) {
        return new ModelAndView("redirect:" + location);
    }

    public static <T> ModelAndView redirect(String location, T data, List<MvcError> errors) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);

        if (data != null) {
            flashMap.put(DATA_ATTRIBUTE, data);
        }
        if (errors != null && !errors.isEmpty()) {
            flashMap.put(ERRORS_ATTRIBUTE, errors);
        }
        flashMap.setTargetRequestPath(location);

        return redirect(location);
    }

    public ModelAndView toModelAndView() {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.setStatus(status);
        
        if (data != null) {
            modelAndView.addObject(DATA_ATTRIBUTE, data);
        }
        if (errors != null && !errors.isEmpty()) {
            modelAndView.addObject(ERRORS_ATTRIBUTE, errors);
        }

        return modelAndView;
    }
}
