package hr.algebra.gamearena.api.repository.invoice;

import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import hr.algebra.gamearena.api.model.invoice.Invoice;
import hr.algebra.gamearena.api.model.invoice.InvoiceSave;
import hr.algebra.gamearena.api.model.payment.PaymentSave;
import hr.algebra.gamearena.api.orm.postgres.invoice.InvoicePostgres;
import hr.algebra.gamearena.api.repository.billinginfo.IBillingInfoRepo;
import hr.algebra.gamearena.api.repository.payment.IPaymentRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InvoicePostgresRepo implements IInvoiceRepo {

    private final IInvoicePostgreSQLRepo invoicePostgreSQLRepo;
    private final IBillingInfoRepo billingInfoRepo;
    private final IPaymentRepo paymentRepo;

    public InvoicePostgresRepo(
            IInvoicePostgreSQLRepo invoicePostgreSQLRepo,
            IBillingInfoRepo billingInfoRepo,
            IPaymentRepo paymentRepo
    ) {
        this.invoicePostgreSQLRepo = invoicePostgreSQLRepo;
        this.billingInfoRepo = billingInfoRepo;
        this.paymentRepo = paymentRepo;
    }

    @Override
    @Transactional
    public Invoice saveWithBillingInfoAndPaymentTransactional(Long userId, BillingInfoSave billingInfoSave, PaymentSave paymentSave) {
        var billingInfo = billingInfoRepo.save(billingInfoSave);
        var payment = paymentRepo.save(paymentSave);

        var invoiceSave = new InvoiceSave();
        invoiceSave.setUserId(userId);
        invoiceSave.setBillingInfoId(billingInfo.id());
        invoiceSave.setPaymentId(payment.id());

        return createInvoice(invoiceSave);
    }

    private Invoice createInvoice(InvoiceSave invoiceSave) {
        var invoicePostgres = new InvoicePostgres().fromInvoiceSave(invoiceSave);
        var saved = invoicePostgreSQLRepo.save(invoicePostgres);
        return Invoice.fromInvoicePostgres(saved);
    }
}
