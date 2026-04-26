# SDLC-73: Na liście ofert dodaj kolumne z okresem ochrony

## Specification

### Overview
This task adds a new read-only `Okres ochrony` field to each offer row on the offers list so users can see the protection period directly from the list view without opening offer details. In the current codebase, the offers list is rendered as card rows in `src/app/features/offers/pages/offers-home-page.component.html`, not as a table, so the business “column” should be implemented as an additional metadata slot within each offer row.

The displayed value must be calculated dynamically from the current system date (`od dziś`), not from stored offer dates. The period starts at `00:00` of the current day and ends at `23:59` on the same calendar day one year later. The visible text format must be `yyyy-MM-dd – yyyy-MM-dd`. If the value cannot be derived, the UI must show `—`.

### User Stories
- US-001: As a sales agent, I want to see the protection period directly on the offers list, so that I can compare offers without opening details.
  - Given I open the offers list
  - When offer rows are rendered
  - Then each row shows `Okres ochrony` in read-only form

- US-002: As a business user, I want the protection period shown in a standardized date range format, so that the information is easy to read and consistent across offers.
  - Given an offer is visible on the list
  - When its protection period is displayed
  - Then the value uses the format `yyyy-MM-dd – yyyy-MM-dd`

- US-003: As any user with access to the offers list, I want the protection period to be derived from the current day, so that the list reflects the requested dynamic business rule.
  - Given today is the current system date
  - When the offers list is loaded
  - Then the displayed period starts today and ends one year later

### Functional Requirements
- FR-001: Add a new read-only `Okres ochrony` metadata field to each offer row on the offers list in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-002: The displayed value must be computed from the current system date, not from persisted offer fields such as `contractData.coverageStartDate` or `contractData.coverageEndDate`.
- FR-003: The protection period start must be the current local day at `00:00`.
- FR-004: The protection period end must be the same calendar day one year later at `23:59`.
- FR-005: The displayed text must use the format `yyyy-MM-dd – yyyy-MM-dd`.
- FR-006: If the value cannot be computed, display `—`.
- FR-007: The new field must be shown for all offers visible on the list, regardless of product (`MOTOR` or `CROP`) or status.
- FR-008: Adding the new field must not change existing filtering, sorting, navigation, status transitions, premium display, or split-button actions on the offers list.
- FR-009: The existing card-row layout must remain the offers-list presentation model; do not replace it with a new table implementation.
- FR-010: Add automated coverage for the date-range calculation and for rendering the new field on the offers page.

### Edge Cases
- EC-001: If the date-range helper receives an invalid reference date, the UI should render `—` instead of a malformed value.
- EC-002: Leap-year rollover must be handled consistently when the current day is February 29.
- EC-003: All rows on a single render pass should use the same computed protection period value, rather than recalculating separately per row and risking inconsistent output around midnight.
- EC-004: Adding a fifth metadata item to `.offer-row__meta-grid` must not break the existing responsive layout on desktop or mobile widths.
- EC-005: Existing mock offers already contain `contractData.coverageStartDate` and `contractData.coverageEndDate`; the offers list must ignore those fields for this feature because the Jira comments explicitly require calculation from today.

### Success Criteria
- [ ] The offers list displays `Okres ochrony` for every visible offer row.
- [ ] The displayed value uses the format `yyyy-MM-dd – yyyy-MM-dd`.
- [ ] The value is derived from the current system date, starting today and ending one year later.
- [ ] The fallback `—` is shown if the display value cannot be computed.
- [ ] Existing offers-list filters, sorting, actions, and navigation continue to work without regression.
- [ ] Automated tests cover both the date-range logic and the rendered offers-list field.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repo is an Angular 20 application using standalone components, signals, and PrimeNG. The offers list is implemented as a card list, not a data table:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The current offer row shows four metadata cells inside `.offer-row__meta-grid`. To keep scope aligned with the existing codebase, the “new column” should be implemented as a fifth metadata cell labeled `Okres ochrony` rather than replacing the page with a table.

Key design decisions:
- Reuse the current offers-list card pattern already used in `offers-home-page.component.html`.
- Add a small pure utility to build and format the protection period from a reference date.
- Compute the display value once per component render and reuse it across all rows, because the Jira comments define the period as dynamic from today, which makes it the same visible value for all listed offers at a given point in time.
- Do not change `Offer`, `OffersRepository`, or `public/mock/offers.json` for data sourcing. Although the mock payload already includes `contractData.coverageStartDate` / `coverageEndDate`, the ticket and comments override that data with a runtime rule based on the current system date.
- Use `—` only as a defensive fallback when the formatter cannot build a valid range.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space — proposing a local frontend implementation in the existing offers feature.
- The `SDLC` space search returned a general application context page, [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), but it does not define a specific constraint for implementing this offers-list field.

Reference codebase patterns:
- The offers list already centralizes UI-only computed values in component methods like `getOfferHeadlineSubject`, `getCropMetaPrimaryLine`, and `getPrimaryPremium` inside `offers-home-page.component.ts`.
- The page already uses labeled metadata blocks in `.offer-row__meta-grid`, which is the correct insertion point for the new display field.
- UI labels are hardcoded in templates, consistent with other pages such as `src/app/features/policies/pages/policies-home-page.component.html`.

### Task Breakdown

#### Phase 1: Add reusable protection-period formatting logic
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create date-range utility | `src/app/features/offers/utils/protection-period.util.ts` | Add a pure helper that accepts a reference `Date`, normalizes the start to local `00:00`, computes the end one year later at local `23:59`, and returns the display string `yyyy-MM-dd – yyyy-MM-dd`. |
| 1.2 | Add defensive fallback behavior | `src/app/features/offers/utils/protection-period.util.ts` | Return `—` when the reference date is invalid or the computed range cannot be formatted safely. |
| 1.3 | Add unit tests for the helper | `src/app/features/offers/utils/protection-period.util.spec.ts` | Cover standard date formatting, one-year rollover, leap-year behavior, and invalid-date fallback. |

#### Phase 2: Integrate the field into the offers list UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Expose computed display value in the offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Import the utility and expose a readonly property/method for the protection-period display so all rows use the same value during a render. |
| 2.2 | Add the new metadata cell | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new `offer-row__meta-grid` item labeled `Okres ochrony` and render the computed date range or `—`. |
| 2.3 | Preserve current interactions | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.html` | Ensure filters, sorting, transitions, copy, print placeholder, and navigation remain unchanged. |
| 2.4 | Ignore stored coverage dates for this screen | `src/app/features/offers/pages/offers-home-page.component.ts` | Keep the list display independent from `offer.contractData.coverageStartDate` / `coverageEndDate`, because Jira requires dynamic calculation from today. |

#### Phase 3: Adjust layout for the extra field
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Expand the metadata grid for a fifth item | `src/app/features/offers/pages/offers-home-page.component.scss` | Update `.offer-row__meta-grid` styling so the additional `Okres ochrony` block fits cleanly on desktop. |
| 3.2 | Preserve responsive behavior | `src/app/features/offers/pages/offers-home-page.component.scss` | Verify the existing breakpoints still collapse the metadata grid cleanly on narrower screens, adding an intermediate adjustment only if the fifth cell causes crowding. |

#### Phase 4: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add offers-page e2e coverage | `tests/e2e/offers-protection-period.spec.ts` | Verify that the offers page renders the `Okres ochrony` label and a value matching the expected `yyyy-MM-dd – yyyy-MM-dd` range derived from the current date. |
| 4.2 | Keep selectors aligned with existing test style | `tests/e2e/offers-protection-period.spec.ts` | Use the same Playwright approach already used in `tests/e2e/offers-filters.spec.ts`, relying on visible text and existing row locators rather than introducing a new testing pattern. |
| 4.3 | Validate rendering across multiple rows | `tests/e2e/offers-protection-period.spec.ts` | Assert that the field appears for multiple offer rows and that the displayed value is stable across the list on first load. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/protection-period.util.ts` | CREATE | Pure helper for building and formatting the dynamic protection-period range from the current date. |
| `src/app/features/offers/utils/protection-period.util.spec.ts` | CREATE | Unit tests for formatting, rollover, leap-year handling, and fallback behavior. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Integrate the helper and expose the protection-period display for the offers list. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add the `Okres ochrony` metadata field to each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adjust the metadata-grid layout so the additional field fits without UI regression. |
| `tests/e2e/offers-protection-period.spec.ts` | CREATE | Playwright coverage for rendering the new offers-list field and its expected date format. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes without template or style errors
5. [ ] `npm test` passes the new unit spec for the protection-period utility
6. [ ] `npm run e2e` confirms `Okres ochrony` is visible on `/offers` with the expected date-range format
7. [ ] Manual check on the offers page confirms the new field does not break filtering, sorting, actions, or responsive layout