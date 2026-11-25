# TW.02.01 – Test Design (Pair 1)

_Problem 3 (ET.02.04, Irrigation) – Step 2: Test design_

**Authors:** Marcos Loro Carrasco, Ignacio Romero Torres  
**Starts when:** Step 1 (#12) is on `develop`  
**Sub-issues:** #14, #15, #16  
**Related PDF:** `ISO2-2026-Grupo3A102-Testing-P3.pdf` (required by #13)

---

## 1. Pseudocode of the method that gives the recommendation

```text
Recommendation recommend(GreenhouseConditions conditions):
    boolean peakHours = conditions.timeOfDay >= 12:00 AND conditions.timeOfDay < 16:00

    IF conditions.soilMoisture > 60 AND conditions.temperature < 5:
        RETURN Emergency Drainage

    IF peakHours AND conditions.soilMoisture < 10:
        RETURN Critical Watering

    IF conditions.rainForecast AND conditions.soilMoisture < 40:
        RETURN Do Not Water

    IF NOT conditions.rainForecast AND conditions.temperature > 25 AND conditions.soilMoisture <= 40:
        IF peakHours:
            RETURN Postpone Watering
        IF conditions.soilMoisture < 20:
            RETURN Heavy Watering
        RETURN Light Watering  // 20 <= moisture <= 40 (Q6)

    RETURN No Action
```

---

## 2. Test units

| # | Test unit | Description |
|---|---|---|
| TU-01 | `recommend()` – Emergency Drainage | Verify Emergency Drainage when moisture > 60 and temperature < 5 |
| TU-02 | `recommend()` – Critical Watering in peak hours | Verify Critical Watering when peak hours and moisture < 10 |
| TU-03 | `recommend()` – Do Not Water with rain | Verify Do Not Water when rain forecast and moisture < 40 |
| TU-04 | `recommend()` – Heavy Watering without rain, hot, low moisture | Verify Heavy Watering when no rain, temperature > 25 and moisture < 20 |
| TU-05 | `recommend()` – Light Watering without rain, hot, medium moisture | Verify Light Watering when no rain, temperature > 25 and 20 ≤ moisture ≤ 40 |
| TU-06 | `recommend()` – Postpone Watering in peak hours | Verify Postpone Watering when no rain, temperature > 25 and peak hours |
| TU-07 | `recommend()` – No Action otherwise | Verify No Action in all other cases |
| TU-08 | Constructor – Invalid moisture | Verify InvalidReadingException when moisture < 0 or moisture > 100 |
| TU-09 | Constructor – Invalid temperature | Verify InvalidReadingException when temperature is NaN or infinite |
| TU-10 | Constructor – Null time | Verify InvalidReadingException when timeOfDay is null |

---

## 3. Variables

| Variable | Type | Valid values (Q1) | Description |
|---|---|---|---|
| Soil moisture | double | 0 to 100 % (both included) | Moisture level measured in percentage |
| Temperature | double | Any finite number (°C) | Temperature measurement |
| Rain forecast | boolean | true / false | Whether rain is forecast in the next 24 hours |
| Time of day | LocalTime | Any valid time; not null | Time of day for peak hours evaluation |

**Named constants from `GreenhouseConditions`:**
- `MIN_MOISTURE = 0`
- `MAX_MOISTURE = 100`
- `CRITICAL_MOISTURE = 10`
- `HEAVY_WATERING_MOISTURE = 20`
- `LIGHT_WATERING_MOISTURE = 40`
- `DRAINAGE_MOISTURE = 60`
- `COLD_TEMPERATURE = 5`
- `HOT_TEMPERATURE = 25`
- `PEAK_START = 12:00`
- `PEAK_END = 16:00`

---

## 4. Test values of each variable (techniques: equivalence classes, boundary values, error guessing)

### 4.1 Equivalence classes

| Variable | Valid equivalence classes | Invalid equivalence classes |
|---|---|---|
| Soil moisture | [0, 100] (whole range) | < 0, > 100, NaN |
| Temperature | ℝ \ {NaN, ±∞} (all finite numbers) | NaN, +∞, −∞ |
| Rain forecast | {true, false} | N/A |
| Time of day | {00:00, 01:00, …, 23:59} | null |

### 4.2 Boundary values (limits from the README)

| Boundary | Value(s) | Rationale |
|---|---|---|
| Moisture lower bound | 0 | Included in valid range (Q1) |
| Moisture upper bound | 100 | Included in valid range (Q1) |
| Critical moisture threshold | 10 | Below → Critical Watering in peak hours; above → other rules apply |
| Heavy Watering threshold | 20 | Below → Heavy Watering; from 20 included → Light Watering (Q6) |
| Light Watering threshold | 40 | Below → Do Not Water with rain; from 40 → other rules (Q6) |
| Drainage threshold | 60 | Above → Emergency Drainage if cold |
| Cold temperature threshold | 5 | Below → Emergency Drainage if moisture > 60 |
| Hot temperature threshold | 25 | Above → watering rules apply; below → other considerations |
| Peak start | 12:00 | Included in peak hours (Q2) |
| Peak end | 16:00 | Not included in peak hours (Q2) |

### 4.3 Test values per boundary

| Boundary | Test values (below/at/above) |
|---|---|
| Moisture 0 | 0 (valid), −1 (invalid) |
| Moisture 100 | 100 (valid), 101 (invalid) |
| Moisture 10 | 9 (below), 10 (at), 11 (above) |
| Moisture 20 | 19 (below), 20 (at), 21 (above) |
| Moisture 40 | 39 (below), 40 (at), 41 (above) |
| Moisture 60 | 59 (below), 60 (at), 61 (above) |
| Temperature 5 | 4 (below), 5 (at), 6 (above) |
| Temperature 25 | 24 (below), 25 (at), 26 (above) |

### 4.4 Error guessing test values

| Scenario | Test value | Expected outcome |
|---|---|---|
| Negative moisture | −5 | InvalidReadingException |
| Over 100% moisture | 101 | InvalidReadingException |
| NaN moisture | Double.NaN | InvalidReadingException |
| Infinite temperature | Double.POSITIVE_INFINITY | InvalidReadingException |
| Null time | null | InvalidReadingException |
| Rain = "y" (yes) | true | Rain forecast = true |
| Rain = "n" (no) | false | Rain forecast = false |
| 00:00 (midnight) | LocalTime.MIDNIGHT | Not peak hours |
| 12:00 (noon) | LocalTime.of(12, 0) | Peak hours start (included) |
| 15:59 | LocalTime.of(15, 59) | Peak hours (still included) |
| 16:00 | LocalTime.of(16, 0) | Peak hours end (not included) |
| 23:59 | LocalTime.of(23, 59) | Not peak hours |

---

## 5. Test design summary matrix

_Test design matrix with all 5 decision table rows covered, including boundary values and edge cases:_

| TC # | Moisture | Temperature | Rain forecast | Time of day | Expected recommendation | Decision row |
|------|----------|-------------|---------------|-------------|------------------------|--------------|
| TC-01 | 80 | 3 | true | 10:00 | Emergency Drainage | 1 |
| TC-02 | 85 | 2 | true | 10:00 | Emergency Drainage | 1 |
| TC-03 | 70 | 4 | true | 10:00 | Emergency Drainage | 1 |
| TC-04 | 80 | 3 | false | 10:00 | Emergency Drainage | 1 |
| TC-05 | 70 | 3 | false | 10:00 | Emergency Drainage | 1 |
| TC-06 | 80 | 26 | false | 10:00 | No Action | 5 |
| TC-07 | 30 | 30 | false | 10:00 | Light Watering | 4c |
| TC-08 | 15 | 30 | false | 10:00 | Heavy Watering | 4b |
| TC-09 | 5 | 3 | true | 13:00 | Critical Watering | 2 |
| TC-10 | 5 | 3 | false | 13:00 | Critical Watering | 2 |
| TC-11 | 5 | 3 | true | 10:00 | Do Not Water | 3 |
| TC-12 | 35 | 30 | true | 13:00 | Do Not Water | 3 |
| TC-13 | 35 | 30 | false | 13:00 | Postpone Watering | 4a |
| TC-14 | 25 | 30 | false | 13:00 | Postpone Watering | 4a |
| TC-15 | 20 | 30 | false | 13:00 | Postpone Watering | 4a |
| TC-16 | 19 | 30 | false | 13:00 | Postpone Watering | 4a |
| TC-17 | 15 | 30 | true | 13:00 | Do Not Water | 3 |
| TC-18 | 80 | 30 | false | 13:00 | No Action | 5 |
| TC-19 | 80 | 30 | true | 13:00 | No Action | 5 |
| TC-20 | 95 | 3 | true | 13:00 | Emergency Drainage | 1 |
| TC-21 | 95 | 6 | true | 13:00 | No Action | 5 |
| TC-22 | 95 | 6 | false | 13:00 | No Action | 5 |
| TC-23 | 45 | 30 | true | 13:00 | No Action | 5 |
| TC-24 | 45 | 30 | false | 13:00 | No Action | 5 |
| TC-25 | 35 | 30 | false | 13:30 | Postpone Watering | 4a |
| TC-26 | 25 | 30 | false | 13:30 | Postpone Watering | 4a |
| TC-27 | 20 | 30 | false | 13:30 | Postpone Watering | 4a |
| TC-28 | 19 | 30 | false | 13:30 | Postpone Watering | 4a |
| TC-29 | 30 | 30 | true | 11:00 | Do Not Water | 3 |
| TC-30 | 30 | 30 | false | 11:00 | Light Watering | 4c |
| TC-31 | 5 | 3 | true | 13:00 | Critical Watering | 2 |
| TC-32 | 10 | 3 | false | 13:00 | No Action | 5 |
| TC-33 | 9 | 3 | false | 13:00 | Critical Watering | 2 |
| TC-34 | 9 | 3 | false | 11:00 | No Action | 5 |
| TC-35 | 95 | 3 | true | 13:30 (peak) | Emergency Drainage | 1 |
| TC-36 | 70 | 3 | true | 13:30 (peak) | Emergency Drainage | 1 |
| TC-37 | 70 | 6 | true | 13:30 (peak) | No Action | 5 |
| TC-38 | 70 | 6 | false | 13:30 (peak) | No Action | 5 |
| TC-39 | 30 | 30 | true | 11:00 (not peak) | Do Not Water | 3 |
| TC-40 | 30 | 30 | false | 11:00 (not peak) | Light Watering | 4c |
| TC-41 | 5 | 3 | true | 13:00 | Critical Watering | 2 |
| TC-42 | 80 | 30 | false | 13:00 | No Action | 5 |
| TC-43 | 80 | 30 | true | 13:00 | No Action | 5 |

---

## 6. Decision table (from README, checked top to bottom)

| # | Condition | Output |
|---|---|---|
| 1 | moisture > 60 and temperature < 5 | Emergency Drainage |
| 2 | peak hours and moisture < 10 | Critical Watering |
| 3 | rain forecast and moisture < 40 | Do Not Water |
| 4 | no rain, temperature > 25 and moisture ≤ 40 | a watering: 4a) Postpone Watering in peak hours; 4b) Heavy Watering if moisture < 20; 4c) Light Watering (20 ≤ moisture ≤ 40, Q6) |
| 5 | none of the above | No Action |

---

## 7. MC/DC test pairs

Inputs are listed as soil moisture (%), temperature (C), rain forecast and time. In each pair only the input for the named condition changes. Earlier decisions are false in both cases, so the decision under test is reached. At 40% moisture, D3 is false even when rain is forecast.

| Condition | Case A | Expected A | Case B | Expected B |
|---|---|---|---|---|
| 1. D1: moisture > 60 | 61 / 3 / false / 10:00 | Emergency Drainage | 60 / 3 / false / 10:00 | No Action |
| 2. D1: temperature < 5 | 61 / 4 / false / 10:00 | Emergency Drainage | 61 / 5 / false / 10:00 | No Action |
| 3. D2: peak hours | 9 / 20 / false / 12:00 | Critical Watering | 9 / 20 / false / 11:59 | No Action |
| 4. D2: moisture < 10 | 9 / 20 / false / 13:00 | Critical Watering | 10 / 20 / false / 13:00 | No Action |
| 5. D3: rain forecast | 39 / 20 / true / 10:00 | Do Not Water | 39 / 20 / false / 10:00 | No Action |
| 6. D3: moisture < 40 | 39 / 20 / true / 10:00 | Do Not Water | 40 / 20 / true / 10:00 | No Action |
| 7. D4: no rain | 40 / 26 / false / 10:00 | Light Watering | 40 / 26 / true / 10:00 | No Action |
| 8. D4: temperature > 25 | 40 / 26 / false / 10:00 | Light Watering | 40 / 25 / false / 10:00 | No Action |
| 9. D4: moisture <= 40 | 40 / 26 / false / 10:00 | Light Watering | 41 / 26 / false / 10:00 | No Action |
| 10. D4a: peak hours | 30 / 26 / false / 15:59 | Postpone Watering | 30 / 26 / false / 16:00 | Light Watering |
| 11. D4b: moisture < 20 | 19 / 26 / false / 10:00 | Heavy Watering | 20 / 26 / false / 10:00 | Light Watering |

The peak-hours pairs exercise 11:59/12:00 and 15:59/16:00 independently. No Action is the fallback outcome and has no separate Boolean decision.

---

## 8. Variable equivalence class test coverage

| EC Class | Test values | Minimum tests needed |
|---|---|---|
| Moisture valid [0,100] | 0, 50, 100 | 3 |
| Moisture invalid | −1, 101 | 2 |
| Temperature finite | −100, 0, 100 | 3 |
| Temperature infinite/NaN | NaN, ±∞ | 2 |
| Rain forecast | true, false | 2 |
| Time of day | 00:00, 12:00, 23:59 | 3 |

Select representatives for each class. A Cartesian product of values is not a minimum for equivalence-class coverage.

---

## 9. Suggested test method names (@DisplayName)

| Method name | @DisplayName (English) |
|---|---|
| `tcEmergencyDrainage` | "TC03C-01 Emergency Drainage when moisture>60 and temperature<5" |
| `tcCriticalWatering` | "TC03C-02 Critical Watering when peak hours and moisture<10" |
| `tcDoNotWater` | "TC03C-03 Do Not Water when rain forecast and moisture<40" |
| `tcHeavyWatering` | "TC03C-04 Heavy Watering when no rain, temp>25 and moisture<20" |
| `tcLightWatering` | "TC03C-05 Light Watering when no rain, temp>25 and 20≤moisture≤40" |
| `tcPostponeWatering` | "TC03C-06 Postpone Watering when no rain, temp>25 and peak hours" |
| `tcNoAction` | "TC03C-07 No Action in all other cases" |
| `tcInvalidMoistureLow` | "TC03C-08 Constructor: InvalidReadingException if moisture<0" |
| `tcInvalidMoistureHigh` | "TC03C-09 Constructor: InvalidReadingException if moisture>100" |
| `tcInvalidTemperature` | "TC03C-10 Constructor: InvalidReadingException if temperature is NaN/∞" |
| `tcInvalidTime` | "TC03C-11 Constructor: InvalidReadingException if timeOfDay is null" |

---

## 10. Test values table (complete and verified against the decision table)

| # | Soil moisture | Temperature | Rain forecast | Time of day | Expected recommendation | Decision row | Verified |
|---|---|---|---|---|---|---|---|
| 1 | 80 | 3 | true | 10:00 | Emergency Drainage | 1 | ✓ |
| 2 | 85 | 2 | true | 10:00 | Emergency Drainage | 1 | ✓ |
| 3 | 70 | 4 | true | 10:00 | Emergency Drainage | 1 | ✓ |
| 4 | 80 | 3 | false | 10:00 | Emergency Drainage | 1 | ✓ |
| 5 | 70 | 3 | false | 10:00 | Emergency Drainage | 1 | ✓ |
| 6 | 80 | 26 | false | 10:00 | No Action | 5 | ✓ |
| 7 | 30 | 30 | false | 10:00 | Light Watering | 4c | ✓ |
| 8 | 15 | 30 | false | 10:00 | Heavy Watering | 4b | ✓ |
| 9 | 5 | 3 | true | 13:00 | Critical Watering | 2 | ✓ |
| 10 | 5 | 3 | false | 13:00 | Critical Watering | 2 | ✓ |
| 11 | 5 | 3 | true | 10:00 | Do Not Water | 3 | ✓ |
| 12 | 35 | 30 | true | 13:00 | Do Not Water | 3 | ✓ |
| 13 | 35 | 30 | false | 13:00 | Postpone Watering | 4a | ✓ |
| 14 | 25 | 30 | false | 13:00 | Postpone Watering | 4a | ✓ |
| 15 | 20 | 30 | false | 13:00 | Postpone Watering | 4a | ✓ |
| 16 | 19 | 30 | false | 13:00 | Postpone Watering | 4a | ✓ |
| 17 | 15 | 30 | true | 13:00 | Do Not Water | 3 | ✓ |
| 18 | 80 | 30 | false | 13:00 | No Action | 5 | ✓ |
| 19 | 80 | 30 | true | 13:00 | No Action | 5 | ✓ |
| 20 | 95 | 3 | true | 13:00 | Emergency Drainage | 1 | ✓ |
| 21 | 95 | 6 | true | 13:00 | No Action | 5 | ✓ |
| 22 | 95 | 6 | false | 13:00 | No Action | 5 | ✓ |
| 23 | 45 | 30 | true | 13:00 | No Action | 5 | ✓ |
| 24 | 45 | 30 | false | 13:00 | No Action | 5 | ✓ |
| 25 | 35 | 30 | false | 13:30 | Postpone Watering | 4a | ✓ |
| 26 | 25 | 30 | false | 13:30 | Postpone Watering | 4a | ✓ |
| 27 | 20 | 30 | false | 13:30 | Postpone Watering | 4a | ✓ |
| 28 | 19 | 30 | false | 13:30 | Postpone Watering | 4a | ✓ |
| 29 | 30 | 30 | true | 11:00 | Do Not Water | 3 | ✓ |
| 30 | 30 | 30 | false | 11:00 | Light Watering | 4c | ✓ |
| 31 | 5 | 3 | true | 13:00 | Critical Watering | 2 | ✓ |
| 32 | 10 | 3 | false | 13:00 | No Action | 5 | ✓ |
| 33 | 9 | 3 | false | 13:00 | Critical Watering | 2 | ✓ |
| 34 | 9 | 3 | false | 11:00 | No Action | 5 | ✓ |
| 35 | 95 | 3 | true | 13:30 (peak) | Emergency Drainage | 1 | ✓ |
| 36 | 70 | 3 | true | 13:30 (peak) | Emergency Drainage | 1 | ✓ |
| 37 | 70 | 6 | true | 13:30 (peak) | No Action | 5 | ✓ |
| 38 | 70 | 6 | false | 13:30 (peak) | No Action | 5 | ✓ |
| 39 | 30 | 30 | true | 11:00 (not peak) | Do Not Water | 3 | ✓ |
| 40 | 30 | 30 | false | 11:00 (not peak) | Light Watering | 4c | ✓ |
| 41 | 5 | 3 | true | 13:00 | Critical Watering | 2 | ✓ |
| 42 | 80 | 30 | false | 13:00 | No Action | 5 | ✓ |
| 43 | 80 | 30 | true | 13:00 | No Action | 5 | ✓ |

_(All 43 test cases verified against the decision table from the README. Row numbers correspond to the 5-row decision table.)_

---

## Test execution guidance

1. **Create branch**: `git switch develop`, `git pull`, `git switch -c hotfix`
2. **Update pom.xml**: Set `<haltOnFailure>true</haltOnFailure>` (Step 3 requirement)
3. **Write tests** in `src/test/java/es/uclm/iso2/irrigation/domain/` following the test units and cases above
4. **Run**: `mvn verify`
   - test results: `target/surefire-reports`
   - coverage: open `target/site/jacoco/index.html` in a browser
5. **Report**: Copy results to wiki page [Test reports](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/wiki/Test-reports)
6. **Pull request**: Open PR from `hotfix` to `develop`, non-author approval required (D-001)

**Note:** The PDF `ISO2-2026-Grupo3A102-Testing-P3.pdf` must be referenced in the wiki test reports section as required by issue #13.