package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.TeamMemberPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeamMemberPostgreSQLRepo extends JpaRepository<TeamMemberPostgres, Long> {
    long countByTeamId(Long teamId);
    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
    void deleteByTeamIdAndUserId(Long teamId, Long userId);
    List<TeamMemberPostgres> findByTeamId(Long teamId);
}
