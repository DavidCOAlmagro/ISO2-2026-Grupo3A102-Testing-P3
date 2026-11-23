# DECISIONS

Decisions of this repository (Problem 3, *Irrigation*, of T2, group 3A1.02): how we work, how the program is built and how it is delivered to the other pairs. The decisions about the statement itself (rule priority, limits, invalid data, outputs) are in table Q of the [README](README.md#decisions-on-the-statement), not here.

One entry per decision. A decision that is later changed is not deleted: its status says what replaces it. The AI agent follows these decisions and never changes them on its own (AGENTS.md).

---

## D-001 · Branch protection and pull requests
**Date:** 21/11/2026 · **Step:** 1

- **Context:** The statement asks for a `develop` branch and, in Step 3, a `hotfix` branch from it. The main repository protects `main` and `develop` (D-001 there).
- **Decision:**
  - `main` and `develop` are protected with the same ruleset as the main repository: no deletion, no force push, no creation, and a pull request with one approval to change them. Empty bypass list.
  - A second ruleset covers `feature/**` and `hotfix/**`: no deletion and no force push, empty bypass list. It does not stop anyone from pushing.
  - `develop` is the default branch, so new pull requests point to it and `Closes #n` closes the issue when the pull request is merged.
  - A pull request is approved by a member of Pair 3 who is not its author.
- **Reason:** The person who writes a change and the person who reviews it must be different, and the branches keep the history of each step for the other pairs and the professor.
- **Status:** Applied on 21/11/2026; to be approved by Pair 3 in the review of the pull request that adds this file.

## D-002 · Roles of the three pairs
**Date:** 21/11/2026 · **Step:** 1 · **Issues:** [#162](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/162), [#163](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/163), [#164](https://github.com/igrt08-uclm/2026-ISO2-3A1.02/issues/164)

- **Context:** The statement (ET.02.00) asks that the pair that writes a program does not test it.
- **Decision:** Pair 3 (Denis-Alexandru Ionescu Telibasa, Rodrigo Muñoz Martín, David Calzado Olmo) writes the program (Step 1). Pair 1 (Marcos Loro Carrasco, Ignacio Romero Torres) designs the tests in `ISO2-2026-Grupo3A102-Testing-P3.pdf` (Step 2). Pair 2 (Adam Gouram El Bouhali, Javier Villanueva Crespo) writes the JUnit tests (Step 3).
- **Reason:** It is the assignment of the wiki page *Testing Part* of the main repository.
- **Status:** Assignment used by the whole group (wiki page *Testing Part*, issues #155 to #166 of the main repository).

## D-003 · The program must be easy to test
**Date:** 21/11/2026 · **Step:** 1 · **Issue:** [#3](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/3)

- **Context:** Step 1 has no assessment of its own, but the code decides the effort of Steps 2 and 3 (statement ET.02.00).
- **Decision:**
  - The limits of the rules (10, 20, 40, 60 %, 5 and 25 °C, 12:00 and 16:00) are named constants.
  - The rules are in one public method, as simple `if` statements in the order of Q3.
  - Invalid data are rejected in the constructor with an exception (Q1).
  - Every public class and method has its Javadoc: what it receives, what it returns and what it throws.
  - The domain (`es.uclm.iso2.irrigation.domain`) has no console input or output; the console (`es.uclm.iso2.irrigation.ui`) only reads, calls the domain and prints.
  - The artifact is `ISO2-2026-Grupo3A102-Testing-P3`, version `0.1.0-SNAPSHOT`.
- **Reason:** The boundary values come from the constants, the pseudocode, decision coverage and MC/DC come from the method, and Pair 2 writes the tests without having written the code.
- **Status:** Proposed by David on 21/11/2026 (Planning, issue #3); to be approved by Pair 3 in the review of the pull request that adds this file.

## D-004 · Coverage threshold, only a warning until Step 3
**Date:** 21/11/2026 · **Step:** 1 · **Pull request:** [#1](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/pull/1)

- **Context:** The statement asks for at least 80 % coverage. The program is merged before its tests exist, because Pair 2 writes them in Step 3.
- **Decision:** JaCoCo checks a minimum of 80 % of lines in `mvn verify`, with `haltOnFailure` set to `false` until Step 3. Pair 2 sets it to `true` on the `hotfix` branch.
- **Alternatives:** Failing the build from the start (our own code could not be merged); no check at all.
- **Reason:** Our code can be merged in Step 1, and from Step 3 the build enforces the minimum of the statement.
- **Status:** Proposed by David on 21/11/2026 in pull request #1; to be approved by Pair 3 in its review.

## D-005 · Automatic check of every pull request
**Date:** 21/11/2026 · **Step:** 1 · **Issue:** [#2](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/2)

- **Context:** The reviewer of a pull request needs to know that it builds and passes, not only on the author's computer.
- **Decision:** A GitHub Actions workflow, copied from `maven.yml` of the main repository, runs `mvn -B verify` with Java 21 on every push and pull request to `main` and `develop`.
- **Reason:** Every pull request, from Step 1 to the `hotfix` of Step 3, is checked on a clean machine.
- **Status:** Proposed by David on 21/11/2026 (branch `feature/ci-workflow`); to be approved by Pair 3 in the review of its pull request.

## D-006 · Tasks of Step 1 and their owners
**Date:** 23/11/2026 · **Step:** 1 · **Issues:** [#2](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/2), [#3](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/3), [#4](https://github.com/DavidCOAlmagro/ISO2-2026-Grupo3A102-Testing-P3/issues/4)

- **Context:** Step 1 is split so that each member has one task and nobody works twice on the same files.
- **Decision:** T1 Maven skeleton and CI (#2): David. T2 Domain and irrigation rules (#3): Rodrigo. T3 Console interface (#4): Denis. T1 goes first, because T2 and T3 start from it.
- **Reason:** David had already started T1; T2 is the biggest task.
- **Status:** Proposed by David on 23/11/2026; to be approved by Pair 3 in the review of the pull request that adds this file.

## D-007 · Decisions on the statement
**Date:** 21/11/2026 · **Step:** 1

- **Decision:** The open points of the statement are answered in table Q of the [README](README.md#decisions-on-the-statement) (Q1–Q8), each with its reason. A new one is added as Q9, Q10…, never in this file.
- **Reason:** Pair 1 designs the tests from that table, so it must be in one place, next to how to build the program.
- **Status:** Proposed by David on 21/11/2026 in pull request #1; to be approved by Pair 3 in its review.
