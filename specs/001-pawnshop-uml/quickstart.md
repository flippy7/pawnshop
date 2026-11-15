# Настанова перевірки: задача 001

Комплект створено в `docs/`, генератор книги — `scripts/generate_crc.py`.
Робочий каталог усіх команд — корінь репозиторію. Фактичні результати перевірки
збережено в `%TEMP%/pawnshop-uml-001-review/validation.md` і `traceability.md`.

## Передумови

Потрібні Python 3.11+, openpyxl, локальний PlantUML JAR із сумісним Java,
Mermaid CLI з його локальним браузерним рушієм та переглядач XLSX.
Під час реалізації перевірено Python 3.14.0, openpyxl 3.1.5, Java 25.0.4,
PlantUML 1.2026.2 та Mermaid CLI 12.2.0. Python/openpyxl розміщено в `.venv/`,
PlantUML — `.tools/plantuml.jar`, Mermaid CLI — `.tools/node_modules/.bin/mmdc.cmd`.
Файл `.tools/puppeteer.json` задає локальний Chrome. Ці службові каталоги ігнорує Git.
На іншому комп’ютері потрібно підготувати інструменти й указати власний шлях браузера.
Перевірки використовують локальні засоби без онлайн-редакторів.

Для нового середовища: `python -m venv .venv`,
`.venv/Scripts/python.exe -m pip install openpyxl==3.1.5`,
`npm.cmd install --prefix .tools @mermaid-js/mermaid-cli@12.2.0`.
PlantUML JAR 1.2026.2 потрібно зберегти в `.tools/plantuml.jar`.
Для генерації можна застосувати точний шлях інтерпретатора нижче або додати
`.venv/Scripts` до PATH і виконати `python scripts/generate_crc.py`.

## Послідовність перевірки

1. Чотири текстові джерела вже підготовлені за [контрактом](contracts/artifacts.md).
   Щоб відтворити книгу з фіксованих даних дев’яти карток:

   ```powershell
   .venv/Scripts/python.exe scripts/generate_crc.py
   ```

   Очікування: дев’ять карток, успішне повторне читання, код завершення 0.
2. Перевірити наявність і розміри:

   ```powershell
   Get-Item docs/usecase.puml, docs/class.mmd, docs/sequence.mmd, docs/state.mmd, docs/crc.xlsx
   ```

   Очікування: п'ять непорожніх файлів за точними шляхами.
3. Створити тимчасовий каталог перевірки й відобразити Mermaid:

   ```powershell
   $umlCheckDir = Join-Path $env:TEMP 'pawnshop-uml-001-review'
   New-Item -ItemType Directory -Force -Path $umlCheckDir
   .tools/node_modules/.bin/mmdc.cmd -p .tools/puppeteer.json -i docs/class.mmd -o "$umlCheckDir/class.svg"
   .tools/node_modules/.bin/mmdc.cmd -p .tools/puppeteer.json -i docs/sequence.mmd -o "$umlCheckDir/sequence.svg"
   .tools/node_modules/.bin/mmdc.cmd -p .tools/puppeteer.json -i docs/state.mmd -o "$umlCheckDir/state.svg"
   ```

   Очікування: код завершення 0 для кожної команди та три читабельні SVG.
4. Для PlantUML установити `PLANTUML_JAR` на фактичний абсолютний шлях локального JAR:

   ```powershell
   $env:PLANTUML_JAR = (Resolve-Path .tools/plantuml.jar).Path
   java -jar "$env:PLANTUML_JAR" -charset UTF-8 -tsvg -o "$umlCheckDir" docs/usecase.puml
   ```

   Очікування: SVG варіантів використання без повідомлення про синтаксичну помилку.
   Перевірити результат і код завершення; наявність файлу сама по собі недостатня.
5. Повторно прочитати книгу:

   ```powershell
   .venv/Scripts/python.exe -c "from openpyxl import load_workbook; w=load_workbook('docs/crc.xlsx'); print(w.sheetnames); assert len(w.sheetnames)==9; assert all(s['A2'].value=='Обов’язки класу'.replace('’', chr(39)) and s['B2'].value=='З ким співпрацює' for s in w)"
   ```

   Очікування: дев'ять імен із data-model.md, без винятків. Додатково зіставити
   точну множину назв із моделлю, перевірити рядки змісту та відкрити XLSX у переглядачі.
6. Переглянути всі SVG та книгу: український текст не обрізаний, зв'язки й кратності
   читабельні, final/override видно, обидві сигнатури appraise присутні.
7. Зіставити кожну сигнатуру й зв'язок з data-model.md, кожен виклик — з методом
   отримувача; пройти сценарії нижче. Додатково перевірити всі граничні випадки spec.md.

## Чотири контрольні маршрути

- **Викуп**: оцінка 1000, позика 800, належне погашення 840; річ не викрадена.
  Після видачі стан Active, після сплати 840 — Redeemed, повернення і кінець.
- **Продовження**: той самий договір; строк 2026-12-15 змінюється на 2027-01-15.
  Стан Active зберігається, повторної видачі немає; далі викуп і кінець.
- **Відмова**: ідентифікатор речі є в демонстраційному stolenItemIds.
  Після prepare — Rejected, кошти не видаються, процес завершується.
- **Недопустима дія**: платіж 839 замість 840 або строк не пізніший за чинний.
  Відмова без зміни Active; повторна видача та дії після Redeemed також заборонені.

Числа й дати — навчальні приклади, не тариф чи юридичне правило.
Зіставлення операцій і станів визначене в [моделі](data-model.md).

## Звіт

Записати версії засобів, фактичні команди, коди завершення, спостережені результати,
підсумок FR-001–FR-023 та SC-001–SC-007. Невиконану перевірку позначити невиконаною.
Java-код не генерується, тому компіляція застосунку для цього комплекту не потрібна.
