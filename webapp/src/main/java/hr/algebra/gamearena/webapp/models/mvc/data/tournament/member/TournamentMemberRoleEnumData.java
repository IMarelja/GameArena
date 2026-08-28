package hr.algebra.gamearena.webapp.models.mvc.data.tournament.member;

import hr.algebra.gamearena.webapp.models.cereal.tournament.member.TournamentMemberRoleDecereal;

public enum TournamentMemberRoleEnumData {
    ORGANIZER,
    PARTICIPANTS,
    DISQUALIFIED;

    public static TournamentMemberRoleEnumData from(TournamentMemberRoleDecereal role) {
        return switch (role) {
            case ORGANIZER -> ORGANIZER;
            case PARTICIPANTS -> PARTICIPANTS;
            case DISQUALIFIED -> DISQUALIFIED;
        };
    }

    public String badgeClass() {
        return switch (this) {
            case ORGANIZER -> "text-bg-primary";
            case PARTICIPANTS -> "text-bg-secondary";
            case DISQUALIFIED -> "text-bg-danger";
        };
    }
}
