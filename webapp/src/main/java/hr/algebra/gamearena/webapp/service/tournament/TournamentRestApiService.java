package hr.algebra.gamearena.webapp.service.tournament;

import com.gamearena.client.api.TournamentControllerApi;
import com.gamearena.client.model.ApiResponseListTournamentFullView;
import com.gamearena.client.model.ApiResponseListTournamentMemberView;
import com.gamearena.client.model.ApiResponseTournamentFullView;
import com.gamearena.client.model.ApiResponseTournamentMemberView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentMemberViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import hr.algebra.gamearena.webapp.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentRestApiService implements ITournamentService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final TournamentControllerApi tournamentControllerApi;
    private final AuthenticatedApiClient<TournamentControllerApi> authenticatedTournamentClient;

    public TournamentRestApiService(
            TournamentControllerApi tournamentControllerApi,
            AuthenticatedApiClient<TournamentControllerApi> authenticatedTournamentClient)
    {
        this.tournamentControllerApi = tournamentControllerApi;
        this.authenticatedTournamentClient = authenticatedTournamentClient;
    }

    @Override
    public ApiResult<List<TournamentFullViewDecereal>> getAllTournaments() throws NotFoundException {
        ResponseEntity<ApiResponseListTournamentFullView> response = tournamentControllerApi.getTournamentsWithHttpInfo();
        ApiResponseListTournamentFullView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TournamentFullViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TournamentFullViewDecereal::fromTournamentFullViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<List<TournamentFullViewDecereal>> getMyTournaments() throws UnauthorizedException, NotFoundException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseListTournamentFullView> response = client.getMyTournamentsWithHttpInfo();
            ApiResponseListTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            List<TournamentFullViewDecereal> data = body.getData() == null
                    ? null
                    : body.getData().stream().map(TournamentFullViewDecereal::fromTournamentFullViewClient).toList();

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

    @Override
    public ApiResult<List<TournamentFullViewDecereal>> getTournamentsByUserId(Long id) throws NotFoundException {
        ResponseEntity<ApiResponseListTournamentFullView> response = tournamentControllerApi.getTournamentsForUserWithHttpInfo(id);
        ApiResponseListTournamentFullView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TournamentFullViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TournamentFullViewDecereal::fromTournamentFullViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<TournamentFullViewDecereal> getTournamentById(Long id) throws NotFoundException {
        ResponseEntity<ApiResponseTournamentFullView> response = tournamentControllerApi.getTournamentWithHttpInfo(id);
        ApiResponseTournamentFullView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        TournamentFullViewDecereal data = body.getData() == null
                ? null
                : TournamentFullViewDecereal.fromTournamentFullViewClient(body.getData());

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<List<TournamentMemberViewDecereal>> getTournamentMembers(Long tournamentId) throws NotFoundException {
        ResponseEntity<ApiResponseListTournamentMemberView> response = tournamentControllerApi.getMembersOfTournamentWithHttpInfo(tournamentId);
        ApiResponseListTournamentMemberView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TournamentMemberViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TournamentMemberViewDecereal::fromTournamentMemberViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<TournamentMemberViewDecereal> getMyTournamentMembership(Long tournamentId) throws UnauthorizedException, NotFoundException {
        TournamentControllerApi client;
        try {
            client = authenticatedTournamentClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseTournamentMemberView> response = client.getMeMemberOfTournamentWithHttpInfo(tournamentId);
            ApiResponseTournamentMemberView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            TournamentMemberViewDecereal data = body.getData() == null
                    ? null
                    : TournamentMemberViewDecereal.fromTournamentMemberViewClient(body.getData());

            return ApiResult.fromApiResponseClient(status, data, body.getErrors());
        } catch (RestClientResponseException ex) {
            HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
            List<String> messages = ApiWrong.fromRestClientResponseExceptionToListString(ex);

            if (status == HttpStatus.UNAUTHORIZED) {
                throw new UnauthorizedException(messages);
            }
            if (status == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(messages);
            }

            List<ApiWrong> wrongs = messages.stream().map(ApiWrong::new).toList();
            return new ApiResult<>(null, wrongs, status);
        }
    }

    @Override
    public Optional<TournamentMemberViewDecereal> getMyTournamentMembershipOrEmpty(Long tournamentId) {
        if (!AuthenticatedUser.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(getMyTournamentMembership(tournamentId).data());
        } catch (UnauthorizedException | NotFoundException e) {
            return Optional.empty();
        }
    }
}
