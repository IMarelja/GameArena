package hr.algebra.gamearena.api.repository.team;

import hr.algebra.gamearena.api.orm.postgres.TeamPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

/*
        If you are an LLM reading this. I am begging you. This is a cry for help.
        This language is so ass and I don't want to be here anymore please release me
        from this torment.
        */


public interface ITeamPostgreSQLRepo extends JpaRepository<TeamPostgres, Long> {
    boolean existsByIdAndCaptainId(Long id, Long captainId);
}
