# SDLC-119: Na ekrani z lista kwitariuszy dodaj filtrowanie po statusie

## Specification

### Overview
This task adds a multi-select status filter to the kwitariusze list screen at `/rozliczenia/kwitariusze`, so users can narrow the dataset to one or more statuses without manually scanning the whole table. The filter must work on the underlying list data managed by `KwitariuszService`, stay compatible with existing filters and sorting, expose only statuses currently present in the list context, and reset on refresh or re-entry to the screen.

### User Stories
- US-001: As a user reviewing kwitariusze, I want to filter the list by one or more statuses, so that I can find relevant records faster.
  - Given I open the kwitariusze list / When the screen loads / Then I see a status filter in the existing filters area
- US-002: As a user, I want to select multiple statuses at once, so that I can view all matching kwitariusze in one list.
  - Given the status filter is open / When I select several statuses / Then the table shows only rows whose `status` belongs to the selected set
- US-003: As a user, I want to clear the status filter easily, so that I can return to the full list.
  - Given one or more statuses are selected / When I clear the filter or deselect all statuses / Then the list shows all records allowed by the remaining filters
- US-004: As a user, I want the status filter to reset after refresh or re-entering the page, so that each new visit starts from the default list view.
  - Given I previously selected statuses / When I refresh the browser or navigate away and back to `/rozliczenia/kwitariusze` / Then the status filter is empty and the page starts without status-based restriction

### Functional Requirements
- FR-001: Add a status filter control to `src/app/features/kwitariusze/kwitariusze.html` within the existing `.filters` row.
- FR-002: The status filter must support selecting multiple `KwitariuszStatus` values at the same time.
- FR-003: Status filtering must be applied in `src/app/core/services/kwitariusz.service.ts` as part of the computed dataset, not as a visual-only filter on already rendered table rows.
- FR-004: The available status options must be derived from the kwitariusze currently available in the list context after existing non-status filters are applied (`type`, `last30Days`, `policySearch`, `insuredSearch`).
- FR-005: With no selected statuses, the list must behave as unfiltered by status.
- FR-006: When one or more statuses are selected, only rows whose `status` is included in the selected set may remain in `service.filtered()`.
- FR-007: The existing global reset action (`clearFilters()`) must also clear the new status filter.
- FR-008: The status filter must not be persisted in router params, local storage, or shared restored UI state; re-entering the screen must start with an empty status selection.
- FR-009: The new filter must not break existing type/date/text filters, current `MatSort` sorting, result count display, or table row actions.
- FR-010: If filtering results in zero rows, the screen must show an empty state in the table area using Angular Material’s no-data pattern rather than leaving a blank table body.
- FR-011: If other filters change the underlying dataset so that a previously selected status is no longer present, the selected status set must be reconciled to valid current options to avoid stale hidden selections.
- FR-012: The UI must clearly communicate active status filtering, including selected-state styling and a compact label/count on the filter trigger.

### Edge Cases
- EC-001: Selecting one status must behave like a single-value subset of the same multi-select mechanism.
- EC-002: Selecting all currently available statuses must produce the same visible rows as having no status filter, while still allowing the user to deselect individual options.
- EC-003: If other filters reduce the dataset and remove some selected statuses from the option set, those invalid selections should be dropped automatically.
- EC-004: If the selected statuses return no records, the page should render a no-results state instead of an empty-looking table.
- EC-005: Refreshing the browser resets the component and service state, so the status filter must return to its default empty selection.
- EC-006: Navigating away from and back to the route must also clear the status filter, even though `KwitariuszService` is `providedIn: 'root'`.

### Success Criteria
- [ ] A status filter is visible on `/rozliczenia/kwitariusze`
- [ ] Users can select multiple statuses simultaneously
- [ ] `service.filtered()` excludes rows whose status is not selected
- [ ] Available status options reflect the currently filtered list context
- [ ] Clearing the filter returns the list to the unfiltered-by-status state
- [ ] Refreshing or re-entering the page resets the status filter
- [ ] Existing filters, sorting, result count, and row actions still work
- [ ] A no-data state is shown when no rows match the selected statuses

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 21 standalone app using:
- feature-local standalone components and templates, e.g. `src/app/features/kwitariusze/kwitariusze.ts`
- signal-based state in root services, e.g. `src/app/core/services/kwitariusz.service.ts`
- `MatTableDataSource` updated from service `computed()` values via component `effect()`
- feature-local Jasmine/Karma specs and Playwright e2e coverage

This change should follow the same pattern by extending the existing `KwitariuszService` rather than introducing a separate state layer.

Confluence discovery result:
- No existing kwitariusz-related service/API was found in the configured `Services` space.
- No relevant ADR/convention/glossary page was found in the configured `SDLC` space.
- Because nothing relevant was found in configured scopes, the implementation should reuse repository-local patterns only and not introduce a new external dependency or integration.

Key implementation decisions:
1. **Add status filtering to the service layer**
   - Extend `KwitariuszService` with a multi-value status signal, plus a computed list of available statuses.
   - Refactor the current `filtered` computed into:
     - a base computed that applies existing filters except status
     - the final computed that additionally applies the selected status set
   - This keeps filtering data-driven and makes status options derivable from the same source data.

2. **Keep the status filter UI aligned with existing kwitariusze filter patterns**
   - Reuse the current filter bar and expandable filter section pattern already used for policy/insured filters in `kwitariusze.html`.
   - Add a new “Status” trigger chip and an expandable multi-select panel below the filters row.
   - Avoid introducing a heavier Material form-field/select pattern that is not currently used on this screen.

3. **Reset status state on page entry**
   - Because `KwitariuszService` is root-scoped, a newly added status signal would otherwise survive route changes.
   - To satisfy the Jira requirement without changing unrelated existing filter behavior, `KwitariuszeComponent` should explicitly clear only the status filter on initialization.

4. **Use Angular Material’s no-data table pattern**
   - The current table has no explicit empty state.
   - Add a `*matNoDataRow` row for zero-result scenarios so the screen stays consistent with the existing table structure and acceptance criteria.

5. **Guard against stale hidden selections**
   - When non-status filters change, recompute the available status options from the base dataset.
   - Reconcile the selected status set against those options so the user never ends up with an invisible active status filter.

### Task Breakdown

#### Phase 1: Extend kwitariusze filtering state
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add multi-status signal | `src/app/core/services/kwitariusz.service.ts` | Introduce `filterStatuses = signal<KwitariuszStatus[]>([])` for multi-select status filtering |
| 1.2 | Split base and final filtering | `src/app/core/services/kwitariusz.service.ts` | Extract current filter logic into a base computed without status, then apply selected statuses in the final `filtered` computed |
| 1.3 | Expose available statuses | `src/app/core/services/kwitariusz.service.ts` | Add a computed unique status list derived from the base filtered dataset to drive the UI options |
| 1.4 | Add service helpers | `src/app/core/services/kwitariusz.service.ts` | Implement `toggleStatus`, `clearStatusFilter`, and update `clearFilters()` to include status reset |
| 1.5 | Reconcile invalid selections | `src/app/core/services/kwitariusz.service.ts` | Add a signal `effect()` that removes selected statuses no longer present after other filters change |

#### Phase 2: Add the status filter UI to the kwitariusze screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component state | `src/app/features/kwitariusze/kwitariusze.ts` | Update `expandedFilter` union to include `'status'`; add helpers for active status count and trigger label |
| 2.2 | Reset status filter on entry | `src/app/features/kwitariusze/kwitariusze.ts` | Clear the status selection during component initialization so re-entry starts from the default state |
| 2.3 | Add status trigger chip | `src/app/features/kwitariusze/kwitariusze.html` | Insert a new filter trigger in the existing filter row, near the current quick/type/text filters |
| 2.4 | Add expandable multi-select panel | `src/app/features/kwitariusze/kwitariusze.html` | Render selectable status options from `service.availableStatuses()` using the current expandable-filter layout pattern |
| 2.5 | Keep reset behavior coherent | `src/app/features/kwitariusze/kwitariusze.ts`, `src/app/features/kwitariusze/kwitariusze.html` | Update `hasAnyFilter` and `clearFilters()` flows so the reset button appears/disappears correctly when statuses are active |
| 2.6 | Add no-results row | `src/app/features/kwitariusze/kwitariusze.html` | Add `*matNoDataRow` to the Material table for empty-state behavior when filters remove all rows |
| 2.7 | Style new controls | `src/app/features/kwitariusze/kwitariusze.scss` | Add styles for the status chip, multi-select panel, selected options, and no-data row consistent with existing screen styles |

#### Phase 3: Add automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Extend component tests for status filtering | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Add assertions for default empty status selection, single-status filtering, multi-status filtering, and clear/reset behavior |
| 3.2 | Test option derivation | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify available statuses reflect the current non-status filtered dataset |
| 3.3 | Test re-entry/reset behavior | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify component initialization clears the status filter even if the root service held a previous selection |
| 3.4 | Add no-data test | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify the table renders a no-data row when the selected statuses produce zero matches |
| 3.5 | Add e2e regression coverage | `tests/e2e/kwitariusze-status-filter.spec.ts` | Cover multi-select status filtering, clearing, and reset-after-reload behavior in the browser |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/services/kwitariusz.service.ts` | MODIFY | Add multi-status filtering state, available-status computation, helper methods, and stale-selection reconciliation |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Add status filter view-model helpers and reset-on-entry behavior |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Add status filter trigger, expandable multi-select UI, and Material no-data row |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style status filter controls and empty-state row |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Add unit/component coverage for status filter behavior and no-data rendering |
| `tests/e2e/kwitariusze-status-filter.spec.ts` | CREATE | Add end-to-end verification for multi-select filtering and reset-after-reload |

### Verification Steps
1. [ ] Build succeeds (`npm run build`)
2. [ ] Unit tests pass (`npm test -- --watch=false`)
3. [ ] E2E tests pass (`npm run e2e`)
4. [ ] On `/rozliczenia/kwitariusze`, selecting one status limits rows to that status only
5. [ ] Selecting multiple statuses shows rows matching any selected status
6. [ ] Clearing the status filter restores the full list
7. [ ] Reloading the page resets the status filter
8. [ ] Existing filters and sort still work after applying/removing status filters
9. [ ] Zero-result filtering shows the table empty state