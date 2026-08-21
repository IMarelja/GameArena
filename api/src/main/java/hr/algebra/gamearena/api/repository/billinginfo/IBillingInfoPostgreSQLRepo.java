package hr.algebra.gamearena.api.repository.billinginfo;

import hr.algebra.gamearena.api.orm.postgres.invoice.BillingInfoPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBillingInfoPostgreSQLRepo extends JpaRepository<BillingInfoPostgres, Long> {
}
