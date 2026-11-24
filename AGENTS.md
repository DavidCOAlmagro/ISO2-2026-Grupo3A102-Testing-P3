# AGENTS.md

Instructions for every AI agent that works on this repository. Read this file completely before doing anything. The reasons behind each rule are in [DECISIONS.md](DECISIONS.md).

## The project

Problem 3 (*Irrigation*, ET.02.04) of the second theoretical exercise (T2) of Software Engineering II (UCLM, 2026/27), group 3A1.02. The program recommends how to irrigate a greenhouse from the soil moisture, the temperature, the rain forecast and the time of day.

| Step | Pair | What | Issue |
|---|---|---|---|
| 1 – Program | Pair 3 (Denis, Rodrigo, David) | The Maven project, easy to test | [#162](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/162) |
| 2 – Test design | Pair 1 (Marcos, Ignacio) | A PDF with the test cases | [#163](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/163) |
| 3 – JUnit tests | Pair 2 (Adam, Javier) | The JUnit tests, with at least 80 % coverage | [#164](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/164) |

Step 1 has no assessment of its own: what is graded is the test design and its JUnit implementation. So the program has to be **simple, easy to test and documented**, and nothing more.

## How you work

1. **Read** your task issue, this file, DECISIONS.md and table Q of the [README](README.md).
2. **Ask** every doubt in one message and wait for the answers. Never fill a gap with an assumption.
3. **Plan** the files and steps, and **wait for an explicit "yes"** from the member before changing anything.
4. **Implement** one step at a time. After each step `mvn -B verify` must pass; then commit.
5. **Done needs evidence:** a passing build and every point of the issue checked.

## Rules

1. **You suggest, the team decides.** Never change a decision of DECISIONS.md or a row of table Q on your own.
2. **Only your task.** One task is one issue and one branch. Change only what the issue asks for.
3. **The specification is the statement (ET.02.04) plus table Q.** Do not add rules, inputs or outputs that they do not name. If they leave a gap, stop and ask: the trio adds a new row Q9, Q10…
4. **Keep it simple** and follow D-003: named constants for the limits, one public method with simple `if` statements in the order of table Q3, Javadoc on every public class and method, no console in the domain.
5. **No new dependencies.** Only JUnit, Surefire and JaCoCo.
6. **Branches.** `feature/short-description` from `develop` in Step 1; `hotfix` from `develop` in Step 3. Never commit to `main` or `develop`, never delete a branch, never merge a pull request yourself (D-001).
7. **Ask before acting on GitHub:** push, pull requests, issues, comments and wiki pages only when the member asks for that exact action.
8. **No destructive Git commands:** no `push --force`, `reset --hard` or `rebase` of a pushed branch.
9. **Nobody writes tests in Step 1** (D-002). In Step 3 Pair 2 sets `haltOnFailure` to `true` in `pom.xml` (D-004).
10. **Cite your sources; do not invent.** Every fact about the problem comes from the statement or table Q.
11. **Authorship.** Commits use the identity of the member who asked for the work, with no `Co-Authored-By` lines or tool signatures.
12. **English** in everything written in the repository.

## Conventions

| Topic | Convention | Source |
|---|---|---|
| Packages | `es.uclm.iso2.irrigation.domain` and `es.uclm.iso2.irrigation.ui`; artifact `ISO2-2026-Grupo3A102-Testing-P3`, version `0.1.0-SNAPSHOT` | D-003 |
| Commits | Short message in English, in the imperative, with the task, e.g. `Add the irrigation rules (T2, #3)` | — |
| Pull requests | To `develop` (the default branch), approved by another member of Pair 3, with `Closes #n` | D-001 |
| Tests | Step 3 only: `<Class>Test` in `src/test/java`, same package, one method per case of the PDF of Pair 1 | D-002 |

## Where to find things

| What | Where |
|---|---|
| Why the repository is the way it is | [DECISIONS.md](DECISIONS.md) |
| Decisions on the statement (table Q), how to build, guides for Pair 1 and Pair 2 | [README.md](README.md) |
| Tasks, dates and how we work | Wiki: *Planning* |
| What has been done | Wiki: *Traceability* |
| Test results | Wiki: *Test reports* (Step 3) |
