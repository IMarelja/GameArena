package hr.algebra.gamearena.webapp.service.leaderboard;

import com.gamearena.client.api.LeaderboardControllerApi;
import com.gamearena.client.model.ApiResponseListTournamentStatsEntryView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class LeaderboardRestApiService implements ILeaderboardService {

    private final AuthenticatedApiClient<LeaderboardControllerApi> authenticatedLeaderboardClient;

    public LeaderboardRestApiService(AuthenticatedApiClient<LeaderboardControllerApi> authenticatedLeaderboardClient) {
        this.authenticatedLeaderboardClient = authenticatedLeaderboardClient;
    }

    @Override
    public ApiResult<List<TournamentStatsEntryViewDecereal>> getMyStats() throws UnauthorizedException, NotFoundException {
        LeaderboardControllerApi client;
        try {
            client = authenticatedLeaderboardClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseListTournamentStatsEntryView> response = client.getMyStatsWithHttpInfo();
            ApiResponseListTournamentStatsEntryView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of("No response received from the GameArena API"));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<TournamentStatsEntryViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream().map(TournamentStatsEntryViewDecereal::fromTournamentStatsEntryViewClient).toList();

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
