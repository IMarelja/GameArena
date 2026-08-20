package hr.algebra.gamearena.api.orm.postgres.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import java.time.OffsetDateTime;

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
}
