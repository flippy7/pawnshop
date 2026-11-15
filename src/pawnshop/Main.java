package pawnshop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/** Складальник сценаріїв і локальні перевірки навчальних інваріантів. */
public class Main {
    private Set<String> createdItemIds = new HashSet<>();
    private Set<String> assignedItemIds = new HashSet<>();
    private Borrower borrower = new Borrower("B1", "Олена");
    private Appraiser appraiser = new Appraiser("A1", "Іван");
    private Cashier cashier = new Cashier("C1", "Марія", appraiser);
    private int serial;
    private int checks;

    public static void main(String[] args) {
        try {
            if (args.length > 1 || (args.length == 1 && !Set.of("all", "verify", "fault-check").contains(args[0]))) {
                System.err.println("Допустимі режими: all, verify, fault-check; не більше одного аргументу");
                System.exit(2);
                return;
            }
            Main demonstration = new Main();
            String mode = args.length == 0 ? "all" : args[0];
            if (mode.equals("fault-check")) {
                System.out.println("Контроль неочікуваної помилки");
                throw new IllegalStateException("Контрольована аварія");
            }
            if (mode.equals("verify")) demonstration.verify();
            else demonstration.demonstrate();
        } catch (RuntimeException | AssertionError e) {
            System.err.println("Неочікувана помилка: " + e.getMessage());
            System.exit(1);
        }
    }

    private BigDecimal money(String value) { return new BigDecimal(value); }
    private LocalDate date(String value) { return LocalDate.parse(value); }
    private CollateralItem createItem(String id, String description) {
        if (createdItemIds.contains(id)) throw new IllegalStateException("Повторний id речі");
        CollateralItem item = new CollateralItem(id, description);
        createdItemIds.add(id);
        return item;
    }
    private LoanAgreement createAgreement(CollateralItem item, BigDecimal principal,
            BigDecimal repayment, LocalDate due, IStolenItemCheck service) {
        if (item != null && assignedItemIds.contains(item.getId())) throw new IllegalStateException("Річ уже передано договору");
        LoanAgreement agreement = new LoanAgreement("L" + (++serial), borrower, item, principal, repayment, due, service);
        assignedItemIds.add(item.getId());
        return agreement;
    }
    private LoanAgreement fresh(String state) {
        CollateralItem item = createItem("I" + (++serial), "Годинник");
        borrower.submitItem(item);
        cashier.appraiseItem(item);
        LoanAgreement agreement = createAgreement(item, money("800"), money("840"), date("2026-12-15"),
                new StolenItemCheckService(state.equals("Rejected") ? Set.of(item.getId()) : Set.of()));
        if (state.equals("Rejected")) {
            expect(IllegalStateException.class, "Річ викрадена", () -> cashier.prepareAgreement(agreement));
        } else if (!state.equals("Draft")) {
            cashier.prepareAgreement(agreement);
            if (!state.equals("Ready")) cashier.issueFunds(agreement);
            if (state.equals("Redeemed")) borrower.requestRedemption(cashier, agreement, money("840"));
        }
        check(field(agreement, "status").equals(state), "Початковий стан " + state);
        return agreement;
    }
    private void demonstrate() {
        for (PawnshopParticipant participant : List.of(borrower, appraiser, cashier))
            System.out.println("Учасник " + participant.getId() + ": " + participant.getRole());
        System.out.println("Маршрут 1: оформлення та викуп");
        LoanAgreement direct = fresh("Active");
        borrower.requestRedemption(cashier, direct, money("840"));
        check(field(direct, "status").equals("Redeemed"), "Безпосередній викуп");
        System.out.println("Маршрут 2: два продовження та викуп");
        LoanAgreement extended = fresh("Active");
        borrower.requestExtension(cashier, extended, date("2027-01-15"));
        borrower.requestExtension(cashier, extended, date("2027-02-15"));
        check(extended.getRepaymentAmount().compareTo(money("840")) == 0, "Належна сума незмінна");
        borrower.requestRedemption(cashier, extended, money("840"));
        System.out.println("Маршрут 3: відмова через викрадення");
        fresh("Rejected");
        System.out.println("Маршрут 4: некоректні дії");
        LoanAgreement invalid = fresh("Draft");
        unchanged(invalid, IllegalStateException.class, "Видача", () -> cashier.issueFunds(invalid));
        cashier.prepareAgreement(invalid); cashier.issueFunds(invalid);
        unchanged(invalid, IllegalStateException.class, "Видача", () -> cashier.issueFunds(invalid));
        unchanged(invalid, IllegalArgumentException.class, "Платіж менше", () -> borrower.requestRedemption(cashier, invalid, money("839")));
        unchanged(invalid, IllegalArgumentException.class, "Новий строк", () -> borrower.requestExtension(cashier, invalid, date("2026-12-15")));
        unchanged(invalid, IllegalArgumentException.class, "Новий строк", () -> borrower.requestExtension(cashier, invalid, date("2026-12-14")));
        borrower.requestRedemption(cashier, invalid, money("840"));
        unchanged(invalid, IllegalStateException.class, "Викуп", () -> borrower.requestRedemption(cashier, invalid, money("840")));
        System.out.println("Демонстрацію завершено успішно: 4 маршрути");
    }
    private void check(boolean condition, String label) {
        if (!condition) throw new IllegalStateException("Перевірка не пройдена: " + label);
        checks++;
        System.out.println("Перевірка " + checks + ": " + label + " — успішно");
    }
    private void expect(Class<? extends RuntimeException> type, String reason, Runnable action) {
        try { action.run(); }
        catch (RuntimeException e) {
            if (e.getClass() != type || !e.getMessage().contains(reason)) throw e;
            check(true, "Очікувана відмова: " + e.getMessage());
            return;
        }
        throw new IllegalStateException("Очікуваної відмови немає: " + reason);
    }
    // Reflection застосовується лише до перевірок; предметні операції її не використовують.
    private Object field(Object target, String name) {
        try {
            Field f = target.getClass().getDeclaredField(name); f.setAccessible(true); return f.get(target);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException("Не вдалося прочитати контрольний стан", e); }
    }
    private List<Object> snapshot(LoanAgreement agreement) {
        List<Object> values = new ArrayList<>();
        for (String name : List.of("id", "borrower", "item", "checkService", "principal", "repaymentAmount", "dueDate", "status"))
            values.add(field(agreement, name));
        return values;
    }
    private String capture(Runnable action) {
        PrintStream previous = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream redirected = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(redirected); action.run();
        } finally { System.setOut(previous); }
        String output = bytes.toString(StandardCharsets.UTF_8);
        System.out.print(output);
        return output;
    }
    private void unchanged(LoanAgreement agreement, Class<? extends RuntimeException> type, String reason, Runnable action) {
        List<Object> before = snapshot(agreement);
        String output = capture(() -> expect(type, reason, action));
        check(before.equals(snapshot(agreement)), "Дані й стан після відмови незмінні");
        check(!output.contains(": видано ") && !output.contains("погашення виконано") && !output.contains(": повернено "), "Після відмови фінансових імітацій немає");
    }
    private long occurrences(String text, String part) { return text.split(java.util.regex.Pattern.quote(part), -1).length - 1; }

    private void verify() {
        verifyItems();
        verifyAgreements();
        verifyRedemption();
        verifyExtension();
        verifyStates();
        String output = capture(this::demonstrate);
        check(occurrences(output, "Маршрут ") == 4, "Чотири маршрути");
        check(occurrences(output, ": видано ") == 3 && occurrences(output, ": повернено ") == 3
                && occurrences(output, "погашення виконано") == 3, "По одній фінансовій імітації на успішний цикл");
        System.out.println("Усі перевірки успішні: " + checks);
    }
    private void verifyItems() {
        for (String invalid : new String[]{null, "", " ", "a b", "a\tb", "a\u00a0b"})
            expect(IllegalArgumentException.class, "ідентифікатор", () -> createItem(invalid, "Опис"));
        for (String invalid : new String[]{null, "", "   "})
            expect(IllegalArgumentException.class, "Опис", () -> createItem("D" + (++serial), invalid));
        CollateralItem item = createItem("Exact", "Коштовність");
        createItem("exact", "Інша коштовність");
        expect(IllegalStateException.class, "Повторний id", () -> createItem("Exact", "Дублікат"));
        check(item.getAppraisedValue() == null, "Початкова оцінка відсутня");
        check(cashier.appraiseItem(item).compareTo(money("1000")) == 0, "Базова оцінка 1000");
        for (String valid : List.of("840", "840.01", "840.000"))
            check(appraiser.appraise(item, money(valid)).compareTo(money(valid)) == 0, "Явна точна оцінка " + valid);
        for (BigDecimal invalid : new BigDecimal[]{null, money("0"), money("-1"), money("840.005")}) {
            BigDecimal before = item.getAppraisedValue();
            String reason = invalid != null && invalid.signum() > 0 ? "Частки копійки" : "Оцінка";
            expect(IllegalArgumentException.class, reason, () -> appraiser.appraise(item, invalid));
            check(before.equals(item.getAppraisedValue()), "Невдале оцінювання зберігає оцінку");
            expect(IllegalArgumentException.class, reason, () -> item.setAppraisedValue(invalid));
            check(before.equals(item.getAppraisedValue()), "Невдалий setter зберігає оцінку");
        }
        expect(IllegalArgumentException.class, "Річ", () -> appraiser.appraise(null));
        expect(IllegalArgumentException.class, "Річ", () -> appraiser.appraise(null, money("1")));
        expect(IllegalArgumentException.class, "Річ", () -> borrower.submitItem(null));
        expect(IllegalArgumentException.class, "Оцінювання", () -> new Cashier("C2", "Касир", null));
        expect(IllegalArgumentException.class, "Набір", () -> new StolenItemCheckService(null));
        Set<String> ids = new HashSet<>();
        StolenItemCheckService service = new StolenItemCheckService(ids); ids.add(item.getId());
        check(!service.isStolen(item), "Захисна копія набору");
        expect(IllegalArgumentException.class, "Річ", () -> service.isStolen(null));
        ICollateralAppraisal alternative = new ICollateralAppraisal() {
            @Override public BigDecimal appraise(CollateralItem i) { return appraise(i, money("900")); }
            @Override public BigDecimal appraise(CollateralItem i, BigDecimal value) { i.setAppraisedValue(value); return i.getAppraisedValue(); }
        };
        CollateralItem alternativeItem = createItem("Alternative", "Контроль DI");
        check(new Cashier("OtherCashier", "Касир", alternative).appraiseItem(alternativeItem).compareTo(money("900")) == 0,
                "Заміна оцінювання через DI без зміни касира");
        try {
            Set<String> getters = new HashSet<>();
            for (Method method : LoanAgreement.class.getDeclaredMethods()) {
                if (method.getName().startsWith("get")) {
                    getters.add(method.getName());
                    check((method.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE)) == 0,
                            "Пакетна видимість " + method.getName());
                }
            }
            check(getters.equals(Set.of("getId", "getPrincipal", "getRepaymentAmount", "getDueDate")), "Лише чотири погоджені getters");
        } catch (SecurityException e) { throw new IllegalStateException("Не вдалося перевірити структуру", e); }
        for (PawnshopParticipant role : List.of(borrower, appraiser, cashier)) check(!role.getRole().isBlank(), "Поліморфна роль " + role.getRole());
        try { check(Modifier.isFinal(PawnshopParticipant.class.getMethod("getId").getModifiers()), "final getId"); }
        catch (ReflectiveOperationException e) { throw new IllegalStateException("Контракт ідентифікації", e); }
    }
    private void verifyAgreements() {
        CollateralItem item = createItem("Terms", "Річ");
        IStolenItemCheck service = new StolenItemCheckService(Set.of());
        expect(IllegalArgumentException.class, "Суми або оцінку", () -> createAgreement(item, money("800"), money("840"), date("2026-12-15"), service));
        cashier.appraiseItem(item);
        for (BigDecimal p : new BigDecimal[]{null, money("0"), money("-1"), money("1000.01"), money("800.005")})
            expect(IllegalArgumentException.class, p == null ? "Суми" : p.scale() == 3 ? "Частки" : "Некоректні суми", () -> createAgreement(item, p, money("1100"), date("2026-12-15"), service));
        for (BigDecimal repayment : new BigDecimal[]{null, money("799"), money("-1"), money("840.005")})
            expect(IllegalArgumentException.class, repayment == null ? "Суми" : repayment.scale() == 3 ? "Частки" : "Некоректні суми", () -> createAgreement(item, money("800"), repayment, date("2026-12-15"), service));
        for (LocalDate due : new LocalDate[]{null, date("2026-11-15"), date("2026-11-14")})
            expect(IllegalArgumentException.class, "початковий строк", () -> createAgreement(item, money("800"), money("840"), due, service));
        expect(IllegalArgumentException.class, "Залежності", () -> createAgreement(null, money("800"), money("840"), date("2026-12-15"), service));
        expect(IllegalArgumentException.class, "Залежності", () -> createAgreement(item, money("800"), money("840"), date("2026-12-15"), null));
        expect(IllegalArgumentException.class, "Залежності", () -> new LoanAgreement("NullBorrower", null, item, money("800"), money("840"), date("2026-12-15"), service));
        LoanAgreement agreement = createAgreement(item, money("1000"), money("1000"), date("2026-12-15"), service);
        check(field(agreement, "status").equals("Draft"), "Межі principal=оцінка і repayment=principal");
        unchanged(agreement, IllegalStateException.class, "Річ уже", () -> createAgreement(item, money("800"), money("840"), date("2026-12-15"), service));
        CollateralItem duplicate = new CollateralItem("Terms", "Пряма контрольна копія");
        unchanged(agreement, IllegalStateException.class, "Річ уже", () -> createAgreement(duplicate, money("800"), money("840"), date("2026-12-15"), service));
        for (String amount : List.of("800", "800.01", "800.000")) {
            CollateralItem i = createItem("Precision" + (++serial), "Річ"); cashier.appraiseItem(i);
            LoanAgreement a = createAgreement(i, money(amount), money(amount), date("2026-12-15"), service);
            check(a.getPrincipal().compareTo(money(amount)) == 0 && a.getRepaymentAmount().compareTo(money(amount)) == 0, "Точність позики й належної суми " + amount);
        }
        CollateralItem pending = createItem("Pending", "Річ"); cashier.appraiseItem(pending);
        IStolenItemCheck interrupted = new IStolenItemCheck() {
            @Override public boolean isStolen(CollateralItem i) { throw new IllegalStateException("Контрольована незавершена перевірка"); }
        };
        LoanAgreement draft = createAgreement(pending, money("800"), money("840"), date("2026-12-15"), interrupted);
        unchanged(draft, IllegalStateException.class, "Контрольована незавершена", () -> cashier.prepareAgreement(draft));
        unchanged(draft, IllegalStateException.class, "Видача", () -> cashier.issueFunds(draft));
    }
    private void verifyRedemption() {
        for (BigDecimal payment : new BigDecimal[]{null, money("0"), money("-1"), money("839"), money("840.005")}) {
            LoanAgreement a = fresh("Active");
            String reason = payment == null || payment.signum() <= 0 ? "Платіж має" : payment.scale() == 3 ? "Частки" : "Платіж менше";
            unchanged(a, IllegalArgumentException.class, reason, () -> borrower.requestRedemption(cashier, a, payment));
            unchanged(a, IllegalArgumentException.class, reason, () -> borrower.requestRedemption(cashier, a, payment));
        }
        for (String value : List.of("840", "841", "840.01", "840.000")) {
            LoanAgreement a = fresh("Active"); Object item = field(a, "item");
            String output = capture(() -> borrower.requestRedemption(cashier, a, money(value)));
            check(field(a, "status").equals("Redeemed") && field(a, "item") == item, "Викуп без знищення речі " + value);
            check(output.contains("належить 840.00") && output.contains(money(value).setScale(2).toString()), "Запропонована й належна суми");
            check(occurrences(output, "погашення виконано") == 1 && occurrences(output, ": повернено ") == 1, "Одне погашення й повернення");
        }
    }
    private void verifyExtension() {
        LoanAgreement a = fresh("Active");
        for (LocalDate due : new LocalDate[]{null, date("2026-12-15"), date("2026-12-14")})
            unchanged(a, IllegalArgumentException.class, "Новий строк", () -> borrower.requestExtension(cashier, a, due));
        BigDecimal principal = a.getPrincipal(), repayment = a.getRepaymentAmount();
        String output = capture(() -> {
            borrower.requestExtension(cashier, a, date("2026-12-16"));
            check(a.getDueDate().equals(date("2026-12-16")), "Продовження на один день");
            borrower.requestExtension(cashier, a, date("2027-01-15"));
            borrower.requestExtension(cashier, a, date("2027-02-15"));
        });
        check(a.getDueDate().equals(date("2027-02-15")) && field(a, "status").equals("Active"), "Повторне продовження");
        check(principal.equals(a.getPrincipal()) && repayment.equals(a.getRepaymentAmount()) && !output.contains(": видано "), "Продовження змінює лише строк");
        borrower.requestRedemption(cashier, a, money("840"));
    }
    private void verifyStates() {
        for (String state : List.of("Draft", "Ready", "Active", "Rejected", "Redeemed")) {
            LoanAgreement a = fresh(state);
            if (!state.equals("Draft")) unchanged(a, IllegalStateException.class, "Оформлення", () -> cashier.prepareAgreement(a));
            if (!state.equals("Ready")) unchanged(a, IllegalStateException.class, "Видача", () -> cashier.issueFunds(a));
            if (!state.equals("Active")) {
                unchanged(a, IllegalStateException.class, "Викуп", () -> borrower.requestRedemption(cashier, a, money("840")));
                unchanged(a, IllegalStateException.class, "Продовження", () -> borrower.requestExtension(cashier, a, date("2027-01-15")));
            }
        }
        expect(IllegalArgumentException.class, "Касира", () -> borrower.requestRedemption(null, null, money("840")));
        expect(IllegalArgumentException.class, "Касира", () -> borrower.requestExtension(null, null, date("2027-01-15")));
        expect(IllegalArgumentException.class, "Договір", () -> cashier.prepareAgreement(null));
        expect(IllegalArgumentException.class, "Договір", () -> cashier.issueFunds(null));
        expect(IllegalArgumentException.class, "Договір", () -> cashier.redeemItem(null, money("840")));
        expect(IllegalArgumentException.class, "Договір", () -> cashier.extendAgreement(null, date("2027-01-15")));
    }
}
