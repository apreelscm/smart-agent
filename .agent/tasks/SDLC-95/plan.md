# SDLC-95: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Task adds a new visible metadata column on the offers list so users can see the protection period for each offer without opening details. The value is not sourced from offer data; it is calculated from the current system date at list render time and displayed in the exact format `yyyy/MM/dd - yyyy/MM/dd`.

### User Stories
- US-001: As a sales user, I want to see the protection period directly on the offers list, so that I can quickly verify the annual coverage range for an offer.
  - Given I have access to the offers list / When the list is displayed / Then each offer row shows a protection period value in the required format.
- US-002: As a business user, I want the protection period to be calculated automatically from today, so that I do not need to interpret or enter dates manually.
  - Given the offers list is opened on a specific system date / When the UI renders the new field / Then the start date equals today and the end date equals one calendar year later.
- US-003: As any user with access to the offers list, I want the new column to be visible without role-based differences, so that the list remains consistent for all users.
  - Given a user can open `/offers` / When the list is rendered / Then the protection period column is visible for every offer row.

### Functional Requirements
- FR-001: Add a new offers-list metadata column labeled to clearly indicate protection period, e.g. `Okres ochrony`.
- FR-002: Render the protection period for every visible offer row on `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-003: Calculate the start of the period from the current system date at UI render time, not from any persisted offer field.
- FR-004: Calculate the end of the period as one calendar year after the start date.
- FR-005: Format the displayed value exactly as `yyyy/MM/dd - yyyy/MM/dd`.
- FR-006: Do not include time, timezone text, locale-specific separators, or extra labels inside the value.
- FR-007: Keep the change limited to the offers list presentation layer; no repository payload or mock data contract changes are required.
- FR-008: Keep the new column visible for all users who can access the offers list.

### Edge Cases
- EC-001: If the current date falls on a leap day, the end date must still resolve to a valid calendar date one year later and remain formatted as `yyyy/MM/dd`.
- EC-002: If an offer contains its own contract or coverage dates, the list must still show the dynamically derived period from the current system date, per ticket scope.
- EC-003: All offer rows displayed in one render should show the same protection period value for that render cycle.
- EC-004: Responsive layout must remain usable after adding the extra metadata column.

### Success Criteria
- [ ] `/offers` shows a new protection-period column for every offer row.
- [ ] The displayed value matches `yyyy/MM/dd - yyyy/MM/dd`.
- [ ] The start date equals the current system date when the list is displayed.
- [ ] The end date equals start date plus one calendar year.
- [ ] No backend, repository, or mock payload changes are required for runtime behavior.
- [ ] Automated tests verify the rendered value for a fixed system date.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular 20 page component using signals and inline view helpers in `src/app/features/offers/pages/offers-home-page.component.ts`, with card-style rows rendered by `offers-home-page.component.html` and styled by `offers-home-page.component.scss`. The change should follow that existing pattern and stay inside the page component rather than introducing repository or model changes.

Confluence discovery produced no relevant reusable service/API pages in `Services` and no relevant domain guidance pages in `SDLC`. Therefore:
- No existing service found in `Services` space — no reuse candidate for this task.
- No relevant ADR/convention/glossary entry found in `SDLC` space — proceed with repository conventions.

Implementation should:
- add one more metadata cell in the existing `.offer-row__meta-grid` block rather than redesigning the list into a table,
- compute the protection period in the component layer from the current system date,
- format the value with a deterministic helper instead of relying on locale defaults, so the output always stays `yyyy/MM/dd - yyyy/MM/dd`,
- keep the calculation independent from `Offer.contractData.coverageStartDate` / `coverageEndDate`, because the Jira scope explicitly requires a dynamic value based on “today”.

Recommended component changes:
- add a protected helper such as `getProtectionPeriodLabel(): string`,
- back it with private helpers for:
  - adding one calendar year safely,
  - formatting a `Date` to `yyyy/MM/dd`,
- render the result in a new `Okres ochrony` metadata block in the existing grid.

UI/layout changes should be minimal:
- update the metadata grid from 4 to 5 desktop columns, or otherwise adjust the grid so the new item reads as a first-class column on wide screens,
- preserve current mobile stacking behavior under the existing breakpoints.

Testing should add a focused component spec for the offers page:
- stub `OffersRepository`, `ReferenceDataRepository`, and `SalesFlowRuntimeRepository`,
- freeze the system date with Jasmine clock,
- verify the new column renders the expected value,
- include a leap-year-oriented case if feasible to lock down one-calendar-year behavior.

### Task Breakdown

#### Phase 1: Add dynamic protection period to the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add protection-period formatting helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Add page-level logic to derive start date from current system date, compute end date as one calendar year later, and format both dates as `yyyy/MM/dd`. |
| 1.2 | Render new metadata column | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new metadata block in `.offer-row__meta-grid` labeled `Okres ochrony` and bind it to the computed/formatted value for each offer row. |
| 1.3 | Adjust responsive layout | `src/app/features/offers/pages/offers-home-page.component.scss` | Update the offer metadata grid to accommodate the additional column while keeping existing desktop and mobile readability. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add component test for standard date | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Create a standalone component spec that mocks repositories, freezes the system date, renders the offers page, and asserts the protection period value appears in the required format. |
| 2.2 | Add edge-case test for yearly rollover | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add a second test covering a boundary date such as leap day to confirm one-calendar-year behavior remains valid and formatted correctly. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add dynamic protection-period calculation and formatting helpers in the offers page component. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render the new `Okres ochrony` metadata column on each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adjust grid/layout styling for the extra metadata column. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Add automated tests for protection-period rendering and date calculation behavior. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Tests pass with `npm run test -- --watch=false`
3. [ ] New tests cover required formatting and dynamic date calculation
4. [ ] Manual check on `/offers` confirms every row shows `Okres ochrony` in `yyyy/MM/dd - yyyy/MM/dd`
5. [ ] Manual check confirms no offer payload/model changes were required for the new UI field