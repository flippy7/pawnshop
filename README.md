# Імітаційна система «Ломбард»

Навчальний автономний Java-прототип: позичальник подає річ, оцінювач визначає вартість, касир оформлює договір, імітує видачу, погашення та повернення. Зовнішніх платежів, мережі, БД і збереження даних немає. Умови 1000/800/840 — навчальні приклади, не тарифи реального ломбарду.

## Середовище та запуск у PowerShell

Потрібні JDK 17 або новіший, java і javac у PATH. IDE, Maven/Gradle й завантаження бібліотек не потрібні. Відкрийте PowerShell у корені цього репозиторію (каталозі з src, docs, specs).

```powershell
java -version
javac -version
chcp 65001
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
$OutputEncoding = [Console]::OutputEncoding
New-Item -ItemType Directory -Force -Path out | Out-Null
javac --release 17 -encoding UTF-8 -d out src/pawnshop/*.java
if ($LASTEXITCODE -ne 0) { throw 'Помилка компіляції' }
java '-Dfile.encoding=UTF-8' '-Dsun.stdout.encoding=UTF-8' '-Dsun.stderr.encoding=UTF-8' -cp out pawnshop.Main all
if ($LASTEXITCODE -ne 0) { throw 'Помилка демонстрації' }
```

Лапки навколо параметрів -D потрібні для коректного передавання аргументів у PowerShell. Клас запуску — pawnshop.Main, вихідні class-файли — out/pawnshop. Компілюються десять Java-файлів.

## Режими й очікувані результати

Без аргументу або `all` — чотири маршрути, код 0: оформлення/викуп; два продовження/викуп; викрадення з Rejected без виплати; некоректні дії зі збереженням стану. За успішний цикл видача й повернення відбуваються по одному разу. Дати продовження: 2026-12-15 → 2027-01-15 → 2027-02-15. Повторний запуск починається з нових даних.

```powershell
java '-Dfile.encoding=UTF-8' '-Dsun.stdout.encoding=UTF-8' '-Dsun.stderr.encoding=UTF-8' -cp out pawnshop.Main verify
if ($LASTEXITCODE -ne 0) { throw 'Перевірки не пройдено' }
java '-Dfile.encoding=UTF-8' '-Dsun.stdout.encoding=UTF-8' '-Dsun.stderr.encoding=UTF-8' -cp out pawnshop.Main fault-check
if ($LASTEXITCODE -ne 1) { throw 'Не підтверджено аварійне завершення' }
```

`verify` перевіряє дані, стани, точність грошей, тотожність речей, DI, фінансові імітації й відмови; не потребує -ea. Код 0 означає успіх усіх перевірок. `fault-check` навмисно завершується кодом 1 з українським stderr, без бізнес-маршрутів. Очікувані бізнес-відмови в all/verify залишають код 0; інші помилки припиняють запуск кодом 1. Невідомий або зайвий аргумент дає код 2 та інструкцію, без сценаріїв.

Оцінювання завершується до договору. Унікальність id контролює складальник на весь запуск; id непорожні, без пробілів, регістр значущий. Цілі суми й копійки дозволені; частки копійки відхиляються без округлення. Повернення речі не знищує об’єкт. Переоцінювання, проценти, штрафи, здача, часткове погашення, прострочення та юридичні процедури не підтримуються. Дата звернення фіксована: 2026-11-15.

## Моделі й докази

Оригінали практичної №1: [класи](docs/class.mmd), [послідовність](docs/sequence.mmd), [стани](docs/state.mmd), [варіанти використання](docs/usecase.puml), [CRC](docs/crc.xlsx).

[Діаграма №2](docs/practice2/class.mmd) додає чотири погоджені пакетні методи читання LoanAgreement і технічний Main; оригінали №1 не редагувалися. Getters деталізують атрибути та не є операціями вихідної UML.

[Завдання](specs/002-pawnshop-prototype/tasks.md), [валідація](specs/002-pawnshop-prototype/validation.md), [двосторонній аудит](specs/002-pawnshop-prototype/model-code-review.md), [реальні логи](specs/002-pawnshop-prototype/logs/).
