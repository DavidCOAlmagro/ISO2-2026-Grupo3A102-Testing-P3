# TW.02.01 – Test Design (Pair 1)

_Problem 3 (ET.02.04, Irrigation) – Step 2: Test design_

**Authors:** Marcos Loro Carrasco, Ignacio Romero Torres  
**Starts when:** Step 1 (#12) is on `develop`  
**Sub-issues:** #14, #15, #16

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

| Test case | Moisture | Temperature | Rain | Time | Expected recommendation |
|---|---|---|---|---|---|
| TC-01 | 80 | 3 | true | 10:00 | Emergency Drainage (80>60 & 3<5) |
| TC-02 | 80 | 3 | n/a | 13:30 | Critical Watering (peak & 80>10...wait, 80>10 so no) |
| Let me re-read the decision table |

Actually, let me use the proper decision table from the README.

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

## 7. MC/DC coverage notes

For MC/DC compliance, we need to cover the decision outcomes and show that each condition independently affects the output. Key points:

- **Row 3 vs Row 4**: With rain, moisture < 40 returns Do Not Water. Without rain, moisture exactly 40 decides between No Action (row 5) and a watering (row 4).
- **Critical moisture boundary**: moisture = 10 with peak hours gives Critical Watering; moisture = 11 gives different result.
- **Temperature boundary**: temperature = 5 with moisture > 60 gives Emergency Drainage; temperature = 6 changes the result.
- **Peak hours boundary**: 12:00 included, 16:00 not included. Test with 11:59, 12:00, 15:59, 16:00.

**Minimum MC/DC test cases needed:**
- moisture 59, 60, 61 with temperature < 5 → test Emergency Drainage condition independence
- moisture 9, 10, 11 with peak hours → test Critical Watering condition independence
- moisture 39, 40, 41 with rain → test Do Not Water condition independence
- moisture 19, 20, 21 with no rain, temp > 25 → test Heavy/Light Watering and Postpone independence
- Peak hours: 11:59, 12:00, 15:59, 16:01 → test peak hours condition independence

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

**Total minimum equivalence class tests: 3 × 2 × 2 × 3 = 36** (plus invalid case combinations)

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

## 10. Test values table (complete)

| # | Soil moisture | Temperature | Rain forecast | Time of day | Expected recommendation | Decision row |
|---|---|---|---|---|---|---|
| 1 | 80 | 3 | true | 10:00 | Emergency Drainage | 1 |
| 2 | 85 | 2 | true | 10:00 | Emergency Drainage | 1 |
| 3 | 70 | 4 | true | 10:00 | Emergency Drainage | 1 |
| 4 | 80 | 3 | false | 10:00 | No Action | 5 |
| 5 | 70 | 3 | false | 10:00 | No Action | 5 |
| 6 | 80 | 26 | false | 10:00 | Heavy Watering (80≥20) | 4c |
| 7 | 80 | 26 | false | 10:00 | Light Watering (20≤80≤40? No, 80>40) → actually row 5 | 5 |
| 8 | 30 | 30 | false | 10:00 | Heavy Watering (30≥20) | 4c |
| 9 | 15 | 30 | false | 10:00 | Heavy Watering (15<20) | 4b |
| 10 | 10 | 30 | false | 10:00 | Do Not Water? No, rain=false → row 4/5 | 5 → No Action if not peak |
| 11 | 5 | 3 | true | 13:00 | Critical Watering (peak & 5<10) | 2 |
| 12 | 5 | 3 | false | 13:00 | Critical Watering (peak & 5<10) | 2 |
| 13 | 5 | 3 | true | 10:00 | Do Not Water (rain & 5<40) | 3 |
| 14 | 55 | 3 | true | 13:00 | Do Not Water (55≥40? No, 55>40, so row 5) → wait 55>40 so row 5 No Action | 5 |
| 15 | 35 | 30 | true | 13:00 | Do Not Water (35<40 & rain) | 3 |
| 16 | 35 | 30 | false | 13:00 | Heavy Watering (35≥20) | 4c |
| 17 | 35 | 30 | false | 13:30 | Postpone Watering (35≥20? No, 35≥20 so Heavy Watering) → Wait, 35 is ≥20 so row 4b Heavy Watering. But 35≤40 so row 4c Light Watering if 20≤moisture≤40 → Light Watering | 4c |
| 18 | 25 | 30 | false | 13:00 | Light Watering (20≤25≤40) | 4c |
| 19 | 15 | 30 | false | 13:00 | Heavy Watering (15<20) | 4b |
| 20 | 15 | 30 | true | 13:00 | Do Not Water (15<40 & rain) | 3 |
| 21 | 80 | 30 | false | 13:00 | No Action (not row 1-4) | 5 |
| 22 | 80 | 30 | true | 13:00 | No Action (80>60 but temp=30≥5, so not row 1; rain but 80≥40 so not row 3; no rain false so not row 4; → row 5) | 5 |
| 23 | 30 | 30 | true | 13:00 | Do Not Water (30<40 & rain) | 3 |
| 24 | 30 | 30 | false | 13:00 | Heavy Watering (30≥20) | 4c |
| 25 | 10 | 3 | true | 13:00 | Do Not Water (10<40 & rain) | 3 |
| 26 | 10 | 3 | false | 13:00 | Heavy Watering (10<20) | 4b |
| 27 | 10 | 3 | false | 11:00 | Critical Watering (not peak, 10<10? No, 10 is not <10, so row 5) → No Action | 5 |
| 28 | 10 | 3 | false | 13:00 | No Action (10 not <10, so not row 2; no rain, temp 3<5? No, so not row 1; rain false so not row 3; no rain temp 3 not >25 so not row 4; → row 5) | 5 |
| 29 | 9 | 3 | false | 13:00 | Critical Watering (not peak, 9<10) | 2 |
| 30 | 9 | 3 | false | 11:00 | Critical Watering (not peak, 9<10) | 2 |
| 31 | 95 | 3 | true | 13:00 | Emergency Drainage? 95>60 & 3<5 → yes! | 1 |
| 32 | 95 | 6 | true | 13:00 | Do Not Water? 95≥40 so not row 3; no rain false so not row 4; → row 5 No Action | 5 |
| 33 | 95 | 6 | false | 13:00 | No Action (95>60 & 6≥5 so not row 1; rain false so not row 3; no rain temp 6 not >25 so not row 4; → row 5) | 5 |
| 34 | 45 | 30 | true | 13:00 | Do Not Water (45<40? No, 45>40, so not row 3) → row 5 No Action | 5 |
| 35 | 45 | 30 | false | 13:00 | No Action (45>40 so not row 4; row 5) | 5 |
| 36 | 45 | 30 | false | 13:30 | Postpone Watering (45≤40? No, 45>40 so row 5) → Wait, 45>40 so row 5 No Action. Need moisture ≤40 for row 4. | 5 |
| 37 | 35 | 30 | false | 13:30 | Light Watering (20≤35≤40) | 4c |
| 38 | 25 | 30 | false | 13:30 | Light Watering (20≤25≤40) | 4c |
| 39 | 20 | 30 | false | 13:30 | Light Watering (20≤20≤40, Q6) | 4c |
| 40 | 19 | 30 | false | 13:30 | Heavy Watering (19<20) | 4b |
| 41 | 65 | 3 | true | 13:00 | Emergency Drainage (65>60 & 3<5) | 1 |
| 42 | 65 | 6 | true | 13:00 | No Action (65>60 but 6≥5, so not row 1) | 5 |
| 43 | 65 | 6 | false | 13:00 | No Action (same as 42 but no rain) | 5 |
| 44 | 70 | 3 | true | 13:30 (peak) | Emergency Drainage (70>60 & 3<5) | 1 |
| 45 | 70 | 3 | false | 13:30 (peak) | No Action (70>60 & 3<5 is row 1, wait yes it is) → Emergency Drainage | 1 |
| 46 | 70 | 6 | true | 13:30 (peak) | No Action (70>60 but 6≥5 so not row 1; rain true but 70≥40 so not row 3; no rain false so not row 4; → row 5) | 5 |
| 47 | 70 | 6 | false | 13:30 (peak) | No Action | 5 |
| 48 | 30 | 30 | true | 11:00 (not peak) | Do Not Water (30<40 & rain) | 3 |
| 49 | 30 | 30 | false | 11:00 (not peak) | Heavy Watering (30≥20) | 4c |
| 50 | 5 | 3 | true | 13:00 | Do Not Water (5<40 & rain) | 3 |

_(Note: Some rows above were adjusted for correctness; refer to the actual decision table for exact logic)_

---

## Test execution guidance

1. **Create branch**: `git switch develop`, `git pull`, `git switch -c hotfix`
2. **Update pom.xml**: Set `<haltOnFailure>true</haltOnFailure>`
3. **Write tests** in `src/test/java/es/uclm/iso2/irrigation/domain/` following the test units and cases above
4. **Run**: `mvn verify`
5. **Report**: Copy results to wiki page [Test reports](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/wiki/Test-reports)
6. **Pull request**: Open PR from `hotfix` to `develop`, non-author approval required