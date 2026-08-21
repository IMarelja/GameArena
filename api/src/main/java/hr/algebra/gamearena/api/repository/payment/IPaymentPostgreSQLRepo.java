package hr.algebra.gamearena.api.repository.payment;

import hr.algebra.gamearena.api.orm.postgres.payment.PaymentPostgres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentPostgreSQLRepo extends JpaRepository<PaymentPostgres, Long> {
}
