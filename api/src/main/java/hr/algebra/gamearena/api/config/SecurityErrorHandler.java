package hr.algebra.gamearena.api.config;

import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Turns the two security verdicts into {@link ApiResponse} bodies.
 * <p>
 * Both cases happen inside the filter chain, before the DispatcherServlet runs, so
 * {@code GlobalExceptionHandler} never sees them and the JSON has to be written by hand here.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final String NO_TOKEN_MESSAGE = "Token not provided";
    private static final String FORBIDDEN_MESSAGE = "You are not allowed to access this resource";

    private final ObjectMapper objectMapper;

    /** 401 - the endpoint needs an identity and the caller has none. */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException
    {
        Object refusalReason = request.getAttribute(JwtAuthenticationFilter.AUTH_ERROR_ATTRIBUTE);
        String message = refusalReason instanceof String reason ? reason : NO_TOKEN_MESSAGE;

        write(response, HttpStatus.UNAUTHORIZED, message);
    }

    /** 403 - the caller is known, but does not hold the role the endpoint requires. */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException
    {
        write(response, HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE);
    }

    private void write(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        log.debug("SecurityErrorHandler: responding {}: {}", status.value(), message);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), ApiResponse.error(new ApiError(message)));
    }
}
