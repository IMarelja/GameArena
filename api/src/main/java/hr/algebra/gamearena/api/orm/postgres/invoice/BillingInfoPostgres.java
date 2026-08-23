package hr.algebra.gamearena.api.orm.postgres.invoice;

import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "billing_info")
@DynamicInsert
public class BillingInfoPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "country", nullable = false, length = 3)
    private String country;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public BillingInfoPostgres fromBillingInfoSave(BillingInfoSave save) {
        this.fullName = save.getFullName();
        this.email = save.getEmail();
        this.addressLine = save.getAddressLine();
        this.city = save.getCity();
        this.state = StringUtils.hasText(save.getState())
                ? save.getState()
                : null;
        this.zipCode = save.getZipCode();
        this.country = save.getCountry();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
