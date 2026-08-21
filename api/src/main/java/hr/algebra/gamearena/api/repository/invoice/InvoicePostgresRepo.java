package hr.algebra.gamearena.api.repository.invoice;

import hr.algebra.gamearena.api.model.invoice.Invoice;
import hr.algebra.gamearena.api.model.invoice.InvoiceSave;
import hr.algebra.gamearena.api.orm.postgres.invoice.InvoicePostgres;
import org.springframework.stereotype.Repository;

@Repository
public class InvoicePostgresRepo implements IInvoiceRepo {

    private final IInvoicePostgreSQLRepo invoicePostgreSQLRepo;

    public InvoicePostgresRepo(IInvoicePostgreSQLRepo invoicePostgreSQLRepo) {
        this.invoicePostgreSQLRepo = invoicePostgreSQLRepo;
    }

    @Override
    public Invoice createInvoice(InvoiceSave invoiceSave) {
        var invoicePostgres = new InvoicePostgres().fromInvoiceSave(invoiceSave);
        var saved = invoicePostgreSQLRepo.save(invoicePostgres);
        return Invoice.fromInvoicePostgres(saved);
    }
}
