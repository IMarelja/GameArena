package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.TeamMemberRole;
import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITeamMemberPostgreSQLRepo extends JpaRepository<TeamMemberPostgres, Long> {
    long countByTeamId(Long teamId);
    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
    boolean existsByTeamIdAndUserIdAndRole(Long teamId, Long userId, TeamMemberRole role);
    long countByTeamIdAndRole(Long teamId, TeamMemberRole role);
    void deleteByTeamIdAndUserId(Long teamId, Long userId);
    List<TeamMemberPostgres> findByTeamId(Long teamId);
    List<TeamMemberPostgres> findByUserId(Long userId);
    Optional<TeamMemberPostgres> findByTeamIdAndUserId(Long teamId, Long userId);
}
