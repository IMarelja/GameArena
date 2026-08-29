package hr.algebra.gamearena.webapp.models.cereal.notification;

import java.util.Optional;

public enum NotificationReferenceTypeDecereal {
    MATCH,
    TEAM_INVITATION;

    public static Optional<NotificationReferenceTypeDecereal> fromReferenceTypeClient(com.gamearena.client.model.NotificationMinimalView.ReferenceTypeEnum typeEnum){
        if(typeEnum == null){
            return Optional.empty();
        }

        return switch (typeEnum) {
            case MATCH -> Optional.of(MATCH);
            case TEAM_INVITATION -> Optional.of(TEAM_INVITATION);
            default -> Optional.empty();
        };
    }

    public static Optional<NotificationReferenceTypeDecereal> fromReferenceTypeSteamClient(com.gamearena.streamclient.model.NotificationMinimalView.ReferenceTypeEnum typeEnum){
        if(typeEnum == null){
            return Optional.empty();
        }

        return switch (typeEnum) {
            case MATCH -> Optional.of(MATCH);
            case TEAM_INVITATION -> Optional.of(TEAM_INVITATION);
            default -> Optional.empty();
        };
    }
}
