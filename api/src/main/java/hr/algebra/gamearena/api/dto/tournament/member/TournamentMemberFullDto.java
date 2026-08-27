package hr.algebra.gamearena.api.dto.tournament.member;

import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMember;
import hr.algebra.gamearena.api.model.user.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record TournamentMemberFullDto(
    Long memberId,
    Long tournamentId,
    TournamentMemberRoleView role,
    UserViewDto user,
    OffsetDateTime joinedAt,
    Boolean confirmed
) {
    public static TournamentMemberFullDto fromTournamentMemberUser(TournamentMember member, User user) {
        return new TournamentMemberFullDto(
                member.id(),
                member.tournamentId(),
                TournamentMemberRoleView.fromTournamentMemberRole(member.role()),
                UserViewDto.fromUser(user),
                member.joinedAt().atOffset(ZoneOffset.UTC),
                member.confirmed()
        );
    }
}
