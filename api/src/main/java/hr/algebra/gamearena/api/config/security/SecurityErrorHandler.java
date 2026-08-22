package hr.algebra.gamearena.api.config.security;

import hr.algebra.gamearena.api.dto.other.ApiError;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.filter.JwtAuthenticationFilter;
import hr.algebra.gamearena.api.filter.UnconfiguredEndpointDenier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private static final String UNCONFIGURED_MESSAGE = "Method refused no matter what, fix by configuring security";

    /** 401 */
    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException) throws IOException
    {
        if (respondIfUnconfigured(request, response)) {
            return;
        }

        Object refusalReason = request.getAttribute(JwtAuthenticationFilter.AUTH_ERROR_ATTRIBUTE);
        String message = refusalReason instanceof String reason ? reason : "Token not provided";

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), ApiResponse.error(new ApiError(message)));
    }

    /** 403 */
    @Override
    public void handle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AccessDeniedException accessDeniedException) throws IOException
    {
        if (respondIfUnconfigured(request, response)) {
            return;
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), ApiResponse.error(new ApiError("You are not allowed to access this resource")));
    }

    /* 405 */
    private boolean respondIfUnconfigured(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response
    ) throws IOException {
        if (!Boolean.TRUE.equals(request.getAttribute(UnconfiguredEndpointDenier.UNCONFIGURED_ATTRIBUTE))) {
            return false;
        }

        /*
        This code will bomb a whole school, please don't change anything
        */

        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), ApiResponse.error(new ApiError(UNCONFIGURED_MESSAGE)));
        return true;
    }
}
