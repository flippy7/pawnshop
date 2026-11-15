# Двосторонній аудит прототипу «Ломбард»

**Дата**: 2026-11-15. Докази: [validation.md](validation.md), [results.json](logs/results.json), [javap](logs/javap.stdout.log), [verify](logs/verify.stdout.log), [all](logs/all.stdout.log).

Погоджені відмінності I2/N1 відокремлено; буквальна тотожність переліків операцій №1 і коду не заявляється. Усі адреси Java нижче відносні до кореня репозиторію.

## Модель → код: кожен UML-член

| Елемент | Місце й декларація коду | Вимога / статус |
| --- | --- | --- |
| docs/class.mmd:5 `String id` | src/pawnshop/PawnshopParticipant.java:5 `private String id;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:6 `String name` | src/pawnshop/PawnshopParticipant.java:6 `private String name;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:7 `PawnshopParticipant(String id, String name)` | src/pawnshop/PawnshopParticipant.java:7 `protected PawnshopParticipant(String id, String name) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:8 `getId() String` | src/pawnshop/PawnshopParticipant.java:13 `public final String getId() { return id; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:9 `getRole() String*` | src/pawnshop/PawnshopParticipant.java:14 `public abstract String getRole();` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:12 `Borrower(String id, String name)` | src/pawnshop/Borrower.java:7 `public Borrower(String id, String name) { super(id, name); }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:13 `getRole() String` | src/pawnshop/Borrower.java:8 `@Override public String getRole() { return "Позичальник"; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:14 `submitItem(CollateralItem item) void` | src/pawnshop/Borrower.java:9 `public void submitItem(CollateralItem item) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:15 `requestRedemption(Cashier cashier, LoanAgreement agreement, BigDecimal payment) void` | src/pawnshop/Borrower.java:13 `public void requestRedemption(Cashier cashier, LoanAgreement agreement, BigDecimal payment) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:16 `requestExtension(Cashier cashier, LoanAgreement agreement, LocalDate newDueDate) void` | src/pawnshop/Borrower.java:17 `public void requestExtension(Cashier cashier, LoanAgreement agreement, LocalDate newDueDate) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:19 `Appraiser(String id, String name)` | src/pawnshop/Appraiser.java:6 `public Appraiser(String id, String name) { super(id, name); }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:20 `getRole() String` | src/pawnshop/Appraiser.java:7 `@Override public String getRole() { return "Оцінювач"; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:21 `appraise(CollateralItem item) BigDecimal` | src/pawnshop/Appraiser.java:8 `@Override public BigDecimal appraise(CollateralItem item) { return appraise(item, new BigDecimal("1000")); }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:22 `appraise(CollateralItem item, BigDecimal proposedValue) BigDecimal` | src/pawnshop/Appraiser.java:9 `@Override public BigDecimal appraise(CollateralItem item, BigDecimal proposedValue) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:25 `ICollateralAppraisal appraisal` | src/pawnshop/Cashier.java:7 `private ICollateralAppraisal appraisal;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:26 `Cashier(String id, String name, ICollateralAppraisal appraisal)` | src/pawnshop/Cashier.java:8 `public Cashier(String id, String name, ICollateralAppraisal appraisal) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:27 `getRole() String` | src/pawnshop/Cashier.java:13 `@Override public String getRole() { return "Касир"; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:28 `appraiseItem(CollateralItem item) BigDecimal` | src/pawnshop/Cashier.java:14 `public BigDecimal appraiseItem(CollateralItem item) { return appraisal.appraise(item); }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:29 `prepareAgreement(LoanAgreement agreement) void` | src/pawnshop/Cashier.java:15 `public void prepareAgreement(LoanAgreement agreement) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:30 `issueFunds(LoanAgreement agreement) void` | src/pawnshop/Cashier.java:20 `public void issueFunds(LoanAgreement agreement) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:31 `redeemItem(LoanAgreement agreement, BigDecimal payment) void` | src/pawnshop/Cashier.java:25 `public void redeemItem(LoanAgreement agreement, BigDecimal payment) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:32 `extendAgreement(LoanAgreement agreement, LocalDate newDueDate) void` | src/pawnshop/Cashier.java:31 `public void extendAgreement(LoanAgreement agreement, LocalDate newDueDate) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:35 `String id` | src/pawnshop/CollateralItem.java:7 `private String id;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:36 `String description` | src/pawnshop/CollateralItem.java:8 `private String description;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:37 `BigDecimal appraisedValue` | src/pawnshop/CollateralItem.java:9 `private BigDecimal appraisedValue;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:38 `CollateralItem(String id, String description)` | src/pawnshop/CollateralItem.java:10 `public CollateralItem(String id, String description) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:39 `setAppraisedValue(BigDecimal value) void` | src/pawnshop/CollateralItem.java:16 `public void setAppraisedValue(BigDecimal value) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:40 `getAppraisedValue() BigDecimal` | src/pawnshop/CollateralItem.java:22 `public BigDecimal getAppraisedValue() { return appraisedValue; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:41 `getId() String` | src/pawnshop/CollateralItem.java:23 `public String getId() { return id; }` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:44 `String id` | src/pawnshop/LoanAgreement.java:8 `private String id;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:45 `Borrower borrower` | src/pawnshop/LoanAgreement.java:9 `private Borrower borrower;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:46 `CollateralItem item` | src/pawnshop/LoanAgreement.java:10 `private CollateralItem item;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:47 `IStolenItemCheck checkService` | src/pawnshop/LoanAgreement.java:11 `private IStolenItemCheck checkService;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:48 `BigDecimal principal` | src/pawnshop/LoanAgreement.java:12 `private BigDecimal principal;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:49 `BigDecimal repaymentAmount` | src/pawnshop/LoanAgreement.java:13 `private BigDecimal repaymentAmount;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:50 `LocalDate dueDate` | src/pawnshop/LoanAgreement.java:14 `private LocalDate dueDate;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:51 `String status` | src/pawnshop/LoanAgreement.java:15 `private String status;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:52 `LoanAgreement(String id, Borrower borrower, CollateralItem item, BigDecimal principal, BigDecimal repaymentAmount, LocalDate dueDate, IStolenItemCheck checkService)` | src/pawnshop/LoanAgreement.java:16 `public LoanAgreement(String id, Borrower borrower, CollateralItem item, BigDecimal principal,                          BigDecimal repaymentAmount, LocalDate dueDate, IStolenItemCheck checkService) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:53 `prepare() void` | src/pawnshop/LoanAgreement.java:33 `public void prepare() {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:54 `activate() void` | src/pawnshop/LoanAgreement.java:39 `public void activate() {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:55 `redeem(BigDecimal payment) void` | src/pawnshop/LoanAgreement.java:43 `public void redeem(BigDecimal payment) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:56 `extend(LocalDate newDueDate) void` | src/pawnshop/LoanAgreement.java:51 `public void extend(LocalDate newDueDate) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:59 `Set~String~ stolenItemIds` | src/pawnshop/StolenItemCheckService.java:7 `private Set<String> stolenItemIds;` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:60 `StolenItemCheckService(Set~String~ stolenItemIds)` | src/pawnshop/StolenItemCheckService.java:8 `public StolenItemCheckService(Set<String> stolenItemIds) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:61 `isStolen(CollateralItem item) boolean` | src/pawnshop/StolenItemCheckService.java:12 `@Override public boolean isStolen(CollateralItem item) {` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:65 `appraise(CollateralItem item) BigDecimal` | src/pawnshop/ICollateralAppraisal.java:6 `BigDecimal appraise(CollateralItem item);` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:66 `appraise(CollateralItem item, BigDecimal proposedValue) BigDecimal` | src/pawnshop/ICollateralAppraisal.java:7 `BigDecimal appraise(CollateralItem item, BigDecimal proposedValue);` | FR-016; підтверджено декларацією та javap |
| docs/class.mmd:70 `isStolen(CollateralItem item) boolean` | src/pawnshop/IStolenItemCheck.java:4 `public interface IStolenItemCheck { boolean isStolen(CollateralItem item); }` | FR-016; підтверджено декларацією та javap |

## Код → модель: декларації й службові члени

| Місце | Декларація | Відповідник / статус |
| --- | --- | --- |
| src/pawnshop/Appraiser.java:5 | `public class Appraiser extends PawnshopParticipant implements ICollateralAppraisal {` | class: Appraiser; CRC Appraiser!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/Appraiser.java:6 | `public Appraiser(String id, String name) { super(id, name); }` | class: Appraiser; CRC Appraiser!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/Appraiser.java:7 | `@Override public String getRole() { return "Оцінювач"; }` | class: Appraiser; CRC Appraiser!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/Appraiser.java:8 | `@Override public BigDecimal appraise(CollateralItem item) { return appraise(item, new BigDecimal("1000")); }` | class: Appraiser; CRC Appraiser!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/Appraiser.java:9 | `@Override public BigDecimal appraise(CollateralItem item, BigDecimal proposedValue) {` | class: Appraiser; CRC Appraiser!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:6 | `public class Borrower extends PawnshopParticipant {` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:7 | `public Borrower(String id, String name) { super(id, name); }` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:8 | `@Override public String getRole() { return "Позичальник"; }` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:9 | `public void submitItem(CollateralItem item) {` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:13 | `public void requestRedemption(Cashier cashier, LoanAgreement agreement, BigDecimal payment) {` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Borrower.java:17 | `public void requestExtension(Cashier cashier, LoanAgreement agreement, LocalDate newDueDate) {` | class: Borrower; CRC Borrower!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:6 | `public class Cashier extends PawnshopParticipant {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:7 | `private ICollateralAppraisal appraisal;` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:8 | `public Cashier(String id, String name, ICollateralAppraisal appraisal) {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:13 | `@Override public String getRole() { return "Касир"; }` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:14 | `public BigDecimal appraiseItem(CollateralItem item) { return appraisal.appraise(item); }` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:15 | `public void prepareAgreement(LoanAgreement agreement) {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:20 | `public void issueFunds(LoanAgreement agreement) {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:25 | `public void redeemItem(LoanAgreement agreement, BigDecimal payment) {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/Cashier.java:31 | `public void extendAgreement(LoanAgreement agreement, LocalDate newDueDate) {` | class: Cashier; CRC Cashier!A3:B7; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:6 | `public class CollateralItem {` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:7 | `private String id;` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:8 | `private String description;` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:9 | `private BigDecimal appraisedValue;` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:10 | `public CollateralItem(String id, String description) {` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:16 | `public void setAppraisedValue(BigDecimal value) {` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:22 | `public BigDecimal getAppraisedValue() { return appraisedValue; }` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/CollateralItem.java:23 | `public String getId() { return id; }` | class: CollateralItem; CRC CollateralItem!A3:B6; FR-016/017; підтверджено |
| src/pawnshop/ICollateralAppraisal.java:5 | `public interface ICollateralAppraisal {` | class: ICollateralAppraisal; CRC ICollateralAppraisal!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/ICollateralAppraisal.java:6 | `BigDecimal appraise(CollateralItem item);` | class: ICollateralAppraisal; CRC ICollateralAppraisal!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/ICollateralAppraisal.java:7 | `BigDecimal appraise(CollateralItem item, BigDecimal proposedValue);` | class: ICollateralAppraisal; CRC ICollateralAppraisal!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/IStolenItemCheck.java:4 | `public interface IStolenItemCheck { boolean isStolen(CollateralItem item); }` | class: IStolenItemCheck; CRC IStolenItemCheck!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:7 | `public class LoanAgreement {` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:8 | `private String id;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:9 | `private Borrower borrower;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:10 | `private CollateralItem item;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:11 | `private IStolenItemCheck checkService;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:12 | `private BigDecimal principal;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:13 | `private BigDecimal repaymentAmount;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:14 | `private LocalDate dueDate;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:15 | `private String status;` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:16 | `public LoanAgreement(String id, Borrower borrower, CollateralItem item, BigDecimal principal,` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:33 | `public void prepare() {` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:39 | `public void activate() {` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:43 | `public void redeem(BigDecimal payment) {` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:51 | `public void extend(LocalDate newDueDate) {` | class: LoanAgreement; CRC LoanAgreement!A3:B8; FR-016/017; підтверджено |
| src/pawnshop/LoanAgreement.java:57 | `String getId() { return id; }` | I2: однойменний атрибут class; CRC LoanAgreement!A3, Cashier!A7; FR-010/016; підтверджено |
| src/pawnshop/LoanAgreement.java:58 | `BigDecimal getPrincipal() { return principal; }` | I2: однойменний атрибут class; CRC LoanAgreement!A3, Cashier!A7; FR-010/016; підтверджено |
| src/pawnshop/LoanAgreement.java:59 | `BigDecimal getRepaymentAmount() { return repaymentAmount; }` | I2: однойменний атрибут class; CRC LoanAgreement!A3, Cashier!A7; FR-010/016; підтверджено |
| src/pawnshop/LoanAgreement.java:60 | `LocalDate getDueDate() { return dueDate; }` | I2: однойменний атрибут class; CRC LoanAgreement!A3, Cashier!A7; FR-010/016; підтверджено |
| src/pawnshop/Main.java:11 | `public class Main {` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:12 | `private Set<String> createdItemIds = new HashSet<>();` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:13 | `private Set<String> assignedItemIds = new HashSet<>();` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:14 | `private Borrower borrower = new Borrower("B1", "Олена");` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:15 | `private Appraiser appraiser = new Appraiser("A1", "Іван");` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:16 | `private Cashier cashier = new Cashier("C1", "Марія", appraiser);` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:17 | `private int serial;` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:18 | `private int checks;` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:20 | `public static void main(String[] args) {` | sequence: складальник; FR-011/015; підтверджено |
| src/pawnshop/Main.java:41 | `private BigDecimal money(String value) { return new BigDecimal(value); }` | class: BigDecimal; FR-004; підтверджено |
| src/pawnshop/Main.java:42 | `private LocalDate date(String value) { return LocalDate.parse(value); }` | class: LocalDate; FR-008; підтверджено |
| src/pawnshop/Main.java:43 | `private CollateralItem createItem(String id, String description) {` | class: примітка композиції; FR-018; підтверджено |
| src/pawnshop/Main.java:49 | `private LoanAgreement createAgreement(CollateralItem item, BigDecimal principal,` | sequence: створення договору; FR-004/018; підтверджено |
| src/pawnshop/Main.java:56 | `private LoanAgreement fresh(String state) {` | sequence: передумови й альтернативи; FR-011; підтверджено |
| src/pawnshop/Main.java:72 | `private void demonstrate() {` | usecase: сім операцій; sequence: alt/loop; FR-011; підтверджено |
| src/pawnshop/Main.java:99 | `private void check(boolean condition, String label) {` | state: умови переходів; SC-002; підтверджено |
| src/pawnshop/Main.java:104 | `private void expect(Class<? extends RuntimeException> type, String reason, Runnable action) {` | sequence: очікувані відмови; FR-009; підтверджено |
| src/pawnshop/Main.java:114 | `private Object field(Object target, String name) {` | state: контроль стану; research §7; підтверджено |
| src/pawnshop/Main.java:119 | `private List<Object> snapshot(LoanAgreement agreement) {` | state: незмінність даних; SC-002; підтверджено |
| src/pawnshop/Main.java:125 | `private String capture(Runnable action) {` | class: фінансові повідомлення після успіху; SC-003; підтверджено |
| src/pawnshop/Main.java:135 | `private void unchanged(LoanAgreement agreement, Class<? extends RuntimeException> type, String reason, Runnable action) {` | state: заборони; FR-009; підтверджено |
| src/pawnshop/Main.java:141 | `private long occurrences(String text, String part) { return text.split(java.util.regex.Pattern.quote(part), -1).length - 1; }` | sequence: одноразові фінансові дії; SC-003; підтверджено |
| src/pawnshop/Main.java:143 | `private void verify() {` | state: усі умови; SC-002; підтверджено |
| src/pawnshop/Main.java:155 | `private void verifyItems() {` | class/CRC Appraiser, CollateralItem; FR-002/003/004/018; підтверджено |
| src/pawnshop/Main.java:185 | `@Override public BigDecimal appraise(CollateralItem i) { return appraise(i, money("900")); }` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:186 | `@Override public BigDecimal appraise(CollateralItem i, BigDecimal value) { i.setAppraisedValue(value); return i.getAppraisedValue(); }` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:206 | `private void verifyAgreements() {` | class/CRC LoanAgreement; FR-004/005/018; підтверджено |
| src/pawnshop/Main.java:232 | `@Override public boolean isStolen(CollateralItem i) { throw new IllegalStateException("Контрольована незавершена перевірка"); }` | class/sequence: технічний складальник, залежності й набори id; FR-011/018; підтверджено |
| src/pawnshop/Main.java:238 | `private void verifyRedemption() {` | state: Active → Redeemed; FR-007; підтверджено |
| src/pawnshop/Main.java:253 | `private void verifyExtension() {` | state: Active → Active; FR-008; підтверджено |
| src/pawnshop/Main.java:268 | `private void verifyStates() {` | state: п’ять станів, заборони; FR-009; підтверджено |
| src/pawnshop/PawnshopParticipant.java:4 | `public abstract class PawnshopParticipant {` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/PawnshopParticipant.java:5 | `private String id;` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/PawnshopParticipant.java:6 | `private String name;` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/PawnshopParticipant.java:7 | `protected PawnshopParticipant(String id, String name) {` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/PawnshopParticipant.java:13 | `public final String getId() { return id; }` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/PawnshopParticipant.java:14 | `public abstract String getRole();` | class: PawnshopParticipant; CRC PawnshopParticipant!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/StolenItemCheckService.java:6 | `public class StolenItemCheckService implements IStolenItemCheck {` | class: StolenItemCheckService; CRC StolenItemCheckService!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/StolenItemCheckService.java:7 | `private Set<String> stolenItemIds;` | class: StolenItemCheckService; CRC StolenItemCheckService!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/StolenItemCheckService.java:8 | `public StolenItemCheckService(Set<String> stolenItemIds) {` | class: StolenItemCheckService; CRC StolenItemCheckService!A3:B5; FR-016/017; підтверджено |
| src/pawnshop/StolenItemCheckService.java:12 | `@Override public boolean isStolen(CollateralItem item) {` | class: StolenItemCheckService; CRC StolenItemCheckService!A3:B5; FR-016/017; підтверджено |

Синтетичні lambda$-методи javap є компіляцією Runnable-перевірок, не новими предметними операціями. Неявний конструктор Main складає локальні залежності. Локальні змінні, оператори та виклики JDK належать обов’язкам відповідного методу: BigDecimal — суми/копійки, LocalDate — строки, колекції — локальний пошук/тотожність, reflection та потоки — лише контроль станів/імітацій. Винятки відповідають заборонам state/sequence. Анонімні IStolenItemCheck та ICollateralAppraisal відповідають незавершеній перевірці та заміні DI; вони не є виробничими сутностями.

## CRC: кожна непорожня відповідальність і співпраця

| Аркуш!Клітинка / зміст | Код | Статус / доказ |
| --- | --- | --- |
| PawnshopParticipant!A3: Абстрактний клас: зберігати спільні id та name учасника. Захищений конструктор ініціалізує нащадків. | src/pawnshop/PawnshopParticipant.java:4 | підтверджено; FR-013/017, декларації, all/verify |
| PawnshopParticipant!B3: Borrower — нащадок, успадковує ідентифікацію та перевизначає getRole. | src/pawnshop/PawnshopParticipant.java:4, Borrower.java | підтверджено; FR-013/017, декларації, all/verify |
| PawnshopParticipant!A4: final getId(): String повертає стабільний ідентифікатор і не допускає перевизначення. | src/pawnshop/PawnshopParticipant.java:13 | підтверджено; FR-013/017, декларації, all/verify |
| PawnshopParticipant!B4: Appraiser — нащадок, успадковує ідентифікацію та перевизначає getRole. | src/pawnshop/PawnshopParticipant.java:4, Appraiser.java | підтверджено; FR-013/017, декларації, all/verify |
| PawnshopParticipant!A5: abstract getRole(): String визначає спільний контракт ролі. LSP: нащадки виконують його без додаткових передумов. | src/pawnshop/PawnshopParticipant.java:14 | підтверджено; FR-013/017, декларації, all/verify |
| PawnshopParticipant!B5: Cashier — нащадок, успадковує ідентифікацію та перевизначає getRole. | src/pawnshop/PawnshopParticipant.java:4, Cashier.java | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!A3: Представляти позичальника; конструктор Borrower(id, name), override getRole(): String. | src/pawnshop/Borrower.java:8 | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!B3: PawnshopParticipant — успадковані id, name та final getId. | src/pawnshop/Borrower.java:6, PawnshopParticipant.java | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!A4: submitItem(item) — подавати річ із непорожнім описом для оцінювання. | src/pawnshop/Borrower.java:9 | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!B4: CollateralItem — параметр подання речі. | src/pawnshop/Borrower.java:6, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!A5: requestRedemption(cashier, agreement, payment) — ініціювати викуп через касира. | src/pawnshop/Borrower.java:13 | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!B5: Cashier — отримувач запитів на викуп і продовження. | src/pawnshop/Borrower.java:6, Cashier.java | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!A6: requestExtension(cashier, agreement, newDueDate) — ініціювати продовження через касира. | src/pawnshop/Borrower.java:17 | підтверджено; FR-013/017, декларації, all/verify |
| Borrower!B6: LoanAgreement — договір у параметрах запитів; асоціація договору з позичальником. | src/pawnshop/Borrower.java:6, LoanAgreement.java | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!A3: Визначати й записувати заставну вартість речі. Конструктор Appraiser(id, name), override getRole(): String. | src/pawnshop/Appraiser.java:7 | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!B3: PawnshopParticipant — наслідування ідентифікації та контракту ролі. | src/pawnshop/Appraiser.java:5, PawnshopParticipant.java | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!A4: appraise(item): BigDecimal — визначати навчальну базову оцінку, записувати й повертати її. | src/pawnshop/Appraiser.java:8 | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!B4: ICollateralAppraisal — реалізація обох сигнатур вузького контракту оцінювання. | src/pawnshop/Appraiser.java:5, ICollateralAppraisal.java | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!A5: appraise(item, proposedValue): BigDecimal — перевантажений метод для явно поданої додатної оцінки; записувати й повертати її. | src/pawnshop/Appraiser.java:8 | підтверджено; FR-013/017, декларації, all/verify |
| Appraiser!B5: CollateralItem — отримувач setAppraisedValue; недодатна оцінка відхиляється. | src/pawnshop/Appraiser.java:5, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!A3: Координувати операції. Cashier(id, name, appraisal) отримує ICollateralAppraisal ззовні; override getRole(): String. | src/pawnshop/Cashier.java:13, ICollateralAppraisal.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!B3: PawnshopParticipant — наслідування ідентифікації та контракту ролі. | src/pawnshop/Cashier.java:6, PawnshopParticipant.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!A4: appraiseItem(item) — делегувати оцінювання контракту. OCP і DIP: заміна реалізації не змінює касира. | src/pawnshop/Cashier.java:14 | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!B4: ICollateralAppraisal — асоціація appraisal, DI через конструктор; виклик appraise. | src/pawnshop/Cashier.java:6, ICollateralAppraisal.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!A5: prepareAgreement(agreement) — делегувати prepare; issueFunds(agreement) — делегувати activate. | src/pawnshop/Cashier.java:15, src/pawnshop/Cashier.java:20 | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!B5: CollateralItem — параметр оцінювання. | src/pawnshop/Cashier.java:6, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!A6: redeemItem(agreement, payment) та extendAgreement(agreement, newDueDate) — делегувати правила договору, не дублювати їх. | src/pawnshop/Cashier.java:25, src/pawnshop/Cashier.java:31 | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!B6: LoanAgreement — отримувач prepare, activate, redeem, extend. | src/pawnshop/Cashier.java:6, LoanAgreement.java | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!A7: Після успішних операцій виводити консольні імітації видачі, погашення та повернення речі. Після відмови зовнішню дію не імітувати. | src/pawnshop/Cashier.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| Cashier!B7: Borrower — ініціатор запитів на викуп і продовження (вхідна залежність). | src/pawnshop/Cashier.java:6, Borrower.java | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!A3: Зберігати id, непорожній description та appraisedValue. Конструктор CollateralItem(id, description). | src/pawnshop/CollateralItem.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!B3: LoanAgreement — композиційний власник: одна річ у договорі, 0..1 власника речі. | src/pawnshop/CollateralItem.java:6, LoanAgreement.java | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!A4: setAppraisedValue(value) — приймати лише додатну заставну вартість. | src/pawnshop/CollateralItem.java:16 | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!B4: Borrower — подає річ; Cashier — передає її на оцінювання (вхідні залежності). | src/pawnshop/CollateralItem.java:6, Borrower.java, Cashier.java | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!A5: getAppraisedValue(): BigDecimal та getId(): String — надавати оцінку й ідентифікатор. | src/pawnshop/CollateralItem.java:22, src/pawnshop/CollateralItem.java:23 | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!B5: Appraiser та ICollateralAppraisal — запис оцінки й параметр контракту оцінювання. | src/pawnshop/CollateralItem.java:6, Appraiser.java, ICollateralAppraisal.java | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!A6: Бути об’єктом застави не більш ніж одного договору. Повернення речі не означає фізичне знищення. | src/pawnshop/CollateralItem.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| CollateralItem!B6: StolenItemCheckService та IStolenItemCheck — локальна перевірка і параметр її контракту. | src/pawnshop/CollateralItem.java:6, IStolenItemCheck.java, StolenItemCheckService.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A3: Зберігати умови та стан. Конструктор отримує id, borrower, item, principal, repaymentAmount, dueDate, checkService. DI через IStolenItemCheck. | src/pawnshop/LoanAgreement.java:7, IStolenItemCheck.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B3: Borrower — один позичальник договору; позичальник може мати 0..* договорів. | src/pawnshop/LoanAgreement.java:7, Borrower.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A4: Перевіряти оцінку, 0 < principal ≤ оцінки, repaymentAmount ≥ principal, строк і залежності. Початковий статус Draft. | src/pawnshop/LoanAgreement.java:7 | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B4: CollateralItem — одна складова композиції; складальник сценарію забороняє подвійного власника. | src/pawnshop/LoanAgreement.java:7, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A5: prepare() — перевіряти річ через isStolen: false переводить у Ready, true — у Rejected без виплати. | src/pawnshop/LoanAgreement.java:33 | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B5: IStolenItemCheck — асоціація checkService, передана конструктором, виклик isStolen до видачі. | src/pawnshop/LoanAgreement.java:7, IStolenItemCheck.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A6: activate() — лише з Ready переводити в Active, блокувати повторну видачу. | src/pawnshop/LoanAgreement.java:39 | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B6: StolenItemCheckService — лише опосередкована співпраця через IStolenItemCheck, без прямого створення чи залежності від конкретного класу. | src/pawnshop/LoanAgreement.java:7, IStolenItemCheck.java, StolenItemCheckService.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A7: redeem(payment) — лише в Active за повного погашення переводити в Redeemed; extend(newDueDate) — залишати Active з пізнішим строком. | src/pawnshop/LoanAgreement.java:43, src/pawnshop/LoanAgreement.java:51 | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B7: Cashier — викликає операції договору й імітує фінансові дії після їх успіху (вхідна залежність). | src/pawnshop/LoanAgreement.java:7, Cashier.java | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!A8: Відхиляти неповний платіж, неправильний строк і дії після завершення без зміни стану. SRP: фінансові консольні повідомлення залишати касиру. | src/pawnshop/LoanAgreement.java:7 | підтверджено; FR-013/017, декларації, all/verify |
| LoanAgreement!B8: Borrower — передає договір у запитах на викуп і продовження (вхідна залежність). | src/pawnshop/LoanAgreement.java:7, Borrower.java | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!A3: Виконувати навчальну локальну перевірку. Конструктор StolenItemCheckService(stolenItemIds) отримує набір ідентифікаторів. | src/pawnshop/StolenItemCheckService.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!B3: IStolenItemCheck — реалізація вузького контракту перевірки. | src/pawnshop/StolenItemCheckService.java:6, IStolenItemCheck.java | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!A4: isStolen(item): boolean — перевіряти getId() речі у stolenItemIds та повертати демонстраційний результат. | src/pawnshop/StolenItemCheckService.java:12, src/pawnshop/StolenItemCheckService.java:14 | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!B4: CollateralItem — параметр перевірки, джерело ідентифікатора. | src/pawnshop/StolenItemCheckService.java:6, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!A5: Виводити консольну імітацію перевірки. Не звертатися до мережевих API або баз даних. | src/pawnshop/StolenItemCheckService.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| StolenItemCheckService!B5: LoanAgreement — споживач поведінки лише опосередковано через IStolenItemCheck; прямого структурного зв’язку немає. | src/pawnshop/StolenItemCheckService.java:6, IStolenItemCheck.java, LoanAgreement.java | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!A3: Інтерфейс без конструктора. Вузький контракт лише оцінювання застави (ISP). | src/pawnshop/ICollateralAppraisal.java:5 | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!B3: Appraiser — реалізатор обох перевантажених операцій. | src/pawnshop/ICollateralAppraisal.java:5, Appraiser.java | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!A4: appraise(item): BigDecimal — контракт навчальної базової оцінки із записом і поверненням значення. | src/pawnshop/ICollateralAppraisal.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!B4: Cashier — споживач контракту, отримує реалізацію через конструктор (OCP, DIP). | src/pawnshop/ICollateralAppraisal.java:5, Cashier.java | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!A5: appraise(item, proposedValue): BigDecimal — друга сигнатура, явна додатна оцінка із записом і поверненням значення. | src/pawnshop/ICollateralAppraisal.java:6 | підтверджено; FR-013/017, декларації, all/verify |
| ICollateralAppraisal!B5: CollateralItem — параметр обох методів контракту. | src/pawnshop/ICollateralAppraisal.java:5, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |
| IStolenItemCheck!A3: Інтерфейс без конструктора. Вузький контракт перевірки речі на викрадення (ISP). | src/pawnshop/IStolenItemCheck.java:4 | підтверджено; FR-013/017, декларації, all/verify |
| IStolenItemCheck!B3: StolenItemCheckService — реалізатор локальної навчальної перевірки. | src/pawnshop/IStolenItemCheck.java:4, StolenItemCheckService.java | підтверджено; FR-013/017, декларації, all/verify |
| IStolenItemCheck!A4: isStolen(item): boolean — true для викраденої речі, false для дозволеної. Контракт не керує станом договору й платежами. | src/pawnshop/IStolenItemCheck.java:4 | підтверджено; FR-013/017, декларації, all/verify |
| IStolenItemCheck!B4: LoanAgreement — споживач контракту через конструктор; перевіряє до активації (DIP). | src/pawnshop/IStolenItemCheck.java:4, LoanAgreement.java | підтверджено; FR-013/017, декларації, all/verify |
| IStolenItemCheck!B5: CollateralItem — параметр isStolen. | src/pawnshop/IStolenItemCheck.java:4, CollateralItem.java | підтверджено; FR-013/017, декларації, all/verify |

## Зв’язки, кожен бізнес-виклик і перехід моделі → код

| Місце моделі | Елемент | Доказ коду / статус |
| --- | --- | --- |
| docs/class.mmd:72 | `PawnshopParticipant </-- Borrower` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:73 | `PawnshopParticipant </-- Appraiser` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:74 | `PawnshopParticipant </-- Cashier` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:77 | `Cashier "0..*" --> "1" ICollateralAppraisal : appraisal через конструктор` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:78 | `LoanAgreement "0..*" --> "1" Borrower : borrower` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:79 | `LoanAgreement "0..1" *-- "1" CollateralItem : виключне володіння item` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:80 | `LoanAgreement "0..*" --> "1" IStolenItemCheck : checkService через конструктор` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:81 | `Borrower ..> CollateralItem : подає` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:82 | `Borrower ..> Cashier : ініціює викуп і продовження` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:83 | `Borrower ..> LoanAgreement : параметр запиту` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:84 | `Cashier ..> CollateralItem : передає оцінюванню` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:85 | `Cashier ..> LoanAgreement : координує операції` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:86 | `Appraiser ..> CollateralItem : записує оцінку` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:87 | `ICollateralAppraisal ..> CollateralItem : параметр контракту` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:88 | `StolenItemCheckService ..> CollateralItem : перевіряє ідентифікатор` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/class.mmd:89 | `IStolenItemCheck ..> CollateralItem : параметр контракту` | Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements; підтверджено |
| docs/sequence.mmd:17 | `b->>b: submitItem(i)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:19 | `c->>c: appraiseItem(i)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:20 | `c->>a: appraise(i)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:22 | `a->>i: setAppraisedValue(1000)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:28 | `c->>l: LoanAgreement("L1", b, i, 800, 840, "2026-12-15", s)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:30 | `c->>c: prepareAgreement(l)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:31 | `c->>l: prepare()` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:32 | `l->>s: isStolen(i)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:33 | `s->>i: getId()` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:42 | `c->>c: issueFunds(l)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:43 | `c->>l: activate()` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:47 | `b->>b: requestRedemption(c, l, 839)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:48 | `b->>c: redeemItem(l, 839)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:49 | `c->>l: redeem(839)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:52 | `b->>b: requestExtension(c, l, "2026-12-15")` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:53 | `b->>c: extendAgreement(l, "2026-12-15")` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:54 | `c->>l: extend("2026-12-15")` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:57 | `c->>c: issueFunds(l)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:58 | `c->>l: activate()` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:65 | `b->>b: requestExtension(c, l, newDueDate)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:66 | `b->>c: extendAgreement(l, newDueDate)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:67 | `c->>l: extend(newDueDate)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:73 | `b->>b: requestRedemption(c, l, 840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:74 | `b->>c: redeemItem(l, 840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:75 | `c->>l: redeem(840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:79 | `b->>b: requestRedemption(c, l, 840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:80 | `b->>c: redeemItem(l, 840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/sequence.mmd:81 | `c->>l: redeem(840)` | Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log; підтверджено |
| docs/state.mmd:15 | `[*] --> Submitted: submitItem [опис непорожній]` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:16 | `Submitted --> Appraised: appraiseItem → appraise [вартість > 0] / записати оцінку` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:17 | `Appraised --> Draft: LoanAgreement(...) [коректні умови і залежності, річ вільна]` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:18 | `Draft --> Ready: prepareAgreement → prepare → isStolen [false] / завершити оформлення` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:19 | `Draft --> Rejected: prepareAgreement → prepare → isStolen [true] / відмовити без видачі` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:20 | `Ready --> Active: issueFunds → activate [0 < principal ≤ оцінки] / консольна імітація видачі` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:21 | `Active --> Active: requestExtension → extendAgreement → extend [нова дата пізніша] / оновити строк` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:22 | `Active --> Redeemed: requestRedemption → redeemItem → redeem [payment ≥ repaymentAmount] / імітувати погашення і повернення` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:23 | `Rejected --> [*]: звернення завершено відмовою` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/state.mmd:24 | `Redeemed --> [*]: звернення завершено викупом` | LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log; підтверджено |
| docs/usecase.puml:6 | `actor "Позичальник" as Borrower` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:7 | `actor "Оцінювач" as Appraiser` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:8 | `actor "Касир" as Cashier` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:10 | `usecase "Подати річ" as Submit` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:11 | `usecase "Оцінити річ" as Appraise` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:12 | `usecase "Перевірити річ\nна викрадення" as Check` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:13 | `usecase "Оформити позику" as Prepare` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:14 | `usecase "Видати кошти" as Issue` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:15 | `usecase "Викупити річ" as Redeem` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:16 | `usecase "Продовжити договір" as Extend` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:18 | `Borrower -- Submit` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:19 | `Borrower -- Prepare` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:20 | `Borrower -- Redeem` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:21 | `Borrower -- Extend` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:22 | `Appraiser -- Appraise` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:23 | `Cashier -- Appraise` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:24 | `Cashier -- Check` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:25 | `Cashier -- Prepare` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:26 | `Cashier -- Issue` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:27 | `Cashier -- Redeem` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:28 | `Cashier -- Extend` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |
| docs/usecase.puml:29 | `Prepare ..> Check : <<include>>` | Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log; підтверджено |

## Кожен бізнес-виклик коду → модель

| Місце | Виклик | Відповідник / статус |
| --- | --- | --- |
| src/pawnshop/Appraiser.java:11 | `setAppraisedValue` | class/sequence: операція setAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Appraiser.java:12 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Appraiser.java:12 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Appraiser.java:13 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Borrower.java:11 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Borrower.java:15 | `redeemItem` | class/sequence: операція redeemItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Borrower.java:19 | `extendAgreement` | class/sequence: операція extendAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:14 | `appraise` | class/sequence: операція appraise; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:17 | `prepare` | class/sequence: операція prepare; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:18 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:22 | `activate` | class/sequence: операція activate; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:23 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:23 | `getPrincipal` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Cashier.java:27 | `redeem` | class/sequence: операція redeem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:28 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:28 | `getRepaymentAmount` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Cashier.java:29 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:33 | `extend` | class/sequence: операція extend; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:34 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Cashier.java:34 | `getDueDate` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/LoanAgreement.java:21 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/LoanAgreement.java:27 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/LoanAgreement.java:35 | `isStolen` | class/sequence: операція isStolen; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:51 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:53 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:58 | `submitItem` | class/sequence: операція submitItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:59 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:61 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:63 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:65 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:66 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:67 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:74 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:74 | `getRole` | class/sequence: операція getRole; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:77 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:81 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:82 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:83 | `getRepaymentAmount` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:84 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:89 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:90 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:90 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:91 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:92 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:93 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:94 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:95 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:96 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:163 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:164 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:166 | `appraise` | class/sequence: операція appraise; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:168 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:170 | `appraise` | class/sequence: операція appraise; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:171 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:172 | `setAppraisedValue` | class/sequence: операція setAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:173 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:175 | `appraise` | class/sequence: операція appraise; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:176 | `appraise` | class/sequence: операція appraise; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:177 | `submitItem` | class/sequence: операція submitItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:181 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:182 | `isStolen` | class/sequence: операція isStolen; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:183 | `isStolen` | class/sequence: операція isStolen; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:186 | `setAppraisedValue` | class/sequence: операція setAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:186 | `getAppraisedValue` | class/sequence: операція getAppraisedValue; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:189 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:202 | `getRole` | class/sequence: операція getRole; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:202 | `getRole` | class/sequence: операція getRole; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:210 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:226 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:228 | `getPrincipal` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:228 | `getRepaymentAmount` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:230 | `appraiseItem` | class/sequence: операція appraiseItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:235 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:236 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:242 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:243 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:247 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:256 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:257 | `getPrincipal` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:257 | `getRepaymentAmount` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:259 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:260 | `getDueDate` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:261 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:262 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:264 | `getDueDate` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:265 | `getPrincipal` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:265 | `getRepaymentAmount` | I2: атрибут LoanAgreement і CRC Cashier!A7; підтверджено, all/verify |
| src/pawnshop/Main.java:266 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:271 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:272 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:274 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:275 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:278 | `requestRedemption` | class/sequence: операція requestRedemption; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:279 | `requestExtension` | class/sequence: операція requestExtension; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:280 | `prepareAgreement` | class/sequence: операція prepareAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:281 | `issueFunds` | class/sequence: операція issueFunds; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:282 | `redeemItem` | class/sequence: операція redeemItem; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/Main.java:283 | `extendAgreement` | class/sequence: операція extendAgreement; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/StolenItemCheckService.java:14 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |
| src/pawnshop/StolenItemCheckService.java:15 | `getId` | class/sequence: операція getId; CRC відповідного типу, таблиці вище; підтверджено, all/verify |

## Порядок, межі та винятки

Повернення sequence не є окремими методами: це BigDecimal/boolean, завершення void або очікуваний виняток. Стрілка конструктора — дія складальника. alt/opt/loop відтворено demonstrate й verify. Submitted/Appraised — етапи до договору. Усі оцінювання основних маршрутів завершені до createAgreement; verifyItems змінює ще не передану річ. Контрольна копія Terms створена напряму лише для доказу блокування другої передачі за id; це негативний тест, а не дозволений бізнес-маршрут. Rejected — погоджений виняток до незмінності стану після відмови.

## Конституція, ООП і SOLID

| Принцип | Доказ | Статус |
| --- | --- | --- |
| I: простота | 10 Java-файлів, один пакет, тільки JDK | підтверджено |
| II / SRP | LoanAgreement — правила/стан; Cashier — координація/фінансові імітації; Main — сценарії/перевірки | підтверджено |
| III: імітація | Фінансовий println після успіху; unchanged перевіряє відсутність імітацій після відмов | підтверджено |
| IV: javac/java | compile/clean-compile та clean-* логи, без готового out | підтверджено |
| V / DIP / DI | Конструктори Cashier і LoanAgreement; alternative/interrupted у Main | підтверджено |
| VI: імена/мова | Англійські ідентифікатори, українські коментарі/повідомлення; UTF-8 PowerShell | підтверджено |
| VII: UML | Окрема docs/practice2/class.mmd; вихідні моделі захищено SHA-256 | підтверджено |
| VIII: документація | README і документи українською, назви API збережено | підтверджено |
| LSP / наслідування | getRole через PawnshopParticipant, final getId та override трьох ролей | підтверджено |
| OCP / ISP | Контрольна реалізація оцінювання дає 900 без зміни Cashier; два вузькі інтерфейси | підтверджено |
| Інкапсуляція / overload | private поля, дві appraise; verify перевіряє рівно чотири пакетні getters договору | підтверджено |

## Відмінності діаграми №2

Додано чотири пакетні (~) getters, технічний Main з полями/методами/залежностями та пояснення двох анонімних тестових реалізаторів. Предметні типи, вихідні операції, атрибути й кратності збережено. Нові бізнес-правила не вводилися. Mermaid-джерело зберігається в docs; рендер не є залежністю запуску.

## Підсумок

Вихідних UML-членів: 48; декларацій коду у зворотному переліку: 91; бізнес-викликів: 100; непорожніх CRC-клітинок обов’язків/співпраці: 67. Охоплено 5/5 джерел і 9/9 карток. У наведеному інвентарі неперевірених елементів — 0, невирішених розбіжностей — 0. Висновок про відповідність стосується навчального обсягу й погодженої деталізації I2/N1; оригінальні UML не містять додаткових getters.
