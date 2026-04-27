# SDLC-94: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Task adds a new read-only protection-period column to the offers list screen. The value is not sourced from offer data; it is calculated dynamically from the current day in CET and displayed in a consistent `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm` format for every visible offer row.

### User Stories
- US-001: As a user of the offers list, I want to see the protection period in a separate column, so that I can quickly read the effective coverage window for an offer.
  - Given I am on the offers list
  - When the list is rendered
  - Then each offer row shows a protection-period value in a dedicated column-like metadata field
- US-002: As a user, I want the protection period to be calculated automatically from the current day, so that I do not need to determine it manually.
  - Given the current date is known by the application
  - When the offers list is displayed
  - Then the start of the protection period is the current CET day at `00:00`
  - And the end of the protection period is start + 1 year - 1 minute
- US-003: As a user, I want the period to be formatted consistently, so that I can compare values across offers.
  - Given a protection period is calculated
  - When it is displayed on the offers list
  - Then it uses the format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`

### Functional Requirements
- FR-001: The offers list must display a new protection-period field for every offer row.
- FR-002: The protection-period value must be visible for all users who can access the offers list.
- FR-003: The value must be calculated dynamically from the current day at render time, not from offer creation date or persisted offer fields.
- FR-004: The protection-period start must be the current CET day at `00:00`.
- FR-005: The protection-period end must equal start + 1 year - 1 minute, so the displayed end time is `23:59` on the day before the anniversary.
- FR-006: The displayed value must use format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- FR-007: The value must be read-only; no edit control or persistence change is allowed.
- FR-008: All rows on the same rendered list view must show the same protection-period value for the current CET day.
- FR-009: The displayed value must refresh after the CET day changes while the user remains on the offers page.

### Edge Cases
- EC-001: If the user keeps the offers page open across midnight in CET, the displayed period must update to the new day’s range without requiring offer data changes.
- EC-002: Leap-year boundaries must follow the specified rule `+1 year - 1 minute`, even when the anniversary date does not exist in the next year.
- EC-003: Existing `contractData.coverageStartDate` and `contractData.coverageEndDate` values in mock offers must not be used for this list field, because Jira explicitly requires a current-day-based calculation.
- EC-004: The new metadata field must not break the current responsive card/grid layout on desktop and mobile widths.
- EC-005: Draft, calculation, issued, and crop offers must all display the same calculated protection period, because the value is independent of offer type and status.

### Success Criteria
- [ ] A new protection-period field is visible on `/offers` for every offer row.
- [ ] The displayed value matches `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] For a fixed current date of `2025/01/15`, the formatter returns `2025/01/15 00:00 - 2026/01/14 23:59`.
- [ ] The value is calculated from the current CET day, not from persisted offer dates.
- [ ] Keeping the page open across a CET date change results in a refreshed protection period.
- [ ] No offer editing, filtering, sorting, repository contract, or mock-data schema changes are introduced.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular page in `src/app/features/offers/pages/offers-home-page.component.ts` with a card-style list template in `src/app/features/offers/pages/offers-home-page.component.html`. Although Jira says “column,” this screen is not a table; it uses the existing `offer-row__meta-grid` metadata layout. The change should therefore reuse that pattern by adding one more metadata cell rather than redesigning the screen into a table.

Confluence discovery:
- No existing API/service relevant to this UI-only change was found in `Services` space — proposing no new integration and no repository/API contract change.
- General product context was found in SDLC: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). It confirms the application is screen-driven and supports implementing this as a presentation-layer addition on the existing offers view.

Codebase patterns to follow:
- State on the offers page is managed with Angular signals/computed values (`searchTerm`, `filteredOffers`, `filtersChanged`), so the protection-period value should also be exposed as a computed/read-only view value.
- Existing repositories (`src/app/core/repositories/offers.repository.ts`) return raw offer data from `mock/offers.json`; because Jira requires a runtime-calculated display field, repository and mock payload changes are unnecessary and should be avoided.
- Existing E2E coverage for the page lives in `tests/e2e/offers.spec.ts`; extend it instead of creating a new E2E suite.

Recommended implementation:
1. Create a small pure utility dedicated to protection-period calculation and formatting, e.g. under `src/app/features/offers/utils/`.
2. Implement date-only CET logic there, returning a formatted string from an injected/current `Date`.
3. In `OffersHomePageComponent`, expose a single computed protection-period label for the whole screen, because every row must use the same current-day-based value.
4. Add a lightweight time tick on the page so the computed value can refresh after the CET day changes. A signal driven by RxJS `interval`/`startWith` is aligned with current repo usage (`toSignal`) and avoids manual subscription management.
5. Render the new field inside `offer-row__meta-grid`.
6. Adjust offers-page SCSS so the metadata grid handles five cells cleanly on wider screens and remains responsive.
7. Add unit tests for the formatter and extend Playwright coverage for visible rendering.

Important scope constraints:
- Do not add or modify runtime API integration.
- Do not use mock JSON as a runtime fallback for the protection period.
- Do not persist this value into `Offer` or `mock/offers.json`.
- Do not change filters, sorting, permissions, or unrelated list behavior.

### Task Breakdown

#### Phase 1: Add pure protection-period calculation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create formatter utility | `src/app/features/offers/utils/offer-protection-period.util.ts` | Add a pure helper that derives the current CET calendar date, computes start at `00:00`, computes end as `+1 year - 1 minute`, and returns the final `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm` string. |
| 1.2 | Encode CET handling explicitly | `src/app/features/offers/utils/offer-protection-period.util.ts` | Use a deterministic timezone-aware approach for deriving the CET day boundary used by the formatter, keeping the implementation isolated from component code. |
| 1.3 | Cover date edge cases in unit tests | `src/app/features/offers/utils/offer-protection-period.util.spec.ts` | Add focused tests for the Jira example, year rollover, and leap-year behavior to lock down the business rule independently of the UI. |

#### Phase 2: Expose the calculated value on the offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add a refresh signal for current time | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce a signal backed by `toSignal(...)` and a periodic tick so the page can recalculate the display value when the day changes while the page remains open. |
| 2.2 | Add computed protection-period label | `src/app/features/offers/pages/offers-home-page.component.ts` | Create a computed property that converts the current tick into a single formatted protection-period label reused by all rows. |
| 2.3 | Keep the change presentation-only | `src/app/features/offers/pages/offers-home-page.component.ts` | Do not modify `Offer`, repository mapping, or runtime merge logic; keep the new field outside domain persistence and runtime repositories. |

#### Phase 3: Render the new column-like field in the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add the new metadata field to each offer row | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new `offer-row__meta-grid` block labeled `Okres ochrony` and bind it to the computed protection-period label. |
| 3.2 | Preserve read-only behavior | `src/app/features/offers/pages/offers-home-page.component.html` | Render the field as display text only, with no input, button, or edit affordance. |
| 3.3 | Place the field consistently with existing list content | `src/app/features/offers/pages/offers-home-page.component.html` | Keep the new field within the existing meta grid near other row attributes, rather than altering the premium/action panel. |

#### Phase 4: Adjust layout for the additional field
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Update offers metadata grid layout | `src/app/features/offers/pages/offers-home-page.component.scss` | Adjust the grid definition so the additional cell fits cleanly on desktop without crowding existing fields. |
| 4.2 | Validate responsive behavior | `src/app/features/offers/pages/offers-home-page.component.scss` | Preserve the current stacked mobile behavior and ensure the new field wraps predictably at existing breakpoints. |

#### Phase 5: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add formatter unit tests | `src/app/features/offers/utils/offer-protection-period.util.spec.ts` | Verify exact formatting and business-rule outputs, including `2025/01/15 00:00 - 2026/01/14 23:59`. |
| 5.2 | Extend offers E2E assertions | `tests/e2e/offers.spec.ts` | Add a Playwright assertion that the offers page renders the new `Okres ochrony` label and a value matching the expected date-range format. |
| 5.3 | Keep existing offers-page regression checks intact | `tests/e2e/offers.spec.ts` | Ensure the new assertion coexists with current coverage for page load and filter reset behavior. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/offer-protection-period.util.ts` | CREATE | Pure helper for CET-based protection-period calculation and string formatting. |
| `src/app/features/offers/utils/offer-protection-period.util.spec.ts` | CREATE | Unit tests for formatter rules and edge cases. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add current-time refresh signal and computed protection-period label for the offers list. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render the new read-only `Okres ochrony` field in each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Update layout to accommodate the extra metadata field without breaking responsiveness. |
| `tests/e2e/offers.spec.ts` | MODIFY | Add page-level regression coverage for visible protection-period rendering. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Run `ng test` and confirm formatter unit tests pass for the Jira example and edge cases
5. [ ] Run `playwright test tests/e2e/offers.spec.ts` and confirm the new `Okres ochrony` field is visible on `/offers`
6. [ ] Open `/offers` and verify every row shows the same protection-period value in format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`
7. [ ] Confirm the rendered value does not depend on `offer.createdAt`, `offer.validTo`, or `contractData.coverageStartDate/coverageEndDate`
8. [ ] Confirm the offers page layout remains readable on desktop and narrow widths