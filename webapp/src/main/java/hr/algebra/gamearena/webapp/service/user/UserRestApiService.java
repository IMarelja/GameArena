package hr.algebra.gamearena.webapp.service.user;

import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.model.ApiResponseListUserViewDto;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Talks to the generated OpenAPI client and never lets a com.gamearena.client.*
 * type escape - callers only ever see this webapp's own Decereal types, wrapped
 * in an ApiResult.
 */
@Service
public class UserRestApiService implements IUserService {

    private final UserControllerApi userControllerApi;

    public UserRestApiService(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
    }

    @Override
    public ApiResult<List<UserViewDtoDecereal>> getAllUsers() {
        ResponseEntity<ApiResponseListUserViewDto> response = userControllerApi.findAllWithHttpInfo();
        ApiResponseListUserViewDto body = response.getBody();
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<UserViewDtoDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(UserViewDtoDecereal::fromUserViewDtoClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }
}
