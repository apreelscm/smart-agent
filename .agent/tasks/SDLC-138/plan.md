# SDLC-138: Ekran logowania do eUmowy

## Specification

### Overview
Zadanie dostarcza i domyka ekran logowania do eUmowy w aplikacji Angular, tak aby nieautoryzowany użytkownik trafiał na prosty formularz z polami login i hasło, przechodził przez standardowy mechanizm uwierzytelniania platformy, a po sukcesie był przekierowywany na `/empty`.

Repozytorium zawiera już działający szkielet tego flow (`src/app/core/auth/`, `src/app/pages/login/`, `src/app/app.routes.ts`), więc zakres implementacji powinien koncentrować się na dopasowaniu do wymagań Jira, utrzymaniu zgodności z istniejącym kontraktem auth oraz uzupełnieniu testów potwierdzających kryteria akceptacji.

### User Stories
- US-001: As a eUmowy user, I want to enter my login and password, so that I can access the application.
  - Given I am not authenticated / When I open the application or a protected route / Then I am shown the login screen

- US-002: As a eUmowy user, I want required-field validation on the login form, so that I know I must provide both values before submitting.
  - Given I am on the login screen / When I submit an empty or whitespace-only form / Then validation messages are shown and no login request is sent

- US-003: As a eUmowy user, I want a clear but generic authentication failure message, so that I can retry without exposing security details.
  - Given I submit invalid credentials / When authentication fails with invalid credentials / Then I stay on the login screen and see a generic invalid-login message

- US-004: As a product owner, I want login to reuse the standard platform authentication/session flow, so that eUmowy remains consistent with platform security.
  - Given the authentication API is available / When the user submits valid credentials / Then the application uses the existing auth service contract, establishes authenticated state, and navigates to `/empty`

### Functional Requirements
- FR-001: The application shall redirect unauthenticated users from `/` to `/login`.
- FR-002: The application shall protect `/empty` and other guarded views using the existing route guard pattern from `src/app/core/auth/auth.guard.ts`.
- FR-003: The login screen shall contain only:
  - a login field,
  - a password field,
  - a submit button,
  - validation/authentication error messages.
- FR-004: The password field shall use masked input.
- FR-005: Both login and password fields shall be required and shall reject whitespace-only values.
- FR-006: Submitting an invalid form shall not trigger a network request.
- FR-007: The UI shall label the first field as “Login”, while the request payload may continue using the existing API property `username`.
- FR-008: The login flow shall use the existing runtime auth endpoints already wired in `src/app/core/auth/auth.service.ts`:
  - `POST /api/auth/login`
  - `POST /api/auth/logout`
- FR-009: The login request payload shall remain `{ username: string, password: string }`.
- FR-010: The login success response handled by the frontend shall remain compatible with the existing shape:
  - `{ token: string, refreshToken?: string, user: { username, firstName?, lastName?, email?, sellerNumber?, auwId?, roles? } }`
- FR-011: On successful authentication, the app shall normalize and persist the current user state via `AuthService` and navigate to `/empty`.
- FR-012: On 401 authentication failure, the app shall remain on `/login` and show a generic invalid-credentials message that does not reveal which field was incorrect.
- FR-013: On non-401 failures, the app shall remain on `/login` and show a generic technical error message.
- FR-014: An already authenticated user opening `/login` shall be redirected to `/empty`.
- FR-015: Persisted authenticated state shall be restored after refresh using the existing localStorage-based mechanism in `AuthService`.
- FR-016: A temporary runtime mock backend shall be introduced for authentication so the frontend can work without the real backend for now; the mock shall preserve the `/api/auth/login` and `/api/auth/logout` contract and be easy to replace with the live backend later.

### Edge Cases
- EC-001: User submits spaces in both fields.
  - Expected behavior: values are treated as empty, validation is shown, no HTTP request is sent.
- EC-002: User clicks submit multiple times before the first response returns.
  - Expected behavior: duplicate submission is blocked while `isSubmitting()` is true.
- EC-003: User opens `/empty` or `/design` without an active session.
  - Expected behavior: route guard redirects to `/login`.
- EC-004: User refreshes `/empty` after a successful login.
  - Expected behavior: persisted auth state is restored and the user stays authorized.
- EC-005: Backend returns 401.
  - Expected behavior: auth state is not persisted and the UI shows only a generic invalid-login message.
- EC-006: Backend returns 5xx/network error.
  - Expected behavior: auth state is not persisted and the UI shows a generic retry-later message.
- EC-007: localStorage contains malformed or legacy auth data.
  - Expected behavior: `AuthService` normalizes valid legacy data or clears invalid data safely.
- EC-008: Authenticated user navigates back to `/login`.
  - Expected behavior: `guestGuard` redirects back to `/empty`.

### Success Criteria
- [ ] Opening `/` as a guest redirects to `/login`
- [ ] Opening a guarded route as a guest redirects to `/login`
- [ ] The login page shows login, password, and submit controls only
- [ ] The password input masks entered characters
- [ ] Empty or whitespace-only values do not submit
- [ ] Valid credentials authenticate the user and navigate to `/empty`
- [ ] Invalid credentials keep the user on `/login` and show a generic auth error
- [ ] Authenticated users cannot stay on `/login`
- [ ] Refresh after successful login preserves authenticated state
- [ ] Unit and e2e tests cover validation, success, failure, and route protection

### Open Questions
- None at this stage.

---

## Implementation Plan

### Technical Approach
The repository already follows the right architectural pattern for this task:

- standalone Angular components (`src/app/pages/login/login.component.ts`, `src/app/pages/empty/empty.component.ts`)
- lazy-loaded routing in `src/app/app.routes.ts`
- centralized auth state in `src/app/core/auth/auth.service.ts`
- functional guards in `src/app/core/auth/auth.guard.ts` and `src/app/core/auth/guest.guard.ts`
- component-local UI state with Angular signals
- Reactive Forms for form validation
- co-located unit specs plus Playwright e2e coverage

Implementation should therefore **reuse and harden the existing flow**, not create a parallel login mechanism.

Existing service reuse from Confluence:
- [F01 — Authentication & Session Management](https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management)
- [F01 — Authentication & Session — Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/174653441/F01+Authentication+Session+Test+Scenarios)

These pages confirm that eUmowy should reuse the platform authentication/session capability, backed by MicroLDAP and HTTP session lifecycle management. The implementation plan should therefore preserve the existing auth contract already encoded in the repo:

- runtime login endpoint: `POST /api/auth/login`
- runtime logout endpoint: `POST /api/auth/logout`
- login request body: `{ username, password }`
- handled success payload: `{ token, refreshToken?, user }`

This is the contract the frontend must preserve. Because the backend is not yet available for this story, the plan should introduce a temporary mock HTTP backend for authentication that intercepts `/api/auth/login` and `/api/auth/logout`, returns contract-compatible success and error responses, and keeps `AuthService` plus the login screen working end to end without a live backend dependency.

The mock should be isolated behind app-level HTTP configuration so it can be removed or disabled once the real backend is ready. Test doubles remain acceptable in:
- `src/app/core/auth/auth.service.spec.ts` via `HttpTestingController`
- `tests/e2e/login.spec.ts` via Playwright route mocking when scenario-specific overrides are needed

Repository observations that drive the plan:
- `src/app/app.routes.ts` already redirects `/` to `/login` and guards `/empty` and `/design`.
- `src/app/pages/login/login.component.ts` already implements trimmed required validation, submit de-duplication, 401 vs generic error handling, and redirect to `/empty`.
- `src/app/core/auth/auth.service.ts` already encapsulates request/response typing, current-user normalization, and localStorage persistence.
- Existing tests already cover most core flows, so the work should focus on closing any remaining acceptance gaps and making coverage explicit rather than redesigning the feature.

No relevant domain guidance was found in the configured `SDLC` Confluence space, so the plan relies on repo conventions plus the existing Services-space auth documentation above.

Deployment/config note:
- Until the real backend is available, the frontend should serve `/api/auth/*` from the temporary auth mock backend; once backend routing exists, the same contract should point to the real platform backend or same-origin API without changing consuming code.
- No application-level auth secret is required in the frontend for this story; end-user credentials are supplied interactively at login time.

### Task Breakdown

#### Phase 1: Align auth core with platform login flow
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Preserve auth API contract behind a temporary mock backend | `src/app/core/auth/auth.service.ts`, `src/app/core/auth/auth.mock.interceptor.ts`, `src/app/app.config.ts` | Keep `POST /api/auth/login` and `POST /api/auth/logout` as the consumed paths, preserve the existing `{ username, password }` request contract, and introduce a removable mock HTTP backend so the frontend works without the real backend for now. |
| 1.2 | Verify user-state normalization and persistence | `src/app/core/auth/auth.service.ts` | Confirm the service keeps successful auth state only after valid responses, restores persisted state on refresh, and clears malformed storage values safely. |
| 1.3 | Confirm route protection behavior | `src/app/app.routes.ts`, `src/app/core/auth/auth.guard.ts`, `src/app/core/auth/guest.guard.ts` | Retain guest-only access to `/login`, authenticated access to `/empty` and `/design`, and root redirect behavior. |
| 1.4 | Strengthen auth unit coverage | `src/app/core/auth/auth.service.spec.ts`, `src/app/core/auth/auth.guard.spec.ts`, `src/app/core/auth/guest.guard.spec.ts` | Add or refine tests for restore-from-storage, failed-login non-persistence, guest redirect, authenticated redirect away from `/login`, and logout cleanup. |

#### Phase 2: Finalize login screen UX against Jira acceptance criteria
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Keep the login form minimal and compliant | `src/app/pages/login/login.component.html` | Ensure the screen exposes only login, password, submit, and error messaging; no reset-password links, SSO options, or extra support content. |
| 2.2 | Retain form validation and submit behavior | `src/app/pages/login/login.component.ts` | Keep trimmed required validation, `markAllAsTouched()` on invalid submit, in-flight submission guard, and redirect to `/empty` on success. |
| 2.3 | Ensure secure and clear failure messaging | `src/app/pages/login/login.component.ts`, `src/app/pages/login/login.component.html` | Preserve generic invalid-credentials messaging for 401 and separate generic technical failure messaging for other errors without leaking field-level auth detail. |
| 2.4 | Polish login form styling states | `src/app/pages/login/login.component.scss` | Keep masked password styling, invalid-field visuals, error box, and disabled button behavior consistent with the shared design tokens in `src/styles.scss`. |
| 2.5 | Keep the success destination deterministic | `src/app/pages/empty/empty.component.html`, `src/app/pages/empty/empty.component.ts`, `src/app/pages/empty/empty.component.scss` | Preserve `/empty` as the simple post-login destination used by the ticket and automated tests. |

#### Phase 3: Prove the behavior with automated tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Complete login component tests | `src/app/pages/login/login.component.spec.ts` | Verify rendering, password masking, whitespace rejection, trimmed submit payload, 401 handling, generic error handling, and duplicate-submit blocking. |
| 3.2 | Keep e2e auth tests aligned with the temporary mock backend | `tests/e2e/login.spec.ts` | Validate root redirect, guarded-route protection, successful login, refresh persistence, redirect away from `/login` after auth, and invalid-credentials flow against the temporary mock backend, using Playwright route stubs only when a scenario needs explicit response overriding. |
| 3.3 | Stabilize smoke coverage for authenticated startup | `tests/e2e/smoke.spec.ts` | Seed a valid stored user shape and confirm the app loads correctly when auth state already exists. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/auth/auth.service.ts` | MODIFY | Keep the auth contract stable while consuming mock-backed `/api/auth/*` calls and preserving state persistence/normalization behavior |
| `src/app/core/auth/auth.mock.interceptor.ts` | CREATE | Add a temporary mock backend for `/api/auth/login` and `/api/auth/logout` that returns contract-compatible success and error responses |
| `src/app/app.config.ts` | MODIFY | Register the temporary auth mock HTTP backend in the application HTTP provider chain |
| `src/app/core/auth/auth.service.spec.ts` | MODIFY | Expand service tests for success, failure, restore, and logout cleanup |
| `src/app/core/auth/auth.guard.spec.ts` | MODIFY | Verify protected-route redirect behavior |
| `src/app/core/auth/guest.guard.spec.ts` | MODIFY | Verify redirect-away behavior for authenticated users opening `/login` |
| `src/app/pages/login/login.component.ts` | MODIFY | Finalize submit, validation, and error-state behavior against Jira criteria |
| `src/app/pages/login/login.component.html` | MODIFY | Keep the login screen minimal and acceptance-criteria compliant |
| `src/app/pages/login/login.component.scss` | MODIFY | Align invalid, error, and disabled visual states with the app theme |
| `src/app/pages/login/login.component.spec.ts` | MODIFY | Cover login UI behavior end to end at component level |
| `src/app/pages/empty/empty.component.html` | MODIFY | Preserve `/empty` as the explicit success target after login |
| `src/app/pages/empty/empty.component.scss` | MODIFY | Maintain simple success-page presentation for post-login state |
| `tests/e2e/login.spec.ts` | MODIFY | Cover guest redirects, success, invalid login, and persistence scenarios against the temporary auth mock backend |
| `tests/e2e/smoke.spec.ts` | MODIFY | Ensure authenticated startup remains stable in browser automation |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Navigating to `/` as a guest lands on `/login`
5. [ ] Navigating to `/empty` as a guest redirects to `/login`
6. [ ] Submitting whitespace-only values shows validation and sends no login request
7. [ ] Submitting valid credentials sends `POST /api/auth/login` with trimmed `{ username, password }` through the temporary mock backend
8. [ ] Successful login navigates to `/empty`
9. [ ] Invalid credentials keep the user on `/login` with a generic auth error
10. [ ] Refresh after successful login preserves authenticated access
11. [ ] Visiting `/login` while authenticated redirects to `/empty`

## Revision History
| Revision | Reviewer | Summary of changes |
|---|---|---|
| 1 | razorapid | Updated the plan to introduce a temporary mock HTTP auth backend for `/api/auth/login` and `/api/auth/logout`, adjusted tasks and file changes, and aligned e2e/verification steps so the frontend can work without the real backend for now. |