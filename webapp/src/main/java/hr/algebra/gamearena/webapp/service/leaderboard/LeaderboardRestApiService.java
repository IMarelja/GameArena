package hr.algebra.gamearena.webapp.service.leaderboard;

import com.gamearena.client.api.LeaderboardControllerApi;
import com.gamearena.client.model.ApiResponseListTournamentStatsEntryView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.leaderboard.TournamentStatsEntryViewDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class LeaderboardRestApiService implements ILeaderboardService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final AuthenticatedApiClient<LeaderboardControllerApi> authenticatedLeaderboardClient;
    private final LeaderboardControllerApi leaderboardControllerApi;

    public LeaderboardRestApiService(
            AuthenticatedApiClient<LeaderboardControllerApi> authenticatedLeaderboardClient,
            LeaderboardControllerApi leaderboardControllerApi)
    {
        this.authenticatedLeaderboardClient = authenticatedLeaderboardClient;
        this.leaderboardControllerApi = leaderboardControllerApi;
    }

    @Override
    public List<TournamentStatsEntryViewDecereal> getMyStats() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {
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
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch your leaderboard stats"));
            }

            return body.getData().stream()
                    .map(TournamentStatsEntryViewDecereal::fromTournamentStatsEntryViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOnly(ex);
        }
    }

    @Override
    public List<TournamentStatsEntryViewDecereal> getStatsByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListTournamentStatsEntryView> response = leaderboardControllerApi.getStatsForUserWithHttpInfo(id);
            ApiResponseListTournamentStatsEntryView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch user's leaderboard stats"));
            }

            return body.getData().stream()
                    .map(TournamentStatsEntryViewDecereal::fromTournamentStatsEntryViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }
}
