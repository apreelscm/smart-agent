# SDLC-76: Zla kolumna

## Specification

### Overview
This task corrects the visual order of metadata fields on the offers list screen. The `Okres ochrony` field already exists on each offer row, but it is currently rendered after `Aktualizacja`. The fix is to reorder the existing metadata blocks so `Okres ochrony` appears before `Aktualizacja`, matching the Jira requirement without changing data sourcing, business logic, filtering, sorting, or backend integration.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the row layout matches the expected business order.
  - Given I open the offers list
  - When offer metadata is rendered
  - Then `Okres ochrony` is displayed immediately before `Aktualizacja`

- US-002: As a user reviewing multiple offers, I want the metadata order to be consistent across all offer rows, so that scanning the list is predictable.
  - Given multiple offer rows are visible
  - When I compare their metadata sections
  - Then each row uses the same field order with `Okres ochrony` before `Aktualizacja`

### Functional Requirements
- FR-001: On the offers list, reorder the metadata cells in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` is rendered before `Aktualizacja`.
- FR-002: The fix must reuse the existing `Okres ochrony` display value and not change how that value is computed.
- FR-003: The fix must preserve the existing `Aktualizacja` content and formatting.
- FR-004: The metadata order must be consistent for all offer rows, regardless of product or status.
- FR-005: No changes may be made to repository data loading, offer models, runtime transitions, or route/navigation behavior.
- FR-006: Automated tests must verify the corrected DOM/rendered order.

### Edge Cases
- EC-001: The reordered fields must remain correct for both `MOTOR` and `CROP` offers.
- EC-002: The order must remain correct when rows include optional content such as renewal links or different statuses.
- EC-003: Reordering the metadata blocks must not break the existing responsive grid behavior defined in `offers-home-page.component.scss`.
- EC-004: Existing protection-period logic must remain untouched so this bug fix does not regress SDLC-73 behavior.

### Success Criteria
- [ ] Each offer row shows `Okres ochrony` before `Aktualizacja`.
- [ ] The visible values for both fields remain unchanged apart from their position.
- [ ] Existing offers-page behaviors continue to work without regression.
- [ ] Automated tests validate the corrected field order.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a UI-only bug fix in the Angular standalone offers page. The relevant screen is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.scss`

Current state:
- `Okres ochrony` is already implemented and populated via `protectionPeriodDisplay` in `offers-home-page.component.ts`.
- The HTML currently renders the `Aktualizacja` metadata block before the `Okres ochrony` block inside `.offer-row__meta-grid`.
- The SCSS already supports a 5-column metadata grid, so no layout expansion is needed for this ticket unless manual verification reveals a visual regression.

Planned change:
- Swap the order of the two existing metadata `<div>` blocks in `offers-home-page.component.html` so the DOM/rendered sequence becomes:
  1. Pojazd/Uprawy
  2. Kanał
  3. Wariant
  4. Okres ochrony
  5. Aktualizacja

Testing strategy:
- Extend the existing unit test in `src/app/features/offers/pages/offers-home-page.component.spec.ts` to assert metadata label order within each `.offer-row__meta-grid`.
- Extend the existing Playwright test in `tests/e2e/offers-protection-period.spec.ts` to verify that `Okres ochrony` precedes `Aktualizacja` in the rendered row.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space — no reuse required.
- The `SDLC` search returned the general page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), but it does not add a more specific implementation constraint for this ordering fix.

### Task Breakdown

#### Phase 1: Reorder the offers-list metadata
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Swap metadata block order | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` block so it is rendered immediately before the existing `Aktualizacja` block inside `.offer-row__meta-grid`. |
| 1.2 | Preserve existing bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `protectionPeriodDisplay` and `offer.updatedAt` bindings unchanged so the fix only affects presentation order. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update component test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add an assertion that the ordered metadata labels in each row place `Okres ochrony` before `Aktualizacja`. |
| 2.2 | Update e2e test | `tests/e2e/offers-protection-period.spec.ts` | Add a browser-level assertion that the rendered metadata sequence on `/offers` shows `Okres ochrony` before `Aktualizacja`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the existing metadata cells so `Okres ochrony` appears before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add unit coverage for the corrected metadata order. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add end-to-end verification of the visible field order on the offers page. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Run `npm run build` and confirm the template compiles successfully
5. [ ] Run `npm test` and confirm the updated `OffersHomePageComponent` spec passes
6. [ ] Run `npm run e2e` and confirm the offers page shows `Okres ochrony` before `Aktualizacja`
7. [ ] Manually verify on `/offers` that field values remain unchanged and only the order is corrected