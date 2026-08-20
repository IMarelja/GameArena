package hr.algebra.gamearena.api.orm.postgres.invoice;

import hr.algebra.gamearena.api.model.invoice.InvoiceSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "invoices")
@DynamicInsert
public class InvoicePostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "billing_info_id", nullable = false, unique = true)
    private Long billingInfoId;

    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public InvoicePostgres fromInvoiceSave(InvoiceSave save) {
        this.userId = save.getUserId();
        this.billingInfoId = save.getBillingInfoId();
        this.paymentId = save.getPaymentId();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
