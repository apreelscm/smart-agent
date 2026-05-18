# SDLC-120: Filtr

## Specification

### Overview
The repository already contains the kwitariusze status filter UI and service logic in:
- `src/app/features/kwitariusze/kwitariusze.ts`
- `src/app/features/kwitariusze/kwitariusze.html`
- `src/app/core/services/kwitariusz.service.ts`

Today, that filter is explicitly reset on component creation (`this.service.clearStatusFilter()`), and the current tests expect the selection to disappear after reload/re-entry. This task changes that behavior: the existing multi-select status filter must now remember the last selected statuses locally per user and automatically reapply them when the user comes back to `/rozliczenia/kwitariusze`.

### User Stories
- US-001: As a user of the kwitariusze list, I want my last selected statuses to be remembered locally for me, so that I do not need to set the filter again every time I return to the screen.
  - Given I selected one or more statuses on `/rozliczenia/kwitariusze` / When I leave the screen and come back / Then the same statuses are restored and the list is already filtered

- US-002: As a user, I want the remembered status filter to be scoped to my user identity, so that another user on the same browser does not inherit my filter state.
  - Given one user saved a status selection locally / When a different user opens the kwitariusze screen on the same browser / Then that second user sees only their own saved selection or the default unfiltered state

- US-003: As a user, I want remembered statuses to stay valid for the current screen context, so that hidden or obsolete values are not silently applied.
  - Given saved statuses include values unavailable in the current list context / When the screen restores the filter / Then invalid values are dropped and only valid statuses remain applied

### Functional Requirements
- FR-001: Preserve the existing multi-select status filter behavior already implemented in `KwitariuszService` and `KwitariuszeComponent`.
- FR-002: Persist the selected `KwitariuszStatus[]` locally in browser storage for the kwitariusze list screen.
- FR-003: Scope the persisted value per user by including a resolved user identifier in the storage key.
- FR-004: Restore the persisted status selection automatically when `KwitariuszeComponent` is created, and apply it to the list without requiring an extra user action.
- FR-005: Replace the current reset-on-entry behavior in `src/app/features/kwitariusze/kwitariusze.ts`; the component must no longer clear the status filter on initialization.
- FR-006: Continue deriving available filter options from `baseFiltered()` in `src/app/core/services/kwitariusz.service.ts`, so the filter still shows only statuses available in the current list context.
- FR-007: Validate restored storage data against known `KwitariuszStatus` values before applying it.
- FR-008: If restored statuses are not available in the current context, reconcile them with `availableStatuses()` and persist the cleaned result.
- FR-009: Clearing the status filter, deselecting all statuses, or using the global reset action must also clear the persisted value for the current user.
- FR-010: The persistence mechanism must remain client-local only; no backend API, router params, or shared server-side preference store may be introduced.
- FR-011: Existing filters (`type`, `last30Days`, `policySearch`, `insuredSearch`), result count, sorting, no-data row, and row actions must continue to work unchanged.
- FR-012: The implementation must use the existing user context already visible in the repo:
  - primary source: `localStorage['auth.currentUser']` when available in the browser
  - fallback source: `AgentService.currentAgent().username`

### Edge Cases
- EC-001: If no saved value exists for the current user, the screen loads with no status restriction.
- EC-002: If the saved storage value contains malformed JSON or non-array data, the app ignores it and falls back to an empty selection.
- EC-003: If the saved array contains unknown status values, only valid `KwitariuszStatus` entries are restored.
- EC-004: If another filter makes some restored statuses unavailable, those statuses are removed by the existing reconciliation flow and the persisted value is updated accordingly.
- EC-005: If the user clears all selected statuses, the persisted storage entry is removed rather than left as stale active state.
- EC-006: If two users use the same browser, each user’s kwitariusze status filter is isolated by storage key.
- EC-007: Reloading the page and navigating away/back to `/rozliczenia/kwitariusze` both restore the saved selection for the current user.

### Success Criteria
- [ ] The kwitariusze status filter selection is restored after browser reload
- [ ] The kwitariusze status filter selection is restored after navigating away and back to the screen
- [ ] Saved filter state is isolated per user
- [ ] Invalid or unavailable saved statuses are ignored or reconciled safely
- [ ] Clearing the filter removes the remembered selection
- [ ] Existing status filtering, other filters, sorting, result count, and no-data behavior still work

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
This task is an enhancement of the already-implemented status filter, not a new filter build.

Current codebase state:
- `src/app/core/services/kwitariusz.service.ts` already owns `filterStatuses`, `availableStatuses`, `filtered`, and reconciliation logic.
- `src/app/features/kwitariusze/kwitariusze.ts` currently forces reset behavior via `this.service.clearStatusFilter()` in the constructor.
- `src/app/features/kwitariusze/kwitariusze.spec.ts` and `tests/e2e/kwitariusze-status-filter.spec.ts` currently encode the old expectation that status selection is reset on reload/re-entry.

The least invasive solution is to keep status filter state in `KwitariuszService`, add user-scoped `localStorage` persistence there, and replace the component’s reset-on-entry with restore-on-entry.

Confluence discovery:
- No existing kwitariusz-related service/API was found in the configured `Services` space.
- Domain context was found in [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the screen-oriented smart-agent UX context, but it does not define a separate persistence convention for this feature.
- Because no reusable service or persistence convention was found in configured scopes, the implementation should reuse repository-local Angular patterns only.

Key design decisions:
1. **Persist only selected statuses**
   - Do not persist `availableStatuses()` or the full filtered dataset.
   - Store only the current `KwitariuszStatus[]` selection.

2. **Keep persistence in `KwitariuszService`**
   - This keeps filtering logic, reconciliation, and persistence in one place.
   - The component remains a thin UI/view-model layer.

3. **Restore on component entry, not only in service constructor**
   - `KwitariuszService` is `providedIn: 'root'`, so constructor-only restoration would not reliably rerun on every route re-entry.
   - Add an explicit restore method in the service and call it from `KwitariuszeComponent` during construction/initialization.

4. **Use a user-scoped storage key**
   - Resolve the current user from `localStorage['auth.currentUser']` first, because that pattern is already used in Playwright setup (`tests/e2e/kwitariusze-status-filter.spec.ts`).
   - Prefer a stable identifier such as `auwId`, then fallback to `username`, then fallback to `AgentService.currentAgent().username`.
   - Recommended key format: `kwitariusze.status-filter.{userKey}`.

5. **Validate and reconcile restored values**
   - Parse the stored JSON safely.
   - Filter restored values against the known `KwitariuszStatus` set.
   - Let the existing `availableStatuses()` reconciliation remove values no longer valid in the current screen context.
   - After reconciliation, persist the cleaned state back to storage.

6. **Update the existing tests, not add parallel behavior**
   - Replace the current “reset after reload/re-entry” assertions with restore assertions.
   - Add explicit coverage for malformed storage and per-user isolation.

### Task Breakdown

#### Phase 1: Add user-scoped local persistence to kwitariusze status filtering
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Export known status values for validation | `src/app/core/models/kwitariusz.model.ts` | Add a reusable constant/list of valid `KwitariuszStatus` values so restored localStorage payloads can be validated safely |
| 1.2 | Add storage key resolution helpers | `src/app/core/services/kwitariusz.service.ts` | Inject `AgentService` and add helpers to resolve the current user key from `auth.currentUser` or fallback agent username |
| 1.3 | Add storage read/parse logic | `src/app/core/services/kwitariusz.service.ts` | Implement safe localStorage read, JSON parsing, type guarding, and invalid-data fallback for persisted status selections |
| 1.4 | Add persistence effect | `src/app/core/services/kwitariusz.service.ts` | Persist `filterStatuses()` changes to a user-scoped key and remove the key when selection becomes empty |
| 1.5 | Add explicit restore method | `src/app/core/services/kwitariusz.service.ts` | Expose a method such as `restorePersistedStatusFilter()` that reads saved statuses and sets `filterStatuses` for the current user |
| 1.6 | Persist reconciled selections | `src/app/core/services/kwitariusz.service.ts` | Ensure the existing reconciliation effect updates storage after removing statuses that are no longer available in the current context |

#### Phase 2: Switch the kwitariusze screen from reset-on-entry to restore-on-entry
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Remove forced reset behavior | `src/app/features/kwitariusze/kwitariusze.ts` | Delete the constructor call to `this.service.clearStatusFilter()` that currently wipes the filter on each screen entry |
| 2.2 | Restore saved statuses on init | `src/app/features/kwitariusze/kwitariusze.ts` | Call the new service restore method before/alongside the existing dataSource `effect()` so the table starts in the remembered filtered state |
| 2.3 | Preserve current UI behavior | `src/app/features/kwitariusze/kwitariusze.ts`, `src/app/features/kwitariusze/kwitariusze.html` | Keep current trigger label, active count, clear action, available-status rendering, and no-data behavior unchanged; this task only changes lifecycle persistence behavior |

#### Phase 3: Update unit and e2e regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Isolate browser storage in test setup | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Clear or seed `localStorage` explicitly in test setup/teardown so persisted state does not leak between specs |
| 3.2 | Replace default reset expectations | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Update tests that currently expect empty status selection on recreated component to instead verify restore-and-auto-apply behavior when storage exists |
| 3.3 | Add restore and cleanup coverage | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify persisted statuses are restored, invalid values are ignored, and clearing filters removes the saved value |
| 3.4 | Add per-user storage coverage | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify different resolved user identities map to different storage keys and do not share saved filters |
| 3.5 | Update browser regression tests | `tests/e2e/kwitariusze-status-filter.spec.ts` | Replace the current reload-reset scenario with reload-restore and add a navigate-away/return scenario that verifies automatic reapplication |
| 3.6 | Keep existing filter regression coverage | `tests/e2e/kwitariusze-status-filter.spec.ts` | Preserve existing checks for multi-select filtering, results count, clear/reset, and no-data behavior while aligning them with the new persistence rules |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/kwitariusz.model.ts` | MODIFY | Export canonical status values for localStorage validation |
| `src/app/core/services/kwitariusz.service.ts` | MODIFY | Add user-scoped localStorage persistence, restore logic, safe parsing, and persistence-aware reconciliation |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Remove reset-on-entry and restore persisted status filter on screen initialization |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Update component tests for restore, cleanup, invalid storage, and per-user isolation |
| `tests/e2e/kwitariusze-status-filter.spec.ts` | MODIFY | Replace reset-after-reload expectations with restore-after-reload/re-entry behavior |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Select one or more statuses on `/rozliczenia/kwitariusze`, reload the browser, and confirm the same statuses are still selected and already applied
5. [ ] Navigate from `/rozliczenia/kwitariusze` to another route and back, and confirm the same statuses are restored automatically
6. [ ] Clear the status filter and confirm the list returns to the unfiltered state and no saved status selection is restored on the next entry
7. [ ] Seed a different `auth.currentUser` in localStorage and confirm the saved filter does not bleed across users
8. [ ] Confirm other filters, sorting, result count, currency switching, and no-data rendering still behave as before