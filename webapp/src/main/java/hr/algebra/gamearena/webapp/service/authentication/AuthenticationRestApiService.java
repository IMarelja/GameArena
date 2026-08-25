package hr.algebra.gamearena.webapp.service.authentication;

import com.gamearena.client.api.AuthenticationControllerApi;
import com.gamearena.client.model.ApiResponseTokenDto;
import hr.algebra.gamearena.webapp.exceptions.extenders.BadRequestedExceptions;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@Slf4j
public class AuthenticationRestApiService implements IAuthenticationApiService {

    private final AuthenticationControllerApi authenticationControllerApi;

    public AuthenticationRestApiService(AuthenticationControllerApi authenticationControllerApi) {
        this.authenticationControllerApi = authenticationControllerApi;
    }

    @Override
    public ApiResult<TokenDecereal> login(LoginCereal loginCereal)
            throws BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException
    {
        try {
            ResponseEntity<ApiResponseTokenDto> response = authenticationControllerApi.loginWithHttpInfo(loginCereal.toLoginRequestClient());
            ApiResponseTokenDto body = response.getBody();

            if (body == null) {
                log.error("AuthenticationRestApiService login(): API returned a {} response with no body", response.getStatusCode());
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            TokenDecereal data = body.getData() == null ? null : TokenDecereal.fromTokenDtoClient(body.getData());

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {

            HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
            List<String> messages = ApiWrong.fromRestClientResponseExceptionToListString(ex);

            if (status == HttpStatus.BAD_REQUEST) {
                throw new BadRequestedExceptions(messages);
            }
            if (status == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(messages);
            }
            if (status == HttpStatus.FORBIDDEN) {
                throw new ForbiddenException(messages);
            }

            List<ApiWrong> wrongs = messages.stream().map(ApiWrong::new).toList();
            return new ApiResult<>(null, wrongs, status);
        }
    }
}
