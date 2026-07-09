# SDLC-142: Lista procesów

## Specification

### Overview
This task delivers the first usable process-list screen for authenticated users. It adds a dedicated `/processes` page, loads process data through a typed Angular data-access layer aligned to the authoritative Confluence contract for F02, and renders the result as a sortable, frontend-paginated list with loading, empty, and error states.

Because the Jira ticket explicitly requires backend-independent development/testing, the implementation should also add an explicit mock mode that intercepts the same F02 endpoint and returns deterministic fixture data. The real F02 API contract remains the runtime default; the mock path is an opt-in local/test configuration, not the default production path.

### User Stories
- US-001: As a ZRD operator, I want to see the process list after entering the app, so that I can review available contract processes.
  - Given I am authenticated / When I navigate to the process list / Then I see rows rendered from the F02 process-list contract.

- US-002: As a user, I want to sort the process list and move between pages, so that I can browse data conveniently.
  - Given the list contains multiple records / When I change sorting or page / Then the table updates on the frontend without another backend round-trip.

- US-003: As a tester or frontend developer, I want to run the process list without a live backend, so that I can validate the UI independently.
  - Given the app is started in mock mode / When I open the process list / Then the page loads deterministic fixture data through a mocked response for the same F02 endpoint.

- US-004: As a user, I want clear feedback when there are no processes or when loading fails, so that I understand the state of the screen.
  - Given the endpoint returns an empty collection or an error / When the view renders / Then I see the project-standard empty or error state instead of a broken table.

### Functional Requirements
- FR-001: Add a new authenticated route for the process list in `src/app/app.routes.ts`.
- FR-002: Update the post-login redirect path in `src/app/pages/login/login.component.ts` and the authenticated-user redirect in `src/app/core/auth/guest.guard.ts` so the new process list is the primary landing page for signed-in users.
- FR-003: Create a dedicated process-list standalone page under `src/app/pages/process-list/`.
- FR-004: Create a typed data-access service that calls the F02 process-list endpoint documented in Confluence: [F02 — Process List & Search](https://apreel.atlassian.net/wiki/spaces/Services/pages/174424069/F02+Process+List+Search).
- FR-005: The service request/response model must mirror the F02 contract from Confluence rather than inventing a frontend-only shape.
- FR-006: The initial request/default state must respect the Confluence defaults already visible in the service documentation and test scenarios: WAITING status, last 30 days, and 10 results per page semantics from [F02 — Process List & Search — Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/174882817/F02+Process+List+Search+Test+Scenarios).
- FR-007: The page must render the list columns defined on the F02 Confluence page, using the documented field names/meanings as the source of truth.
- FR-008: Sorting must be implemented on the frontend for the supported columns only.
- FR-009: Pagination must be implemented on the frontend and must work across more than one page of data.
- FR-010: The page must expose loading, empty, and error states.
- FR-011: Add deterministic fixture data representing multiple statuses, varied text lengths, and date values sufficient to verify table rendering, sorting, and multi-page pagination.
- FR-012: Add an explicit mock build/serve configuration that intercepts the same documented F02 endpoint and returns fixture data for local no-backend runs.
- FR-013: The real F02 API contract remains the runtime default; mock responses are enabled only via explicit mock configuration and tests.
- FR-014: Existing auth behavior and auth mock flow must remain intact outside the redirect target change.

### Edge Cases
- EC-001: If the API returns an empty collection, the table must not render stale rows; a dedicated empty-state message should be shown.
- EC-002: If the API call fails, the page should show an error state with retry guidance/action consistent with current app patterns instead of crashing.
- EC-003: If some sortable fields are null or missing, sorting must remain stable and deterministic.
- EC-004: If the current page becomes invalid after a sort change or data refresh, pagination should reset to the first page.
- EC-005: Long text values in documented list columns must not break the table layout or cause horizontal overflow at standard widths.
- EC-006: Date/time fields from the F02 payload must be formatted consistently and remain sortable by underlying value rather than display string.
- EC-007: Mock fixtures must include enough records to validate at least a second page when the page size is 10.
- EC-008: The mock layer must intercept the exact real endpoint/contract shape, so switching between real and mock mode does not require component changes.

### Success Criteria
- [ ] Authenticated users can reach a rendered `/processes` screen in the app.
- [ ] The page loads data through a typed service aligned to the Confluence F02 contract.
- [ ] The default list behavior reflects the documented F02 defaults already visible in Confluence (WAITING, last 30 days, 10-per-page semantics).
- [ ] Frontend sorting works for the supported columns.
- [ ] Frontend pagination works across multiple pages.
- [ ] Empty and error states render correctly.
- [ ] An explicit mock mode allows the screen to run without a live backend while keeping the real F02 integration as the default path.
- [ ] Unit and e2e coverage verify data, empty, error, sorting, pagination, and auth-to-process-list navigation.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This repository is an Angular 21 standalone-component application with route-level lazy loading in `src/app/app.routes.ts`, auth behavior centered in `src/app/core/auth/`, and UI built primarily with native HTML plus project SCSS tokens from `src/styles.scss`. Existing patterns favor:
- standalone route components,
- small feature folders under `src/app/pages/`,
- signals for local state,
- `HttpClient` services in core feature folders,
- test selectors via `data-testid`,
- HTTP interception already demonstrated by `src/app/core/auth/auth.mock.interceptor.ts`.

The safest implementation is to follow the same structure for processes:
1. create a dedicated process-list page component,
2. create a typed `ProcessesService` for the F02 endpoint,
3. keep sort/pagination state in the page component with signals/computed values,
4. add a mock interceptor for the same endpoint,
5. register that interceptor only in an explicit mock configuration.

Reuse from Confluence:
- Reuse the authoritative service definition from [F02 — Process List & Search](https://apreel.atlassian.net/wiki/spaces/Services/pages/174424069/F02+Process+List+Search).
- Reuse the documented defaults from [F02 — Process List & Search — Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/174882817/F02+Process+List+Search+Test+Scenarios), which already state the default WAITING status, last-30-days range, and 10-per-page behavior.
- No relevant domain convention or ADR was found in the configured `SDLC` space, so the implementation should follow existing repo patterns rather than introducing new project conventions.

Important integration decision:
- The plan treats the documented F02 API as the runtime default.
- The mock response path is opt-in via dedicated mock configuration in Angular build/serve settings.
- The component and service must hit the same endpoint and use the same DTOs in both real and mock mode.
- Mock JSON files will not be used as a production/runtime fallback; fixture data is limited to the mock interceptor and tests.

Codebase-specific decisions:
- Keep styling local to the new process-list page and reuse global tokens/classes from `src/styles.scss`; do not introduce PrimeNG table patterns because the current app pages do not use PrimeNG widgets.
- Use semantic table markup in `process-list.component.html` plus page-specific SCSS for alignment, responsive overflow handling, status chips, empty/error banners, and pagination controls.
- Keep `/empty` intact unless directly needed; scope changes should focus on making `/processes` the authenticated landing page and updating the tests that currently assume `/empty` is the post-login target.
- Preserve existing auth mocks and auth tests, adjusting only the route expectations impacted by the new landing page.

### Task Breakdown

#### Phase 1: Add typed process-list data access and mock capability
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Define process-list DTOs and UI model mapping | `src/app/core/processes/process-list.models.ts` | Add interfaces/types for the F02 request, response, row item, sort key, and pagination metadata, matching the Confluence contract fields used by the list. |
| 1.2 | Implement real process-list service | `src/app/core/processes/process-list.service.ts` | Create the `HttpClient` service that calls the documented F02 endpoint with the default request parameters required by the Confluence page and maps the response into page-ready items. |
| 1.3 | Add deterministic mock fixtures | `src/app/core/processes/process-list.mock-data.ts` | Create stable fixture datasets for happy path, empty state, and error-path testing, with more than 10 rows and varied statuses/text/date values. |
| 1.4 | Implement process-list mock interceptor | `src/app/core/processes/process-list.mock.interceptor.ts` | Intercept the exact F02 endpoint and return fixture data only when mock mode is enabled, following the style of `auth.mock.interceptor.ts`. |
| 1.5 | Add environment/config support for opt-in mock mode | `src/environments/environment.ts`, `src/environments/environment.mock.ts`, `src/app/app.config.ts`, `angular.json`, `package.json` | Introduce a mock flag and Angular build/serve configuration so local development can run against mocks without making mocks the default runtime path. |

#### Phase 2: Build the authenticated process-list page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Create the standalone page component | `src/app/pages/process-list/process-list.component.ts` | Build the page using signals for loading, error, rows, sort state, and pagination state; fetch data on init through the new service. |
| 2.2 | Render the F02 list layout | `src/app/pages/process-list/process-list.component.html` | Add the page heading, table, sortable column headers, row rendering, loading state, empty state, error state, and pagination controls, binding only to fields defined by F02. |
| 2.3 | Style the page using existing design tokens | `src/app/pages/process-list/process-list.component.scss` | Implement table, toolbar/header, status styling, responsive overflow handling, and pagination visuals using the existing global color/spacing system in `src/styles.scss`. |
| 2.4 | Implement frontend sorting and pagination | `src/app/pages/process-list/process-list.component.ts` | Use computed state to derive sorted rows and current page slices without changing the service contract or requiring backend pagination for this task. |

#### Phase 3: Wire the new page into the authenticated flow
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add the route for the process list | `src/app/app.routes.ts` | Add a lazy-loaded authenticated `/processes` route using `authGuard`. |
| 3.2 | Make process list the authenticated landing page | `src/app/pages/login/login.component.ts`, `src/app/core/auth/guest.guard.ts` | Replace the current `/empty` redirect target with `/processes` for successful login and guest-guard redirects. |
| 3.3 | Keep the rest of auth behavior unchanged | `src/app/core/auth/auth.service.ts` | No behavioral change expected here; verify the existing service remains untouched except for tests/route expectations elsewhere. |

#### Phase 4: Add focused unit coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Test the service request/response contract | `src/app/core/processes/process-list.service.spec.ts` | Verify the service calls the correct F02 endpoint, applies the documented defaults, and maps responses safely. |
| 4.2 | Test component rendering and state transitions | `src/app/pages/process-list/process-list.component.spec.ts` | Cover loading, row rendering, empty state, error state, sort toggling, and page switching. |
| 4.3 | Update login unit expectations for the new landing page | `src/app/pages/login/login.component.spec.ts` | Change only the redirect assertion from `/empty` to `/processes`; keep validation and auth error behavior intact. |

#### Phase 5: Update end-to-end coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add process-list e2e coverage | `tests/e2e/process-list.spec.ts` | Verify an authenticated user can open `/processes`, see mock/real contract-compatible rows, sort columns, paginate, and see empty/error states in mock-enabled runs. |
| 5.2 | Update login flow e2e assertions | `tests/e2e/login.spec.ts`, `tests/e2e/admin-login.spec.ts` | Update successful-login and authenticated-redirect expectations from `/empty` to `/processes`. |
| 5.3 | Preserve existing `/empty` coverage only where still relevant | `tests/e2e/empty-start.spec.ts` | Keep or minimally adjust only if route assumptions break; do not expand the empty screen scope. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.routes.ts` | MODIFY | Add authenticated lazy route for `/processes`. |
| `src/app/app.config.ts` | MODIFY | Register the process mock interceptor conditionally in explicit mock mode. |
| `src/app/core/auth/guest.guard.ts` | MODIFY | Redirect authenticated users to `/processes` instead of `/empty`. |
| `src/app/pages/login/login.component.ts` | MODIFY | Redirect successful logins to `/processes`. |
| `src/app/core/processes/process-list.models.ts` | CREATE | Define typed request/response/model contracts for the F02 process list. |
| `src/app/core/processes/process-list.service.ts` | CREATE | Implement real API access for the documented F02 endpoint. |
| `src/app/core/processes/process-list.service.spec.ts` | CREATE | Add unit coverage for endpoint usage, defaults, and mapping. |
| `src/app/core/processes/process-list.mock-data.ts` | CREATE | Store deterministic fixtures for list, empty, and error scenarios. |
| `src/app/core/processes/process-list.mock.interceptor.ts` | CREATE | Provide opt-in backendless responses for the exact F02 endpoint. |
| `src/app/pages/process-list/process-list.component.ts` | CREATE | Implement page state, data loading, sorting, and pagination. |
| `src/app/pages/process-list/process-list.component.html` | CREATE | Render the process list table and all UI states. |
| `src/app/pages/process-list/process-list.component.scss` | CREATE | Add page-specific styling for the list view. |
| `src/app/pages/process-list/process-list.component.spec.ts` | CREATE | Add unit tests for render/state/sort/pagination behavior. |
| `src/app/pages/login/login.component.spec.ts` | MODIFY | Update redirect expectation to the new landing page. |
| `src/environments/environment.ts` | CREATE | Define default runtime config with real process API path and mock mode disabled. |
| `src/environments/environment.mock.ts` | CREATE | Define mock-enabled config for local backendless runs. |
| `angular.json` | MODIFY | Add file replacements/build-serve config for mock mode. |
| `package.json` | MODIFY | Add a script such as `start:mock` for explicit no-backend local runs. |
| `tests/e2e/process-list.spec.ts` | CREATE | Add e2e validation for process list rendering and interactions. |
| `tests/e2e/login.spec.ts` | MODIFY | Update post-login assertions to `/processes`. |
| `tests/e2e/admin-login.spec.ts` | MODIFY | Update admin login success assertions to `/processes`. |
| `tests/e2e/empty-start.spec.ts` | MODIFY | Adjust only if required by the authenticated landing-page change. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` succeeds with the default real-integration configuration
5. [ ] `npm run start:mock` (or equivalent mock serve configuration) loads the app without a live backend
6. [ ] Successful login redirects to `/processes`
7. [ ] `/processes` renders rows from the F02-shaped response
8. [ ] Sorting works for each supported column
9. [ ] Pagination works across at least two pages of fixture data
10. [ ] Empty-state fixture shows the no-data message instead of table rows
11. [ ] Error fixture shows the error state instead of a broken page
12. [ ] Unit tests pass for `ProcessesService` and `ProcessListComponent`
13. [ ] Playwright tests pass for login redirect and process-list interactions