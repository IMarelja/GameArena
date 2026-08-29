package hr.algebra.gamearena.webapp.models.rest.notification;

import hr.algebra.gamearena.webapp.models.cereal.notification.NotificationUpdateCereal;

public record NotificationEditDtoRequest(
        Boolean read
) {
    public NotificationUpdateCereal toUpdateCereal() {
        return new NotificationUpdateCereal(this.read);
    }
}
