package pawnshop;

import java.math.BigDecimal;
import java.time.LocalDate;
/** Подає річ та ініціює запити через касира. */
public class Borrower extends PawnshopParticipant {
    public Borrower(String id, String name) { super(id, name); }
    @Override public String getRole() { return "Позичальник"; }
    public void submitItem(CollateralItem item) {
        if (item == null) throw new IllegalArgumentException("Річ не задано");
        System.out.println("Позичальник " + getId() + ": подано річ " + item.getId());
    }
    public void requestRedemption(Cashier cashier, LoanAgreement agreement, BigDecimal payment) {
        if (cashier == null) throw new IllegalArgumentException("Касира не задано");
        cashier.redeemItem(agreement, payment);
    }
    public void requestExtension(Cashier cashier, LoanAgreement agreement, LocalDate newDueDate) {
        if (cashier == null) throw new IllegalArgumentException("Касира не задано");
        cashier.extendAgreement(agreement, newDueDate);
    }
}
