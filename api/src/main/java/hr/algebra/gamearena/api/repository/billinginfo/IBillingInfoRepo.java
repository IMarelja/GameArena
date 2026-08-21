package hr.algebra.gamearena.api.repository.billinginfo;

import hr.algebra.gamearena.api.model.invoice.BillingInfo;
import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;

public interface IBillingInfoRepo {
    BillingInfo createBillingInfo(BillingInfoSave billingInfoSave);
}
