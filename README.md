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
- Coverage report (JaCoCo): `target/site/jacoco/index.html`. The minimum is 80 % of the lines.

> Until Step 3 the coverage check only shows a warning (`haltOnFailure` is `false` in `pom.xml`), because the program is written before its tests. Pair 2 sets `haltOnFailure` to `true` in Step 3.

## Run the program

```
mvn compile
java -cp target/classes es.uclm.iso2.irrigation.ui.IrrigationConsole
```

The program asks for the soil moisture (%), the temperature (°C), whether rain is forecast in the next 24 hours (`y`/`n`) and the time of day (`HH:mm`, 24 h), and shows the recommendation. Decimal numbers are written with a dot, for example `35.5`. It handles one set of conditions per run.

- If a value is not a number, not `y`/`n` or not a time, the program explains why and asks for that value again.
- If the data are out of range (Q1), the domain rejects them: the program shows the reason and asks for the four values again.

Example:

```
Irrigation advisor
Soil moisture (%): 15
Temperature (Celsius): 30
Rain forecast in the next 24 hours? (y/n): n
Time of day (HH:mm): 9:30
Recommendation: Heavy Watering
```

## Structure

| Package | Content |
|---|---|
| `es.uclm.iso2.irrigation.domain` | Input data, validations and business rules |
| `es.uclm.iso2.irrigation.ui` | Command-line interface |
| `src/test/java` | JUnit tests (Step 3) |

## Decisions on the statement

The statement leaves these questions open. These are the answers the program follows, so that Pair 1 can design the tests from them. New decisions are added as Q9, Q10…

| # | Question | Answer | Why |
|---|---|---|---|
| Q1 | Which values are valid, and what happens with invalid ones? | Moisture from 0 to 100 % (both included); temperature any finite number; time required. Otherwise the constructor throws an exception. | A percentage cannot be outside 0–100, and the statement gives no temperature range, so we do not invent one. |
| Q2 | Are 12:00 and 16:00 inside the peak hours? | 12:00 is inside, 16:00 is outside (from 12:00 to 15:59). | At 4:00 PM the peak ends, and this way each time belongs to only one period. |
| Q3 | When several rules apply at the same time, which one wins? | In this order: 1) Emergency Drainage, 2) Critical Watering, 3) Do Not Water, 4) Heavy or Light Watering, which are postponed in peak hours. | Drainage protects the roots from freezing and is not a watering cycle; Critical Watering is the only exception the statement gives to the peak hours rule. |
| Q4 | Peak hours, moisture below 10 % and rain forecast: Critical Watering or Do Not Water? | Critical Watering. | The statement says "unless soil moisture drops below 10 %" without any exception for rain. |
| Q5 | Does Critical Watering apply outside peak hours? | No: outside peak hours the normal rules apply (for example, moisture below 10 %, more than 25 °C and no rain gives Heavy Watering). | The statement only names this protocol inside the peak hours. |
| Q6 | Does "between 20 % and 40 %" include 20 and 40? | Yes, both. | Heavy Watering is "below 20 %", so 20 % must belong to Light Watering to leave no gap. |
| Q7 | What is the output when a watering is postponed, and when no rule applies? | Two outputs of our own: "Postpone Watering" and "No Action". | The statement asks to postpone but names no output, and a separate output lets the tests tell these cases apart from "Do Not Water". |
| Q8 | Is "imminent rain" the same as "rain forecast within the next 24 hours"? | Yes: one yes/no input, "rain forecast in the next 24 hours". | The statement only gives one rain forecast as input, and a second one would add a variable with no rule of its own. |

## For Pair 1 – Test design (Step 2)

Everything needed for the sections of TW.02.01, taken from the decisions above.

*Why this section:* Pair 1 did not write the program. With the inputs, outputs, decision table and limits in one place, they can write the pseudocode, the variables, the equivalence classes, the boundary values and the decision and MC/DC coverage without guessing what the code does.

**Methods to test** (package `es.uclm.iso2.irrigation.domain`)

| Class | Method | Throws |
|---|---|---|
| `GreenhouseConditions` | constructor `GreenhouseConditions(double soilMoisture, double temperature, boolean rainForecast, LocalTime timeOfDay)`: validates the inputs (Q1) | `InvalidReadingException` (a subclass of `IllegalArgumentException`) if the moisture is NaN or outside 0–100, the temperature is NaN or infinite, or the time is `null` |
| `GreenhouseConditions` | `Recommendation recommend()`: the rules in the same order as the decision table below | nothing |

The outputs are the values of the enum `Recommendation`: `EMERGENCY_DRAINAGE`, `CRITICAL_WATERING`, `DO_NOT_WATER`, `HEAVY_WATERING`, `LIGHT_WATERING`, `POSTPONE_WATERING` and `NO_ACTION`; `getLabel()` returns the text to show (for example `"Heavy Watering"`): the name in the statement, or our own name for "Postpone Watering" and "No Action" (Q7). The limits are the public constants of `GreenhouseConditions` (`CRITICAL_MOISTURE`, `HEAVY_WATERING_MOISTURE`, `LIGHT_WATERING_MOISTURE`, `DRAINAGE_MOISTURE`, `COLD_TEMPERATURE`, `HOT_TEMPERATURE`, `PEAK_START`, `PEAK_END`, `MIN_MOISTURE`, `MAX_MOISTURE`).

**Inputs**

| Variable | Type | Valid values (Q1) |
|---|---|---|
| Soil moisture | decimal number (%) | 0 to 100, both included |
| Temperature | decimal number (°C) | any finite number |
| Rain forecast in the next 24 hours | yes / no | yes, no |
| Time of day | hours and minutes | any time; it cannot be missing |

**Outputs:** Emergency Drainage, Critical Watering, Do Not Water, Heavy Watering, Light Watering, Postpone Watering, No Action, or an exception for invalid data.

**Decision table** (checked from top to bottom; the first row that matches wins, Q3)

| # | Condition | Output |
|---|---|---|
| 1 | moisture > 60 and temperature < 5 | Emergency Drainage |
| 2 | peak hours and moisture < 10 | Critical Watering |
| 3 | rain forecast and moisture < 40 | Do Not Water |
| 4 | no rain, temperature > 25 and moisture ≤ 40 | a watering: 4a) Postpone Watering in peak hours; 4b) otherwise Heavy Watering if moisture < 20; 4c) otherwise Light Watering (20 ≤ moisture ≤ 40, Q6) |
| 5 | none of the above | No Action |

Peak hours: from 12:00 (included) to 16:00 (not included) (Q2).

*Note for MC/DC:* with rain, moisture below 40 already returns Do Not Water in row 3, so "no rain" in row 4 only decides the result when moisture is exactly 40 (with rain: No Action; without rain: a watering). Use moisture 40 to cover it.

**Limits that appear in the rules**: moisture 0, 10, 20, 40, 60 and 100 %; temperature 5 and 25 °C; time 12:00 and 16:00.
*Why:* boundary value analysis tests the values on and next to each limit, so they are listed here and are named constants in the code.

## For Pair 2 – JUnit tests (Step 3)

*Why this section:* Pair 2 did not write the program or the test design. These steps are everything they need to go from the PDF to the reports, without setting anything up.

1. Create the branch `hotfix` from `develop`: `git switch develop`, `git pull`, `git switch -c hotfix`.
   *Why:* the statement requires the tests on a `hotfix` branch from `develop`, and `develop` is protected.
2. In `pom.xml`, change `<haltOnFailure>false</haltOnFailure>` to `true`.
   *Why:* until now the coverage check only warned, because the program had no tests yet. From Step 3 the build must fail below 80 % of lines, the minimum the statement asks for.
3. Write the tests of the PDF of Pair 1 in `src/test/java`, in the same package as the class they test (for example `src/test/java/es/uclm/iso2/irrigation/domain/`). JUnit is already in `pom.xml`.
   *Why:* in the same package the tests can use the classes directly, and Maven and JaCoCo find them without extra configuration.
4. Run `mvn verify`:
   - test results: `target/surefire-reports`
   - coverage: open `target/site/jacoco/index.html` in a browser

   *Why:* one command compiles, runs the tests with Surefire and measures the coverage with JaCoCo.
5. Copy the results to the wiki page [Test reports](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/wiki/Test-reports).
   *Why:* the statement asks for the testing report results in the wiki of each project.
6. Open a pull request from `hotfix` to `develop`. GitHub Actions runs `mvn verify` on it, and a member who is not the author approves it.
   *Why:* `develop` only changes through approved pull requests (D-001 of the main repository), and the automatic check shows that the tests pass on a clean machine, not only on yours.
