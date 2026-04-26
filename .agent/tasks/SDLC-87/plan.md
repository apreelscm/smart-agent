# SDLC-87: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task adds a new **„Okres ochrony”** field to the prepared offers list so users can immediately see the currently applicable protection period for every offer. In the current application, the offers list is rendered as repeated offer cards rather than a literal HTML table, so the requested “column” should be implemented as a new, consistently positioned metadata item in each offer row.

The value is not sourced from offer data and does not require backend persistence. It is derived dynamically from the current business day in the Polish/CET business timezone and shown in the exact format required by the Jira ticket.

### User Stories
- US-001: As a sales agent, I want to see the protection period on the offers list, so that I can quickly verify the effective coverage range for each offer.
  - Given I open the prepared offers list
  - When the offer rows are rendered
  - Then each row shows an „Okres ochrony” value in a consistent position

- US-002: As a user of the offers list, I want the protection period displayed in one consistent date-time format, so that it is easy to read and compare.
  - Given the current business date is known
  - When the protection period is displayed
  - Then it uses the format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm` with 24-hour time

- US-003: As a business owner, I want the protection period calculated automatically for one year from the current day, so that users do not need to enter or maintain it manually.
  - Given the current day in the CET business timezone
  - When the period is calculated
  - Then it starts at `00:00` on that day and ends at `23:59` on the day preceding the yearly anniversary

### Functional Requirements
- FR-001: Add a new visible field labeled **„Okres ochrony”** to each row on the offers list screen implemented in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-002: The field must be visible for all users who can access the offers list; no role-based gating or feature-flag behavior is introduced.
- FR-003: The displayed value must be calculated dynamically on the client side and must not be stored in `Offer` or fetched from repository data.
- FR-004: The start of the period must be the current day in the business timezone at `00:00`.
- FR-005: The end of the period must be exactly one year minus one minute from that start, i.e. `23:59` on the day before the yearly anniversary.
- FR-006: The displayed value must use the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- FR-007: The hour format must be 24-hour.
- FR-008: The calculated value must be identical for all offers rendered on the same day, because the Jira requirements state the period is based on “today”, not on individual offer attributes.
- FR-009: Existing offer filtering, sorting, transitions, navigation, and premium display must remain unchanged.
- FR-010: No repository, mock payload, or domain model changes are required unless needed purely for tests.

### Edge Cases
- EC-001: If the current date is a leap-day start (e.g. `2024/02/29 00:00`), the end date should resolve to the day before the next yearly anniversary (`2025/02/28 23:59`).
- EC-002: Daylight-saving transitions must not change the visible rule; the period still displays as local-day `00:00` to `23:59`.
- EC-003: Offers missing optional business fields must still display the protection period, because the value is independent of offer content.
- EC-004: If the list is filtered to zero rows, the empty state remains unchanged; no standalone protection-period display is required outside offer rows.
- EC-005: Long formatted values must remain readable in the existing responsive card layout without overlapping other metadata.

### Success Criteria
- [ ] Each offer row on `/offers` displays a new **„Okres ochrony”** field.
- [ ] The value matches `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] For a business date of `2025/05/10`, the UI shows `2025/05/10 00:00 - 2026/05/09 23:59`.
- [ ] The value is identical across all offers displayed on the same day.
- [ ] The change does not alter offer repository payloads or existing offer actions.
- [ ] Automated tests cover the date calculation and rendered presence of the new field.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone-component application using signals/computed state and PrimeNG. The offers list screen is implemented in `src/app/features/offers/pages/offers-home-page.component.ts` and rendered through `offers-home-page.component.html` as card-like rows with a metadata grid (`offer-row__meta-grid`), not as a literal table. The requested “column” should therefore be added as a new metadata slot in that grid.

Domain context found in Confluence confirms that smart-agent is the sales-agent workflow application and that screen-level changes should fit the existing user journey without changing business flow: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany).

No existing offer/coverage-period service, endpoint, or integration library was found in the configured `Services` space — proposing a frontend-only calculation with **no external service integration**.

Implementation should:
- reuse the current `OffersHomePageComponent` signal-based presentation pattern;
- keep `Offer` and `OffersRepository` unchanged, because the value is UI-derived;
- add a small pure utility for date-range calculation/formatting so logic is deterministic and unit-testable;
- compute the date range from the current day in the Polish business timezone (`Europe/Warsaw`, used as the practical implementation of the CET business requirement in this app);
- render the same computed string for every offer row;
- update the SCSS grid so the additional field remains readable on desktop and responsive breakpoints.

Because the formatted range is long, CSS should be adjusted rather than forcing inline styles. A dedicated utility is preferable to embedding date math directly in the component, as it keeps `OffersHomePageComponent` aligned with its existing responsibility: orchestration of signals and UI helpers.

### Task Breakdown

#### Phase 1: Add deterministic coverage-period calculation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create coverage-period utility | `src/app/features/offers/utils/coverage-period.util.ts` | Add a pure helper that derives the current business date in `Europe/Warsaw`, builds the start (`00:00`) and end (`23:59` on previous day before yearly anniversary), and formats the final string as `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`. |
| 1.2 | Cover calculation edge cases with unit tests | `src/app/features/offers/utils/coverage-period.util.spec.ts` | Add focused tests for the Jira example date, leap-year handling, year rollover, and formatting consistency. |

#### Phase 2: Expose and render the new offers-list field
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Expose computed value in offers page component | `src/app/features/offers/pages/offers-home-page.component.ts` | Import the utility and add a readonly/computed property such as `coveragePeriodLabel` so the value is calculated once for the page state and reused for every rendered offer row. |
| 2.2 | Render the new field in each offer row | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new metadata item labeled `Okres ochrony` inside `offer-row__meta-grid`, positioned consistently with the other row attributes, and bind it to `coveragePeriodLabel()`. |
| 2.3 | Adjust layout styling for the extra metadata slot | `src/app/features/offers/pages/offers-home-page.component.scss` | Update grid sizing/wrapping so the fifth metadata item remains legible, including desktop and existing responsive breakpoints. |
| 2.4 | Add component-level rendering test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add a component test with stubbed repositories and a fixed current date to verify that the offers page renders the `Okres ochrony` label and expected formatted value. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/coverage-period.util.ts` | CREATE | Pure helper for computing and formatting the dynamic one-year protection period. |
| `src/app/features/offers/utils/coverage-period.util.spec.ts` | CREATE | Unit tests for date calculation rules and edge cases. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Expose the computed coverage-period label for the offers list view. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add the new `Okres ochrony` metadata field to each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Update the offer-row metadata grid and text wrapping for the additional field. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component rendering test for the new field on the offers list. |

### Verification Steps
1. [ ] Build succeeds.
2. [ ] Tests pass.
3. [ ] New tests cover requirements.
4. [ ] Navigating to `/offers` shows `Okres ochrony` for every visible offer row.
5. [ ] With the date fixed to `2025-05-10` in tests, the rendered value is `2025/05/10 00:00 - 2026/05/09 23:59`.
6. [ ] Existing filters, sorting, offer transitions, and navigation still behave as before.
7. [ ] The added field remains readable at desktop and mobile breakpoints.