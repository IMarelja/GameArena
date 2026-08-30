package hr.algebra.gamearena.webapp.service.loginlog;

import com.gamearena.client.api.LoggingControllerApi;
import com.gamearena.client.model.ApiResponseListLoginLogsFullView;
import hr.algebra.gamearena.webapp.config.ApiClientConfig;
import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.loginog.LoginLogDecereal;
import hr.algebra.gamearena.webapp.service.ApiExceptionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class LogRestApiService implements ILogService {

    private static final String NO_RESPONSE_RECEIVED_API = "No response received from the GameArena API";

    private final ApiClientConfig.AuthenticatedApiClient<LoggingControllerApi> authenticatedLogApiClient;

    public LogRestApiService(ApiClientConfig.AuthenticatedApiClient<LoggingControllerApi> authenticatedLogApiClient) {
        this.authenticatedLogApiClient = authenticatedLogApiClient;
    }

    @Override
    public List<LoginLogDecereal> getLoginLogs() throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException {
        LoggingControllerApi client = authenticatedLogApiClient.get()
                .orElseThrow(() -> new UnauthorizedException(List.of("You must be logged in to view this page")));

        try {
            ResponseEntity<ApiResponseListLoginLogsFullView> response = client.getLoggedTournamentsWithHttpInfo();
            ApiResponseListLoginLogsFullView body = response.getBody();
            if (body == null) {
                throw new NotFoundException(List.of(NO_RESPONSE_RECEIVED_API));
            }

            if (body.getData() == null) {
                throw new NotFoundException(List.of("Failed to fetch login logs"));
            }

            return body.getData().stream().map(LoginLogDecereal::fromLoginLogsFullViewClient).toList();
        } catch (RestClientResponseException ex) {
            return ApiExceptionMapper.unauthorizedOrForbidden(ex);
        }
    }
}
