package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;

public enum TournamentMemberRoleView {
    ORGANIZER,
    PARTICIPANTS,
    DISQUALIFIED;

    public static TournamentMemberRoleView fromTournamentMemberRole(TournamentMemberRole role) {
        return switch (role) {
            case ORGANIZER -> ORGANIZER;
            case PARTICIPANTS -> PARTICIPANTS;
            case DISQUALIFIED -> DISQUALIFIED;
        };
    }
}
