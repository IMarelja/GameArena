package hr.algebra.gamearena.webapp.service.notification;

import com.gamearena.client.api.NotificationControllerApi;
import com.gamearena.client.model.NotificationEditRequest;
import com.gamearena.streamclient.model.NotificationMinimalView;
import com.gamearena.streamclient.model.NotificationUnreadCountView;
import hr.algebra.gamearena.webapp.client.reactive.NotificationReactiveControllerApi;
import hr.algebra.gamearena.webapp.config.ApiClientConfig.AuthenticatedApiClient;
import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.TokenNotValidException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationDecereal;
import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationUnreadCountDecereal;
import hr.algebra.gamearena.webapp.models.rest.TypedSseEmitter;
import hr.algebra.gamearena.webapp.models.service.ApiExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class NotificationRestApiService implements INotificationService {

    /**
     * I am actually gonna cry I can't take this anymore
     * */

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

    /*Thank god this was easier*/

    @Override
    public void deleteNotification(Long id) throws UnauthorizedException, ForbiddenException, NotFoundException {
        NotificationControllerApi client;
        try {
            client = authenticatedNotificationClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to delete notifications"));
        }

        try {
            client.deleteNotificationWithHttpInfo(id);
        } catch (RestClientResponseException ex) {
            ApiExceptionMapper.unauthorizedForbiddenOrNotFound(ex);
        }
    }

    @Override
    public TypedSseEmitter<NotificationUnreadCountDecereal> streamUnreadCount() throws UnauthorizedException {
        return relay(
                client -> client.notificationCountStreamWithResponseSpec()
                        .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<NotificationUnreadCountView>>() {}),
                NotificationUnreadCountDecereal::fromNotificationUnreadCountViewClient);
    }

    @Override
    public TypedSseEmitter<List<NotificationDecereal>> streamAll() throws UnauthorizedException {
        return relay(
                client -> client.notificationAllStreamWithResponseSpec()
                        .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<List<NotificationMinimalView>>>() {}),
                views -> views.stream().map(NotificationDecereal::fromNotificationMinimalViewClient).toList());
    }

    private <T, R> TypedSseEmitter<R> relay(
            Function<NotificationReactiveControllerApi, Flux<ServerSentEvent<T>>> streamFactory,
            Function<T, R> toDecereal)
            throws UnauthorizedException {
        NotificationReactiveControllerApi client;
        try {
            client = authenticatedReactiveNotificationClient.get();
        } catch (TokenNotFoundException | TokenNotValidException e) {
            throw new UnauthorizedException(List.of("You must be logged in to receive notifications"));
        }

        TypedSseEmitter<R> emitter = new TypedSseEmitter<>(EMITTER_TIMEOUT_MILLIS);
        Disposable subscription = streamFactory.apply(client).subscribe(
                event -> forward(emitter, event, toDecereal),
                error -> {
                    log.debug("NotificationRestApiService relay(): upstream stream failed - {}", error.getMessage());
                    emitter.complete();
                },
                emitter::complete);

        emitter.onCompletion(subscription::dispose);
        emitter.onTimeout(subscription::dispose);
        emitter.onError(ignored -> subscription.dispose());

        return emitter;
    }

    private <T, R> void forward(TypedSseEmitter<R> emitter, ServerSentEvent<T> event, Function<T, R> toDecereal) {
        try {
            R data = event.data() != null ? toDecereal.apply(event.data()) : null;
            emitter.sendEvent(event.event(), data);
        } catch (IOException e) {
            log.debug("NotificationRestApiService forward(): failed to relay SSE event - {}", e.getMessage());
            emitter.complete();
        }
    }
}
