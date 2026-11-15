package pawnshop;

import java.math.BigDecimal;
import java.math.RoundingMode;
/** Дані речі та її оцінка. */
public class CollateralItem {
    private String id;
    private String description;
    private BigDecimal appraisedValue;
    public CollateralItem(String id, String description) {
        if (id == null || id.isEmpty() || id.codePoints().anyMatch(c -> Character.isWhitespace(c) || Character.isSpaceChar(c)))
            throw new IllegalArgumentException("Некоректний ідентифікатор");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Опис не задано");
        this.id = id; this.description = description;
    }
    public void setAppraisedValue(BigDecimal value) {
        if (value == null || value.signum() <= 0) throw new IllegalArgumentException("Оцінка має бути додатною");
        try { value = value.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException e) { throw new IllegalArgumentException("Частки копійки заборонені"); }
        appraisedValue = value;
    }
    public BigDecimal getAppraisedValue() { return appraisedValue; }
    public String getId() { return id; }
}
