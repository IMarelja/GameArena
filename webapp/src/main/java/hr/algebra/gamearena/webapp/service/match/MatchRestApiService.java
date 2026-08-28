package hr.algebra.gamearena.webapp.service.match;

import com.gamearena.client.api.MatchControllerApi;
import com.gamearena.client.model.ApiResponseListMatchDetailedFullView;
import com.gamearena.client.model.ApiResponseMatchDetailedFullView;
import com.gamearena.client.model.MatchCreateRequest;
import com.gamearena.client.model.MatchEditRequest;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchCreateCereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchEditCereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class MatchRestApiService implements IMatchService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient;
    private final MatchControllerApi matchControllerApi;

    public MatchRestApiService(
            AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient,
            MatchControllerApi matchControllerApi)
    {
        this.authenticatedMatchClient = authenticatedMatchClient;
        this.matchControllerApi = matchControllerApi;
    }

    @Override
    public List<MatchDetailFullViewDecereal> getMyMatches() throws UnauthorizedException, NotFoundException, UnexpectedApiErrorException {
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

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to could not fetch your matches"));
            }

            return body.getData().stream()
                    .map(MatchDetailFullViewDecereal::fromMatchDetailFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOnly(ex);
        }
    }

    @Override
    public List<MatchDetailFullViewDecereal> getMatchesByUserId(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListMatchDetailedFullView> response = matchControllerApi.getMatchesForUserWithHttpInfo(id);
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to could not fetch user's matches"));
            }

            return body.getData().stream()
                    .map(MatchDetailFullViewDecereal::fromMatchDetailFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public List<MatchDetailFullViewDecereal> getMatchesByTournamentId(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseListMatchDetailedFullView> response = matchControllerApi.getMatchesTournamentWithHttpInfo(id);
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to could not fetch Tournaments matches"));
            }

            return body.getData().stream()
                    .map(MatchDetailFullViewDecereal::fromMatchDetailFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public MatchDetailFullViewDecereal getMatchById(Long id) throws NotFoundException, UnexpectedApiErrorException {
        try {
            ResponseEntity<ApiResponseMatchDetailedFullView> response = matchControllerApi.getMatchWithHttpInfo(id);
            ApiResponseMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch this match"));
            }

            return MatchDetailFullViewDecereal.fromMatchDetailFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.notFoundOnly(ex);
        }
    }

    @Override
    public MatchDetailFullViewDecereal createMatch(MatchCreateCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException, BadRequestedExceptions {
        MatchControllerApi client;
        try {
            client = authenticatedMatchClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to create a match"));
        }

        try {
            var request = new MatchCreateRequest()
                    .tournamentId(cereal.tournamentId())
                    .playerOneId(cereal.playerOneId())
                    .playerTwoId(cereal.playerTwoId())
                    .scheduledAt(cereal.scheduledAt());

            ResponseEntity<ApiResponseMatchDetailedFullView> response = client.createMatchWithHttpInfo(request);
            ApiResponseMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return MatchDetailFullViewDecereal.fromMatchDetailFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }
    }

    @Override
    public MatchDetailFullViewDecereal editMatch(Long id, MatchEditCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        MatchControllerApi client;
        try {
            client = authenticatedMatchClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to edit a match"));
        }

        try {
            var request = new MatchEditRequest()
                    .playerOneId(cereal.playerOneId())
                    .playerTwoId(cereal.playerTwoId())
                    .playerOneScore(cereal.playerOneScore())
                    .playerTwoScore(cereal.playerTwoScore())
                    .winnerId(cereal.winnerId())
                    .status(cereal.status().toClient())
                    .scheduledAt(cereal.scheduledAt())
                    .playedAt(cereal.playedAt());

            ResponseEntity<ApiResponseMatchDetailedFullView> response = client.editMatchWithHttpInfo(id, request);
            ApiResponseMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException("The GameArena API returned an unexpected empty response");
            }

            return MatchDetailFullViewDecereal.fromMatchDetailFullViewClient(body.getData());
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }
}
