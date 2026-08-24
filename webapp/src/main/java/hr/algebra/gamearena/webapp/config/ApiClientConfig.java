package hr.algebra.gamearena.webapp.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gamearena.client.api.TournamentControllerApi;
import com.gamearena.client.api.UserControllerApi;
import com.gamearena.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

@Configuration
public class ApiClientConfig {

    private static final Pattern HAS_OFFSET = Pattern.compile(".*(Z|[+-]\\d{2}:?\\d{2})$");

    @Bean
    public ApiClient apiClient(@Value("${gamearena.api.base-url}") String baseUrl) {
        ObjectMapper mapper = ApiClient.createDefaultMapper(null);
        mapper.registerModule(offsetlessDateTimeModule());

        ApiClient apiClient = new ApiClient(mapper, ApiClient.createDefaultDateFormat());
        apiClient.setBasePath(baseUrl);
        return apiClient;
    }

    @Bean
    public TournamentControllerApi tournamentControllerApi(ApiClient apiClient) {
        return new TournamentControllerApi(apiClient);
    }

    @Bean
    public UserControllerApi userControllerApi(ApiClient apiClient) {
        return new UserControllerApi(apiClient);
    }

    /**
     * The api emits some timestamps (e.g. TournamentFullView, UserViewDto) as
     * LocalDateTime, with no offset, while the generated client types every
     * OpenAPI date-time field as OffsetDateTime. Treat an offset-less timestamp
     * as UTC instead of failing to parse.
     */
    private SimpleModule offsetlessDateTimeModule() {
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
