package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;

public enum TournamentMemberRoleEdit {
    ORGANIZER,
    PARTICIPANTS,
    DISQUALIFIED;

    public TournamentMemberRole toTournamentMemberRole() {
        return switch (this) {
            case ORGANIZER -> TournamentMemberRole.ORGANIZER;
            case PARTICIPANTS -> TournamentMemberRole.PARTICIPANTS;
            case DISQUALIFIED -> TournamentMemberRole.DISQUALIFIED;
        };
    }
}
