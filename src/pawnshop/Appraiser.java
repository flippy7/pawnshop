package pawnshop;

import java.math.BigDecimal;
/** Визначає, записує та повертає оцінку. */
public class Appraiser extends PawnshopParticipant implements ICollateralAppraisal {
    public Appraiser(String id, String name) { super(id, name); }
    @Override public String getRole() { return "Оцінювач"; }
    @Override public BigDecimal appraise(CollateralItem item) { return appraise(item, new BigDecimal("1000")); }
    @Override public BigDecimal appraise(CollateralItem item, BigDecimal proposedValue) {
        if (item == null) throw new IllegalArgumentException("Річ не задано");
        item.setAppraisedValue(proposedValue);
        System.out.println("Оцінювання речі " + item.getId() + ": " + item.getAppraisedValue() + " грн");
        return item.getAppraisedValue();
    }
}
