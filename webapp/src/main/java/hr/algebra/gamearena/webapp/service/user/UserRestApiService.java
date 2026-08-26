package hr.algebra.gamearena.webapp.service.user;

import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.model.ApiResponseListUserViewDto;
import com.gamearena.client.model.ApiResponseUserFullViewDto;
import com.gamearena.client.model.ApiResponseUserViewDto;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.user.UserFullViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@Slf4j
public class UserRestApiService implements IUserService {

    private final UserControllerApi userControllerApi;
    private final AuthenticatedApiClient<UserControllerApi> authenticatedUserClient;

    public UserRestApiService(UserControllerApi userControllerApi, AuthenticatedApiClient<UserControllerApi> authenticatedUserClient) {
        this.userControllerApi = userControllerApi;
        this.authenticatedUserClient = authenticatedUserClient;
    }

    @Override
    public ApiResult<List<UserViewDtoDecereal>> getAllUsers() throws NotFoundException {
        ResponseEntity<ApiResponseListUserViewDto> response = userControllerApi.findAllWithHttpInfo();
        ApiResponseListUserViewDto body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of("No response received from the GameArena API"));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<UserViewDtoDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(UserViewDtoDecereal::fromUserViewDtoClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<UserViewDtoDecereal> getUserById(Long id) throws NotFoundException {
        ResponseEntity<ApiResponseUserViewDto> response = userControllerApi.getByIdWithHttpInfo(id);
        ApiResponseUserViewDto body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of("No response received from the GameArena API"));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        UserViewDtoDecereal data = body.getData() == null
                ? null
                : UserViewDtoDecereal.fromUserViewDtoClient(body.getData());

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<UserFullViewDtoDecereal> getMe() throws UnauthorizedException, UnexpectedApiErrorException {
        UserControllerApi client;
        try {
            client = authenticatedUserClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseUserFullViewDto> response = client.getMeWithHttpInfo();
            ApiResponseUserFullViewDto body = response.getBody();

            if (body == null) {
                log.error("UserRestApiService getMe(): API returned a {} response with no body", response.getStatusCode());
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());
            UserFullViewDtoDecereal data = body.getData() == null
                    ? null
                    : UserFullViewDtoDecereal.fromUserFullViewDtoClient(body.getData());

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
            List<String> messages = ApiWrong.fromRestClientResponseExceptionToListString(ex);

            if (status == HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException(messages);
            }

            List<ApiWrong> wrongs = messages.stream().map(ApiWrong::new).toList();
            return new ApiResult<>(null, wrongs, status);
        }
    }
}
