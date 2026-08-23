package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserFullViewDto;
import hr.algebra.gamearena.api.dto.user.UserSuspendRequest;
import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.exceptions.extenders.ConflictException;
import hr.algebra.gamearena.api.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.api.model.tournament.member.TournamentMemberRole;
import hr.algebra.gamearena.api.model.team.TeamMemberRole;
import hr.algebra.gamearena.api.model.user.UserActiveStatusUpdate;
import hr.algebra.gamearena.api.model.user.UserSelfDeleteUpdate;
import hr.algebra.gamearena.api.repository.team.ITeamRepo;
import hr.algebra.gamearena.api.repository.tournament.ITournamentRepo;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import hr.algebra.gamearena.api.utils.RandomUtilities;
import hr.algebra.gamearena.api.utils.SecurityUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    private final IUserRepo userRepo;
    private final ITournamentRepo tournamentRepo;
    private final ITeamRepo teamRepo;

    public UserService(IUserRepo userRepo, ITournamentRepo tournamentRepo, ITeamRepo teamRepo) {
        this.userRepo = userRepo;
        this.tournamentRepo = tournamentRepo;
        this.teamRepo = teamRepo;
    }

    @Override
    public boolean isActiveAndNotDeleted(Long id) {
        return userRepo.existsByIdAndIsActiveAndNotDeleted(id);
    }

    @Override
    public List<UserViewDto> findAll() {
        log.info("UserService findAll(): Fetching all users from the database...");

        var users = this.userRepo.findAll()
                .stream()
                .map(UserViewDto::fromUser)
                .toList();

        log.info("UserService findAll(): All users have been fetched from the database.");
        return users;

    }

    @Override
    public Optional<UserViewDto> findById(Long id) {
        return this.userRepo.findById(id)
                .map(UserViewDto::fromUser);
    }

    @Override
    public Optional<UserFullViewDto> findFullInfoById(Long id) {
        return this.userRepo.findById(id)
                .map(UserFullViewDto::fromUser);
    }

    @Override
    public void deleteMyAccount(Long callerId) {
        userRepo.findById(callerId)
                .orElseThrow(() -> new NotFoundException("This user no longer exists"));

        validateNoUnhandedOrganizerResponsibilities(callerId);
        validateNoUnhandedCaptainResponsibilities(callerId);

        var scrambleToken = RandomUtilities.randomHex(16);
        var scrambledSalt = SecurityUtilities.saltForPassword();

        var update = new UserSelfDeleteUpdate();
        update.setUsername("deleted_user_" + scrambleToken);
        update.setEmail("deleted_" + scrambleToken + "@deleted.invalid");
        update.setPasswordSalt(scrambledSalt);
        update.setPasswordHash(SecurityUtilities.hashPasswordWithSalt(RandomUtilities.randomHex(32), scrambledSalt));

        userRepo.selfDelete(callerId, update);
    }

    private void validateNoUnhandedOrganizerResponsibilities(Long userId) {
        for (var membership : tournamentRepo.getAllTournamentMembersForUser(userId)) {
            if (membership.role() != TournamentMemberRole.ORGANIZER) {
                continue;
            }

            if (tournamentRepo.countTournamentMembersByRole(membership.tournamentId(), TournamentMemberRole.ORGANIZER) <= 1) {
                throw new ConflictException("You are the only organizer of a tournament. "
                        + "Assign someone else as organizer before deleting your account");
            }
        }
    }

    private void validateNoUnhandedCaptainResponsibilities(Long userId) {
        for (var team : teamRepo.getTeamsForUser(userId)) {
            if (!teamRepo.isUserTeamCaptain(userId, team.id())) {
                continue;
            }

            if (teamRepo.countTeamMembersByRole(team.id(), TeamMemberRole.CAPTAIN) <= 1) {
                throw new ConflictException("You are the only captain of a team. "
                        + "Assign someone else as captain before deleting your account");
            }
        }
    }

    @Override
    public UserFullViewDto suspendAccount(UserSuspendRequest request) {
        var update = new UserActiveStatusUpdate();
        update.setIsActive(request.getIsActive());

        return userRepo.updateActiveStatus(request.getUserId(), update)
                .map(UserFullViewDto::fromUser)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId()));
    }
}
