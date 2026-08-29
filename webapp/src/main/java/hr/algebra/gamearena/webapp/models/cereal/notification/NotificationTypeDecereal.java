package hr.algebra.gamearena.webapp.models.cereal.notification;


public enum NotificationTypeDecereal {
    CREATED_MATCH,
    TEAM_INVITATION,
    TEAM_INVITATION_RESPONSE,
    TEAM_INVITATION_UPDATE,
    TEST,
    ERROR;

    public static NotificationTypeDecereal fromNotificationTypeClient(com.gamearena.client.model.NotificationMinimalView.TypeEnum type) {
        if(type == null){
            return ERROR;
        }

        return switch (type) {
            case CREATED_MATCH -> CREATED_MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> TEAM_INVITATION_UPDATE;
            case TEST -> TEST;
            default -> ERROR;
        };
    }

    public static NotificationTypeDecereal fromNotificationTypeStreamClient(com.gamearena.streamclient.model.NotificationMinimalView.TypeEnum type) {
        if(type == null){
            return ERROR;
        }

        return switch (type) {
            case CREATED_MATCH -> CREATED_MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
            case TEAM_INVITATION_RESPONSE -> TEAM_INVITATION_RESPONSE;
            case TEAM_INVITATION_UPDATE -> TEAM_INVITATION_UPDATE;
            case TEST -> TEST;
            default -> ERROR;
        };
    }
}
