package hr.algebra.gamearena.webapp.service.notification;

import com.gamearena.client.api.NotificationControllerApi;
import com.gamearena.client.model.NotificationEditRequest;
import com.gamearena.streamclient.model.NotificationMinimalView;
import com.gamearena.streamclient.model.NotificationUnreadAndCountView;
import hr.algebra.gamearena.webapp.client.reactive.NotificationReactiveControllerApi;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.service.ApiExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class NotificationRestApiService implements INotificationService {

    private static final long EMITTER_TIMEOUT_MILLIS = Duration.ofMinutes(30).toMillis();

    private final AuthenticatedApiClient<NotificationControllerApi> authenticatedNotificationClient;
    private final AuthenticatedApiClient<NotificationReactiveControllerApi> authenticatedReactiveNotificationClient;

    public NotificationRestApiService(
            AuthenticatedApiClient<NotificationControllerApi> authenticatedNotificationClient,
            AuthenticatedApiClient<NotificationReactiveControllerApi> authenticatedReactiveNotificationClient)
    {
        this.authenticatedNotificationClient = authenticatedNotificationClient;
        this.authenticatedReactiveNotificationClient = authenticatedReactiveNotificationClient;
    }

    @Override
    public void setReadStatus(Long id, boolean read) throws UnauthorizedException, ForbiddenException, NotFoundException {
        NotificationControllerApi client;
        try {
            client = authenticatedNotificationClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to update notifications"));
        }

        try {
            client.updateNotificationWithHttpInfo(id, new NotificationEditRequest().read(read));
        } catch (RestClientResponseException ex) {
            ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public SseEmitter streamUnread() throws UnauthorizedException {
        return relay(client -> client.notificationStreamWithResponseSpec()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<NotificationUnreadAndCountView>>() {}));
    }

    @Override
    public SseEmitter streamAll() throws UnauthorizedException {
        return relay(client -> client.notificationAllStreamWithResponseSpec()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<List<NotificationMinimalView>>>() {}));
    }

    private <T> SseEmitter relay(
            Function<NotificationReactiveControllerApi,
            Flux<ServerSentEvent<T>>> streamFactory
    )
            throws UnauthorizedException {
        NotificationReactiveControllerApi client;
        try {
            client = authenticatedReactiveNotificationClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to receive notifications"));
        }

        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT_MILLIS);
        Disposable subscription = streamFactory.apply(client).subscribe(
                event -> forward(emitter, event),
                emitter::completeWithError,
                emitter::complete);

        emitter.onCompletion(subscription::dispose);
        emitter.onTimeout(subscription::dispose);
        emitter.onError(ignored -> subscription.dispose());

        return emitter;
    }

    private <T> void forward(SseEmitter emitter, ServerSentEvent<T> event) {
        try {
            SseEmitter.SseEventBuilder builder = SseEmitter.event();
            if (event.event() != null) {
                builder.name(event.event());
            }
            if (event.id() != null) {
                builder.id(event.id());
            }
            if (event.data() != null) {
                builder.data(event.data());
            }
            emitter.send(builder);
        } catch (IOException e) {
            log.debug("NotificationRestApiService forward(): failed to relay SSE event - {}", e.getMessage());
            emitter.completeWithError(e);
        }
    }
}
