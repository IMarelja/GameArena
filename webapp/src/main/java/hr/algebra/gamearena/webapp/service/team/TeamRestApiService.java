package hr.algebra.gamearena.webapp.service.team;

import com.gamearena.client.api.TeamControllerApi;
import com.gamearena.client.model.ApiResponseListTeamMemberMinimalView;
import com.gamearena.client.model.ApiResponseListTeamMinimalView;
import com.gamearena.client.model.ApiResponseTeamMemberFullView;
import com.gamearena.client.model.ApiResponseTeamMinimalView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.service.ApiWrong;
import hr.algebra.gamearena.webapp.security.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class TeamRestApiService implements ITeamService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final TeamControllerApi teamControllerApi;
    private final AuthenticatedApiClient<TeamControllerApi> authenticatedTeamClient;

    public TeamRestApiService(TeamControllerApi teamControllerApi, AuthenticatedApiClient<TeamControllerApi> authenticatedTeamClient) {
        this.teamControllerApi = teamControllerApi;
        this.authenticatedTeamClient = authenticatedTeamClient;
    }

    @Override
    public ApiResult<List<TeamMinimalViewDecereal>> getAllTeams() throws NotFoundException {
        ResponseEntity<ApiResponseListTeamMinimalView> response = teamControllerApi.getAllTeamsWithHttpInfo();
        ApiResponseListTeamMinimalView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TeamMinimalViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TeamMinimalViewDecereal::fromTeamMinimalViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<TeamMinimalViewDecereal> getTeamById(Long id) throws NotFoundException {
        ResponseEntity<ApiResponseTeamMinimalView> response = teamControllerApi.getTeamByIdWithHttpInfo(id);
        ApiResponseTeamMinimalView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        TeamMinimalViewDecereal data = body.getData() == null
                ? null
                : TeamMinimalViewDecereal.fromTeamMinimalViewClient(body.getData());

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<List<TeamMemberMinimalViewDecereal>> getTeamMembers(Long teamId) throws NotFoundException {
        ResponseEntity<ApiResponseListTeamMemberMinimalView> response = teamControllerApi.getTeamMembersWithHttpInfo(teamId);
        ApiResponseListTeamMemberMinimalView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TeamMemberMinimalViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TeamMemberMinimalViewDecereal::fromTeamMemberMinimalViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }

    @Override
    public ApiResult<TeamMemberFullViewDecereal> getMyTeamMembership(Long teamId) throws UnauthorizedException, NotFoundException {
        TeamControllerApi client;
        try {
            client = authenticatedTeamClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to view this page"));
        }

        try {
            ResponseEntity<ApiResponseTeamMemberFullView> response = client.getMeTeamMemberWithHttpInfo(teamId);
            ApiResponseTeamMemberFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }
            HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

            TeamMemberFullViewDecereal data = body.getData() == null
                    ? null
                    : TeamMemberFullViewDecereal.fromTeamMemberFullViewClient(body.getData());

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
    public Optional<TeamMemberFullViewDecereal> getMyTeamMembershipOrEmpty(Long teamId) {
        if (!AuthenticatedUser.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(getMyTeamMembership(teamId).data());
        } catch (UnauthorizedException | NotFoundException e) {
            return Optional.empty();
        }
    }
}
