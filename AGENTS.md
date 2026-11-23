# AGENTS.md

Rules for any AI agent that works on this repository. Read this file before doing anything. The decisions about
the statement are in [DECISIONS.md](DECISIONS.md) and in table Q of the [README](README.md).

## The project
Problem 3 (*Irrigation*, ET.02.04) of the second theoretical exercise of Software Engineering II (UCLM, 2026/27),
group 3A1.02. The program recommends how to irrigate a greenhouse from the soil moisture, the temperature, the rain
forecast and the time of day.

| Step | Pair | What |
|---|---|---|
| 1 – Program | Pair 3 (Denis, Rodrigo, David) | The Maven project, easy to test |
| 2 – Test design | Pair 1 (Marcos, Ignacio) | A PDF with the test cases |
| 3 – JUnit tests | Pair 2 (Adam, Javier) | The JUnit tests, with at least 80 % line coverage |

## How you work
1. **Read** the issue of your task, this file and DECISIONS.md. Check that the issue is assigned to the member
   who asked for the work.
2. **Ask** every doubt in one message and wait for the answers. Never fill a gap with an assumption.
3. **Plan** the files and steps, and wait for an explicit "yes" from the member before changing anything.
4. **Implement** one step at a time. `mvn -B verify` must pass before each commit.
5. **Done needs evidence:** a green build and every point of the issue checked.

## Rules
1. **You suggest, Pair 3 decides.** Never take or change a decision about the statement on your own. A new open
   question becomes a new row (Q9, Q10…) agreed by Pair 3.
2. **Only your task.** One task is one issue and one branch. Do not refactor or "improve" anything else.
3. **Only ET.02.04.** Do not add rules, inputs or outputs that the statement or table Q do not name.
4. **Keep it simple.** The smallest code that meets the specification. The simpler, the easier to test.
5. **No new dependencies.** Only JUnit, Surefire and JaCoCo, as the statement says.
6. **Branches.** Work on `feature/…` (Step 1) or `hotfix/…` (Step 3) and open a pull request to `develop`.
   Never commit to `main` or `develop`, never force-push and never delete a branch (the rulesets block it).
7. **Reviews.** A pull request is approved by a member of Pair 3 who is not its author, and the members merge it,
   never the agent.
8. **Domain without console.** `es.uclm.iso2.irrigation.domain` has no input or output; the console is in
   `es.uclm.iso2.irrigation.ui`.
