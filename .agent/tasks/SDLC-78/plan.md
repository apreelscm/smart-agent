# SDLC-78: Na ekranie listy ofert okres ochrony powinien byc przed kolumna Aktualizacja

## Specification

### Overview
This task corrects the metadata order on the offers list so that `Okres ochrony` is displayed before `Aktualizacja` in every offer row. The change is purely presentational and must not alter offer data, filtering, sorting, navigation, or status-transition behavior.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the row layout matches the expected business order.
  - Given the offers list is loaded / When offer rows are rendered / Then the `Okres ochrony` field is shown before `Aktualizacja`

- US-002: As a product owner, I want this adjustment to be regression-safe, so that future UI changes do not accidentally revert the field order.
  - Given automated tests run / When the offers page is validated / Then the DOM order of `Okres ochrony` and `Aktualizacja` is asserted

### Functional Requirements
- FR-001: Within the offer metadata grid in `src/app/features/offers/pages/offers-home-page.component.html`, the `Okres ochrony` block must be positioned before the `Aktualizacja` block.
- FR-002: The `Okres ochrony` value must continue to use the existing `protectionPeriodDisplay` binding.
- FR-003: The `Aktualizacja` value must continue to use the existing `offer.updatedAt` date/time bindings.
- FR-004: No changes may be made to the offer repository, runtime state, filtering, sorting, navigation, or action menu behavior for this task.
- FR-005: Automated coverage must verify relative order, not only presence of the fields.

### Edge Cases
- EC-001: The order must be correct for both `MOTOR` and `CROP` offers, since both render through the same `.offer-row__meta-grid`.
- EC-002: The order must remain correct on responsive breakpoints where the grid collapses from 5 columns to fewer columns, because DOM order drives visual stacking.
- EC-003: The task must not replace displayed values; only the layout order is in scope.

### Success Criteria
- [ ] Every rendered offer row shows `Okres ochrony` before `Aktualizacja`
- [ ] The displayed protection-period value remains unchanged
- [ ] The displayed update date/time remains unchanged
- [ ] Unit/component coverage verifies the order
- [ ] E2E coverage verifies the order in the browser

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Jira provides no additional description, acceptance criteria, or comments beyond the summary, so the implementation should stay tightly scoped to the offers-list UI order.

Confluence findings:
- No existing service/API relevant to this UI-only task was found in the `Services` space — no service reuse is needed.
- The `SDLC` space search returned the product-context page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), but it does not add technical constraints beyond confirming the application is screen-driven.

Repository findings:
- The app is an Angular 20 standalone application with PrimeNG, defined in `package.json`.
- The offers list is implemented in:
  - `src/app/features/offers/pages/offers-home-page.component.ts`
  - `src/app/features/offers/pages/offers-home-page.component.html`
  - `src/app/features/offers/pages/offers-home-page.component.scss`
- Protection-period formatting already exists in `src/app/features/offers/utils/protection-period.util.ts`.
- Existing test patterns for this screen live in:
  - `src/app/features/offers/pages/offers-home-page.component.spec.ts`
  - `tests/e2e/offers-protection-period.spec.ts`

Important note from the current repo snapshot: these files already reflect the intended behavior for this ticket. The template currently renders `Okres ochrony` before `Aktualizacja`, and both unit and Playwright tests already assert that order. For SDLC-78, the implementation should therefore be treated as a branch-alignment/backport task:
- first verify whether the target working branch already contains the same HTML order and regression tests,
- if not, apply the same minimal template reorder and test assertions,
- do not change TypeScript business logic, repository behavior, or mock/runtime data.

Because no relevant API/service was found in Confluence and this task is purely presentational, there is no integration work and no runtime service change. The existing `OffersRepository` mock-backed data source in `src/app/core/repositories/offers.repository.ts` should remain untouched.

### Task Breakdown

#### Phase 1: Align the offers-row metadata order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Verify current branch state | `src/app/features/offers/pages/offers-home-page.component.html` | Check whether the target branch already places the `Okres ochrony` metadata cell before `Aktualizacja` inside `.offer-row__meta-grid`. |
| 1.2 | Reorder template blocks if needed | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` block so it is declared immediately before the existing `Aktualizacja` block, without changing bindings or labels. |
| 1.3 | Preserve layout styling | `src/app/features/offers/pages/offers-home-page.component.scss` | Confirm the existing grid styling still applies after the reorder; only adjust SCSS if an unexpected visual regression appears on the target branch. |

#### Phase 2: Align regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Verify component test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Ensure the spec asserts that `Okres ochrony` exists, appears before `Aktualizacja`, and keeps the expected displayed value for each rendered row. |
| 2.2 | Add or backport component assertions if missing | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | If the target branch lacks order assertions, add DOM-order checks using `.offer-row__meta-grid > div` and label-index comparison. |
| 2.3 | Verify e2e coverage | `tests/e2e/offers-protection-period.spec.ts` | Ensure the browser test checks row-level metadata order and confirms values still render. |
| 2.4 | Add or backport e2e assertions if missing | `tests/e2e/offers-protection-period.spec.ts` | If absent on the target branch, add Playwright checks that `Okres ochrony` precedes `Aktualizacja` for all visible offer rows. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Align the metadata-cell order so `Okres ochrony` is before `Aktualizacja` if the target branch does not already match the current repo state. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Only if necessary, preserve visual consistency after the DOM reorder; otherwise leave unchanged. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add or backport component-level assertions for metadata order and preserved value rendering. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add or backport browser-level assertions for the rendered order in the offers list. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Run `npm run build` successfully
5. [ ] Run `npm test -- --watch=false --include src/app/features/offers/pages/offers-home-page.component.spec.ts`
6. [ ] Run `npm run e2e -- tests/e2e/offers-protection-period.spec.ts`
7. [ ] Manually confirm on `/offers` that `Okres ochrony` is displayed before `Aktualizacja` for both motor and crop offers
8. [ ] Confirm no regression in offer actions, premium box, filters, or responsive layout