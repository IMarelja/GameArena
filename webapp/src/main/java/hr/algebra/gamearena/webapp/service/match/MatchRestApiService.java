package hr.algebra.gamearena.webapp.service.match;

import com.gamearena.client.api.MatchControllerApi;
import com.gamearena.client.model.ApiResponseListMatchFullView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchFullViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class MatchRestApiService implements IMatchService {

    private final AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient;

    public MatchRestApiService(AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient) {
        this.authenticatedMatchClient = authenticatedMatchClient;
    }

    @Override
    public ApiResult<List<MatchFullViewDecereal>> getMyMatches() throws UnauthorizedException, NotFoundException {
        MatchControllerApi client;
        try {
            client = authenticatedMatchClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseListMatchFullView> response = client.getMyMatchesWithHttpInfo();
            ApiResponseListMatchFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of("No response received from the GameArena API"));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<MatchFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream().map(MatchFullViewDecereal::fromMatchFullViewClient).toList();

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
