package hr.algebra.gamearena.api.dto.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDetails {
    @NotBlank(message = "Your full name is required")
    private String fullName;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    @NotBlank(message = "Zip code required")
    @Length(max = 30, min = 1)
    private String zipCode;

    @NotBlank(message = "Country code is required")
    @Length(max = 3, min = 3)
    private String countryCode;
}
