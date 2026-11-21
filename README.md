# ISO2-2026-Grupo3A102-Testing-P3

Problem 3 (ET.02.04, Irrigation) of the second theoretical exercise of Software Engineering II (UCLM, 2026/27), group 3A1.02. The program recommends how to irrigate a greenhouse from the soil moisture, the temperature, the rain forecast and the time of day.

| Step | Subgroup | Members |
|---|---|---|
| Step 1 – Program | Pair 3 | Denis-Alexandru Ionescu Telibasa, Rodrigo Muñoz Martín, David Calzado Olmo |
| Step 2 – Test design | Pair 1 | Marcos Loro Carrasco, Ignacio Romero Torres |
| Step 3 – JUnit tests | Pair 2 | Adam Gouram El Bouhali, Javier Villanueva Crespo |

Tasks, dates and how we work: [Planning](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/wiki/Planning) in the wiki.

## Requirements

- Java 21 or later
- Maven 3.9 or later

## Build and test

```
mvn verify
```

- Test results (Surefire): `target/surefire-reports`
- Coverage report (JaCoCo): `target/site/jacoco/index.html`. The build fails if less than 80 % of the lines are covered.

## Structure

| Package | Content |
|---|---|
| `es.uclm.iso2.irrigation.domain` | Input data, validations and business rules |
| `es.uclm.iso2.irrigation.ui` | Command-line interface |
| `src/test/java` | JUnit tests (Step 3) |

## Decisions on the statement

The statement does not answer these questions. Pair 3 agrees on them before the code is merged into `develop`.

| # | Question | Answer |
|---|---|---|
| Q1 | Which values are valid for moisture, temperature and time, and what happens with invalid ones? | *pending* |
| Q2 | Are 12:00 and 16:00 inside the peak solar radiation hours? | *pending* |
| Q3 | When several rules apply at the same time, which one wins? | *pending* |
| Q4 | During peak hours, with moisture below 10 % and rain forecast: Critical Watering or Do Not Water? | *pending* |
| Q5 | Does Critical Watering apply outside peak hours? | *pending* |
| Q6 | Does "between 20 % and 40 %" include 20 and 40? | *pending* |
| Q7 | What does the program say when a watering is postponed during peak hours, and when no rule applies? | *pending* |
| Q8 | Is "imminent rain" the same as "rain forecast within the next 24 hours"? | *pending* |
