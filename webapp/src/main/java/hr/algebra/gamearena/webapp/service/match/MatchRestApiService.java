package hr.algebra.gamearena.webapp.service.match;

import com.gamearena.client.api.GamesControllerApi;
import com.gamearena.client.api.MatchControllerApi;
import com.gamearena.client.model.ApiResponseListMatchDetailedFullView;
import com.gamearena.client.model.GamesView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiExceptionMapper;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class MatchRestApiService implements IMatchService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient;
    private final MatchControllerApi matchControllerApi;
    private final GamesControllerApi gamesControllerApi;

    public MatchRestApiService(
            AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient,
            MatchControllerApi matchControllerApi,
            GamesControllerApi gamesControllerApi)
    {
        this.authenticatedMatchClient = authenticatedMatchClient;
        this.matchControllerApi = matchControllerApi;
        this.gamesControllerApi = gamesControllerApi;
    }

    @Override
    public ApiResult<List<MatchDetailFullViewDecereal>> getMyMatches() throws UnauthorizedException, NotFoundException {
        MatchControllerApi client;
        try {
            client = authenticatedMatchClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseListMatchDetailedFullView> response = client.getMyMatchesWithHttpInfo();
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<MatchDetailFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream()
                            .map(match -> {
                                GamesView game = null;

                                if(match.getGameId() != null){
                                    try {
                                        game = gamesControllerApi.getGames(match.getGameId()).getData();
                                    } catch (RestClientResponseException ex) {
                                        game = null;
                                    }
                                }

                                return MatchDetailFullViewDecereal.fromMatchDetailFullViewClientAndGameView(match, game);

                            })
                            .toList();

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOnly(ex);
        }
    }

    @Override
    public ApiResult<List<MatchDetailFullViewDecereal>> getMatchesByUserId(Long id) throws NotFoundException {
        try {
            ResponseEntity<ApiResponseListMatchDetailedFullView> response = matchControllerApi.getMatchesForUserWithHttpInfo(id);
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<MatchDetailFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream()
                            .map(match -> {
                                GamesView game = null;

                                if(match.getGameId() != null){
                                    try {
                                        game = gamesControllerApi.getGames(match.getGameId()).getData();
                                    } catch (RestClientResponseException ex) {
                                        game = null;
                                    }
                                }

                                return MatchDetailFullViewDecereal.fromMatchDetailFullViewClientAndGameView(match, game);

                            })
                            .toList();

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public ApiResult<List<MatchDetailFullViewDecereal>> getMatchesByTournamentId(Long id) throws NotFoundException {
        try {
            ResponseEntity<ApiResponseListMatchDetailedFullView> response = matchControllerApi.getMatchesTournamentWithHttpInfo(id);
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<MatchDetailFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream()
                    .map(match -> {
                        GamesView game = null;

                        if(match.getGameId() != null){
                            try {
                                game = gamesControllerApi.getGames(match.getGameId()).getData();
                            } catch (RestClientResponseException ex) {
                                game = null;
                            }
                        }

                        return MatchDetailFullViewDecereal.fromMatchDetailFullViewClientAndGameView(match, game);

                    })
                    .toList();

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

}
