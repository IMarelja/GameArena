package hr.algebra.gamearena.webapp.service.user;

import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.model.ApiResponseListUserViewDto;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRestApiService implements IUserService {

    private final UserControllerApi userControllerApi;

    public UserRestApiService(UserControllerApi userControllerApi) {
        this.userControllerApi = userControllerApi;
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
}
