package hr.algebra.gamearena.webapp.service.games;

import com.gamearena.client.api.GamesControllerApi;
import com.gamearena.client.model.ApiResponseListGamesView;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.games.GamesViewDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class GamesRestApiService implements IGamesService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final GamesControllerApi gamesControllerApi;

    public GamesRestApiService(GamesControllerApi gamesControllerApi) {
        this.gamesControllerApi = gamesControllerApi;
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
                throw new NotFoundException(List.of("Failed to fetch active games"));
            }

            return body.getData().stream().map(GamesViewDecereal::fromGamesViewClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }
}
