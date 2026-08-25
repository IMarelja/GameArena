package hr.algebra.gamearena.webapp.config.security;

import hr.algebra.gamearena.webapp.filter.UnconfiguredEndpointDenier;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final String UNCONFIGURED_MESSAGE = "This endpoint has no @PreAuthorize configured, refusing access";

    private final ThymeleafViewResolver viewResolver;

    /* 401 */
    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException) throws IOException, ServletException
    {
        if (respondIfUnconfigured(request, response)) {
            return;
        }

        renderView(request, response, HttpStatus.UNAUTHORIZED, "unauthorized", "You must be logged in to view this page");
    }

    /* 403 */
    @Override
    public void handle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        if (respondIfUnconfigured(request, response)) {
            return;
        }

        renderView(request, response, HttpStatus.FORBIDDEN, "forbidden", "You are not allowed to access this resource");
    }

    /* 405 */
    private boolean respondIfUnconfigured(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (!Boolean.TRUE.equals(request.getAttribute(UnconfiguredEndpointDenier.UNCONFIGURED_ATTRIBUTE))) {
            return false;
        }

        renderView(request, response, HttpStatus.METHOD_NOT_ALLOWED, "error", UNCONFIGURED_MESSAGE);
        return true;
    }

    private void renderView(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String viewName,
            String message) throws IOException, ServletException
    {
        log.debug("SecurityErrorHandler: rendering {} for {} - {}", status, viewName, message);

        response.setStatus(status.value());

        Map<String, Object> model = new HashMap<>();
        model.put(MvcResponse.DATA_ATTRIBUTE, null);
        model.put(MvcResponse.ERRORS_ATTRIBUTE, List.of(new MvcError(message)));

        try {
            View view = viewResolver.resolveViewName(viewName, request.getLocale());
            assert view != null;
            view.render(model, request, response);
        } catch (IOException | ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Failed to render " + viewName + " view", e);
        }
    }
}
