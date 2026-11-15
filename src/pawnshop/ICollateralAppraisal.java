package pawnshop;

import java.math.BigDecimal;
/** Вузький контракт оцінювання. */
public interface ICollateralAppraisal {
    BigDecimal appraise(CollateralItem item);
    BigDecimal appraise(CollateralItem item, BigDecimal proposedValue);
}
