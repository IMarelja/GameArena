package hr.algebra.gamearena.webapp.models.cereal.tournament.member;

import com.gamearena.client.model.TournamentMemberView;

public enum TournamentMemberRoleDecereal {
    ORGANIZER,
    PARTICIPANTS,
    DISQUALIFIED;

    public static TournamentMemberRoleDecereal fromTournamentMemberRoleClient(TournamentMemberView.RoleEnum role) {
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
