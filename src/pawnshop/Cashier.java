package pawnshop;

import java.math.BigDecimal;
import java.time.LocalDate;
/** Координує правила й імітує зовнішні дії лише після успіху. */
public class Cashier extends PawnshopParticipant {
    private ICollateralAppraisal appraisal;
    public Cashier(String id, String name, ICollateralAppraisal appraisal) {
        super(id, name);
        if (appraisal == null) throw new IllegalArgumentException("Оцінювання не задано");
        this.appraisal = appraisal;
    }
    @Override public String getRole() { return "Касир"; }
    public BigDecimal appraiseItem(CollateralItem item) { return appraisal.appraise(item); }
    public void prepareAgreement(LoanAgreement agreement) {
        if (agreement == null) throw new IllegalArgumentException("Договір не задано");
        agreement.prepare();
        System.out.println("Договір " + agreement.getId() + ": оформлено, Ready");
    }
    public void issueFunds(LoanAgreement agreement) {
        if (agreement == null) throw new IllegalArgumentException("Договір не задано");
        agreement.activate();
        System.out.println("Договір " + agreement.getId() + ": видано " + agreement.getPrincipal() + " грн, Active");
    }
    public void redeemItem(LoanAgreement agreement, BigDecimal payment) {
        if (agreement == null) throw new IllegalArgumentException("Договір не задано");
        agreement.redeem(payment);
        System.out.println("Договір " + agreement.getId() + ": запропоновано " + payment.setScale(2) + " грн, належить " + agreement.getRepaymentAmount() + " грн; погашення виконано");
        System.out.println("Договір " + agreement.getId() + ": повернено річ, Redeemed");
    }
    public void extendAgreement(LoanAgreement agreement, LocalDate newDueDate) {
        if (agreement == null) throw new IllegalArgumentException("Договір не задано");
        agreement.extend(newDueDate);
        System.out.println("Договір " + agreement.getId() + ": продовжено до " + agreement.getDueDate());
    }
}
