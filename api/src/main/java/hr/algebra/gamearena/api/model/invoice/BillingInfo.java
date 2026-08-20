package hr.algebra.gamearena.api.model.invoice;

import hr.algebra.gamearena.api.orm.postgres.invoice.BillingInfoPostgres;

import java.time.LocalDateTime;

public record BillingInfo(
        Long id,
        String fullName,
        String email,
        String addressLine,
        String city,
        String state,
        String zipCode,
        String country,
        LocalDateTime createdAt
) {
    public static BillingInfo fromBillingInfoPostgres(BillingInfoPostgres billingInfoPostgres) {
        return new BillingInfo(
                billingInfoPostgres.getId(),
                billingInfoPostgres.getFullName(),
                billingInfoPostgres.getEmail(),
                billingInfoPostgres.getAddressLine(),
                billingInfoPostgres.getCity(),
                billingInfoPostgres.getState(),
                billingInfoPostgres.getZipCode(),
                billingInfoPostgres.getCountry(),
                billingInfoPostgres.getCreatedAt().toLocalDateTime()
        );
    }
}
