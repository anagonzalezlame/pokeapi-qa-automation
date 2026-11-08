# PokeAPI QA Automation

![Tests](https://github.com/anagonzalezlame/pokeapi-qa-automation/actions/workflows/tests.yml/badge.svg)

Test automation project for [PokeAPI](https://pokeapi.co), covering both the public REST API and the pokeapi.co website. Scenarios are written in Gherkin so that anyone on a team (QA, devs, product) can read what is being tested without opening the code.

## Tech stack

| Layer | Tool |
| --- | --- |
| Language and build | Java 17+, Maven |
| BDD | Cucumber 7 (Gherkin) on the JUnit 5 Platform |
| API testing | REST Assured, JSON Schema Validator, Hamcrest |
| UI testing | Playwright for Java (Page Object Model) |
| CI | GitHub Actions, with a weekly scheduled run |
| Reports | Cucumber HTML report, with a screenshot attached to every failed UI scenario |

## What is covered

**API (`@api`), 18 scenarios**

| Feature | What it checks |
| --- | --- |
| Get a Pokemon | Lookup by name and by id, data correctness, types, abilities |
| Contract | The Pokemon response is validated against a JSON Schema (`@contract`) |
| Performance | Response time stays under 3 seconds (`@performance`) |
| Negative cases | Unknown names, ids out of range and negative ids return 404 (`@negative`) |
| Pagination | `limit` / `offset`, total count, `next` and `previous` links |
| Types | Fire type strengths and weaknesses |
| Evolution chains | Request chaining: species, then the evolution chain URL it returns |

**UI (`@ui`), 10 scenarios**

| Feature | What it checks |
| --- | --- |
| "Try it now!" explorer | Searching a resource, the suggested hints, the direct link, the "Resource not found" message |
| Navigation | Header links and the "Check out the docs!" call to action open the right page |

Tags available to filter runs: `@api`, `@ui`, `@smoke`, `@negative`, `@contract`, `@performance`.

## Project structure

```
src/test/
├── java/uy/anagonzalez/pokeapi/
│   ├── RunCucumberTest.java        # Suite entry point
│   ├── api/ApiSteps.java           # REST Assured step definitions
│   ├── ui/UiSteps.java             # Playwright step definitions
│   ├── ui/pages/                   # Page Objects (HomePage, SiteHeader)
│   └── support/                    # Config, shared context, browser hooks
└── resources/
    ├── features/api/               # API scenarios
    ├── features/ui/                # Website scenarios
    └── schemas/pokemon.json        # JSON Schema for contract testing
```

## How to run

Requirements: JDK 17 or newer and Maven. IntelliJ IDEA works out of the box (open the folder as a Maven project).

```bash
# 1. Download dependencies and install the browser (only the first time)
mvn test-compile
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.classpathScope=test -Dexec.args="install chromium"

# 2. Run everything
mvn test

# Only API tests / only UI tests / only the smoke suite
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui"
mvn test -Dcucumber.filter.tags="@smoke"

# Watch the browser while the UI tests run
mvn test -Dcucumber.filter.tags="@ui" -Dheadless=false -DslowMo=500

# Run the UI tests on another browser (install it first with the command in step 1)
mvn test -Dcucumber.filter.tags="@ui" -Dbrowser=firefox
```

After a run, open `target/cucumber-reports/cucumber.html` in a browser to see the report.

## Continuous integration

The workflow in `.github/workflows/tests.yml` runs the full suite on every push to `main`, on pull requests, every Monday and on demand. A manual run accepts a tag expression, so it can run only `@smoke` or skip `@performance`. The Cucumber report is uploaded as an artifact of each run.

## Design decisions

- **Readable scenarios first.** Steps describe behaviour ("I request the type fire") instead of HTTP details, so the feature files work as living documentation.
- **Stable UI locators.** The site uses generated CSS class names, so the Page Objects locate elements by role, visible text and ids, as a user would find them.
- **Isolation.** Each UI scenario runs in a fresh browser context and each API scenario keeps its own response, so scenarios can run in any order.
- **Explicit waits on real events.** The home page fires a request for `pokemon/ditto` on load, and its HTML already shows "Resource for ditto" before that request starts. Reading the site's source showed that a slow first response could overwrite a newer search result, so `HomePage.open()` waits for that specific response before any interaction. This removes a source of flaky tests and is also worth reporting as a possible race condition in the site.
- **Live data assumptions are kept small.** Assertions use stable facts (Pikachu is id 25, Fire is strong against Grass) and ranges where data can grow (the total Pokemon count only needs to be above 1000).

## Author

**Ana González Lamé**, QA Analyst.
