"""Створення дев’яти CRC-карток моделі «Ломбард»; запуск із кореня репозиторію."""

from itertools import zip_longest
from pathlib import Path
from textwrap import wrap

from openpyxl import Workbook, load_workbook
from openpyxl.styles import Alignment, Border, Font, PatternFill, Side
from openpyxl.worksheet.page import PageMargins


# Фіксований зміст відповідає specs/001-pawnshop-uml/data-model.md.
CARDS = {
    "PawnshopParticipant": (
        [
            "Абстрактний клас: зберігати спільні id та name учасника. Захищений конструктор ініціалізує нащадків.",
            "final getId(): String повертає стабільний ідентифікатор і не допускає перевизначення.",
            "abstract getRole(): String визначає спільний контракт ролі. LSP: нащадки виконують його без додаткових передумов.",
        ],
        [
            "Borrower — нащадок, успадковує ідентифікацію та перевизначає getRole.",
            "Appraiser — нащадок, успадковує ідентифікацію та перевизначає getRole.",
            "Cashier — нащадок, успадковує ідентифікацію та перевизначає getRole.",
        ],
    ),
    "Borrower": (
        [
            "Представляти позичальника; конструктор Borrower(id, name), override getRole(): String.",
            "submitItem(item) — подавати річ із непорожнім описом для оцінювання.",
            "requestRedemption(cashier, agreement, payment) — ініціювати викуп через касира.",
            "requestExtension(cashier, agreement, newDueDate) — ініціювати продовження через касира.",
        ],
        [
            "PawnshopParticipant — успадковані id, name та final getId.",
            "CollateralItem — параметр подання речі.",
            "Cashier — отримувач запитів на викуп і продовження.",
            "LoanAgreement — договір у параметрах запитів; асоціація договору з позичальником.",
        ],
    ),
    "Appraiser": (
        [
            "Визначати й записувати заставну вартість речі. Конструктор Appraiser(id, name), override getRole(): String.",
            "appraise(item): BigDecimal — визначати навчальну базову оцінку, записувати й повертати її.",
            "appraise(item, proposedValue): BigDecimal — перевантажений метод для явно поданої додатної оцінки; записувати й повертати її.",
        ],
        [
            "PawnshopParticipant — наслідування ідентифікації та контракту ролі.",
            "ICollateralAppraisal — реалізація обох сигнатур вузького контракту оцінювання.",
            "CollateralItem — отримувач setAppraisedValue; недодатна оцінка відхиляється.",
        ],
    ),
    "Cashier": (
        [
            "Координувати операції. Cashier(id, name, appraisal) отримує ICollateralAppraisal ззовні; override getRole(): String.",
            "appraiseItem(item) — делегувати оцінювання контракту. OCP і DIP: заміна реалізації не змінює касира.",
            "prepareAgreement(agreement) — делегувати prepare; issueFunds(agreement) — делегувати activate.",
            "redeemItem(agreement, payment) та extendAgreement(agreement, newDueDate) — делегувати правила договору, не дублювати їх.",
            "Після успішних операцій виводити консольні імітації видачі, погашення та повернення речі. Після відмови зовнішню дію не імітувати.",
        ],
        [
            "PawnshopParticipant — наслідування ідентифікації та контракту ролі.",
            "ICollateralAppraisal — асоціація appraisal, DI через конструктор; виклик appraise.",
            "CollateralItem — параметр оцінювання.",
            "LoanAgreement — отримувач prepare, activate, redeem, extend.",
            "Borrower — ініціатор запитів на викуп і продовження (вхідна залежність).",
        ],
    ),
    "CollateralItem": (
        [
            "Зберігати id, непорожній description та appraisedValue. Конструктор CollateralItem(id, description).",
            "setAppraisedValue(value) — приймати лише додатну заставну вартість.",
            "getAppraisedValue(): BigDecimal та getId(): String — надавати оцінку й ідентифікатор.",
            "Бути об’єктом застави не більш ніж одного договору. Повернення речі не означає фізичне знищення.",
        ],
        [
            "LoanAgreement — композиційний власник: одна річ у договорі, 0..1 власника речі.",
            "Borrower — подає річ; Cashier — передає її на оцінювання (вхідні залежності).",
            "Appraiser та ICollateralAppraisal — запис оцінки й параметр контракту оцінювання.",
            "StolenItemCheckService та IStolenItemCheck — локальна перевірка і параметр її контракту.",
        ],
    ),
    "LoanAgreement": (
        [
            "Зберігати умови та стан. Конструктор отримує id, borrower, item, principal, repaymentAmount, dueDate, checkService. DI через IStolenItemCheck.",
            "Перевіряти оцінку, 0 < principal ≤ оцінки, repaymentAmount ≥ principal, строк і залежності. Початковий статус Draft.",
            "prepare() — перевіряти річ через isStolen: false переводить у Ready, true — у Rejected без виплати.",
            "activate() — лише з Ready переводити в Active, блокувати повторну видачу.",
            "redeem(payment) — лише в Active за повного погашення переводити в Redeemed; extend(newDueDate) — залишати Active з пізнішим строком.",
            "Відхиляти неповний платіж, неправильний строк і дії після завершення без зміни стану. SRP: фінансові консольні повідомлення залишати касиру.",
        ],
        [
            "Borrower — один позичальник договору; позичальник може мати 0..* договорів.",
            "CollateralItem — одна складова композиції; складальник сценарію забороняє подвійного власника.",
            "IStolenItemCheck — асоціація checkService, передана конструктором, виклик isStolen до видачі.",
            "StolenItemCheckService — лише опосередкована співпраця через IStolenItemCheck, без прямого створення чи залежності від конкретного класу.",
            "Cashier — викликає операції договору й імітує фінансові дії після їх успіху (вхідна залежність).",
            "Borrower — передає договір у запитах на викуп і продовження (вхідна залежність).",
        ],
    ),
    "StolenItemCheckService": (
        [
            "Виконувати навчальну локальну перевірку. Конструктор StolenItemCheckService(stolenItemIds) отримує набір ідентифікаторів.",
            "isStolen(item): boolean — перевіряти getId() речі у stolenItemIds та повертати демонстраційний результат.",
            "Виводити консольну імітацію перевірки. Не звертатися до мережевих API або баз даних.",
        ],
        [
            "IStolenItemCheck — реалізація вузького контракту перевірки.",
            "CollateralItem — параметр перевірки, джерело ідентифікатора.",
            "LoanAgreement — споживач поведінки лише опосередковано через IStolenItemCheck; прямого структурного зв’язку немає.",
        ],
    ),
    "ICollateralAppraisal": (
        [
            "Інтерфейс без конструктора. Вузький контракт лише оцінювання застави (ISP).",
            "appraise(item): BigDecimal — контракт навчальної базової оцінки із записом і поверненням значення.",
            "appraise(item, proposedValue): BigDecimal — друга сигнатура, явна додатна оцінка із записом і поверненням значення.",
        ],
        [
            "Appraiser — реалізатор обох перевантажених операцій.",
            "Cashier — споживач контракту, отримує реалізацію через конструктор (OCP, DIP).",
            "CollateralItem — параметр обох методів контракту.",
        ],
    ),
    "IStolenItemCheck": (
        [
            "Інтерфейс без конструктора. Вузький контракт перевірки речі на викрадення (ISP).",
            "isStolen(item): boolean — true для викраденої речі, false для дозволеної. Контракт не керує станом договору й платежами.",
        ],
        [
            "StolenItemCheckService — реалізатор локальної навчальної перевірки.",
            "LoanAgreement — споживач контракту через конструктор; перевіряє до активації (DIP).",
            "CollateralItem — параметр isStolen.",
        ],
    ),
}


def main():
    """Записати повну книгу та перевірити її повторне читання."""
    workbook = Workbook()
    workbook.remove(workbook.active)
    edge = Side(style="thin", color="B8C4D0")
    for name, (responsibilities, collaborators) in CARDS.items():
        sheet = workbook.create_sheet(name)
        sheet.sheet_view.showGridLines = False
        sheet.merge_cells("A1:B1")
        sheet["A1"] = name
        sheet.append(["Обов'язки класу", "З ким співпрацює"])
        for row in zip_longest(responsibilities, collaborators, fillvalue=""):
            sheet.append(row)
        sheet.column_dimensions["A"].width = 60
        sheet.column_dimensions["B"].width = 50
        for row in sheet:
            for cell in row:
                cell.font = Font(name="Arial", size=11, color="172B4D")
                cell.alignment = Alignment(vertical="top", wrap_text=True)
                cell.border = Border(left=edge, right=edge, top=edge, bottom=edge)
        sheet["A1"].font = Font(name="Arial", size=15, bold=True, color="172B4D")
        sheet.row_dimensions[1].height = 32
        sheet.row_dimensions[2].height = 28
        for cell in sheet[2]:
            cell.font = Font(name="Arial", size=11, bold=True, color="FFFFFF")
            cell.fill = PatternFill("solid", fgColor="294666")
            cell.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)
        for row in range(3, sheet.max_row + 1):
            # Консервативний запас висоти для тексту Arial 11 у колонках 60/50.
            count = max(len(wrap(str(sheet.cell(row, col).value or ""), width=width))
                        for col, width in ((1, 52), (2, 43)))
            sheet.row_dimensions[row].height = max(38, count * 16 + 12)
            if row % 2:
                for cell in sheet[row]:
                    cell.fill = PatternFill("solid", fgColor="F1F5F9")
        sheet.print_area = f"A1:B{sheet.max_row}"
        sheet.sheet_properties.pageSetUpPr.fitToPage = True
        sheet.page_setup.orientation = "landscape"
        sheet.page_setup.paperSize = sheet.PAPERSIZE_A4
        sheet.page_setup.fitToWidth = 1
        sheet.page_setup.fitToHeight = 1
        sheet.page_margins = PageMargins(left=0.3, right=0.3, top=0.3, bottom=0.3)
        sheet.print_options.horizontalCentered = True
    output = Path(__file__).resolve().parents[1] / "docs" / "crc.xlsx"
    output.parent.mkdir(exist_ok=True)
    workbook.save(output)
    saved = load_workbook(output)
    assert saved.sheetnames == list(CARDS)
    for sheet in saved:
        assert str(sheet.merged_cells) == "A1:B1"
        assert sheet["A1"].value == sheet.title
        assert sheet["A2"].value == "Обов'язки класу"
        assert sheet["B2"].value == "З ким співпрацює"
        assert sheet.max_row >= 4
        assert all(c.data_type != "f" for row in sheet for c in row)
    print(f"Створено {output}: {len(saved.sheetnames)} CRC-карток; повторне читання успішне.")


if __name__ == "__main__":
    main()
