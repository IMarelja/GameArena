package hr.algebra.gamearena.api.dto.tournament.price;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceEditRequest {
    @NotNull(message = "The solo price is required")
    @Digits(integer = 12, fraction = 2, message = "The solo price must have at most 12 digits and 2 decimal places")
    private BigDecimal soloPrice;

    @NotNull(message = "The group price is required")
    @Digits(integer = 12, fraction = 2, message = "The group price must have at most 12 digits and 2 decimal places")
    private BigDecimal groupPrice;

    @NotNull(message = "The currency is required")
    private AcceptedCurrencyEnumRequest currency;
}
