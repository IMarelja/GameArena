package hr.algebra.gamearena.webapp.client.reactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DateFormat;

public class ApiReactiveClient extends com.gamearena.streamclient.invoker.ApiClient {
    public ApiReactiveClient() {
        super();
    }

    public ApiReactiveClient(WebClient webClient) {
        super(webClient);
    }

    public ApiReactiveClient(ObjectMapper mapper, DateFormat format) {
        super(mapper, format);
    }
}
