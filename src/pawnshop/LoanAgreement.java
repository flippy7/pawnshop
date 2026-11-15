package pawnshop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
/** Правила договору та зміни стану; без фінансового друку. */
public class LoanAgreement {
    private String id;
    private Borrower borrower;
    private CollateralItem item;
    private IStolenItemCheck checkService;
    private BigDecimal principal;
    private BigDecimal repaymentAmount;
    private LocalDate dueDate;
    private String status;
    public LoanAgreement(String id, Borrower borrower, CollateralItem item, BigDecimal principal,
                         BigDecimal repaymentAmount, LocalDate dueDate, IStolenItemCheck checkService) {
        if (id == null || id.isEmpty() || id.codePoints().anyMatch(c -> Character.isWhitespace(c) || Character.isSpaceChar(c)))
            throw new IllegalArgumentException("Некоректний ідентифікатор");
        if (borrower == null || item == null || checkService == null) throw new IllegalArgumentException("Залежності договору не задано");
        if (principal == null || repaymentAmount == null || item.getAppraisedValue() == null)
            throw new IllegalArgumentException("Суми або оцінку не задано");
        try {
            principal = principal.setScale(2, RoundingMode.UNNECESSARY);
            repaymentAmount = repaymentAmount.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) { throw new IllegalArgumentException("Частки копійки заборонені"); }
        if (principal.signum() <= 0 || principal.compareTo(item.getAppraisedValue()) > 0 || repaymentAmount.compareTo(principal) < 0)
            throw new IllegalArgumentException("Некоректні суми договору");
        if (dueDate == null || !dueDate.isAfter(LocalDate.of(2026, 11, 15))) throw new IllegalArgumentException("Некоректний початковий строк");
        this.id = id; this.borrower = borrower; this.item = item; this.checkService = checkService;
        this.principal = principal; this.repaymentAmount = repaymentAmount; this.dueDate = dueDate; status = "Draft";
    }
    public void prepare() {
        if (!status.equals("Draft")) throw new IllegalStateException("Оформлення дозволено лише з Draft");
        boolean stolen = checkService.isStolen(item);
        status = stolen ? "Rejected" : "Ready";
        if (stolen) throw new IllegalStateException("Річ викрадена; договір Rejected");
    }
    public void activate() {
        if (!status.equals("Ready")) throw new IllegalStateException("Видача дозволена лише з Ready");
        status = "Active";
    }
    public void redeem(BigDecimal payment) {
        if (!status.equals("Active")) throw new IllegalStateException("Викуп дозволено лише з Active");
        if (payment == null || payment.signum() <= 0) throw new IllegalArgumentException("Платіж має бути додатним");
        try { payment = payment.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException e) { throw new IllegalArgumentException("Частки копійки заборонені"); }
        if (payment.compareTo(repaymentAmount) < 0) throw new IllegalArgumentException("Платіж менше належної суми");
        status = "Redeemed";
    }
    public void extend(LocalDate newDueDate) {
        if (!status.equals("Active")) throw new IllegalStateException("Продовження дозволено лише з Active");
        if (newDueDate == null || !newDueDate.isAfter(dueDate)) throw new IllegalArgumentException("Новий строк має бути пізнішим");
        dueDate = newDueDate;
    }
    // Погоджена деталізація атрибутів, відсутня серед операцій UML №1.
    String getId() { return id; }
    BigDecimal getPrincipal() { return principal; }
    BigDecimal getRepaymentAmount() { return repaymentAmount; }
    LocalDate getDueDate() { return dueDate; }
}
