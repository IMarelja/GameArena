package hr.algebra.gamearena.api.repository.billinginfo;

import hr.algebra.gamearena.api.model.invoice.BillingInfo;
import hr.algebra.gamearena.api.model.invoice.BillingInfoSave;
import hr.algebra.gamearena.api.orm.postgres.invoice.BillingInfoPostgres;
import org.springframework.stereotype.Repository;

@Repository
public class BillingInfoPostgresRepo implements IBillingInfoRepo {

    private final IBillingInfoPostgreSQLRepo billingInfoPostgreSQLRepo;

    public BillingInfoPostgresRepo(IBillingInfoPostgreSQLRepo billingInfoPostgreSQLRepo) {
        this.billingInfoPostgreSQLRepo = billingInfoPostgreSQLRepo;
    }

    @Override
    public BillingInfo createBillingInfo(BillingInfoSave billingInfoSave) {
        var billingInfoPostgres = new BillingInfoPostgres().fromBillingInfoSave(billingInfoSave);
        var saved = billingInfoPostgreSQLRepo.save(billingInfoPostgres);
        return BillingInfo.fromBillingInfoPostgres(saved);
    }
}
