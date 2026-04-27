# SDLC-97: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task delivers the missing **Okres ochrony** field on the offers list at `/offers`, using the existing card-based list layout in the repository rather than redesigning the screen into a table. The value must be calculated dynamically from the current system date at render time, last exactly one calendar year, and be displayed in the format `yyyy/MM/dd - yyyy/MM/dd` for every visible offer row.

### User Stories
- US-001: As a sales user, I want to see the protection period on the offers list, so that I can quickly read the annual coverage range without opening offer details.
  - Given I have access to the offers list / When the list is displayed / Then each visible offer row shows `Okres ochrony` with a calculated date range.
- US-002: As a business user, I want the protection period to be generated automatically from today, so that no manual data entry is needed.
  - Given the offers list is rendered on a specific system date / When the UI computes the value / Then the start date equals the current system date and the end date equals one calendar year later.
- US-003: As any user who can access the offers list, I want the field to be visible consistently, so that all users see the same information model.
  - Given a user can open `/offers` / When offer rows are shown / Then the `Okres ochrony` field is visible for all rows without role-specific conditions.

### Functional Requirements
- FR-001: The offers list page routed from `src/app/app.routes.ts` under path `/offers` shall display a field labeled `Okres ochrony` for each offer row.
- FR-002: The value shall be calculated in the presentation layer of `src/app/features/offers/pages/offers-home-page.component.ts`; it must not require persistence in `Offer` data.
- FR-003: The start date shall equal the current system date at the time the list view is rendered.
- FR-004: The end date shall be exactly one calendar year after the start date, with valid date rollover behavior.
- FR-005: The rendered value shall use the exact format `yyyy/MM/dd - yyyy/MM/dd`.
- FR-006: The field shall remain visible in the existing `.offer-row__meta-grid` layout for all offer types currently handled by the page (`MOTOR` and `CROP`).
- FR-007: The task shall not introduce repository, API, or model contract changes in `src/app/core/repositories/offers.repository.ts` or `src/app/core/models/offer/offer.model.ts`.
- FR-008: The task shall not add create/edit form inputs, filters, sorting, or export behavior for the new field.

### Edge Cases
- EC-001: For a leap-day render date such as `2024/02/29`, the end date must resolve to a valid calendar date: `2025/02/28`.
- EC-002: If an offer already contains contract coverage dates in `contractData`, the list must still display the dynamic protection period derived from the current system date, because the Jira scope is presentation-only.
- EC-003: All rows rendered in the same page render cycle must show the same protection-period value.
- EC-004: On responsive layouts where `.offer-row__meta-grid` collapses from 5 columns to 3 or 1, the protection-period field must remain readable and present.
- EC-005: If the list is empty after filtering, the empty state must remain unchanged.

### Success Criteria
- [ ] `/offers` displays `Okres ochrony` for every visible offer row.
- [ ] The displayed value matches `yyyy/MM/dd - yyyy/MM/dd`.
- [ ] For a fixed system date of `2025/01/15`, the rendered value is `2025/01/15 - 2026/01/15`.
- [ ] Leap-year behavior is covered and returns a valid one-year range.
- [ ] No `Offer` model, repository, or external integration changes are introduced.
- [ ] Unit and e2e tests verify the behavior.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The current codebase already follows the correct architectural direction for this ticket: the offers list is a standalone Angular page implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The page uses Angular signals, `toSignal(...)`, and page-local helper methods rather than introducing extra services for simple UI derivation. That is the right pattern for this change because Jira explicitly scopes the work to the offers-list presentation layer.

The implementation should remain presentation-only:
- keep `OffersRepository` unchanged,
- keep `Offer` unchanged,
- compute the value in `OffersHomePageComponent`,
- render it inside the existing `.offer-row__meta-grid`,
- preserve the current responsive grid behavior in `offers-home-page.component.scss`.

Repository evidence shows the required behavior should be delivered using:
- a page-level derived value (`protectionPeriodLabel`),
- deterministic helpers for date formatting and year rollover,
- markup-level rendering in the existing offer metadata block,
- regression coverage in both Jasmine and Playwright.

Confluence discovery results:
- No existing service/API relevant to this task was found in `Services` space — no reuse candidate required.
- Domain context was found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which describes smart-agent screens from the business perspective. This supports keeping the solution within the existing offers screen instead of redesigning the list component structure.

Current repository state already contains the expected target pattern:
- `OffersHomePageComponent` exposes `protectionPeriodLabel`,
- `getProtectionPeriodLabel()`, `addOneCalendarYear(...)`, and `formatDate(...)` implement the date logic,
- `offers-home-page.component.html` renders `Okres ochrony`,
- `offers-home-page.component.spec.ts` and `tests/e2e/offers-protection-period.spec.ts` already model the right regression checks.

Therefore, SDLC-97 should be delivered as a **branch-alignment / implementation-reconciliation task**:
- if the delivery branch does not yet contain the current protection-period implementation, port it exactly into the listed files;
- if it already does, keep scope to verification and parity checks only, with no repository or API changes.

### Task Breakdown

#### Phase 1: Align the offers-list UI with the required protection-period behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Confirm screen scope | `src/app/app.routes.ts`, `src/app/features/offers/pages/offers-home-page.component.html` | Verify the Jira change applies to the existing `/offers` page and should be implemented as a metadata field in the current card/grid layout, not as a new table component. |
| 1.2 | Port or preserve protection-period calculation | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure the component exposes a presentation-layer value for `Okres ochrony`, derived from the current system date, one calendar year ahead, formatted as `yyyy/MM/dd - yyyy/MM/dd`. |
| 1.3 | Render the field in each offer row | `src/app/features/offers/pages/offers-home-page.component.html` | Ensure each `.offer-row` includes a labeled `Okres ochrony` block in `.offer-row__meta-grid`, bound to the component’s calculated value. |
| 1.4 | Preserve responsive layout | `src/app/features/offers/pages/offers-home-page.component.scss` | Keep the metadata grid readable with 5 columns on wide screens and existing responsive collapse rules at narrower breakpoints. |
| 1.5 | Avoid data-layer changes | `src/app/core/repositories/offers.repository.ts`, `src/app/core/models/offer/offer.model.ts` | Verify no repository, DTO, or mock payload changes are introduced, because the value is computed at render time only. |

#### Phase 2: Lock the behavior with regression tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Verify standard-date rendering in unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Use `jasmine.clock().mockDate(...)` to assert that a fixed system date renders the exact expected range string. |
| 2.2 | Verify leap-year rollover in unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep or add a leap-day test to confirm one-calendar-year behavior produces a valid end date. |
| 2.3 | Verify browser rendering on `/offers` | `tests/e2e/offers-protection-period.spec.ts` | Confirm the first visible offer row contains `Okres ochrony` with the expected runtime-calculated value in the browser. |
| 2.4 | Verify no regression in row metadata layout | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Preserve layout-level assertions that the protection-period block remains part of the offer metadata presentation. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Align or preserve the protection-period calculation and formatting helpers in the offers page component. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Align or preserve rendering of the `Okres ochrony` field inside each offer-row metadata grid. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Align or preserve responsive grid styling for the extra metadata field. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Keep focused unit coverage for fixed-date rendering and leap-year behavior. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Keep browser-level verification of the protection-period field on the offers list. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes without template or TypeScript errors
5. [ ] `npm run test -- --watch=false --browsers=ChromeHeadless` passes offers-page unit coverage
6. [ ] `npm run e2e` passes the offers protection-period scenario
7. [ ] Manual check on `/offers` confirms every visible offer row shows `Okres ochrony` in `yyyy/MM/dd - yyyy/MM/dd`
8. [ ] Manual check confirms no `Offer` model, repository, or route changes were needed