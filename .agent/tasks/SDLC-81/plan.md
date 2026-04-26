# SDLC-81: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task adds a new business field, **„Okres ochrony”**, to the offers list screen (`Przygotowane oferty`). The value is not stored per offer and does not require new backend data. It must be derived automatically from the current day in the **CET business timezone**, rendered in the required display format, and shown for every visible offer without changing user permissions or requiring user input.

### User Stories
- US-001: As a sales user, I want to see the protection period directly on the offers list, so that I can check the date range without opening offer details.
  - Given I can access the offers list / When the list is displayed / Then each visible offer shows an „Okres ochrony” value.
- US-002: As a business user, I want the protection period to be calculated automatically from the current CET day, so that the displayed value is always based on the current business date.
  - Given the current business date is known / When the offers list is rendered / Then the start date is that CET day at `00:00` and the end date is the day before the anniversary at `23:59`.
- US-003: As an administrator, I want the field to be visible for all users who can see the offers list, so that the list layout is consistent for everyone.
  - Given a user has access to `/offers` / When the page loads / Then the new field is visible without additional role-based conditions.

### Functional Requirements
- FR-001: Add a new list field labeled **„Okres ochrony”** to the offers list UI in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-002: Calculate the protection period dynamically from the current date in the CET business timezone.
- FR-003: Set the protection period start to the current CET day at `00:00`.
- FR-004: Set the protection period end to the day preceding the anniversary of the start date at `23:59`.
- FR-005: Render the value in the format `yyyy/MM/dd hh:mm - yyyy/MM/dd hh:mm`, following the accepted business example (`2025/05/10 00:00 - 2026/05/09 23:59`).
- FR-006: Use the same derived value for every offer row on the list; do not require any new property on the `Offer` model and do not modify mock offer payloads for production behavior.
- FR-007: Keep the field visible for all users already authorized to access the offers list; no new permission checks are introduced.
- FR-008: The new field must not break existing filtering, sorting, transitions, navigation, or offer actions already implemented in `OffersHomePageComponent`.

### Edge Cases
- EC-001: If the browser runs outside Poland/CET, the displayed period must still be based on the CET business timezone, not the client machine’s local timezone.
- EC-002: If the current date is a leap-day boundary, the end date must still follow the rule “add 1 year, then subtract 1 day.”
- EC-003: If the offers list is empty after filtering, no errors occur; the empty state remains unchanged.
- EC-004: Long protection-period text must wrap or fit cleanly in the existing responsive offer-row layout.
- EC-005: If the page remains open across a CET day change, the displayed value should refresh on the next scheduled recalculation rather than requiring manual data entry.

### Success Criteria
- [ ] Every offer row on the offers list shows an **„Okres ochrony”** field.
- [ ] The displayed value matches the CET rule and accepted example (`start 00:00`, `end previous day to anniversary 23:59`).
- [ ] The rendered format is consistent across rows and matches the required `yyyy/MM/dd ... - yyyy/MM/dd ...` output.
- [ ] No `Offer` API/model contract changes are required for this feature.
- [ ] Existing offers-list filtering, sorting, and row actions continue to work without regression.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The current offers list is implemented as a standalone Angular component with signals and computed state in `src/app/features/offers/pages/offers-home-page.component.ts`. The screen is not a literal HTML table; it renders card-like `offer-row` entries with a metadata grid in `offers-home-page.component.html`. To keep scope aligned with the existing UI, the requested “column” should be implemented as an additional, consistently positioned metadata slot within each offer row rather than a separate table refactor.

Confluence discovery produced:
- **Domain context found:** [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) — confirms the application is an agent-facing sales workflow and the offers list is part of that operational screen set. The implementation should therefore stay within the existing `Przygotowane oferty` view and preserve current interaction patterns.
- **API/SERVICE result:** No existing offer-related service/API documentation was found in the `Services` space after scoped searches, so there is no reusable backend/service contract to integrate for this ticket. This supports a UI-only derived-value implementation.

Key design decisions:
- Reuse the existing `OffersHomePageComponent` signal/computed pattern already used for `filteredOffers`, `summaryTiles`, and `totalVisibleOffers`.
- Keep the calculation **view-only**. Do **not** add `protectionPeriod` to `Offer`, do **not** modify `src/app/core/models/offer/offer.model.ts`, and do **not** alter `src/app/core/repositories/offers.repository.ts`.
- Introduce a small feature-local utility for date calculation/formatting so the CET logic is deterministic and unit-testable.
- Calculate the business date using an explicit timezone (`Europe/Warsaw` as the CET business zone), not the browser locale.
- Format the string manually instead of relying on Angular’s `date` pipe token `hh`, because the acceptance example requires `23:59`, which is a 24-hour output. The implementation should follow the accepted example exactly.
- Update the offers-row layout styles in `offers-home-page.component.scss` so the additional field fits without breaking the responsive layout.

Recommended implementation shape:
1. Create a pure helper such as `buildOfferProtectionPeriodLabel(now?: Date): string`.
2. In `OffersHomePageComponent`, expose a reactive `protectionPeriodLabel` signal/computed value.
3. Render that value in each offer row under a new label `Okres ochrony`.
4. Optionally schedule a recalculation for the next CET midnight so long-lived sessions remain correct without refresh.
5. Add focused tests for both date arithmetic and rendered UI.

### Task Breakdown

#### Phase 1: Add deterministic protection-period calculation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create CET date utility | `src/app/features/offers/utils/offer-protection-period.util.ts` | Add a pure helper that derives the CET business day, calculates end date as “+1 year -1 day,” and returns the formatted label string. |
| 1.2 | Cover date arithmetic with unit tests | `src/app/features/offers/utils/offer-protection-period.util.spec.ts` | Test the accepted example (`2025/05/10 00:00 -> 2026/05/09 23:59`), timezone independence, and a leap-year boundary. |

#### Phase 2: Surface the new field on the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add reactive protection-period state to offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Import the utility, expose a `protectionPeriodLabel` value, and optionally schedule refresh at the next CET midnight to keep long-open sessions correct. |
| 2.2 | Render „Okres ochrony” in each offer row | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new metadata block inside `.offer-row__meta-grid` with label `Okres ochrony` and the formatted derived value. |
| 2.3 | Adjust layout for the extra field | `src/app/features/offers/pages/offers-home-page.component.scss` | Expand or rebalance the metadata grid so the new field fits on desktop and stacks cleanly under existing responsive breakpoints. |

#### Phase 3: Verify UI behavior at component level
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers-page rendering tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Render `OffersHomePageComponent` with stubbed repositories and assert that the `Okres ochrony` label/value is shown for visible offers using a fixed clock. |
| 3.2 | Regression-check existing offers interactions | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify the new field does not interfere with existing filters, sorting controls, and row rendering. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/offer-protection-period.util.ts` | CREATE | Pure helper for CET-based protection-period calculation and formatting. |
| `src/app/features/offers/utils/offer-protection-period.util.spec.ts` | CREATE | Unit tests for accepted date rules and edge cases. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add reactive derived value and optional midnight refresh scheduling. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render the new `Okres ochrony` field in each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Update grid/layout styles for the additional field. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component-level tests covering rendered visibility and non-regression. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `/offers` shows `Okres ochrony` for every visible offer row
5. [ ] The displayed value matches the accepted example when the clock is fixed to `2025-05-10`
6. [ ] Browser timezone differences do not change the rendered CET-based result
7. [ ] Existing search, filters, sort controls, and offer actions still work as before