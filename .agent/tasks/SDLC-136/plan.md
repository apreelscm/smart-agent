# SDLC-136: Ekran logowania z domyślnym kontem admin/admin

## Specification

### Overview
This task delivers the first usable authentication entry point for the Angular application: a dedicated login screen for unauthenticated users, validation for required credentials, error handling for failed sign-in attempts, route protection, and redirection to a blank post-login page.

The implementation should fit the current frontend-only Angular repository while aligning with the existing authentication/session capability documented in Confluence. In practice, the UI should submit credentials through a dedicated auth service abstraction, redirect authenticated users to an empty page, and prevent unauthenticated access to protected routes.

### User Stories
- US-001: As an administrator, I want to log in with the default `admin/admin` account, so that I can access the application after first launch.
  - Given I am on the login screen / When I enter valid credentials `admin/admin` and submit / Then I am authenticated and redirected to the empty page

- US-002: As an unauthenticated user, I want to see a simple login form with login and password fields, so that I can start using the application.
  - Given I am not logged in / When I open the application / Then I see the login screen instead of protected content

- US-003: As a user, I want validation and clear error feedback, so that I understand why login did not succeed.
  - Given I leave required fields empty or enter invalid credentials / When I try to submit / Then I see validation or authentication error messages and remain unauthenticated

### Functional Requirements
- FR-001: The application shall expose a dedicated login route for unauthenticated users.
- FR-002: The login screen shall contain required fields for login and password.
- FR-003: The password field shall use masked input.
- FR-004: The login form shall prevent successful submission when required fields are empty and display validation feedback.
- FR-005: The login action shall be handled through a dedicated auth service in the frontend, not inline in the component.
- FR-006: The runtime default integration shall target the existing authentication/session backend documented in Confluence, using the login flow from F01 Authentication & Session Management and F01 Authentication & Session Test Scenarios:
  - Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management
  - Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/174653441/F01+Authentication+Session+Test+Scenarios
- FR-007: The frontend auth service shall submit credentials to the application login endpoint as the runtime default, using `username` and `password`, expecting:
  - `200` success with session establishment
  - `401` invalid credentials
- FR-008: On successful authentication, the application shall persist minimal frontend auth state and redirect the user to a blank page.
- FR-009: On failed authentication, the application shall remain on the login page and show an invalid login message.
- FR-010: Protected routes shall redirect unauthenticated users to the login screen.
- FR-011: Visiting the login route while already authenticated shall redirect the user to the blank page.
- FR-012: The default admin login path shall work consistently across supported application versions by keeping the login flow isolated behind a single frontend auth abstraction.

### Edge Cases
- EC-001: User navigates directly to a protected route without a session.
  - Expected: redirect to `/login`
- EC-002: User refreshes the browser on the blank page after a successful login.
  - Expected: route remains accessible if frontend auth state is still present
- EC-003: User submits whitespace-only values.
  - Expected: values are treated as empty after trimming and validation is shown
- EC-004: User clicks submit multiple times during an in-flight login request.
  - Expected: duplicate submissions are blocked until the request completes
- EC-005: Backend login endpoint is unavailable or returns a non-401 error.
  - Expected: user remains on login page and sees a generic sign-in failure message
- EC-006: Authenticated user opens `/login`.
  - Expected: redirect to `/empty` instead of showing the form

### Success Criteria
- [ ] Opening `/` as an unauthenticated user redirects to the login screen
- [ ] The login screen shows login and password fields plus a submit button
- [ ] The password input is masked
- [ ] Empty required fields show validation feedback and do not authenticate
- [ ] Valid credentials authenticate the user and redirect to a blank page
- [ ] Invalid credentials show an error and keep the user on the login page
- [ ] Protected routes are inaccessible without authentication
- [ ] Automated tests cover success, failure, and validation flows

### Open Questions
- None

---

## Implementation Plan

### Technical Approach
The repository is an Angular 21 standalone application with lazy-loaded routes in `src/app/app.routes.ts`, global providers in `src/app/app.config.ts`, shared design tokens in `src/styles.scss`, and reusable UI shell components in `src/app/shared/components`. The current app is a prototype/demo UI with a single `design` page and no auth module yet.

The implementation should follow existing codebase patterns:
- standalone components like `src/app/pages/design/design.component.ts`
- lazy route loading from `src/app/app.routes.ts`
- shared styling via `src/styles.scss`
- `HttpClient` usage enabled already in `src/app/app.config.ts`

Repo-specific reuse:
- Reuse `WizardCardComponent` (`src/app/shared/components/wizard-card/wizard-card.component.*`) as the visual shell for the login page instead of building a new layout
- Reuse the existing global form/button tokens from `src/styles.scss`
- Reuse the current E2E auth storage convention from `tests/e2e/smoke.spec.ts`, which already seeds `localStorage['auth.currentUser']`

Confluence reuse:
- Existing authentication capability was found in Services space:
  - F01 Authentication & Session Management: https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management
  - F01 Authentication & Session Test Scenarios: https://apreel.atlassian.net/wiki/spaces/Services/pages/174653441/F01+Authentication+Session+Test+Scenarios
- The plan should reuse that auth/session flow as the runtime default. The frontend should call the login endpoint through `HttpClient`, sending `username` and `password`, handling `200` as successful session creation and `401` as invalid credentials.
- Mock data files are not needed and must not be used in production code. Test-layer request mocking is acceptable only in Playwright.
- No relevant domain guidance was found in the `SDLC` Confluence space.

Implementation strategy:
1. Add a small auth layer under `src/app/core/auth/`:
   - one service responsible for login, auth-state persistence, and auth checks
   - one guard for protected routes
   - one guest-only guard for the login page
2. Add two new standalone pages:
   - `login` page
   - `empty` page
3. Update routing so:
   - `/` redirects to `/login`
   - `/login` redirects authenticated users to `/empty`
   - `/empty` requires authentication
   - existing `/design` should also be protected so the app does not expose non-login content to unauthenticated users
4. Keep backend authority in the auth API call, but cache minimal frontend session state in local storage for route guards and refresh continuity
5. Add E2E coverage for:
   - valid login
   - invalid login
   - empty-form validation
   - route protection/redirect behavior

### Task Breakdown

#### Phase 1: Authentication foundation and routing
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create auth service | `src/app/core/auth/auth.service.ts` | Add a dedicated service for `login`, `logout` helper, auth-state persistence, and `isAuthenticated` checks. Use `HttpClient` and the existing `auth.currentUser` localStorage key. |
| 1.2 | Add auth guard | `src/app/core/auth/auth.guard.ts` | Create a route guard that redirects unauthenticated users to `/login`. |
| 1.3 | Add guest-only guard | `src/app/core/auth/guest.guard.ts` | Create a route guard that redirects authenticated users away from `/login` to `/empty`. |
| 1.4 | Update route map | `src/app/app.routes.ts` | Replace the current root redirect to `design` with login-first routing, add lazy routes for `login` and `empty`, and protect `empty` and `design`. |

#### Phase 2: Login and empty-page UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Create login page component | `src/app/pages/login/login.component.ts` | Build a standalone login page using Reactive Forms, trimmed values, submit state, auth error state, and router navigation on success. |
| 2.2 | Create login template | `src/app/pages/login/login.component.html` | Render login/password fields, validation messages, auth error banner, and submit button inside the existing `app-wizard-card` shell. |
| 2.3 | Style the login page | `src/app/pages/login/login.component.scss` | Add component-scoped styles for validation/error presentation while reusing global tokens from `src/styles.scss`. |
| 2.4 | Create empty destination page | `src/app/pages/empty/empty.component.ts` | Add a minimal standalone page for the post-login destination. |
| 2.5 | Add empty-page template | `src/app/pages/empty/empty.component.html` | Render an intentionally blank/minimal authenticated placeholder view that satisfies the Jira requirement. |
| 2.6 | Style the empty page | `src/app/pages/empty/empty.component.scss` | Add minimal layout styling so the page is visibly loaded but intentionally empty/simple. |

#### Phase 3: Automated verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Update smoke test for new auth flow | `tests/e2e/smoke.spec.ts` | Adjust the existing smoke test so it seeds auth state and verifies the authenticated landing flow under the new routing. |
| 3.2 | Add login E2E scenarios | `tests/e2e/login.spec.ts` | Add Playwright coverage for successful login, invalid credentials, required-field validation, and protected-route redirect behavior. Mock network requests to the login endpoint only in the test layer. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.routes.ts` | MODIFY | Replace root redirect, add `login` and `empty` routes, and protect non-login routes |
| `src/app/core/auth/auth.service.ts` | CREATE | Central auth API integration and frontend auth-state handling |
| `src/app/core/auth/auth.guard.ts` | CREATE | Redirect unauthenticated users to login |
| `src/app/core/auth/guest.guard.ts` | CREATE | Redirect authenticated users away from login |
| `src/app/pages/login/login.component.ts` | CREATE | Standalone login page logic with Reactive Forms |
| `src/app/pages/login/login.component.html` | CREATE | Login form markup and error/validation UI |
| `src/app/pages/login/login.component.scss` | CREATE | Login page styles |
| `src/app/pages/empty/empty.component.ts` | CREATE | Blank authenticated landing page component |
| `src/app/pages/empty/empty.component.html` | CREATE | Minimal blank-page template |
| `src/app/pages/empty/empty.component.scss` | CREATE | Blank-page styling |
| `tests/e2e/smoke.spec.ts` | MODIFY | Align smoke coverage with auth-aware routing |
| `tests/e2e/login.spec.ts` | CREATE | New end-to-end coverage for login scenarios |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements