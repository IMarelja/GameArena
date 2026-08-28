package hr.algebra.gamearena.webapp.models.mvc.data.tournament;

import com.gamearena.client.model.PriceCreateRequest;
import com.gamearena.client.model.PriceEditRequest;

public enum AcceptedCurrencyDataView {
    USD,
    EUR;

    public PriceCreateRequest.CurrencyEnum toPriceCreateClient() {
        return switch (this) {
            case USD -> PriceCreateRequest.CurrencyEnum.USD;
            case EUR -> PriceCreateRequest.CurrencyEnum.EUR;
        };
    }

    public PriceEditRequest.CurrencyEnum toPriceEditClient() {
        return switch (this) {
            case USD -> PriceEditRequest.CurrencyEnum.USD;
            case EUR -> PriceEditRequest.CurrencyEnum.EUR;
        };
    }
}
