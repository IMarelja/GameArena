package hr.algebra.gamearena.webapp.service.team;

import com.gamearena.client.api.TeamControllerApi;
import com.gamearena.client.model.ApiResponseListTeamMinimalView;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.team.TeamMinimalViewDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamRestApiService implements ITeamService {

    private final TeamControllerApi teamControllerApi;

    public TeamRestApiService(TeamControllerApi teamControllerApi) {
        this.teamControllerApi = teamControllerApi;
    }

    @Override
    public ApiResult<List<TeamMinimalViewDecereal>> getAllTeams() throws NotFoundException {
        ResponseEntity<ApiResponseListTeamMinimalView> response = teamControllerApi.getAllTeamsWithHttpInfo();
        ApiResponseListTeamMinimalView body = response.getBody();
        if (body == null) {
            throw new NotFoundException(List.of("No response received from the GameArena API"));
        }
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TeamMinimalViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TeamMinimalViewDecereal::fromTeamMinimalViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }
}
