package hr.algebra.gamearena.api.dto.notification;

import hr.algebra.gamearena.api.model.notification.ReferenceType;

public enum ReferenceTypeView {
    MATCH,
    TEAM_INVITATION;

    public static ReferenceTypeView fromReferenceType(ReferenceType referenceType) {
        if (referenceType == null) {
            return null;
        }

        return switch (referenceType) {
            case MATCH -> MATCH;
            case TEAM_INVITATION -> TEAM_INVITATION;
        };
    }

    public ReferenceType toReferenceType() {
        return switch (this) {
            case MATCH -> ReferenceType.MATCH;
            case TEAM_INVITATION -> ReferenceType.TEAM_INVITATION;
        };
    }
}
