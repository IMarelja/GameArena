package hr.algebra.gamearena.webapp.service.authentication;

import com.gamearena.client.api.AuthenticationControllerApi;
import com.gamearena.client.model.ApiError;
import com.gamearena.client.model.ApiResponseTokenDto;
import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class AuthenticationRestApiService implements IAuthenticationApiService {

    private final AuthenticationControllerApi authenticationControllerApi;

    public AuthenticationRestApiService(AuthenticationControllerApi authenticationControllerApi) {
        this.authenticationControllerApi = authenticationControllerApi;
    }

    @Override
    public ApiResult<TokenDecereal> login(LoginCereal loginCereal) throws BadRequestedExceptions, NotFoundException, ForbiddenException {
        try {
            ResponseEntity<ApiResponseTokenDto> response = authenticationControllerApi.loginWithHttpInfo(loginCereal.toLoginRequestClient());
            ApiResponseTokenDto body = response.getBody();
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            TokenDecereal data = body.getData() == null ? null : TokenDecereal.fromTokenDtoClient(body.getData());

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {

            HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
            List<ApiError> errors = errorsFrom(ex);

            if (status == HttpStatus.BAD_REQUEST) {
                throw new BadRequestedExceptions(firstErrorMessage(errors));
            }
            if (status == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(firstErrorMessage(errors));
            }
            if (status == HttpStatus.FORBIDDEN) {
                // Deleted/suspended accounts - api rejects the login outright, no point
                // showing the login form again.
                throw new ForbiddenException(firstErrorMessage(errors));
            }

            return ApiResult.fromApiResponseClient(status, null, errors);
        }
    }

    private static List<ApiError> errorsFrom(RestClientResponseException ex) {
        ApiResponseTokenDto body = ex.getResponseBodyAs(ApiResponseTokenDto.class);
        return body != null ? body.getErrors() : null;
    }

    private static String firstErrorMessage(List<ApiError> errors) {
        return errors != null && !errors.isEmpty() ? errors.get(0).getMessage() : "Request failed";
    }
}
