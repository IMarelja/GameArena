package hr.algebra.gamearena.webapp.service.admin;

import com.gamearena.client.api.AdminControllerApi;
import com.gamearena.client.model.ApiResponseListMatchDetailedFullView;
import com.gamearena.client.model.ApiResponseListTournamentFullView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.admin.MatchQueryCereal;
import hr.algebra.gamearena.webapp.models.cereal.admin.TournamentQueryCereal;
import hr.algebra.gamearena.webapp.models.cereal.match.MatchDetailFullViewDecereal;
import hr.algebra.gamearena.webapp.models.cereal.tournament.TournamentFullViewDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class AdminRestApiService implements IAdminService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final ApiClientConfig.AuthenticatedApiClient<AdminControllerApi> authenticatedAdminControllerApi;

    public AdminRestApiService(ApiClientConfig.AuthenticatedApiClient<AdminControllerApi> authenticatedAdminControllerApi) {
        this.authenticatedAdminControllerApi = authenticatedAdminControllerApi;
    }

    @Override
    public List<TournamentFullViewDecereal> queryTournaments(TournamentQueryCereal queryCereal) throws NotFoundException, UnauthorizedException, ForbiddenException, UnexpectedApiErrorException, BadRequestedExceptions {
        AdminControllerApi client = authenticatedAdminControllerApi.get()
                .orElseThrow(() -> new UnauthorizedException(List.of("You must be logged in to view this page")));

        try {

            var query = TournamentQueryCereal.toMatchQueryDto(queryCereal);

            ResponseEntity<ApiResponseListTournamentFullView> response = client.queryTournamentsWithHttpInfo(query);
            ApiResponseListTournamentFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException(List.of("Failed to fetch active games"));
            }

            return body.getData()
                    .stream()
                    .map(TournamentFullViewDecereal::fromTournamentFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }
    }

    @Override
    public List<MatchDetailFullViewDecereal> queryMatches(MatchQueryCereal queryCereal) throws UnauthorizedException, NotFoundException, ForbiddenException, UnexpectedApiErrorException, BadRequestedExceptions {
        AdminControllerApi client = authenticatedAdminControllerApi.get()
                .orElseThrow(() -> new UnauthorizedException(List.of("You must be logged in to view this page")));

        try {

            var query = MatchQueryCereal.toMatchQueryDto(queryCereal);

            ResponseEntity<ApiResponseListMatchDetailedFullView> response = client.queryMatchesWithHttpInfo(query);
            ApiResponseListMatchDetailedFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new UnexpectedApiErrorException(List.of("Failed to fetch active games"));
            }

            return body.getData()
                    .stream()
                    .map(MatchDetailFullViewDecereal::fromMatchDetailFullViewClient)
                    .toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedBadRequestNotFoundOrForbidden(ex);
        }
    }
}
