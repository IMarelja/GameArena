package hr.algebra.gamearena.api.service.notification;

import hr.algebra.gamearena.api.dto.notification.NotificationFullView;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-user fan-out hub for {@code GET /stream/*}. A sink is created lazily on first subscription
 * and dropped once its last subscriber disconnects - notifications pushed while a user has no
 * open connection are not lost, they are simply picked up on the next connection's catch-up
 * snapshot instead. Carries every notification (read and unread) for the user, so each stream
 * variant in NotificationService can filter/project it down to whatever shape it needs.
 */
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
