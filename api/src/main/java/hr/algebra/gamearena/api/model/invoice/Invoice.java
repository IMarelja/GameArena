package hr.algebra.gamearena.api.model.invoice;

import hr.algebra.gamearena.api.orm.postgres.invoice.InvoicePostgres;

import java.time.LocalDateTime;

public record Invoice(
        Long id,
        Long userId,
        Long billingInfoId,
        Long paymentId,
        LocalDateTime createdAt
) {
    public static Invoice fromInvoicePostgres(InvoicePostgres invoicePostgres) {
        return new Invoice(
                invoicePostgres.getId(),
                invoicePostgres.getUserId(),
                invoicePostgres.getBillingInfoId(),
                invoicePostgres.getPaymentId(),
                invoicePostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
