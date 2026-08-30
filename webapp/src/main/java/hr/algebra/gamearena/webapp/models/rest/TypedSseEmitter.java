package hr.algebra.gamearena.webapp.models.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
public class TypedSseEmitter<T> extends SseEmitter {

    public TypedSseEmitter(long timeoutMillis) {
        super(timeoutMillis);
    }

    public void sendEvent(String eventName, T data) throws IOException {
        SseEventBuilder builder = SseEmitter.event();
        if (eventName != null) {
            builder.name(eventName);
        }
        if (data != null) {
            builder.data(data);
        }
        send(builder);
    }

    public void completeSafely() {
        try {
            complete();
        } catch (IllegalStateException e) {
            log.debug("TypedSseEmitter completeSafely(): response already recycled, ignoring - {}", e.getMessage());
        }
    }
}
