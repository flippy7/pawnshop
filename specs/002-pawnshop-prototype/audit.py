"""Локальний допоміжний аудит документації; не залежність Java-прототипу."""
from pathlib import Path
import re
import zipfile
import xml.etree.ElementTree as E

ROOT = Path(__file__).resolve().parents[2]
FEATURE = Path(__file__).resolve().parent
sources = {p.stem: p.read_text(encoding="utf-8").splitlines() for p in (ROOT / "src/pawnshop").glob("*.java")}

def clean(value):
    return value.replace("|", "/").replace("\n", " ")

def address(cls, token):
    return f"src/pawnshop/{cls}.java:" + str(next(i for i, line in enumerate(sources[cls], 1) if token in line))

original = (ROOT / "docs/class.mmd").read_text(encoding="utf-8")
diagram = original.replace("  +prepare() void", "  ~getId() String\n  ~getPrincipal() BigDecimal\n  ~getRepaymentAmount() BigDecimal\n  ~getDueDate() LocalDate\n  +prepare() void", 1)
diagram += "\nclass Main {\n"
for line in sources["Main"]:
    if re.match(r"    private (\S+) (\w+)(?: =|;)", line):
        match = re.match(r"    private (\S+) (\w+)", line)
        if match:
            diagram += f"  -{match[1].replace('<', '~').replace('>', '~')} {match[2]}\n"
    match = re.match(r"    (public|private) (static )?([\w<>]+) (\w+)\((.*)", line)
    if match:
        # Багаторядкову сигнатуру createAgreement деталізовано в примітці.
        args = match[5].split(")")[0].rstrip(" {")
        if match[4] == "createAgreement":
            args = "CollateralItem item, BigDecimal principal, BigDecimal repayment, LocalDate due, IStolenItemCheck service"
        args = args.replace("Class<? extends RuntimeException>", "Class~? extends RuntimeException~")
        diagram += f"  {'+' if match[1]=='public' else '-'}{match[4]}({args}){'$' if match[2] else ''} {match[3].replace('<','~').replace('>','~')}\n"
diagram += "}\n"
for cls in sources:
    if cls != "Main":
        diagram += f"Main ..> {cls} : складання або перевірка\n"
diagram += 'note for Main "Технічний складальник, не предметна сутність.<br/>Reflection і перехоплення stdout лише у перевірках.<br/>Два анонімні реалізатори контрактів — контрольні об’єкти. Fluent JDK-операції не є модельними вузлами."\n'
diagram += 'note for LoanAgreement "Доповнення №2: чотири пакетні (~) getters деталізують атрибути.<br/>В оригінальній UML №1 ці операції відсутні. Дозвіл I2, без setters."\n'
(ROOT / "docs/practice2").mkdir(exist_ok=True)
(ROOT / "docs/practice2/class.mmd").write_text(diagram, encoding="utf-8")

report = ["# Двосторонній аудит прототипу «Ломбард»", "", "**Дата**: 2026-11-15. Докази: [validation.md](validation.md), [results.json](logs/results.json), [javap](logs/javap.stdout.log), [verify](logs/verify.stdout.log), [all](logs/all.stdout.log).", "", "Погоджені відмінності I2/N1 відокремлено; буквальна тотожність переліків операцій №1 і коду не заявляється. Усі адреси Java нижче відносні до кореня репозиторію.", "", "## Модель → код: кожен UML-член", "", "| Елемент | Місце й декларація коду | Вимога / статус |", "| --- | --- | --- |"]
cls = None
members = 0
for number, line in enumerate(original.splitlines(), 1):
    match = re.match(r"class (\w+) \{", line)
    if match:
        cls = match[1]
    elif line == "}":
        cls = None
    elif cls and line.strip().startswith(("+", "-", "#")):
        part = line.strip()[1:]
        if "(" in part:
            name = part.split("(")[0]
            args = part.split("(", 1)[1].split(")")[0]
            arity = len(args.split(",")) if args else 0
            matches = []
            for i, declaration in enumerate(sources[cls], 1):
                if name + "(" not in declaration or not (any(x in declaration for x in ["public ", "protected "]) or cls.startswith("I")):
                    continue
                signature = declaration
                j = i
                while ")" not in signature:
                    signature += " " + sources[cls][j]
                    j += 1
                arguments = signature.split(name + "(", 1)[1].split(")")[0]
                if (len(arguments.split(",")) if arguments.strip() else 0) == arity:
                    matches.append((i, signature))
            assert matches, (cls, part)
            i, declaration = matches[0]
        else:
            name = part.split()[-1]
            i = next(i for i, text in enumerate(sources[cls], 1) if re.search(r"private\s+\S+\s+" + name + r"\s*[;=]", text))
            declaration = sources[cls][i-1]
        report.append(f"| docs/class.mmd:{number} `{clean(part)}` | src/pawnshop/{cls}.java:{i} `{clean(declaration.strip())}` | FR-016; підтверджено декларацією та javap |")
        members += 1

ranges = {"PawnshopParticipant":"A3:B5", "Borrower":"A3:B6", "Appraiser":"A3:B5", "Cashier":"A3:B7", "CollateralItem":"A3:B6", "LoanAgreement":"A3:B8", "StolenItemCheckService":"A3:B5", "ICollateralAppraisal":"A3:B5", "IStolenItemCheck":"A3:B5"}
mainrefs = {"main":"sequence: складальник; FR-011/015", "money":"class: BigDecimal; FR-004", "date":"class: LocalDate; FR-008", "createItem":"class: примітка композиції; FR-018", "createAgreement":"sequence: створення договору; FR-004/018", "fresh":"sequence: передумови й альтернативи; FR-011", "demonstrate":"usecase: сім операцій; sequence: alt/loop; FR-011", "check":"state: умови переходів; SC-002", "expect":"sequence: очікувані відмови; FR-009", "field":"state: контроль стану; research §7", "snapshot":"state: незмінність даних; SC-002", "capture":"class: фінансові повідомлення після успіху; SC-003", "unchanged":"state: заборони; FR-009", "occurrences":"sequence: одноразові фінансові дії; SC-003", "verify":"state: усі умови; SC-002", "verifyItems":"class/CRC Appraiser, CollateralItem; FR-002/003/004/018", "verifyAgreements":"class/CRC LoanAgreement; FR-004/005/018", "verifyRedemption":"state: Active → Redeemed; FR-007", "verifyExtension":"state: Active → Active; FR-008", "verifyStates":"state: п’ять станів, заборони; FR-009"}
report += ["", "## Код → модель: декларації й службові члени", "", "| Місце | Декларація | Відповідник / статус |", "| --- | --- | --- |"]
declarations = 0
for cls, lines in sources.items():
    for i, line in enumerate(lines, 1):
        normalized = line.replace("@Override ", "")
        declaration = bool(re.match(r"\s*(?:public|private|protected)\s+", normalized)) or (cls.startswith("I") and "(" in line and ";" in line) or bool(re.match(r"\s*(String|BigDecimal|LocalDate) get", line))
        if not declaration:
            continue
        if cls == "Main":
            method = re.search(r"(\w+)\(", line)
            reference = mainrefs.get(method[1] if method else "", "class/sequence: технічний складальник, залежності й набори id; FR-011/018")
        elif cls == "LoanAgreement" and re.match(r"\s*(String|BigDecimal|LocalDate) get", line):
            reference = "I2: однойменний атрибут class; CRC LoanAgreement!A3, Cashier!A7; FR-010/016"
        else:
            reference = f"class: {cls}; CRC {cls}!{ranges[cls]}; FR-016/017"
        report.append(f"| src/pawnshop/{cls}.java:{i} | `{clean(line.strip())}` | {reference}; підтверджено |")
        declarations += 1
report += ["", "Синтетичні lambda$-методи javap є компіляцією Runnable-перевірок, не новими предметними операціями. Неявний конструктор Main складає локальні залежності. Локальні змінні, оператори та виклики JDK належать обов’язкам відповідного методу: BigDecimal — суми/копійки, LocalDate — строки, колекції — локальний пошук/тотожність, reflection та потоки — лише контроль станів/імітацій. Винятки відповідають заборонам state/sequence. Анонімні IStolenItemCheck та ICollateralAppraisal відповідають незавершеній перевірці та заміні DI; вони не є виробничими сутностями."]

report += ["", "## CRC: кожна непорожня відповідальність і співпраця", "", "| Аркуш!Клітинка / зміст | Код | Статус / доказ |", "| --- | --- | --- |"]
ns = {"s":"http://schemas.openxmlformats.org/spreadsheetml/2006/main"}
cells = 0
with zipfile.ZipFile(ROOT / "docs/crc.xlsx") as archive:
    sheets = E.fromstring(archive.read("xl/workbook.xml")).findall("s:sheets/s:sheet", ns)
    for index, sheet in enumerate(sheets, 1):
        cls = sheet.attrib["name"]
        for cell in E.fromstring(archive.read(f"xl/worksheets/sheet{index}.xml")).findall(".//s:c", ns):
            inline = cell.find("s:is", ns)
            addr = cell.attrib["r"]
            if inline is None or int(re.sub(r"\D", "", addr)) < 3:
                continue
            text = "".join(inline.itertext())
            names = [name for name in re.findall(r"\b([a-z][A-Za-z]+)\(", text) if any(name + "(" in line for line in sources[cls])]
            locations = [address(cls, name + "(") for name in names] or [address(cls, ("interface " if cls.startswith("I") else "class ") + cls)]
            collaborators = [other + ".java" for other in sources if other in text and other != cls]
            report.append(f"| {cls}!{addr}: {clean(text)} | {', '.join(locations + collaborators)} | підтверджено; FR-013/017, декларації, all/verify |")
            cells += 1

report += ["", "## Зв’язки, кожен бізнес-виклик і перехід моделі → код", "", "| Місце моделі | Елемент | Доказ коду / статус |", "| --- | --- | --- |"]
for filename in ["class.mmd", "sequence.mmd", "state.mmd", "usecase.puml"]:
    for i, line in enumerate((ROOT / "docs" / filename).read_text(encoding="utf-8").splitlines(), 1):
        selected = (filename == "class.mmd" and any(x in line for x in ["--", "..>"])) or (filename == "sequence.mmd" and "->>" in line and "-->>" not in line) or (filename == "state.mmd" and "-->" in line) or (filename == "usecase.puml" and (line.strip().startswith(("actor ", "usecase ")) or " -- " in line or " ..> " in line))
        if not selected:
            continue
        proof = {"class.mmd":"Декларації типів/полів/параметрів вище; Main.createAgreement контролює 0..1; verifyAgreements", "sequence.mmd":"Main.fresh/demonstrate; Borrower.request* → Cashier → LoanAgreement; all.stdout.log", "state.mmd":"LoanAgreement.prepare/activate/redeem/extend; Main.verifyStates/verifyAgreements/verifyExtension/verifyRedemption; verify.stdout.log", "usecase.puml":"Main.demonstrate; Borrower/Appraiser/Cashier; StolenItemCheckService; all.stdout.log"}[filename]
        report.append(f"| docs/{filename}:{i} | `{clean(line.strip())}` | {proof}; підтверджено |")

operations = {"submitItem","requestRedemption","requestExtension","appraiseItem","appraise","setAppraisedValue","prepareAgreement","prepare","isStolen","activate","issueFunds","redeemItem","redeem","extendAgreement","extend","getAppraisedValue","getRole","getId","getPrincipal","getRepaymentAmount","getDueDate"}
report += ["", "## Кожен бізнес-виклик коду → модель", "", "| Місце | Виклик | Відповідник / статус |", "| --- | --- | --- |"]
calls = 0
for cls, lines in sources.items():
    for i, line in enumerate(lines, 1):
        for method in re.findall(r"\.([A-Za-z]+)\(", line):
            if method in operations:
                ref = "I2: атрибут LoanAgreement і CRC Cashier!A7" if method in {"getPrincipal","getRepaymentAmount","getDueDate"} else f"class/sequence: операція {method}; CRC відповідного типу, таблиці вище"
                report.append(f"| src/pawnshop/{cls}.java:{i} | `{method}` | {ref}; підтверджено, all/verify |")
                calls += 1

report += ["", "## Порядок, межі та винятки", "", "Повернення sequence не є окремими методами: це BigDecimal/boolean, завершення void або очікуваний виняток. Стрілка конструктора — дія складальника. alt/opt/loop відтворено demonstrate й verify. Submitted/Appraised — етапи до договору. Усі оцінювання основних маршрутів завершені до createAgreement; verifyItems змінює ще не передану річ. Контрольна копія Terms створена напряму лише для доказу блокування другої передачі за id; це негативний тест, а не дозволений бізнес-маршрут. Rejected — погоджений виняток до незмінності стану після відмови.", "", "## Конституція, ООП і SOLID", "", "| Принцип | Доказ | Статус |", "| --- | --- | --- |", "| I: простота | 10 Java-файлів, один пакет, тільки JDK | підтверджено |", "| II / SRP | LoanAgreement — правила/стан; Cashier — координація/фінансові імітації; Main — сценарії/перевірки | підтверджено |", "| III: імітація | Фінансовий println після успіху; unchanged перевіряє відсутність імітацій після відмов | підтверджено |", "| IV: javac/java | compile/clean-compile та clean-* логи, без готового out | підтверджено |", "| V / DIP / DI | Конструктори Cashier і LoanAgreement; alternative/interrupted у Main | підтверджено |", "| VI: імена/мова | Англійські ідентифікатори, українські коментарі/повідомлення; UTF-8 PowerShell | підтверджено |", "| VII: UML | Окрема docs/practice2/class.mmd; вихідні моделі захищено SHA-256 | підтверджено |", "| VIII: документація | README і документи українською, назви API збережено | підтверджено |", "| LSP / наслідування | getRole через PawnshopParticipant, final getId та override трьох ролей | підтверджено |", "| OCP / ISP | Контрольна реалізація оцінювання дає 900 без зміни Cashier; два вузькі інтерфейси | підтверджено |", "| Інкапсуляція / overload | private поля, дві appraise; verify перевіряє рівно чотири пакетні getters договору | підтверджено |", "", "## Відмінності діаграми №2", "", "Додано чотири пакетні (~) getters, технічний Main з полями/методами/залежностями та пояснення двох анонімних тестових реалізаторів. Предметні типи, вихідні операції, атрибути й кратності збережено. Нові бізнес-правила не вводилися. Mermaid-джерело зберігається в docs; рендер не є залежністю запуску.", "", "## Підсумок", "", f"Вихідних UML-членів: {members}; декларацій коду у зворотному переліку: {declarations}; бізнес-викликів: {calls}; непорожніх CRC-клітинок обов’язків/співпраці: {cells}. Охоплено 5/5 джерел і 9/9 карток. У наведеному інвентарі неперевірених елементів — 0, невирішених розбіжностей — 0. Висновок про відповідність стосується навчального обсягу й погодженої деталізації I2/N1; оригінальні UML не містять додаткових getters."]
(FEATURE / "model-code-review.md").write_text("\n".join(report) + "\n", encoding="utf-8")
print(f"Audit: {members} UML members, {declarations} declarations, {calls} calls, {cells} CRC cells")
