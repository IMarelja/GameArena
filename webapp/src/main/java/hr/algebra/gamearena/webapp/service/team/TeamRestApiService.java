package hr.algebra.gamearena.webapp.service.team;

import com.gamearena.client.api.TeamControllerApi;
import com.gamearena.client.model.ApiResponseListTeamMemberMinimalView;
import com.gamearena.client.model.ApiResponseListTeamMinimalView;
import com.gamearena.client.model.ApiResponseTeamMinimalView;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMemberMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamRestApiService implements ITeamService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final TeamControllerApi teamControllerApi;

    public TeamRestApiService(TeamControllerApi teamControllerApi) {
        this.teamControllerApi = teamControllerApi;
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
}
