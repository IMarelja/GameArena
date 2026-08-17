package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.model.team.TeamMemberRole;
import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeamMemberPostgreSQLRepo extends JpaRepository<TeamMemberPostgres, Long> {
    long countByTeamId(Long teamId);
    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
    boolean existsByTeamIdAndUserIdAndRole(Long teamId, Long userId, TeamMemberRole role);
    void deleteByTeamIdAndUserId(Long teamId, Long userId);
    List<TeamMemberPostgres> findByTeamId(Long teamId);
    List<TeamMemberPostgres> findByUserId(Long userId);
}
