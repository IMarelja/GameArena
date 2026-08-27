package hr.algebra.gamearena.webapp.models.service;

import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

public final class ApiExceptionMapper {

    private ApiExceptionMapper() {
    }

    public static <T> ApiResult<T> notFoundOnly(RestClientResponseException ex) throws NotFoundException {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(messages);
        }

        return fallback(status, messages);
    }

    public static <T> ApiResult<T> unauthorizedOnly(RestClientResponseException ex) throws UnauthorizedException {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(messages);
        }

        return fallback(status, messages);
    }

    public static <T> ApiResult<T> unauthorizedOrNotFound(RestClientResponseException ex)
            throws UnauthorizedException, NotFoundException
    {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(messages);
        }
        if (status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(messages);
        }

        return fallback(status, messages);
    }

    public static <T> ApiResult<T> unauthorizedForbiddenOrNotFound(RestClientResponseException ex)
            throws UnauthorizedException, ForbiddenException, NotFoundException
    {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(messages);
        }
        if (status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(messages);
        }
        if (status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(messages);
        }

        return fallback(status, messages);
    }

    public static <T> ApiResult<T> unauthorizedOrForbidden(RestClientResponseException ex)
            throws UnauthorizedException, ForbiddenException
    {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(messages);
        }
        if (status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(messages);
        }

        return fallback(status, messages);
    }

    public static <T> ApiResult<T> unauthorizedBadRequestNotFoundOrForbidden(RestClientResponseException ex)
            throws UnauthorizedException, BadRequestedExceptions, NotFoundException, ForbiddenException
    {
        HttpStatus status = statusOf(ex);
        List<String> messages = messagesOf(ex);

        if (status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(messages);
        }
        if (status == HttpStatus.BAD_REQUEST) {
            throw new BadRequestedExceptions(messages);
        }
        if (status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(messages);
        }
        if (status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(messages);
        }

        return fallback(status, messages);
    }

    private static HttpStatus statusOf(RestClientResponseException ex) {
        return HttpStatus.valueOf(ex.getStatusCode().value());
    }

    private static List<String> messagesOf(RestClientResponseException ex) {
        return ApiWrong.fromRestClientResponseExceptionToListString(ex);
    }

    private static <T> ApiResult<T> fallback(HttpStatus status, List<String> messages) {
        List<ApiWrong> wrongs = messages.stream().map(ApiWrong::new).toList();
        return new ApiResult<>(null, wrongs, status);
    }
}
