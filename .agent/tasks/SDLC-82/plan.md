# SDLC-82: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task adds a new presentation field labeled **„Okres ochrony”** to the offers list view. In the current codebase, the offers screen is rendered as a card-based list in `src/app/features/offers/pages/offers-home-page.component.html`, not a literal HTML table, so the “column” should be implemented as an additional metadata slot within each offer row.

The displayed value must be calculated dynamically from the **current business date in the CET/Poland business timezone** and shown identically for all offers visible on a given day. The value is informational only: it must **not** be persisted in offer data, must **not** depend on per-offer dates, and must remain visible to every user who already has access to the offers list.

### User Stories
- US-001: As an offers-list user, I want to see the current protection period directly on each offer row, so that I can verify the current annual coverage window without opening offer details.
  - Given I am on the offers list
  - When the page renders offer rows
  - Then each row shows the label **„Okres ochrony”** and the same dynamically calculated period value

- US-002: As a system user, I want the protection period to be calculated from the current day in the business timezone, so that the displayed range is always current and consistent regardless of my device timezone.
  - Given my browser/device uses any local timezone
  - When the offers list is opened
  - Then the displayed range is derived from the current date in the configured Poland/CET business timezone, not from the device locale

### Functional Requirements
- FR-001: Add a new visible field labeled **„Okres ochrony”** on the offers list screen (`/offers`).
- FR-002: The value must be calculated once per current business day and reused for every offer row shown on the page.
- FR-003: The protection period start must be the current business date at **00:00**.
- FR-004: The protection period end must be the day preceding the one-year anniversary at **23:59**.
- FR-005: The formatted value must use the exact pattern **`yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`**.
- FR-006: Time must be shown in **24-hour format**.
- FR-007: The value must be computed from the current date in the Poland/CET business timezone and must not depend on `Offer.validTo`, `contractData.coverageStartDate`, `contractData.coverageEndDate`, or any other offer-specific field.
- FR-008: The calculated value must remain presentation-only; no new persisted offer attribute is introduced in repository data or runtime storage.
- FR-009: Existing filtering, sorting, offer actions, permissions, and navigation on the offers page must remain unchanged.

### Edge Cases
- EC-001: All offers must show the same protection period for a given day, even if individual offers contain different validity or contract dates.
- EC-002: Users in non-Polish timezones must still see the range calculated from the Poland/CET business date.
- EC-003: The annual-window calculation must handle leap-year boundaries correctly.
- EC-004: If the offers page remains open across midnight in the business timezone, the displayed value should refresh to the new day’s range without requiring offer data changes.
- EC-005: Offers missing `validTo` or contract dates must still display the protection period because the field is independent of offer payload data.
- EC-006: Adding the new field must not break the current responsive layout of `.offer-row__meta-grid`.

### Success Criteria
- [ ] The offers list shows a new field labeled **„Okres ochrony”** for every offer row.
- [ ] The displayed value follows the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] For business date `2025/05/10`, the UI shows `2025/05/10 00:00 - 2026/05/09 23:59`.
- [ ] The value is identical for all offer rows rendered on the same day.
- [ ] The value is derived from the Poland/CET business timezone, not the user device timezone.
- [ ] No offer model persistence, repository payload, or local-storage schema is changed for this field.
- [ ] Existing offers-list filtering, sorting, actions, and navigation continue to work unchanged.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone-component app using signals/computed state and PrimeNG. The offers list screen is implemented in `src/app/features/offers/pages/offers-home-page.component.ts` with a card layout in `offers-home-page.component.html`, so the change should follow that existing pattern rather than introducing a new table component.

Relevant findings:
- **No existing offer/coverage service was found in Confluence `Services` space** — no reusable API/service documentation was available there, so this task remains a UI-only change on top of the existing repository implementation.
- Domain context found in **SDLC / “Ekrany”** confirms the application is a sales-agent workflow UI and the offers list is a core operational screen:  
  https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany

Implementation strategy:
1. **Keep the value out of the `Offer` model and repositories.**  
   `src/app/core/models/offer/offer.model.ts` and `src/app/core/repositories/offers.repository.ts` should remain unchanged because the Jira explicitly defines this as a dynamic presentation value, not stored offer data.

2. **Introduce a small pure utility for coverage-period calculation/formatting.**  
   Create a feature-local helper such as `src/app/features/offers/utils/coverage-period.util.ts` that:
   - extracts the current calendar date in the Poland business timezone via native `Intl.DateTimeFormat`
   - computes start as `yyyy/MM/dd 00:00`
   - computes end as one calendar year later minus one day at `23:59`
   - returns a formatted string `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`

   This keeps timezone logic testable and avoids embedding date arithmetic directly in the component.

3. **Compute the label once at page level, not per offer.**  
   In `OffersHomePageComponent`, add a signal/computed value like `coveragePeriodLabel` derived from a lightweight “clock” signal. Because the same value applies to every row, compute it once and bind it in the template for each offer row. This matches the current use of `signal`, `computed`, and `toSignal` in the component.

4. **Refresh the label when the business day changes.**  
   To satisfy the “dynamic from current day” requirement, add a component-local refresh mechanism that updates the clock signal while the component is alive. A lightweight interval/timer is sufficient; the important design point is that the label can roll over to the new business day without mutating offers data or requiring a page reload.

5. **Render the new field in the existing metadata grid.**  
   Add a new metadata block inside `.offer-row__meta-grid` in `offers-home-page.component.html`, e.g. between “Wariant” and “Aktualizacja” or immediately before “Aktualizacja”, labeled **„Okres ochrony”**. Because the current grid is defined in SCSS with four columns, update the styles so the extra field fits cleanly on desktop and still collapses correctly at existing breakpoints.

6. **Add deterministic tests around date/time behavior.**  
   Since timezone/date math is the critical business rule, test the utility with fixed inputs, including:
   - the Jira example (`2025/05/10 -> 2026/05/09`)
   - leap-year boundary behavior
   - independence from browser local timezone assumptions

7. **Add a UI-level regression check for the offers page.**  
   Extend test coverage so the page verifies the new label is rendered on `/offers` and the value matches the expected display pattern. This ensures the feature is visible in the actual page and not just in utility tests.

### Task Breakdown

#### Phase 1: Add reusable coverage-period calculation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create coverage-period utility | `src/app/features/offers/utils/coverage-period.util.ts` | Add pure functions to derive the current business date in Poland/CET timezone, calculate the one-year-minus-one-day range, and format the final display string. |
| 1.2 | Cover date rules with unit tests | `src/app/features/offers/utils/coverage-period.util.spec.ts` | Add deterministic tests for formatting, 24-hour output, Jira example date, timezone-based current-date extraction, and leap-year behavior. |

#### Phase 2: Wire coverage period into the offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add page-level coverage-period state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add a clock signal/timer and computed `coveragePeriodLabel` using the new utility, following the component’s existing signal/computed pattern. |
| 2.2 | Render the new field on each offer row | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new metadata item labeled `Okres ochrony` bound to `coveragePeriodLabel()`, ensuring every offer row displays the same value. |
| 2.3 | Adjust layout for an extra metadata slot | `src/app/features/offers/pages/offers-home-page.component.scss` | Update `.offer-row__meta-grid` and related text wrapping so the extra field fits the current card layout without breaking responsiveness. |

#### Phase 3: Add page-level regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers-page visibility test | `tests/e2e/offers-list.spec.ts` | Add an end-to-end check that `/offers` renders `Okres ochrony` and displays a value matching the required date-range format. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/coverage-period.util.ts` | CREATE | Pure helper for timezone-aware coverage-period calculation and formatting. |
| `src/app/features/offers/utils/coverage-period.util.spec.ts` | CREATE | Unit tests for date arithmetic, formatting, and timezone behavior. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add computed coverage-period state and component lifecycle refresh logic. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render the new `Okres ochrony` field in each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adapt metadata grid/layout to accommodate the additional field. |
| `tests/e2e/offers-list.spec.ts` | CREATE | UI regression check for visibility and format of the new field on the offers list. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm run test -- --watch=false --browsers=ChromeHeadless`
3. [ ] New utility tests verify the Jira example `2025/05/10 00:00 - 2026/05/09 23:59`
4. [ ] Offers page shows `Okres ochrony` for every row on `/offers`
5. [ ] Displayed value uses the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`
6. [ ] The same coverage-period value is shown across all rendered offers on a given day
7. [ ] Existing search, filters, sorting, and offer row actions still work
8. [ ] E2E regression passes with `npm run e2e`