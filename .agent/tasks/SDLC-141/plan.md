# SDLC-141: Dostosowanie ekranu logowania.

## Specification

### Overview
This task simplifies the `/login` screen so the user sees only the elements required to authenticate. The current login presentation in `src/app/pages/login/login.component.html` is more elaborate than the Jira scope allows: it uses `WizardCardComponent`, shows a banner/hero area, and renders additional intro and support content, including the visible heading “Zaloguj się do systemu”.

The implementation should reduce the login page to a minimal, responsive form-only view while preserving all existing authentication behavior from `src/app/pages/login/login.component.ts` and `src/app/core/auth/auth.service.ts`. Scope is limited strictly to the login screen.

### User Stories
- US-001: As a system user, I want to see only the login form, so that I can get to credential entry immediately.
  - Given I open `/login` / When the page renders / Then I see only the controls needed to log in.

- US-002: As a product owner, I want the login screen to stop using the wizard/card layout, so that the page matches the new UI assumption for this route only.
  - Given the login page is displayed / When I inspect the rendered layout / Then it does not use the shared wizard/card shell.

- US-003: As a user, I want login validation and authentication to work exactly as before, so that the redesign does not break access to the app.
  - Given I enter valid or invalid credentials / When I submit the form / Then validation, error handling, loading state, and redirect behavior remain unchanged.

### Functional Requirements
- FR-001: The `/login` route must remain protected by `guestGuard` in `src/app/app.routes.ts`.
- FR-002: `LoginComponent` must keep the existing authentication logic, including `trimmedRequiredValidator`, `isSubmitting`, `authError`, and redirect to `/empty`.
- FR-003: The login template must render only login-related UI: username field, password field, validation feedback, authentication error feedback, and submit button.
- FR-004: The login screen must not render the banner/hero currently provided by `WizardCardComponent`.
- FR-005: The login screen must not render the visible text “Zaloguj się do systemu”.
- FR-006: The login screen must not be based on `WizardCardComponent` or the shared wizard/card layout structure.
- FR-007: Existing field labels, submit behavior, and `data-testid` selectors used by current tests should be preserved where possible to minimize unnecessary test churn.
- FR-008: The redesign must remain responsive and render correctly at supported viewport sizes without horizontal overflow.
- FR-009: No changes may be made to backend contracts, authentication endpoints, form fields, or route flow.

### Edge Cases
- EC-001: Whitespace-only values in `username` or `password` must still fail validation and must not trigger `AuthService.login`.
- EC-002: A 401 response must still display `Nieprawidłowy login lub hasło.` in the simplified layout.
- EC-003: Non-401 failures must still display the generic fallback message without breaking the form layout.
- EC-004: While a login request is in flight, duplicate submits must remain blocked and the submit button must stay disabled.
- EC-005: Removing the visible heading must not make the form inaccessible; labels and form-level accessible naming must still be sufficient.
- EC-006: On mobile widths, the simplified layout must keep both inputs and the CTA visible without clipping or overflow.

### Success Criteria
- [ ] `/login` shows only the login form and auth feedback elements.
- [ ] The login page no longer renders a banner, wizard shell, or card-based wrapper.
- [ ] The visible text `Zaloguj się do systemu` is removed from the login screen.
- [ ] Successful login still redirects to `/empty`.
- [ ] Validation, 401 handling, generic error handling, and loading-state behavior remain unchanged.
- [ ] Unit and Playwright tests are updated to verify the simplified layout and preserved auth flow.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is an Angular 21 standalone-component app with route-level lazy loading in `src/app/app.routes.ts`, auth behavior encapsulated in `src/app/pages/login/login.component.ts`, and shared layout primitives under `src/app/shared/components/`. The current login page imports and renders `WizardCardComponent`, which conflicts directly with the Jira acceptance criteria.

Key implementation decision:
- Do **not** try to satisfy the requirement by merely hiding parts of `WizardCardComponent`.
- Instead, remove `WizardCardComponent` from `LoginComponent` entirely and replace the template with plain login-page markup owned by `src/app/pages/login/login.component.html`.
- This is the only approach that cleanly satisfies “no card” and “no wizard” for the login screen while keeping the change scoped to this route only.

Relevant Confluence discovery:
- Existing authentication capability is already documented in Confluence and must be reused unchanged: [F01 — Authentication & Session Management](https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management).
- The repo already reflects that integration through the current auth flow in `src/app/core/auth/auth.service.ts` using `/api/auth/login` and `/api/auth/logout`; this ticket will not alter those endpoints or payload handling.
- No relevant login-layout/domain convention was found in the configured `SDLC` space; search results only surfaced a generic screen overview, so implementation should follow existing repo patterns rather than inventing a Confluence-driven UI rule.

Codebase-specific guidance:
- Keep `LoginComponent` form logic intact; only remove the unused `WizardCardComponent` import from the standalone `imports` array.
- Preserve the current `data-testid` attributes in the form controls and submit button so existing Playwright coverage needs only targeted updates.
- Keep the existing global input/button styling from `src/styles.scss`; add only login-specific positioning and spacing in `src/app/pages/login/login.component.scss`.
- Do not modify `src/app/core/auth/auth.service.ts`, `src/app/app.routes.ts`, `src/app/app.config.ts`, or shared wizard/card files, because the ticket is explicitly limited to the login screen.
- Existing mock behavior via `auth.mock.interceptor.ts` remains test/dev-only and unchanged; no runtime integration changes are needed.

### Task Breakdown

#### Phase 1: Remove wizard/card-based layout from the login route
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove shared wizard dependency from login component | `src/app/pages/login/login.component.ts` | Delete the `WizardCardComponent` import and keep only the existing reactive-form/auth logic. |
| 1.2 | Rewrite login template as form-only markup | `src/app/pages/login/login.component.html` | Replace `<app-wizard-card>` and all intro/banner-related content with a plain container that renders only auth error feedback, the two fields, validation messages, and submit button. |
| 1.3 | Preserve accessibility without visible heading text | `src/app/pages/login/login.component.html` | Replace the current `aria-labelledby` approach tied to the removed heading with a form-level accessible label or equivalent non-visual structure. |

#### Phase 2: Simplify styling while keeping responsive behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Replace decorative/grid styles with minimal centered form layout | `src/app/pages/login/login.component.scss` | Remove styles for intro copy, highlights, panel label, and two-column layout; define a simple responsive wrapper centered within the viewport using existing CSS tokens. |
| 2.2 | Keep only login-form-specific visual states | `src/app/pages/login/login.component.scss` | Retain styles needed for validation, error banner, button sizing, and spacing, but avoid card-like or wizard-like presentation. |

#### Phase 3: Update unit coverage for the new contract
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Replace render assertions tied to removed UI copy | `src/app/pages/login/login.component.spec.ts` | Update the initial render test to assert the presence of only form controls and the absence of intro content, visible heading text, and wizard/card wrapper assumptions. |
| 3.2 | Preserve behavior-focused tests | `src/app/pages/login/login.component.spec.ts` | Keep existing tests for trimmed submission, validation, 401 handling, generic failures, and duplicate-submit blocking, adjusting only selectors/assertions affected by the markup simplification. |

#### Phase 4: Update end-to-end checks for the simplified login screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Align route-guard and login-flow assertions with the new UI | `tests/e2e/login.spec.ts` | Remove heading-based expectations, keep guest redirect/auth-flow assertions, and add checks that the login route does not show banner/wizard/card elements. |
| 4.2 | Replace redesign-specific expectations with simplification checks | `tests/e2e/login-redesign.spec.ts` | Convert the current “redesigned layout” spec into a “minimal login screen” spec that verifies absence of `Zaloguj się do systemu`, intro copy, and decorative sections while retaining validation assertions. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/pages/login/login.component.ts` | MODIFY | Remove `WizardCardComponent` from the standalone component imports while preserving login behavior. |
| `src/app/pages/login/login.component.html` | MODIFY | Replace wizard/card-based layout with minimal form-only markup and remove the visible heading/banner/intro content. |
| `src/app/pages/login/login.component.scss` | MODIFY | Simplify page styling to a responsive, non-card, non-wizard login form layout. |
| `src/app/pages/login/login.component.spec.ts` | MODIFY | Update unit tests to validate the simplified UI contract and preserved auth behavior. |
| `tests/e2e/login.spec.ts` | MODIFY | Update e2e expectations to reflect the simplified login screen and unchanged auth flow. |
| `tests/e2e/login-redesign.spec.ts` | MODIFY | Replace redesign assertions with checks for the new minimal login presentation. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes successfully
5. [ ] `npm run test -- --watch=false` passes with updated `LoginComponent` coverage
6. [ ] `npm run e2e` passes with updated `/login` expectations
7. [ ] Manual check: `/login` shows no banner, no visible `Zaloguj się do systemu`, and no wizard/card shell
8. [ ] Manual check: valid credentials still redirect to `/empty`, invalid credentials still show the existing error message