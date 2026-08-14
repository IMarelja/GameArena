package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationFullView;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationPushService {
    private final Map<Long, Sinks.Many<List<NotificationFullView>>> sinksByUserId = new ConcurrentHashMap<>();

    public Flux<List<NotificationFullView>> subscribe(Long userId) {
        Sinks.Many<List<NotificationFullView>> sink = sinksByUserId.computeIfAbsent(
                userId, id -> Sinks.many().multicast().onBackpressureBuffer());

        return sink.asFlux()
                .doFinally(
                        signal -> sinksByUserId.computeIfPresent(userId, (id, currentSink) ->
                        currentSink == sink && currentSink.currentSubscriberCount() == 0 ? null : currentSink));
    }

    public void push(Long recipientUserId, List<NotificationFullView> snapshot) {
        Sinks.Many<List<NotificationFullView>> sink = sinksByUserId.get(recipientUserId);
        if (sink != null) {
            sink.tryEmitNext(snapshot);
        }
    }
}
