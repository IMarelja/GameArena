package hr.algebra.gamearena.api.dto.tournament;

import hr.algebra.gamearena.api.dto.payment.PaymentRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentJoinRequest {
    @NotNull(message = "Payment is required")
    PaymentRequest paymentRequest;
    @NotNull(message = "You must select a tournament")
    Long tournamentId;

    // Might add later for more complex shit
    Long teamId;
}
