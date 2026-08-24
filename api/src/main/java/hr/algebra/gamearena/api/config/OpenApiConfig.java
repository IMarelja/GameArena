package hr.algebra.gamearena.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gameArenaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GameArena API")
                        .description("REST API for the GameArena esports tournament platform")
                        .version("0.5.0"));
    }
}
