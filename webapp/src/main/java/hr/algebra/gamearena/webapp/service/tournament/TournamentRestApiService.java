package hr.algebra.gamearena.webapp.service.tournament;

import com.gamearena.client.api.TournamentControllerApi;
import com.gamearena.client.model.ApiResponseListTournamentFullView;
import hr.algebra.gamearena.webapp.models.service.ApiResult;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentRestApiService implements ITournamentService {

    private final TournamentControllerApi tournamentControllerApi;

    public TournamentRestApiService(TournamentControllerApi tournamentControllerApi) {
        this.tournamentControllerApi = tournamentControllerApi;
    }

    @Override
    public ApiResult<List<TournamentFullViewDecereal>> getAllTournaments() {
        ResponseEntity<ApiResponseListTournamentFullView> response = tournamentControllerApi.getTournamentsWithHttpInfo();
        ApiResponseListTournamentFullView body = response.getBody();
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode().value());

        List<TournamentFullViewDecereal> data = body.getData() == null
                ? null
                : body.getData().stream().map(TournamentFullViewDecereal::fromTournamentFullViewClient).toList();

        return ApiResult.fromApiResponseClient(status, data, body.getErrors());
    }
}
