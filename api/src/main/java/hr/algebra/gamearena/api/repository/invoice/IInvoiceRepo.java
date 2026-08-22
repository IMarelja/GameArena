package hr.algebra.gamearena.api.repository.invoice;

import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import hr.algebra.gamearena.api.model.invoice.Invoice;
import hr.algebra.gamearena.api.model.invoice.InvoiceSave;
import hr.algebra.gamearena.api.model.payment.Payment;
import hr.algebra.gamearena.api.model.payment.PaymentSave;

public interface IInvoiceRepo {
    // Transaction
    Invoice saveWithBillingInfoAndPaymentTransactional(Long userId, BillingInfoSave billingInfoSave, PaymentSave paymentSave);
}
