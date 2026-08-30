package hr.algebra.gamearena.webapp.service.games;

import com.gamearena.client.api.GamesControllerApi;
import com.gamearena.client.model.ApiResponseGamesFullView;
import com.gamearena.client.model.ApiResponseListGamesView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.games.GameCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.games.GameEditCereal;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class GamesRestApiService implements IGamesService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";
    private static final String FAILED_FETCH = "Failed to fetch active games";

    private final GamesControllerApi gamesControllerApi;
    private final AuthenticatedApiClient<GamesControllerApi> authenticatedGamesApiClient;

    public GamesRestApiService(GamesControllerApi gamesControllerApi, AuthenticatedApiClient<GamesControllerApi> authenticatedGamesApiClient) {
        this.gamesControllerApi = gamesControllerApi;
        this.authenticatedGamesApiClient = authenticatedGamesApiClient;
    }

    @Override
    public GamesViewDecereal getById(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseGamesFullView> response = gamesControllerApi.getGamesFullInfoWithHttpInfo(id);
            ApiResponseGamesFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of(FAILED_FETCH));
            }

            return GamesViewDecereal.fromGamesFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public List<GamesViewDecereal> getActiveGames() throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListGamesView> response = gamesControllerApi.getAllActiveGamesWithHttpInfo();
            ApiResponseListGamesView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of(FAILED_FETCH));
            }

            return body.getData()
                    .stream()
                    .map(GamesViewDecereal::fromGamesViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public List<GamesViewDecereal> getGames() throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListGamesView> response = gamesControllerApi.getAllGamesWithHttpInfo();
            ApiResponseListGamesView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of(FAILED_FETCH));
            }

            return body.getData()
                    .stream()
                    .map(GamesViewDecereal::fromGamesViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public GamesViewDecereal createGame(GameCreateCereal cereal) throws NotFoundException, UnexpectedApiErrorException, ForbiddenException, ConflictException, UnauthorizedException, BadRequestedExceptions {
        GamesControllerApi client = authenticatedGamesApiClient.get()
                .orElseThrow(() -> new UnauthorizedException(List.of("You must be logged in to view this page")));

        try {
            ResponseEntity<ApiResponseGamesFullView> response = client.createGamesWithHttpInfo(cereal.toGamesCreateRequest());
            ApiResponseGamesFullView body = response.getBody();
            if (body == null) {
                throw new UnexpectedApiErrorException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException(List.of(FAILED_FETCH));
            }

            return GamesViewDecereal.fromGamesFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundForbiddenOrConflict(ex);
        }
    }

    @Override
    public GamesViewDecereal editGame(Long id, GameEditCereal cereal) throws NotFoundException, UnexpectedApiErrorException, ForbiddenException, ConflictException, UnauthorizedException, BadRequestedExceptions {
        GamesControllerApi client = authenticatedGamesApiClient.get()
                .orElseThrow(() -> new UnauthorizedException(List.of("You must be logged in to view this page")));

        try {
            ResponseEntity<ApiResponseGamesFullView> response = client.updateGamesWithHttpInfo(id, cereal.toGamesEditRequest());
            ApiResponseGamesFullView body = response.getBody();
            if (body == null) {
                throw new UnexpectedApiErrorException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException(List.of(FAILED_FETCH));
            }

            return GamesViewDecereal.fromGamesFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundForbiddenOrConflict(ex);
        }
    }
}
