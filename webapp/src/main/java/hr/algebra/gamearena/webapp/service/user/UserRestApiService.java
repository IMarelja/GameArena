package hr.algebra.gamearena.webapp.service.user;

import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.model.ApiResponseListUserViewDto;
import com.gamearena.client.model.ApiResponseUserFullViewDto;
import com.gamearena.client.model.ApiResponseUserViewDto;
import com.gamearena.client.model.UserSuspendRequest;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.user.UserFullViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserSuspendCereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@Slf4j
public class UserRestApiService implements IUserService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final UserControllerApi userControllerApi;
    private final AuthenticatedApiClient<UserControllerApi> authenticatedUserClient;

    public UserRestApiService(UserControllerApi userControllerApi, AuthenticatedApiClient<UserControllerApi> authenticatedUserClient) {
        this.userControllerApi = userControllerApi;
        this.authenticatedUserClient = authenticatedUserClient;
    }

    @Override
    public List<UserViewDtoDecereal> getAllUsers() throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListUserViewDto> response = userControllerApi.findAllWithHttpInfo();
            ApiResponseListUserViewDto body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch users"));
            }

            return body.getData().stream().map(UserViewDtoDecereal::fromUserViewDtoClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public UserViewDtoDecereal getUserById(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseUserViewDto> response = userControllerApi.getByIdWithHttpInfo(id);
            ApiResponseUserViewDto body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this user"));
            }

            return UserViewDtoDecereal.fromUserViewDtoClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public UserFullViewDtoDecereal getMe() throws NotFoundException, UnauthorizedException, UnexpectedApiErrorException {
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

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch your profile"));
            }

            return UserFullViewDtoDecereal.fromUserFullViewDtoClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOnly(ex);
        }
    }

    @Override
    public void deleteMyAccount() throws UnauthorizedException {
        UserControllerApi client;
        try {
            client = authenticatedUserClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to delete your account"));
        }

        try {
            client.deleteMe();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 401) {
                throw new UnauthorizedException(List.of("You must be logged in to delete your account"));
            }
            throw ex;
        }
    }

    @Override
    public UserFullViewDtoDecereal getUserFullById(Long id) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        UserControllerApi client;
        try {
            client = authenticatedUserClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseUserFullViewDto> response = client.getByIdFullInfoWithHttpInfo(id);
            ApiResponseUserFullViewDto body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this user"));
            }

            return UserFullViewDtoDecereal.fromUserFullViewDtoClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public UserFullViewDtoDecereal suspendAccount(UserSuspendCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        UserControllerApi client;
        try {
            client = authenticatedUserClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to suspend accounts"));
        }

        try {
            var request = new UserSuspendRequest().userId(cereal.userId()).isActive(cereal.isActive());
            ResponseEntity<ApiResponseUserFullViewDto> response = client.suspendAccountWithHttpInfo(request);
            ApiResponseUserFullViewDto body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return UserFullViewDtoDecereal.fromUserFullViewDtoClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }
}
