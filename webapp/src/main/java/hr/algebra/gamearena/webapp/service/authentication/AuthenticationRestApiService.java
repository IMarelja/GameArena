package hr.algebra.gamearena.webapp.service.authentication;

import com.gamearena.client.api.AuthenticationControllerApi;
import com.gamearena.client.model.ApiResponseTokenDto;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.RegisterCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiExceptionMapper;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@Slf4j
public class AuthenticationRestApiService implements IAuthenticationService {

    private final AuthenticationControllerApi authenticationControllerApi;

    public AuthenticationRestApiService(AuthenticationControllerApi authenticationControllerApi) {
        this.authenticationControllerApi = authenticationControllerApi;
    }

    @Override
    public ApiResult<TokenDecereal> login(LoginCereal loginCereal)
            throws BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, UnauthorizedException
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
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }
    }

    @Override
    public ApiResult<TokenDecereal> register(RegisterCereal registerCereal)
            throws BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, UnauthorizedException
    {
        try {
            ResponseEntity<ApiResponseTokenDto> response = authenticationControllerApi.registerWithHttpInfo(registerCereal.toRegisterRequestClient());
            ApiResponseTokenDto body = response.getBody();

            if (body == null) {
                log.error("AuthenticationRestApiService register(): API returned a {} response with no body", response.getStatusCode());
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            TokenDecereal data = body.getData() == null ? null : TokenDecereal.fromTokenDtoClient(body.getData());

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }
    }
}
