package hr.algebra.gamearena.webapp.models.cereal.notification;

import com.gamearena.client.model.NotificationEditRequest;

public record NotificationUpdateCereal(
        Boolean read
) {
    public NotificationEditRequest toNotificationEditRequest() {
        var request = new NotificationEditRequest();

        request.setRead(this.read);

        return request;
    }
}
