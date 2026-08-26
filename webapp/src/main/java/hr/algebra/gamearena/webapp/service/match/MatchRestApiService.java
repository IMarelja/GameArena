package hr.algebra.gamearena.webapp.service.match;

import com.gamearena.client.api.GamesControllerApi;
import com.gamearena.client.api.MatchControllerApi;
import com.gamearena.client.model.ApiResponseListMatchDetailedFullView;
import com.gamearena.client.model.GamesView;
import com.gamearena.client.model.MatchDetailedFullView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MatchRestApiService implements IMatchService {

    private final AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient;
    private final GamesControllerApi gamesControllerApi;

    public MatchRestApiService(AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient, GamesControllerApi gamesControllerApi) {
        this.authenticatedMatchClient = authenticatedMatchClient;
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
                throw new NotFoundException(List.of("No response received from the GameArena API"));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<MatchDetailFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream()
                            .map(this::toDetailFullViewOrNull)
                            .filter(Objects::nonNull)
                            .toList();

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

    private MatchDetailFullViewDecereal toDetailFullViewOrNull(MatchDetailedFullView match) {
        try {
            assert match.getGameId() != null;
            GamesView game = gamesControllerApi.getGames(match.getGameId()).getData();
            if (game == null) {
                throw new NotFoundException(List.of("No game was provided for match"));
            }
            return MatchDetailFullViewDecereal.fromMatchDetailFullViewClient(match, game);
        } catch (NotFoundException e) {
            log.warn("MatchRestApiService getMyMatches(): skipping match {} - {}", match.getId(), e.getMessage());
            return null;
        }
    }
}
