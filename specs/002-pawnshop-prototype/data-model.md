# Модель даних та відповідальностей

**Задача**: 002 · **Дата**: 2026-11-15. Проєктування, без Java-коду.

Джерела — `docs/class.mmd`, `docs/sequence.mmd`, `docs/state.mmd`, `docs/usecase.puml`, `docs/crc.xlsx` і уточнена специфікація. Бази даних, таблиць зберігання та серіалізації немає.

## Типи й члени

Позначення видимості UML: `+` public, `-` private, `#` protected. Методи основної UML-таблиці public, крім захищеного конструктора предка; чотири технічні getters мають пакетну видимість. Поля private. Методи інтерфейсів — public abstract. Стандартні типи JDK не є новими предметними сутностями.

| Тип | Поля | Конструктори та методи | Відповідальність / CRC |
| --- | --- | --- | --- |
| Абстрактний PawnshopParticipant | String id, String name | protected PawnshopParticipant(String id, String name); final getId(): String; abstract getRole(): String | Ідентифікація, спільний контракт; PawnshopParticipant!A3:B5 |
| Borrower extends PawnshopParticipant | Власних полів немає | Borrower(String id, String name); override getRole(): String; submitItem(CollateralItem item): void; requestRedemption(Cashier cashier, LoanAgreement agreement, BigDecimal payment): void; requestExtension(Cashier cashier, LoanAgreement agreement, LocalDate newDueDate): void | Подання, ініціювання запитів через касира; Borrower!A3:B6 |
| Appraiser extends PawnshopParticipant implements ICollateralAppraisal | Власних полів немає | Appraiser(String id, String name); override getRole(): String; appraise(CollateralItem item): BigDecimal; appraise(CollateralItem item, BigDecimal proposedValue): BigDecimal | Визначення, запис і повернення оцінки; Appraiser!A3:B5 |
| Cashier extends PawnshopParticipant | ICollateralAppraisal appraisal | Cashier(String id, String name, ICollateralAppraisal appraisal); override getRole(): String; appraiseItem(CollateralItem item): BigDecimal; prepareAgreement(LoanAgreement agreement): void; issueFunds(LoanAgreement agreement): void; redeemItem(LoanAgreement agreement, BigDecimal payment): void; extendAgreement(LoanAgreement agreement, LocalDate newDueDate): void | Делегування правил, фінансові імітації після успіху; Cashier!A3:B7 |
| CollateralItem | String id, String description, BigDecimal appraisedValue | CollateralItem(String id, String description); setAppraisedValue(BigDecimal value): void; getAppraisedValue(): BigDecimal; getId(): String | Дані речі та валідна оцінка; CollateralItem!A3:B6 |
| LoanAgreement | String id, Borrower borrower, CollateralItem item, IStolenItemCheck checkService, BigDecimal principal, BigDecimal repaymentAmount, LocalDate dueDate, String status | LoanAgreement(String id, Borrower borrower, CollateralItem item, BigDecimal principal, BigDecimal repaymentAmount, LocalDate dueDate, IStolenItemCheck checkService); prepare(): void; activate(): void; redeem(BigDecimal payment): void; extend(LocalDate newDueDate): void | Умови, правила й стан; LoanAgreement!A3:B8 |
| StolenItemCheckService implements IStolenItemCheck | Set<String> stolenItemIds | StolenItemCheckService(Set<String> stolenItemIds); isStolen(CollateralItem item): boolean | Локальний пошук id та його консольна імітація; StolenItemCheckService!A3:B5 |
| ICollateralAppraisal | Полів і конструктора немає | appraise(CollateralItem item): BigDecimal; appraise(CollateralItem item, BigDecimal proposedValue): BigDecimal | Вузький контракт оцінювання; ICollateralAppraisal!A3:B5 |
| IStolenItemCheck | Полів і конструктора немає | isStolen(CollateralItem item): boolean | Вузький контракт перевірки; IStolenItemCheck!A3:B5 |

Обидва appraise записують і повертають значення. Базовий використовує 1000, перевантажений — явно передане додатне значення. getRole повертає українську назву відповідної ролі без додаткових передумов, що зберігає LSP. Поліморфізм демонструється зверненням до трьох нащадків через PawnshopParticipant.

## Явні технічні доповнення

LoanAgreement отримує лише пакетні методи читання `getId(): String`, `getPrincipal(): BigDecimal`, `getRepaymentAmount(): BigDecimal`, `getDueDate(): LocalDate`. Вони надають касиру дані для повідомлень; бізнес-правила та видимість полів не змінюються. Відповідники — однойменні атрибути class і LoanAgreement!A3, споживач — Cashier!A7. Ці методи не зображені окремими операціями вихідної UML; у звіті їх потрібно явно перевірити як технічну деталізацію. Непідтверджений відповідник записується як розбіжність. Джерела №1 незмінні.

Main не є предметним типом. Його екземпляр складає сценарії, містить набори createdItemIds та assignedItemIds, запускає перевірки й оформлює очікувані відмови. Відповідники — примітки про складальник у class/sequence та FR-011/018. main(String[] args) — лише точка входу та верхній обробник помилок. Допоміжні методи сценаріїв і перевірок мають зіставлятися з конкретними маршрутами, умовами й заборонами моделі.

## Зв'язки й передавання залежностей

- Borrower, Appraiser, Cashier успадковують PawnshopParticipant; Appraiser реалізує обидва методи ICollateralAppraisal; StolenItemCheckService реалізує IStolenItemCheck.
- Один Cashier має одну appraisal; одну реалізацію може використовувати 0..* касирів. Реалізація передається через конструктор, не створюється всередині касира.
- Один LoanAgreement має одного Borrower; позичальник може мати 0..* договорів.
- Один LoanAgreement композиційно володіє однією CollateralItem; у речі 0..1 власника. Складальник гарантує унікальність id на весь запуск і заборону другої передачі, навіть через інший Java-об'єкт.
- Один LoanAgreement має один IStolenItemCheck; реалізація може обслуговувати 0..* договорів. Передається конструктором. Договір не створює конкретний сервіс.
- Borrower передає річ у submitItem, а запити викупу/продовження — Cashier разом із договором. Cashier передає річ appraisal, викликає prepare/activate/redeem/extend договору. Appraiser записує оцінку речі. Сервіс перевірки викликає getId речі.
- Параметричні залежності обох інтерфейсів від CollateralItem та вхідні залежності CRC також входять в аудит. Стрілка створення договору у sequence є дією складальника, а не фабрикою Cashier.

OCP/DIP демонструються роботою касира через контракт оцінювання та договору через контракт перевірки; ISP — розділеними вузькими інтерфейсами. Нові виробничі реалізації для демонстрації не потрібні.

## Інваріанти й валідація

1. Річ створюється з непорожнім і не лише пробільним description. id має відповідати уточненому правилу A2 нижче; стабільність забезпечується відсутністю setter. Початкова appraisedValue відсутня.
2. Оцінка додатна та кратна копійці. Невдале оцінювання до створення договору зберігає попередню коректну оцінку. Після створення договору складальник не викликає повторного оцінювання; предметний механізм блокування не додається.
3. Конструктор договору вимагає borrower, item, checkService, додатну оцінку, `0 < principal <= appraisedValue`, `repaymentAmount >= principal`, dueDate пізніше навчальної дати 2026-11-15. Невдалий конструктор не створює коректної чернетки й не займає річ у складальнику.
4. Кожне грошове значення — BigDecimal, придатне до scale 2 без округлення. Це стосується оцінки, позики, належної суми та платежу. 840.000 допустиме; 840.005 відхиляється. Перевірки здійснюються до запису, порівняння — compareTo, а не equals.
5. Відсутні об'єкти чи залежності блокують відповідну операцію з українським поясненням. Сервіс отримує непорожнє за посиланням значення Set; порожній набір допустимий. Захисна копія набору унеможливлює випадкову зовнішню зміну результатів.
6. Платіж заданий, додатний, точний до копійки й не менший за repaymentAmount. Часткові платежі не накопичуються. За переплати повідомлення розрізняє запропоновану й належну суми; здача поза обсягом.
7. Продовження змінює лише dueDate, вимагає задану дату строго пізніше поточної. На один день пізніше допустимо. Проценти, штрафи й перерахунок repaymentAmount не вводяться.

## Переходи станів

status залишається String з рівно п'ятьма значеннями UML. Submitted та Appraised — додоговірні етапи, не значення status і не нові поля.

| Початок | Подія / умова | Результат | Зовнішній ефект |
| --- | --- | --- | --- |
| До договору | submitItem, непорожній опис | Submitted | Підтвердження подання |
| Submitted | appraiseItem → appraise, коректна оцінка | Appraised | Показ оцінки |
| Appraised | Конструктор, коректні умови й вільна річ | Draft | Немає виплати |
| Draft | prepare → isStolen повернув false | Ready | Повідомлення локальної перевірки й оформлення |
| Draft | prepare → isStolen повернув true | Rejected | Перевірка, пояснення відмови; без виплати |
| Draft | Перевірка не повернула результату | Draft | Без успішного оформлення чи виплати |
| Ready | activate | Active | Cashier імітує видачу рівно раз |
| Active | extend, новий строк пізніший | Active | Підтвердження строку, без видачі |
| Active | redeem, повний платіж | Redeemed | Cashier імітує погашення й повернення по одному разу |
| Будь-який | Некоректний аргумент або недопустима для стану операція | Стан і дані незмінні | Лише пояснення відмови |

З Rejected та Redeemed немає бізнес-переходів. Повторне prepare поза Draft, видача до Ready чи з Active, викуп/продовження до Active та дії з кінцевих станів відхиляються. Викуп не знищує об'єкт речі. У разі неочікуваної помилки весь процес припиняється; незавершена перевірка сама по собі не дає дозволу на виплату.

## Погоджене рішення I2 (2026-11-15)

Дозволено лише чотири пакетні методи читання LoanAgreement: `getId(): String`, `getPrincipal(): BigDecimal`, `getRepaymentAmount(): BigDecimal`, `getDueDate(): LocalDate`. Це погоджена технічна деталізація атрибутів, якої немає серед явно зображених операцій вихідної UML. Методи не змінюють стан і не додають правил, залежностей або відповідальностей; поля залишаються private, setters не додаються. Кожен метод зіставляється з однойменним атрибутом class, LoanAgreement!A3 та потребою Cashier!A7 у CRC. Буквальна тотожність переліків операцій не заявляється. Після реалізації перевірити й за потреби оновити окрему діаграму класів для звіту практичної №2 у `specs/002-pawnshop-prototype/class-practice2.mmd`, відобразивши фактичні методи та пакетну видимість. Оригінальні UML/CRC практичної №1 у docs/ незмінні. Інші непогоджені доповнення є розбіжностями; фактична відповідність потребує аудиту.

## Уточнення I1 та A2

Виявлення викрадення — окремий результат Draft → Rejected; незмінність стану стосується решти відхилених операцій.

Ідентифікатори навчальних об’єктів — непорожні рядки без пробільних символів. null, порожній рядок і рядки з пробільними символами відхиляються. Порівняння точне, з урахуванням регістру, без обрізання пробілів або зміни регістру: I1 та i1 різні, повторне I1 означає ту саму річ у межах запуску.
