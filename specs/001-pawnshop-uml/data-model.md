# Модель даних і поведінки: задача 001

Це проєктний опис, не вихідний код і не готові діаграми.
Грошові значення — BigDecimal, строки — LocalDate, текст — String.
Стандартні типи значень не є додатковими вузлами. Усі атрибути приватні;
конструктори й операції публічні, конструктор абстрактного предка — protected.

## Типи та члени

| Тип | Дані та конструктор | Операції й відповідальність |
| --- | --- | --- |
| PawnshopParticipant, abstract | id: String, name: String; PawnshopParticipant(id, name) | final getId(): String; abstract getRole(): String; спільна ідентифікація |
| Borrower | Borrower(id, name) | getRole(): String override; submitItem(item: CollateralItem): void; requestRedemption(cashier: Cashier, agreement: LoanAgreement, payment: BigDecimal): void; requestExtension(cashier: Cashier, agreement: LoanAgreement, newDueDate: LocalDate): void |
| Appraiser | Appraiser(id, name) | getRole(): String override; appraise(item: CollateralItem): BigDecimal; appraise(item: CollateralItem, proposedValue: BigDecimal): BigDecimal |
| Cashier | appraisal: ICollateralAppraisal; Cashier(id, name, appraisal) | getRole(): String override; appraiseItem(item: CollateralItem): BigDecimal; prepareAgreement(agreement: LoanAgreement): void; issueFunds(agreement: LoanAgreement): void; redeemItem(agreement: LoanAgreement, payment: BigDecimal): void; extendAgreement(agreement: LoanAgreement, newDueDate: LocalDate): void |
| CollateralItem | id: String, description: String, appraisedValue: BigDecimal; CollateralItem(id, description) | setAppraisedValue(value: BigDecimal): void; getAppraisedValue(): BigDecimal; getId(): String; опис та оцінка застави |
| LoanAgreement | id: String, borrower: Borrower, item: CollateralItem, checkService: IStolenItemCheck, principal: BigDecimal, repaymentAmount: BigDecimal, dueDate: LocalDate, status: String; LoanAgreement(id, borrower, item, principal, repaymentAmount, dueDate, checkService) | prepare(): void; activate(): void; redeem(payment: BigDecimal): void; extend(newDueDate: LocalDate): void; допустимість і зміна стану договору |
| StolenItemCheckService | stolenItemIds: Set<String>; StolenItemCheckService(stolenItemIds) | isStolen(item: CollateralItem): boolean; навчальна перевірка за локальним набором |
| ICollateralAppraisal, interface | Конструктора немає | appraise(item: CollateralItem): BigDecimal; appraise(item: CollateralItem, proposedValue: BigDecimal): BigDecimal |
| IStolenItemCheck, interface | Конструктора немає | isStolen(item: CollateralItem): boolean |

Однаковий контракт getRole реалізують усі нащадки без додаткових передумов (LSP).
getId не перевизначається; стабільна ідентифікація пояснює final.
Дві appraise відрізняються параметрами: перша використовує навчальну базову оцінку,
друга — явно подану додатну оцінку. Обидві повертають і записують заставну вартість.
Cashier використовує лише контракт оцінювання (OCP, ISP, DIP).
Статус — обмежений набір рядкових значень, без додаткового enum-типу та CRC-картки.

## Повний реєстр зв'язків

- Borrower, Appraiser, Cashier успадковують PawnshopParticipant.
- Appraiser реалізує ICollateralAppraisal; StolenItemCheckService реалізує IStolenItemCheck.
- Cashier має асоціацію з одним ICollateralAppraisal (переданий конструктором).
- LoanAgreement має асоціацію з одним Borrower; Borrower може мати 0..* договорів.
- LoanAgreement володіє одним CollateralItem через композицію; річ має 0..1 власника.
- LoanAgreement має асоціацію з одним IStolenItemCheck (переданий конструктором).
- Borrower залежить від CollateralItem, Cashier і LoanAgreement через параметри операцій.
- Cashier залежить від CollateralItem і LoanAgreement через параметри операцій.
- Appraiser та ICollateralAppraisal залежать від CollateralItem.
- StolenItemCheckService та IStolenItemCheck залежать від CollateralItem.

Асоціації до контрактів покривають параметри конструкторів; композиція покриває item.
Не додавати фіктивну пряму залежність договору від конкретного сервісу:
зв'язок пояснюється через контракт за FR-011. Усі нові сигнатури потребують перевірки реєстру.

## Створення об'єктів та межі відповідальності

Зовнішній складальник сценарію створює учасників, річ і сервіс, передає залежності
конструкторам. Він не є бізнес-класом і не показується новою роллю системи.
Borrower подає річ; Cashier передає її оцінювання контракту; Appraiser записує оцінку.
Після оцінювання створюється чернетка LoanAgreement з виключним володінням річчю.
Cashier.prepareAgreement делегує prepare; prepare викликає isStolen.
Лише після успішної перевірки дозволена activate через Cashier.issueFunds.
Бізнес-правила залишаються у договорі; консольні фінансові повідомлення — в Cashier,
повідомлення перевірки — у StolenItemCheckService.

## Стани й умови

Початок, подання та оцінювання належать зверненню до створення договору.
Після створення чернетки життєвий цикл продовжується станами договору.
Окремий клас звернення не потрібен: це процесна діаграма одного сценарію.

| Стан | Подія / умова | Наступний стан |
| --- | --- | --- |
| Початок | submitItem з непорожнім описом | Submitted |
| Submitted | appraiseItem успішне | Appraised |
| Appraised | Створено LoanAgreement | Draft |
| Draft | prepare; isStolen повертає false | Ready |
| Draft | prepare; isStolen повертає true | Rejected, далі кінець |
| Ready | issueFunds делегує activate; сума додатна та не перевищує оцінку | Active |
| Active | extend; нова дата пізніша | Active з новим строком |
| Active | redeem; payment не менше repaymentAmount | Redeemed, далі кінець |

Статуси договору: Draft, Ready, Active, Rejected, Redeemed.
Некоректний опис, суми, строки та відсутні залежності відхиляються до зміни стану.
repaymentAmount не менша principal; формула процентів поза обсягом.
Повторна activate, неповний платіж та дії після завершення відхиляються.
Надлишок платежу не моделює реальний переказ; для прикладів використовувати точну суму.
Після Rejected/Redeemed бізнес-переходів немає. Повернення речі не видаляє її фізично.
Виключне володіння перевіряється під час складання сценарію; одна річ не передається двом договорам.

## Наскрізне зіставлення операцій

| Операція | Відповідальний метод | Стан або дія |
| --- | --- | --- |
| Подання | Borrower.submitItem | Submitted |
| Оцінювання | Cashier.appraiseItem → ICollateralAppraisal.appraise | Appraised |
| Перевірка | LoanAgreement.prepare → IStolenItemCheck.isStolen | Ready або Rejected |
| Оформлення | Cashier.prepareAgreement → LoanAgreement.prepare | Draft → Ready |
| Видача | Cashier.issueFunds → LoanAgreement.activate | Ready → Active |
| Викуп | Borrower.requestRedemption → Cashier.redeemItem → LoanAgreement.redeem | Active → Redeemed |
| Продовження | Borrower.requestExtension → Cashier.extendAgreement → LoanAgreement.extend | Active → Active |

Ті самі назви використовувати в sequence та CRC. Створення договору показати
повідомленням створення з конструктором із цього реєстру; не вигадувати factory-метод.
