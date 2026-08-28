package hr.algebra.gamearena.webapp.service;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

public final class ApiExceptionMapper {

    private ApiExceptionMapper() {
    }

    public static <T> T notFoundOnly(RestClientResponseException ex) throws NotFoundException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedOnly(RestClientResponseException ex) throws UnauthorizedException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedOrNotFound(RestClientResponseException ex)
            throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }
        if (wrong.status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedForbiddenOrNotFound(RestClientResponseException ex)
            throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }
        if (wrong.status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(wrong.messages);
        }
        if (wrong.status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedOrForbidden(RestClientResponseException ex)
            throws UnauthorizedException, ForbiddenException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }
        if (wrong.status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedBadRequestNotFoundOrForbidden(RestClientResponseException ex)
            throws UnauthorizedException, BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }
        if (wrong.status == HttpStatus.BAD_REQUEST) {
            throw new BadRequestedExceptions(wrong.messages);
        }
        if (wrong.status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(wrong.messages);
        }
        if (wrong.status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    public static <T> T unauthorizedBadRequestNotFoundForbiddenOrConflict(RestClientResponseException ex)
            throws UnauthorizedException, BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, ConflictException {

        ApiWrongMessage wrong = ApiWrongMessage.fromRestClientResponseException(ex);

        if (wrong.status == HttpStatus.UNAUTHORIZED) {
            throw new UnauthorizedException(wrong.messages);
        }
        if (wrong.status == HttpStatus.BAD_REQUEST) {
            throw new BadRequestedExceptions(wrong.messages);
        }
        if (wrong.status == HttpStatus.NOT_FOUND) {
            throw new NotFoundException(wrong.messages);
        }
        if (wrong.status == HttpStatus.FORBIDDEN) {
            throw new ForbiddenException(wrong.messages);
        }
        if (wrong.status == HttpStatus.CONFLICT) {
            throw new ConflictException(wrong.messages);
        }

        throw new UnexpectedApiErrorException(wrong.status + ": " + wrong.messages);
    }

    private record ApiWrongMessage(
            HttpStatus status,
            List<String> messages
    ){
        public static ApiWrongMessage fromRestClientResponseException(RestClientResponseException ex){
            return new ApiWrongMessage(
                    statusOf(ex),
                    messagesOf(ex)
            );
        }
    }

    private static HttpStatus statusOf(RestClientResponseException ex) {
        return HttpStatus.valueOf(ex.getStatusCode().value());
    }

    private static List<String> messagesOf(RestClientResponseException ex) {
        return ApiWrong.fromRestClientResponseExceptionToListString(ex);
    }
}
