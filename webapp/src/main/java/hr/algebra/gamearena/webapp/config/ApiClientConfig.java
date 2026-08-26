package hr.algebra.gamearena.webapp.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gamearena.client.api.AuthenticationControllerApi;
import com.gamearena.client.api.LeaderboardControllerApi;
import com.gamearena.client.api.MatchControllerApi;
import com.gamearena.client.api.TournamentControllerApi;
import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.invoker.ApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.service.jwt.IJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;
import java.util.regex.Pattern;

@Configuration
public class ApiClientConfig {

    private static final Pattern HAS_OFFSET = Pattern.compile(".*(Z|[+-]\\d{2}:?\\d{2})$");

    @Bean
    public ApiClient apiClient(@Value("${gamearena.api.base-url}") String baseUrl) {
        ObjectMapper mapper = ApiClient.createDefaultMapper(null);
        mapper.registerModule(offsetsDateTimeModule());

        ApiClient apiClient = new ApiClient(mapper, ApiClient.createDefaultDateFormat());
        apiClient.setBasePath(baseUrl);
        return apiClient;
    }

    // -- ANONYMOUS --
    // BEGIN
    @Bean
    public TournamentControllerApi tournamentControllerApi(ApiClient apiClient) {
        return new TournamentControllerApi(apiClient);
    }

    @Bean
    public UserControllerApi userControllerApi(ApiClient apiClient) {
        return new UserControllerApi(apiClient);
    }

    @Bean
    public AuthenticationControllerApi authenticationControllerApi(ApiClient apiClient) {
        return new AuthenticationControllerApi(apiClient);
    }
    // END

    // -- AUTHORIZATION --
    // BEGIN
    @Bean
    public AuthenticatedApiClient<TournamentControllerApi> authenticatedTournamentClient(ApiClient apiClient, IJwtService jwtService) {
        return authenticatedClient(apiClient, jwtService, TournamentControllerApi::new);
    }

    @Bean
    public AuthenticatedApiClient<UserControllerApi> authenticatedUserClient(ApiClient apiClient, IJwtService jwtService) {
        return authenticatedClient(apiClient, jwtService, UserControllerApi::new);
    }

    @Bean
    public AuthenticatedApiClient<MatchControllerApi> authenticatedMatchClient(ApiClient apiClient, IJwtService jwtService) {
        return authenticatedClient(apiClient, jwtService, MatchControllerApi::new);
    }

    @Bean
    public AuthenticatedApiClient<LeaderboardControllerApi> authenticatedLeaderboardClient(ApiClient apiClient, IJwtService jwtService) {
        return authenticatedClient(apiClient, jwtService, LeaderboardControllerApi::new);
    }

    private <T> AuthenticatedApiClient<T> authenticatedClient(ApiClient apiClient, IJwtService jwtService, Function<ApiClient, T> apiFactory) {
        return () -> {
            String token = jwtService.getTokenPlainAndValidate();

            ApiClient authenticatedApiClient = new ApiClient(apiClient.getRestClient());
            authenticatedApiClient.setBasePath(apiClient.getBasePath());
            authenticatedApiClient.addDefaultHeader("Authorization", "Bearer " + token);

            return apiFactory.apply(authenticatedApiClient);
        };
    }

    @FunctionalInterface
    public interface AuthenticatedApiClient<T> {
        T get() throws TokenNotFoundException, TokenNotValidException;
    }
    // END

    private SimpleModule offsetsDateTimeModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class, new JsonDeserializer<>() {
            @Override
            public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String text = p.getText();
                return HAS_OFFSET.matcher(text).matches()
                        ? OffsetDateTime.parse(text)
                        : LocalDateTime.parse(text).atOffset(ZoneOffset.UTC);
            }
        });
        return module;
    }
}
