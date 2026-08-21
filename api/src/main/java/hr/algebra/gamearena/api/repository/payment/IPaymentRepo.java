package hr.algebra.gamearena.api.repository.payment;

import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.model.payment.PaymentUpdate;

import java.util.Optional;

public interface IPaymentRepo {
    Optional<Payment> findPaymentById(Long id);
    Payment createPayment(PaymentSave paymentSave);
    Optional<Payment> updatePayment(Long id, PaymentUpdate paymentUpdate);
}
