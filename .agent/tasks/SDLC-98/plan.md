# SDLC-98: Na liscie ofert kolumna Okres ochrony powinna byc po Wariant.

## Specification

### Overview
This task changes the visual order of metadata on the `/offers` list so that **Okres ochrony** is displayed **after** **Wariant** in every offer row. The protection-period value already exists in the current implementation, so the scope is limited to reordering the rendered metadata and updating regression tests that currently enforce the opposite order.

### User Stories
- US-001: As a sales user, I want to see `Okres ochrony` after `Wariant` on the offers list, so that the row layout matches the expected business order.
  - Given I open `/offers` / When offer rows are rendered / Then the `Wariant` block appears before `Okres ochrony`.
- US-002: As a user reviewing different offer types, I want the new order to be consistent for all rows, so that the list remains predictable.
  - Given motor and crop offers are displayed / When I inspect row metadata / Then both use the same `... Wariant -> Okres ochrony -> Aktualizacja ...` sequence.

### Functional Requirements
- FR-001: The offers list page at route `/offers` shall render the metadata block labeled `Okres ochrony` after the block labeled `Wariant`.
- FR-002: The reordered metadata sequence shall apply to every visible offer row in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-003: The existing `protectionPeriodLabel` calculation in `src/app/features/offers/pages/offers-home-page.component.ts` shall remain unchanged.
- FR-004: The existing variant rendering logic, including selected variant name and renewal link behavior, shall remain unchanged.
- FR-005: The change shall stay within the offers-list presentation and test layers; no changes are required in `src/app/core/repositories/offers.repository.ts` or `src/app/core/models/offer/offer.model.ts`.
- FR-006: The rendered order shall remain consistent in responsive layouts where `.offer-row__meta-grid` collapses to fewer columns or a single column.

### Edge Cases
- EC-001: Renewal offers must still render the source-policy link inside the `Wariant` block after the reorder.
- EC-002: Crop and motor offers must both display the same metadata order even though their first metadata block content differs.
- EC-003: On narrow viewports where the metadata grid stacks vertically, `Wariant` must still appear before `Okres ochrony`.
- EC-004: If no offers are visible after filtering, the empty state remains unchanged.

### Success Criteria
- [ ] Each visible offer row on `/offers` shows `Wariant` before `Okres ochrony`.
- [ ] The protection-period value remains the same as before the reorder.
- [ ] Renewal-link behavior in the variant section still works.
- [ ] Unit and e2e tests assert the updated order.
- [ ] No repository, model, mock-data, or routing changes are introduced.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular page in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

Current repository state shows:
- `OffersHomePageComponent` already exposes `protectionPeriodLabel`.
- The template currently renders `Okres ochrony` **before** `Wariant`.
- Existing regression tests in `src/app/features/offers/pages/offers-home-page.component.spec.ts` and `tests/e2e/offers-protection-period.spec.ts` currently enforce that old order.

Because `.offer-row__meta-grid` order is driven directly by DOM order, the runtime change should be implemented by moving the existing `Okres ochrony` `<div>` so it comes immediately after the existing `Wariant` block in `offers-home-page.component.html`. No TypeScript logic change is needed unless a test reveals hidden coupling.

Repository patterns support keeping this as a minimal template-level change:
- the offers page uses inline metadata sections rather than a configurable column abstraction,
- styling in `offers-home-page.component.scss` defines a generic grid and does not appear to depend on specific child indexes,
- unit and Playwright tests already cover protection-period rendering and field order, so they should be updated rather than replaced.

Confluence discovery results:
- No existing service/API relevant to this UI-only change was found in `Services` space — no reuse candidate required.
- Domain context was found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which describes the smart-agent screens from a business perspective. This supports implementing the change in the existing offers screen only, without introducing new components or flows.

Implementation decision:
- **Use template reordering as the only runtime behavior change.**
- **Update tests that currently assert `Okres ochrony` before `Wariant` to assert the new order instead.**
- **Do not modify repositories, models, or mock payloads.**

### Task Breakdown

#### Phase 1: Reorder metadata on the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Confirm target screen and current order | `src/app/app.routes.ts`, `src/app/features/offers/pages/offers-home-page.component.html` | Verify the Jira applies to the `/offers` route and that the current DOM order is `... Kanał -> Okres ochrony -> Wariant -> Aktualizacja`. |
| 1.2 | Move protection-period block after variant | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the existing metadata `<div>` blocks inside `.offer-row__meta-grid` so `Wariant` is rendered before `Okres ochrony`. |
| 1.3 | Preserve existing variant behavior | `src/app/features/offers/pages/offers-home-page.component.html` | Keep the selected variant label, renewal link, and fallback text unchanged while moving only the block position. |
| 1.4 | Verify no logic or styling updates are required | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.scss` | Confirm `protectionPeriodLabel` and responsive grid styling still work correctly with the new DOM order; avoid unnecessary code changes. |

#### Phase 2: Update regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update component order assertion | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Replace the existing assertion that expects `Okres ochrony` before `Wariant` with an assertion that expects `Wariant` before `Okres ochrony` for every rendered `.offer-row__meta-grid`. |
| 2.2 | Preserve protection-period value assertions | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing fixed-date and leap-year checks unchanged so the reorder does not weaken value coverage. |
| 2.3 | Update Playwright order assertion | `tests/e2e/offers-protection-period.spec.ts` | Change the first-row metadata label order check so it verifies `Wariant` appears before `Okres ochrony` in the visible UI. |
| 2.4 | Keep browser-level value regression | `tests/e2e/offers-protection-period.spec.ts` | Retain the current runtime-calculated protection-period text assertions to ensure the field content still renders correctly after the reorder. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the metadata blocks so `Okres ochrony` appears after `Wariant`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update rendered-order assertions to match the new requirement while keeping existing value checks. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Update browser-level metadata order assertions to verify `Wariant` precedes `Okres ochrony`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes without Angular template or TypeScript errors
5. [ ] `npm run test -- --watch=false --browsers=ChromeHeadless` passes offers-page unit tests
6. [ ] `npm run e2e` passes the offers protection-period scenario
7. [ ] Manual check on `/offers` confirms `Wariant` is shown before `Okres ochrony` on desktop
8. [ ] Manual check on a narrow viewport confirms stacked metadata preserves the same order