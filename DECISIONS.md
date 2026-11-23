# DECISIONS

Decisions of Pair 3 (Denis, Rodrigo, David) about the open questions of the statement of Problem 3 (*Irrigation*,
ET.02.04). The program follows them, and Pair 1 designs the tests from them. They are the same as table Q of the
[README](README.md), where Pair 1 reads them next to the decision table.

A decision is never deleted: if it changes, its status says which one replaces it. New decisions are added as Q9,
Q10… and need the agreement of Pair 3. An AI agent follows them and never changes them on its own.

**Agreed:** 21/11/2026, by Pair 3 · **Issue:** [#2](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/2)

| # | Question | Decision | Reason |
|---|---|---|---|
| Q1 | Which values are valid, and what happens with invalid ones? | Moisture from 0 to 100 % (both included); temperature any finite number; time required. Otherwise the constructor throws an exception. | A percentage cannot be outside 0–100, and the statement gives no temperature range, so we do not invent one. |
| Q2 | Are 12:00 and 16:00 inside the peak hours? | 12:00 is inside, 16:00 is outside (from 12:00 to 15:59). | At 4:00 PM the peak ends, and this way each time belongs to only one period. |
| Q3 | When several rules apply at the same time, which one wins? | In this order: 1) Emergency Drainage, 2) Critical Watering, 3) Do Not Water, 4) Heavy or Light Watering, which are postponed in peak hours. | Drainage protects the roots from freezing and is not a watering cycle; Critical Watering is the only exception the statement gives to the peak hours rule. |
| Q4 | Peak hours, moisture below 10 % and rain forecast: Critical Watering or Do Not Water? | Critical Watering. | The statement says "unless soil moisture drops below 10 %" without any exception for rain. |
| Q5 | Does Critical Watering apply outside peak hours? | No: outside peak hours the normal rules apply (for example, moisture below 10 %, more than 25 °C and no rain gives Heavy Watering). | The statement only names this protocol inside the peak hours. |
| Q6 | Does "between 20 % and 40 %" include 20 and 40? | Yes, both. | Heavy Watering is "below 20 %", so 20 % must belong to Light Watering to leave no gap. |
| Q7 | What is the output when a watering is postponed, and when no rule applies? | Two outputs of our own: "Postpone Watering" and "No Action". | The statement asks to postpone but names no output, and a separate output lets the tests tell these cases apart from "Do Not Water". |
| Q8 | Is "imminent rain" the same as "rain forecast within the next 24 hours"? | Yes: one yes/no input, "rain forecast in the next 24 hours". | The statement only gives one rain forecast as input, and a second one would add a variable with no rule of its own. |

**Status:** all accepted.
