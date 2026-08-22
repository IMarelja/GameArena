package hr.algebra.gamearena.api.repository.payment;

import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.model.payment.PaymentUpdate;
import hr.algebra.gamearena.api.orm.postgres.payment.PaymentPostgres;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentPostgresRepo implements IPaymentRepo {

    private final IPaymentPostgreSQLRepo paymentPostgreSQLRepo;

    public PaymentPostgresRepo(IPaymentPostgreSQLRepo paymentPostgreSQLRepo) {
        this.paymentPostgreSQLRepo = paymentPostgreSQLRepo;
    }

    @Override
    public Optional<Payment> findPaymentById(Long id) {
        return paymentPostgreSQLRepo.findById(id)
                .map(Payment::fromPaymentPostgres);
    }

    @Override
    public Payment save(PaymentSave paymentSave) {
        var paymentPostgres = new PaymentPostgres().fromPaymentSave(paymentSave);
        var saved = paymentPostgreSQLRepo.save(paymentPostgres);
        return Payment.fromPaymentPostgres(saved);
    }

    @Override
    public Optional<Payment> update(Long id, PaymentUpdate paymentUpdate) {
        return paymentPostgreSQLRepo.findById(id)
                .map(existing -> existing.fromPaymentUpdate(paymentUpdate))
                .map(paymentPostgreSQLRepo::save)
                .map(Payment::fromPaymentPostgres);
    }
}
