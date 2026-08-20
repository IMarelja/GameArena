package hr.algebra.gamearena.api.model.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingInfoSave {
    private String fullName;
    private String email;
    private String addressLine;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
