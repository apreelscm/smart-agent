# SDLC-109: Implement process list based on F02 - Process List & Filtering (ZRD) specification available in Confluence

## Specification

### Overview
Implement the first Angular version of the eUmowy process management list for ZRD operators. The screen should replace the current placeholder at `/processes` with a working UI backed by an in-app mock Angular service, supporting list display, filtering, sorting, pagination, empty state, error state, and navigation to the existing process detail route. The report-generation dialog explicitly stays out of scope.

### User Stories
- US-001: As a ZRD operator, I want to see a default list of processes, so that I can browse available records.
  - Given I am authenticated and open `/processes` / When the screen loads / Then I see a paginated list populated from the mock Angular service.
- US-002: As a ZRD operator, I want to filter processes by available criteria, so that I can find relevant records faster.
  - Given the process list is visible / When I enter filter values and apply them / Then only records matching all active criteria are shown.
- US-003: As a ZRD operator, I want to clear filters, so that I can return to the default view quickly.
  - Given one or more filters are active / When I clear filters / Then the list resets to its default state and first page.
- US-004: As a ZRD operator, I want to sort the list, so that I can analyze records in a useful order.
  - Given the process list is visible / When I change the sort field or direction / Then the displayed order updates accordingly.
- US-005: As a ZRD operator, I want to move between result pages, so that I can browse more records without losing my filters.
  - Given the filtered result spans multiple pages / When I move to another page / Then the next subset of records is shown with current filters and sort preserved.
- US-006: As a ZRD operator, I want to open a process from the list, so that I can continue work on that record.
  - Given the list contains a process row / When I click its detail action / Then I am navigated to `/processes/:id`.

### Functional Requirements
- FR-001: `src/app/features/processes/process-list/process-list.component.ts` must render a real process list screen instead of the current placeholder text.
- FR-002: The screen must use the existing standalone Angular component pattern and Bootstrap-based styling already present in the repo.
- FR-003: The list data source must be an Angular mock service in the repo, not a backend dependency.
- FR-004: The mock service must expose a typed contract containing records plus pagination/sort metadata.
- FR-005: The default list view must show at least process identifier, process name/type, status, and relevant business dates referenced in the Jira description.
- FR-006: The screen must support filter application with AND semantics across active criteria.
- FR-007: Changing filters must refresh the result set and reset pagination to the first page.
- FR-008: The screen must support sortable columns or explicit sort controls for the fields required by F02.
- FR-009: The screen must support pagination while preserving active filters and sort state.
- FR-010: Clearing filters must restore the default list state, including default sort and first page.
- FR-011: When no records match, the screen must show an empty-state message without breaking layout.
- FR-012: When the mock service fails, the screen must show an error message consistent with the app’s simple Bootstrap alert pattern.
- FR-013: Each row must expose navigation to the existing process detail route configured in `src/app/app.routes.ts`.
- FR-014: The implementation must not add the report-generation dialog.

### Edge Cases
- EC-001: If filters return no matches, show a valid empty state and keep filter controls usable.
- EC-002: If the current page becomes invalid after filtering, reset to page 1 instead of showing a blank table.
- EC-003: If a filter field is left blank, it must not constrain results.
- EC-004: If the mock service throws, preserve the filter form state so the user can retry.
- EC-005: If a sort field is reselected, toggle direction or reapply deterministically rather than leaving order ambiguous.
- EC-006: If the user navigates through pages, the current filter/sort context must remain applied.
- EC-007: The list must not expose report-generation UI even though the Confluence F02 page mentions it.

### Success Criteria
- [ ] `/processes` loads a populated process list from an in-repo mock Angular service.
- [ ] Users can apply filters and results reflect all active criteria.
- [ ] Users can clear filters and recover the default list state.
- [ ] Users can sort by supported fields and see the order change.
- [ ] Users can paginate through results without losing active filters or sort.
- [ ] Empty results show a non-error empty state.
- [ ] Mock-service failures show a visible error alert without breaking the screen.
- [ ] Clicking a row action opens the existing `/processes/:id` route.
- [ ] The screen does not include the report dialog.

### Open Questions
- [NEEDS CLARIFICATION: Which exact F02 filters, column labels/order, default sort, and page-size options must be rendered? The available Jira text and Confluence snippets confirm filtering/sorting/pagination, but do not enumerate the full field catalog.]

---

## Implementation Plan

### Technical Approach
Use the existing Angular 17 standalone-component structure already present in the repo and extend the current placeholder process list into a self-contained feature backed by a typed mock service.

Relevant discovered documentation:
- Reuse the functional scope from **F02 - Process List & Filtering (ZRD)**: https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD
- Reuse the architectural direction from **eUmowy — High-Level Design**: https://apreel.atlassian.net/wiki/spaces/Services/pages/167641089/eUmowy+High-Level+Design
- No relevant domain conventions/ADRs were found in the configured `SDLC` space.
- No reusable backend service, REST endpoint, or integration library for the process list was found in the configured `Services` space, so this task should implement the in-app mock Angular service explicitly requested in Jira comments.

Codebase patterns to follow:
- standalone components with inline templates, e.g. `src/app/features/login/login.component.ts`, `src/app/core/layout/layout.component.ts`
- Bootstrap-only UI via `src/styles.scss` and existing inline class usage
- lazy-loaded feature routes already defined in `src/app/app.routes.ts`
- simple service and component unit tests using Jasmine/TestBed, as shown in `src/app/core/services/auth.service.spec.ts` and `src/app/features/login/login.component.spec.ts`

Planned design:
1. **Keep routing unchanged**
   - `src/app/app.routes.ts` already exposes `/processes` and `/processes/:id`.
   - The list will navigate to the existing detail route using `RouterLink`; no route restructuring is needed.

2. **Add a feature-scoped mock data layer**
   - Create typed models for list items, filters, sort state, and paginated responses.
   - Create a mock dataset with enough variation to exercise filtering, sorting, pagination, empty results, and navigation.
   - Implement a `ProcessListService` that accepts the full active query state and returns filtered/sorted/paged data.

3. **Keep the UI in the existing process-list standalone component**
   - This matches current repo style better than introducing nested feature modules.
   - The component should import `CommonModule`, `FormsModule`, and `RouterLink`.
   - Use inline template sections for filter form, loading/error/empty states, table, and pagination controls.

4. **Use component state rather than new global state infrastructure**
   - The repo currently has no NgRx/signals store pattern.
   - Keep scope limited to local component state and a feature service.

5. **Make the list configurable around uncertain F02 details**
   - Since exact F02 field definitions are not fully visible from available snippets, model the filters and columns cleanly so the minimum known fields can be implemented now and remaining F02-specific additions can be added without refactoring.
   - Default behavior should cover the Jira-confirmed minimum: identifier, type/name, status, dates, filtering, sorting, pagination, empty/error states, and detail navigation.

6. **Do not add runtime mock JSON fallback files**
   - There is no documented real API to integrate for this task, and Jira explicitly asks for Angular services with mock data.
   - The runtime default should therefore be a TypeScript mock service in application code, not an external JSON file path.
   - Test-only fixtures are acceptable inside spec files.

### Task Breakdown

#### Phase 1: Create typed mock process-list data layer
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Define list contracts | `src/app/features/processes/process-list/process-list.models.ts` | Add interfaces/types for process rows, filter input, sort state, pagination metadata, and list response. |
| 1.2 | Seed realistic mock records | `src/app/features/processes/process-list/process-list.mock.ts` | Add an in-memory dataset with multiple statuses, types, identifiers, and dates to cover multiple pages and filter combinations. |
| 1.3 | Implement feature service | `src/app/features/processes/process-list/process-list.service.ts` | Add a standalone injectable service that receives active query params and returns filtered, sorted, paginated mock results plus metadata. |
| 1.4 | Handle service-level failure path | `src/app/features/processes/process-list/process-list.service.ts` | Include a clear error-return path that can be triggered in tests and handled by the component without adding backend dependencies. |

#### Phase 2: Build the process-list screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Replace placeholder UI | `src/app/features/processes/process-list/process-list.component.ts` | Replace the  text with a full inline-template screen composed of filter controls, table, status messaging, and pagination. |
| 2.2 | Add component state and loading flow | `src/app/features/processes/process-list/process-list.component.ts` | Add fields for filters, sort, page, page size, loading state, error message, empty-state detection, and fetched result metadata. |
| 2.3 | Implement filter actions | `src/app/features/processes/process-list/process-list.component.ts` | Add methods to apply filters, clear filters, and always reload using the full active query state. |
| 2.4 | Implement sort behavior | `src/app/features/processes/process-list/process-list.component.ts` | Add sortable header or explicit sort controls that update service queries and re-render the table deterministically. |
| 2.5 | Implement pagination behavior | `src/app/features/processes/process-list/process-list.component.ts` | Add previous/next and page display controls that preserve current filters and sort while loading the correct subset. |
| 2.6 | Wire detail navigation | `src/app/features/processes/process-list/process-list.component.ts` | Add row-level action links/buttons pointing to `/processes/:id` using the existing route pattern. |
| 2.7 | Add empty and error states | `src/app/features/processes/process-list/process-list.component.ts` | Render Bootstrap alert/message states for no results and service failure without layout breakage. |

#### Phase 3: Add automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Test filtering/sorting/pagination logic | `src/app/features/processes/process-list/process-list.service.spec.ts` | Add unit tests for default query, AND filtering, sort ordering, pagination slicing, empty results, and error path. |
| 3.2 | Test process-list UI behavior | `src/app/features/processes/process-list/process-list.component.spec.ts` | Add component tests for initial load, applying filters, clearing filters, changing sort, changing page, empty state, error state, and detail links. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Replace placeholder with full process list UI and interaction logic. |
| `src/app/features/processes/process-list/process-list.models.ts` | CREATE | Define typed contracts for rows, filters, sorting, and pagination. |
| `src/app/features/processes/process-list/process-list.mock.ts` | CREATE | Store in-memory mock process records used by the runtime mock service. |
| `src/app/features/processes/process-list/process-list.service.ts` | CREATE | Provide mock process-list querying with filtering, sorting, pagination, and error simulation support. |
| `src/app/features/processes/process-list/process-list.service.spec.ts` | CREATE | Add unit tests for service query logic. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | CREATE | Add UI tests for process-list behavior and acceptance criteria. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check: `/processes` shows a populated table after login
5. [ ] Manual check: applying multiple filters narrows results with AND logic
6. [ ] Manual check: clearing filters restores the default list and first page
7. [ ] Manual check: changing sort updates record order
8. [ ] Manual check: changing page preserves current filters and sort
9. [ ] Manual check: no-match filters show an empty-state message
10. [ ] Manual check: mocked service failure shows an error alert without breaking layout
11. [ ] Manual check: row action navigates to `/processes/:id`
12. [ ] Manual check: no report-generation dialog is visible
