package hr.algebra.gamearena.api.repository.invoice;

import hr.algebra.gamearena.api.orm.postgres.invoice.InvoicePostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoicePostgreSQLRepo extends JpaRepository<InvoicePostgres, Long> {
}
