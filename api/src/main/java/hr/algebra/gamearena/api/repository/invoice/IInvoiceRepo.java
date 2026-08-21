package hr.algebra.gamearena.api.repository.invoice;

import hr.algebra.gamearena.api.model.invoice.Invoice;
import hr.algebra.gamearena.api.model.invoice.InvoiceSave;

public interface IInvoiceRepo {
    Invoice createInvoice(InvoiceSave invoiceSave);
}
