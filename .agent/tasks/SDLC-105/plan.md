# SDLC-105: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Dodanie na ekranie listy ofert nowej kolumny/pola prezentacyjnego „Okres ochrony”, wyliczanego dynamicznie na podstawie bieżącej daty systemowej w momencie wyświetlenia listy. Wartość ma być widoczna dla każdej oferty w formacie `yyyy/MM/dd - yyyy/MM/dd`, bez korzystania z dat zapisanych w danych oferty i bez wpływu na istniejące akcje, filtrowanie, sortowanie oraz przełączanie waluty.

### User Stories
- US-001: As a sales agent, I want to see the protection period directly on the offers list, so that I can verify the coverage window without opening offer details.
  - Given the offers list is visible / When the page renders offer rows / Then each row shows `Okres ochrony` in the format `yyyy/MM/dd - yyyy/MM/dd`.
- US-002: As a business user, I want the protection period to be derived automatically from today’s system date, so that the presentation is consistent and requires no manual input.
  - Given the offers list is opened on a given day / When the period is calculated / Then the start date equals today and the end date equals the day before the first anniversary.

### Functional Requirements
- FR-001: Extend the offers list view in `src/app/features/offers/pages/offers-home-page.component.html` with a new visible field labeled `Okres ochrony`.
- FR-002: Place the new field inside the existing `offer-row__meta-grid`, which currently acts as the row’s column layout.
- FR-003: The displayed value must be generated in the frontend presentation layer of `OffersHomePageComponent`.
- FR-004: The protection period start date must equal the current system date at the moment the offers list is presented.
- FR-005: The protection period end date must equal the day immediately preceding the first anniversary of the start date.
- FR-006: The displayed format must be exactly `yyyy/MM/dd - yyyy/MM/dd`, including zero-padded month and day values.
- FR-007: The value must be shown for every rendered offer row, regardless of offer product (`MOTOR` or `CROP`) or status.
- FR-008: The implementation must not use `offer.contractData.coverageStartDate` or `offer.contractData.coverageEndDate`, because the Jira clarification makes the runtime source the current system date, not persisted offer data.
- FR-009: Existing filters, sorting, status transitions, premium currency switching, and navigation from the offers list must continue to work unchanged.
- FR-010: The implementation must include automated tests covering normal rendering and date edge cases.

### Edge Cases
- EC-001: If the page is opened on a leap day, the end date should still resolve to the day before the computed anniversary, e.g. `2024/02/29 - 2025/02/28`.
- EC-002: Timezone conversion must not shift the displayed day; formatting should use local date parts rather than UTC-sensitive string serialization.
- EC-003: All offers on one page instance should show a consistent period snapshot for that render session, rather than recalculating per row at different moments.
- EC-004: Currency changes (`PLN`/`EUR`/`USD`) must not alter the displayed `Okres ochrony`.
- EC-005: Empty filtered results should still leave the page stable; no protection-period rendering errors should occur when no rows are visible.
- EC-006: Both `MOTOR` and `CROP` offers must display the same dynamically computed period format.

### Success Criteria
- [ ] A new `Okres ochrony` field is visible on each offer row in the offers list.
- [ ] The value is shown in the exact format `yyyy/MM/dd - yyyy/MM/dd`.
- [ ] The start date equals the current system date when the list is presented.
- [ ] The end date equals the day before the annual anniversary of the start date.
- [ ] The value is frontend-calculated and not manually entered by the user.
- [ ] Existing list behavior, including filters, actions, and currency switching, remains unaffected.
- [ ] Automated tests verify standard and edge-case date behavior.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The current codebase is an Angular 20 standalone-component application using PrimeNG, with list state and presentation logic concentrated in `src/app/features/offers/pages/offers-home-page.component.ts`. The offers screen already follows a signal/computed-heavy pattern and currently contains:
- repository-backed list loading through `toSignal(...)`,
- computed view models like `filteredOffers`, `summaryTiles`, and `offerMenuModels`,
- presentation-only helpers such as `getDisplayedPremium(...)`,
- UI rendering via `offers-home-page.component.html`,
- layout styling in `offers-home-page.component.scss`,
- component-level unit coverage in `offers-home-page.component.spec.ts`.

For this ticket, the safest implementation is to keep the new coverage-period logic in the same component as a presentation concern, because:
- there is no backend/API integration required,
- the Jira clarification explicitly says the value is based on the current system date,
- the repo already uses component helpers for similar derived display values,
- persisted offer data already contains `contractData.coverageStartDate` / `coverageEndDate`, but those fields must not be reused for this requirement.

Confluence discovery outcome:
- **API/SERVICE scope (`Services`)**: No relevant existing service or endpoint for protection-period calculation was found in the configured space, so no service reuse is needed for this task.
- **DOMAIN scope (`SDLC`)**: The generic screen overview page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) confirms the offers list is a business-facing cockpit view, but no stricter UI/date ADR was found. The plan therefore follows the existing offers page structure already implemented in `offers-home-page.component.html` and `.scss`.

Implementation details:
1. **Capture “today” once per page instance**
   - Add a computed/signal-backed page-level date snapshot in `OffersHomePageComponent`, so all rendered rows use the same coverage period for the same list presentation.
   - This aligns with the Jira wording “w momencie wyświetlenia listy ofert”.

2. **Add date helpers in the component**
   - Implement private/protected helper methods for:
     - normalizing a `Date` to a stable local calendar day,
     - computing the end date as “anniversary minus one day”,
     - formatting date parts into `yyyy/MM/dd`,
     - exposing a final `coveragePeriodLabel` string for template usage.
   - Avoid `toISOString()` for display generation, because it can shift the day across timezones.

3. **Render the new field in the existing row grid**
   - Add a fifth metadata block labeled `Okres ochrony` inside `.offer-row__meta-grid`.
   - Reuse the same visual structure as other metadata cells (`offer-row__meta-label`, `strong`, `span`) to stay consistent with current page styling.

4. **Adjust layout for the extra column**
   - Update the grid definition in `offers-home-page.component.scss` so five metadata items still fit cleanly on desktop while preserving current responsive collapse behavior.

5. **Test deterministically**
   - Extend `offers-home-page.component.spec.ts` using mocked system dates (`jasmine.clock()` / equivalent date mocking approach already compatible with Angular/Jasmine tests).
   - Verify:
     - normal rendering for a fixed date,
     - leap-year boundary,
     - presence of the label in the DOM,
     - no regression for existing currency functionality on the same page.

### Task Breakdown

#### Phase 1: Add protection-period calculation to the offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add page-level date snapshot | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce a stable “today at list presentation time” value so all rows use one consistent period. |
| 1.2 | Implement coverage-period helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Add helper logic to compute anniversary-minus-one-day and format dates as `yyyy/MM/dd`. |
| 1.3 | Expose formatted label to template | `src/app/features/offers/pages/offers-home-page.component.ts` | Add a computed/helper such as `coveragePeriodLabel()` for direct row rendering without using offer data fields. |

#### Phase 2: Render the new column in the list UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add `Okres ochrony` field to row metadata | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new metadata cell in `offer-row__meta-grid` labeled `Okres ochrony` and bind the computed label. |
| 2.2 | Keep card/list layout readable with 5 columns | `src/app/features/offers/pages/offers-home-page.component.scss` | Update the desktop grid sizing for `.offer-row__meta-grid` to support the additional field while keeping responsive breakpoints intact. |
| 2.3 | Preserve current interactions | `src/app/features/offers/pages/offers-home-page.component.html` | Ensure the new field is presentation-only and does not change the placement or behavior of existing premium/action areas. |

#### Phase 3: Add regression and date-edge-case tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add fixed-date rendering test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Mock the system date and assert that the rendered `Okres ochrony` text matches the expected formatted range. |
| 3.2 | Add leap-year edge-case test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify `2024/02/29` produces `2025/02/28` as the end date. |
| 3.3 | Add regression assertion with existing offers UI | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Confirm the new field coexists with current currency/premium rendering and does not break existing page initialization. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add stable current-date snapshot, protection-period calculation helpers, and formatted label exposure for the template. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add the new `Okres ochrony` metadata field to each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adjust row metadata grid layout to accommodate the new field without breaking responsiveness. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add deterministic tests for rendered coverage period and leap-year behavior. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] On `/offers`, each row shows a new `Okres ochrony` field
5. [ ] The displayed value matches `yyyy/MM/dd - yyyy/MM/dd`
6. [ ] For a mocked system date, the start date equals mocked “today”
7. [ ] For a mocked system date, the end date equals anniversary minus one day
8. [ ] Leap-day test passes with expected end date
9. [ ] Existing filters, status actions, and currency switching still work on the offers page