# SDLC-108: Logowanie

## Specification

### Overview
This task delivers a working login flow for the Angular application using a static account with credentials `admin/admin`. The implementation should complete the existing placeholder authentication flow so that users can access protected routes after successful login, see an error for invalid credentials, and retry without reloading the page.

### User Stories
- US-001: As a user, I want to log in with the `admin` account, so that I can access protected areas of the application.
  - Given I am on the login screen / When I enter `admin` as login and `admin` as password and submit / Then I am authenticated and redirected to the default logged-in view.
- US-002: As a user, I want to see clear feedback for invalid credentials, so that I know login failed and can try again.
  - Given I am on the login screen / When I submit any credentials other than `admin/admin` / Then I stay on the login screen, see an error message, and can retry.
- US-003: As the system, I want to maintain authenticated state after successful login, so that guarded routes remain accessible during app usage.
  - Given I have successfully logged in / When I navigate to protected routes or refresh the app / Then the authenticated user state is restored and access is preserved until logout.

### Functional Requirements
- FR-001: The application must expose a login screen at `/login` with username, password, and submit action.
- FR-002: The password field must remain masked using an input of type `password`.
- FR-003: `AuthService.login(username, password)` in `src/app/core/services/auth.service.ts` must validate only the exact static credentials `admin/admin`.
- FR-004: On successful authentication, `AuthService` must populate `currentUser` with a static user object compatible with the existing `User` interface.
- FR-005: The authenticated user state must be persisted in browser storage so protected routes continue to work after app refresh.
- FR-006: On failed authentication, `AuthService.login(...)` must reject with an error and must not leave any authenticated state behind.
- FR-007: `LoginComponent` must call `AuthService.login(...)` instead of navigating unconditionally.
- FR-008: After successful login, `LoginComponent` must redirect the user to the default logged-in route, which is currently `/` and resolves to `/processes` via `src/app/app.routes.ts`.
- FR-009: After failed login, `LoginComponent` must show a user-facing error message such as `Błędny login lub hasło.` and keep the form available for another attempt.
- FR-010: `AuthService.logout()` must clear both in-memory and persisted auth state to stay consistent with the new session handling.

### Edge Cases
- EC-001: If empty values are submitted despite HTML validation bypass, the service must treat them as invalid credentials.
- EC-002: If login fails, no partial user state may remain in memory or local storage.
- EC-003: If stale or malformed auth data exists in local storage, the service must ignore it and treat the user as logged out.
- EC-004: Repeated failed attempts must remain possible without page reload.
- EC-005: If the app is refreshed after a successful login, the guard must still allow access because auth state is restored during service initialization.

### Success Criteria
- [ ] `/login` displays login, password, and submit controls.
- [ ] Entering `admin/admin` logs the user in and redirects to the default authenticated view.
- [ ] Entering invalid credentials shows an error and does not navigate to protected views.
- [ ] The password field does not reveal typed characters.
- [ ] The user can retry login immediately after a failed attempt.
- [ ] Authenticated state survives browser refresh within the same browser session/storage.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository already contains the basic authentication skeleton and routing structure:

- standalone Angular 17 components with inline templates, e.g. `src/app/features/login/login.component.ts`
- lazy-loaded routes in `src/app/app.routes.ts`
- a functional route guard in `src/app/core/guards/auth.guard.ts`
- a placeholder auth service in `src/app/core/services/auth.service.ts`

The implementation should reuse these existing structures rather than introducing a new auth module or backend integration.

Confluence discovery results:
- No existing authentication service/API documentation was found in the configured `Services` space.
- No relevant frontend/domain conventions were found in the configured `SDLC` space.

Because no reusable external auth service was documented, the runtime default should be an in-app static credential check implemented in the existing `AuthService`. This is aligned with the Jira scope, which explicitly requires a permanently available static `admin/admin` account and explicitly excludes external identity-provider integration.

Key implementation decisions:
1. **Keep `AuthService.login(...)` Promise-based**
   - The method already returns `Promise<User>`.
   - Implementing the static check behind that contract minimizes surface-area change and keeps future backend replacement straightforward.

2. **Persist auth state in `localStorage`**
   - Current `authGuard` relies on `auth.isLoggedIn`.
   - Without persistence, refresh would lose session state because `currentUser` is currently in-memory only.
   - Add a small storage hydration path in `AuthService` constructor/private helpers so the existing guard continues to work without changes.

3. **Update `LoginComponent` to use the service instead of unconditional navigation**
   - Replace the  in `onLogin()` with async auth handling.
   - Add local component state for `errorMessage` and `isSubmitting`.
   - Preserve the current inline-template and Bootstrap styling approach already used in the repo.

4. **Keep routing unchanged**
   - `src/app/app.routes.ts` already redirects authenticated root access to `/processes`.
   - Successful login can simply navigate to `/`, letting existing route config determine the default view.

5. **Add focused unit tests**
   - There are currently no visible app tests in the repo tree.
   - Add specs for the service and login component to cover acceptance criteria without expanding scope into unrelated areas.

If nothing relevant was found in a configured scope, state that explicitly: no existing auth service or frontend convention was found in `Services`/`SDLC`, so the plan extends the current in-repo auth placeholders.

### Task Breakdown

#### Phase 1: Complete static authentication service
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Implement static admin credential validation | `src/app/core/services/auth.service.ts` | Replace the `Not implemented` placeholder in `login()` with validation for exact `admin/admin` credentials and return a static `User` object with role `EUM_ADMINISTRATOR`. |
| 1.2 | Add auth-state persistence | `src/app/core/services/auth.service.ts` | Introduce a storage key constant, hydrate `currentUser` from `localStorage` on service creation, and persist/remove user data on login/logout. |
| 1.3 | Harden invalid-state handling | `src/app/core/services/auth.service.ts` | Ensure failed login clears stale state and malformed stored data is ignored safely. |

#### Phase 2: Wire the login screen to real auth behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Replace placeholder submit flow | `src/app/features/login/login.component.ts` | Inject `AuthService`, call `login()` from `onLogin()`, and navigate to `/` only after successful authentication. |
| 2.2 | Add error and submit state | `src/app/features/login/login.component.ts` | Track `errorMessage` and `isSubmitting`, show a Bootstrap error alert on failed login, and prevent double-submit while request logic is running. |
| 2.3 | Preserve retry UX | `src/app/features/login/login.component.ts` | Clear the error before each new submit and keep form inputs editable after failure so the user can retry immediately. |

#### Phase 3: Add automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add service unit tests | `src/app/core/services/auth.service.spec.ts` | Cover successful login, failed login, persisted state hydration, and logout cleanup. |
| 3.2 | Add login component tests | `src/app/features/login/login.component.spec.ts` | Cover masked password field, successful submit + navigation, failed submit + error message, and retry behavior. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/services/auth.service.ts` | MODIFY | Implement static `admin/admin` authentication, persisted auth state, and logout cleanup. |
| `src/app/features/login/login.component.ts` | MODIFY | Connect form to `AuthService`, add error handling, and redirect only after successful login. |
| `src/app/core/services/auth.service.spec.ts` | CREATE | Add unit tests for auth success, failure, persistence, and logout behavior. |
| `src/app/features/login/login.component.spec.ts` | CREATE | Add component tests for UI behavior and login flow outcomes. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check: navigating to `/login` shows the form with masked password input
5. [ ] Manual check: `admin/admin` redirects to `/processes`
6. [ ] Manual check: invalid credentials show an error and keep the user on `/login`
7. [ ] Manual check: refreshing after successful login keeps protected routes accessible
