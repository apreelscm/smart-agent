# SDLC-83: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task adds a new visible offers-list field labeled `Okres ochrony` and fills it with a dynamically calculated one-year protection window based on the current business date. The value must be calculated on the frontend at display time, formatted exactly as `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`, and shown for every offer visible on the list.

### User Stories
- US-001: As a user of the offers list, I want to see the `Okres ochrony` column, so that I can quickly verify the currently applicable protection window.
  - Given I have access to the offers list / When the offers list is rendered / Then each visible offer shows a non-empty `Okres ochrony` value.
- US-002: As a business user, I want the protection period to be displayed in a single, consistent format, so that the date range is unambiguous.
  - Given the value is shown / When I read it / Then it follows the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm` using 24-hour time.
- US-003: As an administrator/business owner, I want the new column to be visible to all users who can access the offers list, so that the information is not role-dependent.
  - Given any user can open the offers list / When the page loads / Then the `Okres ochrony` field is visible without additional permission logic.

### Functional Requirements
- FR-001: Add a new visible field labeled `Okres ochrony` on the offers list screen implemented in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-002: The value must be derived dynamically from the current date in the CET business timezone at render time; it must not be stored in offer data or persisted.
- FR-003: The protection period start must equal the current calendar day at `00:00`.
- FR-004: The protection period end must equal one year minus one day from the start date at `23:59`.
- FR-005: The displayed value must use the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- FR-006: Time must be presented in 24-hour format.
- FR-007: The same calculation rule applies to all listed offers and does not depend on per-offer attributes.
- FR-008: Existing offer filtering, sorting, status actions, navigation, and premium display must remain unchanged.

### Edge Cases
- EC-001: On a leap-year boundary, the end date must still resolve to the day before the anniversary according to civil-date arithmetic; e.g. a start on `2024/02/29 00:00` should end on `2025/02/28 23:59`.
- EC-002: The output must remain stable regardless of the browser’s local locale/timezone settings by explicitly using the CET/Warsaw business timezone for date-part derivation.
- EC-003: The value must not be blank for any rendered offer row, even when the underlying offer lacks contract coverage dates.
- EC-004: The empty-state view remains unchanged; when there are no offers, no protection-period value is rendered because no rows are present.
- EC-005: Responsive layouts must continue to work after adding the extra metadata column/card cell.

### Success Criteria
- [ ] The offers list shows a new `Okres ochrony` field for every rendered offer.
- [ ] The value is non-empty for all rendered offers.
- [ ] The value format is exactly `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] The start time is `00:00` and the end time is `23:59`.
- [ ] The example rule is satisfied in tests: `2025/05/10 00:00 - 2026/05/09 23:59`.
- [ ] The solution does not modify the offer API/model payload or persist the computed value.
- [ ] Existing offers-page behavior continues to work after the change.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repo is an Angular 20 standalone app using signals/computed state in feature pages. The offers list is rendered by `OffersHomePageComponent` in `src/app/features/offers/pages/offers-home-page.component.ts` and its template is a responsive card/list layout rather than a literal HTML table. To stay consistent with the existing codebase, the new “column” should be implemented as an additional metadata block inside `offer-row__meta-grid`, which is the current column-equivalent structure for each offer row.

Key decisions:
- Reuse the existing frontend-only presentation pattern in `OffersHomePageComponent`; do not change `Offer` in `src/app/core/models/offer/offer.model.ts`, `OffersRepository` in `src/app/core/repositories/offers.repository.ts`, or `public/mock/offers.json`.
- Although `Offer.contractData` already contains optional `coverageStartDate` and `coverageEndDate`, the Jira description explicitly says the value is dynamic from the current date and identical for all offers, so those persisted fields should not be repurposed for this task.
- Implement the calculation as a small pure utility, e.g. `src/app/features/offers/utils/protection-period.util.ts`, so the CET/Warsaw date logic is testable and not duplicated in the component/template.
- Do not rely on Angular `date` pipe alone for this requirement: `app.config.ts` sets `LOCALE_ID` to `pl-PL`, but locale does not guarantee the exact required string shape or timezone-safe civil-date calculation. The utility should explicitly derive the current calendar date in `Europe/Warsaw` and format the final string manually as `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- Expose one computed/read-only label from `OffersHomePageComponent` and reuse it across all rows, since the Jira explicitly states the rule is global and independent of offer attributes.
- Update `offers-home-page.component.scss` so the metadata grid supports one more item on large screens while preserving the existing responsive collapse behavior.

Confluence findings:
- No existing offer-list service, endpoint, or API contract was found in the configured `Services` space, so no external service reuse applies for this task.
- Domain context found: [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) confirms the app is a sales-agent workflow UI with offer-related screens. No more specific ADR/convention for “Okres ochrony” or date-format handling was found in the `SDLC` space.
- Because no relevant external service was found and the Jira scope explicitly says there are no new integrations, this plan keeps the implementation UI-only.

### Task Breakdown

#### Phase 1: Add deterministic protection-period calculation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create protection-period utility | `src/app/features/offers/utils/protection-period.util.ts` | Add a pure helper that derives the current civil date in `Europe/Warsaw`, computes start `00:00`, computes end as `+1 year - 1 day` at `23:59`, and formats the exact output string. |
| 1.2 | Add calculation unit tests | `src/app/features/offers/utils/protection-period.util.spec.ts` | Cover the Jira example date, exact string format, 24-hour output, and boundary cases such as leap-year rollover. |

#### Phase 2: Surface the new field on the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Wire computed label into offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Import the utility and expose a single read-only/computed `protectionPeriodLabel` for template binding. Keep filtering, sorting, and actions untouched. |
| 2.2 | Render the new list field | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new metadata block labeled `Okres ochrony` inside `offer-row__meta-grid` and bind the formatted value so it appears for every visible offer row. |
| 2.3 | Adjust layout styling | `src/app/features/offers/pages/offers-home-page.component.scss` | Update the grid to fit the extra metadata item cleanly on desktop and maintain current stacking behavior on narrow screens. |

#### Phase 3: Add UI-level regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers page component test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Create a focused TestBed spec with mocked repositories to verify that the `Okres ochrony` label is rendered and non-empty for displayed offers. |
| 3.2 | Keep existing app smoke path valid | `tests/e2e/smoke.spec.ts` | No functional change required unless the existing smoke test becomes unstable; only update if needed after running the suite. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/protection-period.util.ts` | CREATE | Pure formatter/calculation utility for the CET/Warsaw protection-period string. |
| `src/app/features/offers/utils/protection-period.util.spec.ts` | CREATE | Unit tests for format and date arithmetic rules. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Expose the calculated protection-period label to the offers list view. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render the new `Okres ochrony` field in each offer row. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adjust metadata grid/layout for the added field. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | UI-focused regression test for rendering the new field. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`.
2. [ ] Unit tests pass with `npm test -- --watch=false`.
3. [ ] New utility tests verify the example `2025/05/10 00:00 - 2026/05/09 23:59`.
4. [ ] New component test verifies the `Okres ochrony` label is rendered and non-empty for displayed offers.
5. [ ] Manual check on `/offers`: every rendered offer row shows `Okres ochrony`.
6. [ ] Manual check confirms the string format is exactly `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
7. [ ] Existing filtering, sorting, row actions, and navigation still behave as before.