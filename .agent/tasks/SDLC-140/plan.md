# SDLC-140: Dostosuj stronę logowania i ekran startowy

## Specification

### Overview
This task refreshes the `/login` and `/empty` views so they visually reference the legacy eUmowy experience while staying aligned with the current frontend design system already expressed through shared tokens and layout components in the repository. The scope is strictly presentational: authentication flow, route guards, mocked auth endpoints, and redirect behavior stay unchanged. The post-login start screen must remain the existing `/empty` route, but without the banner/hero and without adding new business content.

### User Stories
- US-001: As an existing eUmowy user, I want the login screen to look familiar, so that the new application feels recognizable and easier to adopt.
  - Given I am unauthenticated / When I open `/login` / Then I see a responsive login page styled with the current design system and recognizable legacy-inspired branding/layout cues.

- US-002: As an authenticated user, I want the start screen to remain simple and empty, so that I am not distracted by placeholder or banner content.
  - Given I successfully log in / When I am redirected to `/empty` / Then I land on a sparse start view with no hero/banner and no new business modules.

- US-003: As a mobile user, I want both screens to work well on small devices, so that I can log in and enter the app comfortably from a phone.
  - Given I use a mobile viewport / When I view `/login` or `/empty` / Then the layout stacks cleanly, remains readable, and preserves usable spacing and control sizes.

- US-004: As a product owner, I want the redesign to reuse the current design system and keep current behavior intact, so that UI consistency improves without introducing regressions.
  - Given the redesign is implemented / When users submit valid or invalid credentials / Then authentication messages, validation, and navigation still behave as they do today.

### Functional Requirements
- FR-001: The `/login` route must remain guarded by `guestGuard` and continue using the existing `LoginComponent` authentication logic in `src/app/pages/login/login.component.ts`.
- FR-002: The login page must be visually redesigned in a mobile-first way using the project’s existing design tokens from `src/styles.scss`.
- FR-003: The login form must keep the same functional controls and behaviors: username, password, validation, submission state, 401 error handling, generic error handling, and redirect to `/empty`.
- FR-004: The redesign must incorporate visual guidance from the legacy reference page in Confluence without copying legacy 1:1 where it conflicts with the current design system.
- FR-005: The `/empty` route must remain the post-login destination and stay protected by `authGuard`.
- FR-006: The `/empty` screen must remove the existing banner/hero presentation and remain intentionally empty/sparse rather than introducing new dashboard content.
- FR-007: Any current placeholder copy on `/empty` that conflicts with the “empty view” requirement should be removed or minimized.
- FR-008: Shared layout changes must be implemented in a way that does not break existing consumers such as the design showcase page.
- FR-009: Automated unit and e2e tests must be updated to reflect the new UI while continuing to verify no regression in login and navigation behavior.

### Edge Cases
- EC-001: Whitespace-only username/password values must still show validation feedback and must not trigger auth requests after the visual redesign.
- EC-002: Invalid credentials must still surface the existing 401 message in the redesigned layout.
- EC-003: Non-401 backend failures must still surface the generic fallback error without breaking the page layout.
- EC-004: An authenticated user refreshing `/empty` must remain on `/empty` and must not see the removed banner reappear.
- EC-005: On narrow mobile widths, the login layout must avoid horizontal overflow and preserve full-width inputs and CTA visibility.
- EC-006: If shared shell changes are introduced to hide the hero/banner for `/empty`, defaults must preserve current behavior for other pages that still depend on the shared shell.

### Success Criteria
- [ ] `/login` is visually updated to better resemble legacy eUmowy while using current design tokens and component conventions.
- [ ] `/empty` remains the post-login destination and no longer renders the current banner/hero area.
- [ ] No new business widgets or flows are added to `/empty`.
- [ ] Existing login validation, error handling, and redirect behavior continue to work unchanged.
- [ ] Both views render cleanly on mobile, tablet, and desktop breakpoints.
- [ ] Unit and Playwright coverage are updated to assert the new presentation and preserved behavior.

### Open Questions
- None at this stage.

---

## Implementation Plan

### Technical Approach
The app is an Angular 21 standalone-component application with lazy-loaded routes in `src/app/app.routes.ts`, global design tokens in `src/styles.scss`, and current auth behavior centered in `src/app/pages/login/login.component.ts` plus `src/app/core/auth/auth.service.ts`. The safest implementation is to keep the existing login/auth logic and routing untouched, and limit changes to templates, styles, shared shell configurability, and tests.

Relevant discovery before repo analysis:
- Reuse visual reference material from Confluence: [Legacy screen captures](https://apreel.atlassian.net/wiki/spaces/Services/pages/175276041/Legacy+screen+captures). This should guide the login page’s visual hierarchy, branding cues, and overall resemblance to the legacy eUmowy screens.
- No reusable API/service integration relevant to this ticket was found in the configured `Services` scope; this task is frontend presentation only.
- No relevant domain convention pages were found in the configured `SDLC` space for design-system/mobile-first guidance, so the implementation should follow the existing repo patterns instead.

Codebase-driven strategy:
- Preserve `LoginComponent` submission and error logic as-is; the current TS already cleanly encapsulates behavior and redirects to `EMPTY_ROUTE = '/empty'`.
- Refactor `WizardCardComponent` non-destructively so pages can opt out of the hero/banner while preserving existing defaults for current consumers. This avoids duplicating the shell and keeps the `design` page usable.
- Redesign `login.component.html`/`.scss` around the current form, keeping existing `data-testid` attributes where practical so e2e coverage stays stable.
- Simplify `empty.component.html`/`.scss` so it truly behaves as an intentionally empty start screen; remove current placeholder headings/copy that contradict the requirement.
- Extend tests instead of changing runtime behavior: unit tests should keep verifying login logic, while Playwright should verify redirect behavior plus banner removal on `/empty`.

When integrating the Confluence findings into implementation:
- The runtime application will continue using the real current frontend route structure and auth flow already implemented in the repo.
- The Confluence page is reused strictly as a visual reference source for styling decisions, not as a mocked runtime asset or production data source.

### Task Breakdown

#### Phase 1: Make the shared shell support a bannerless variant
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add non-breaking shell configuration for hero/banner visibility | `src/app/shared/components/wizard-card/wizard-card.component.ts` | Introduce explicit inputs for hiding the hero/banner section, and optionally header controls if needed, while preserving current defaults for existing pages. |
| 1.2 | Render shell sections conditionally | `src/app/shared/components/wizard-card/wizard-card.component.html` | Wrap the current header/hero markup in conditional blocks so `/empty` can opt out of the banner without affecting `/design` and existing login behavior during transition. |
| 1.3 | Add styles for bannerless shell spacing | `src/app/shared/components/wizard-card/wizard-card.component.scss`, `src/styles.scss` | Ensure card spacing still looks intentional when the hero is hidden; add only the minimal shared SCSS needed for responsive consistency. |

#### Phase 2: Redesign the login page presentation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Rework login markup around legacy-inspired visual hierarchy | `src/app/pages/login/login.component.html` | Keep the same form controls and submit flow, but update structure to match the target look more closely using current design-system building blocks and the Confluence legacy reference. |
| 2.2 | Implement mobile-first responsive styling | `src/app/pages/login/login.component.scss`, `src/styles.scss` | Add page-specific styles for spacing, typography, alignment, error state presentation, and breakpoints so the page scales cleanly from phone to desktop. |
| 2.3 | Preserve behavior while updating tests to the new markup | `src/app/pages/login/login.component.spec.ts` | Replace outdated assertions that currently expect no heading/minimal wrapper markup; keep behavior-focused coverage for validation, error handling, submit locking, and redirect. |

#### Phase 3: Convert `/empty` into the final sparse start screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Remove banner usage from the empty route | `src/app/pages/empty/empty.component.html` | Update the template to use the shared shell in bannerless mode, or equivalent layout, so `/empty` no longer renders the hero/banner area. |
| 3.2 | Strip placeholder content and keep an intentionally empty layout | `src/app/pages/empty/empty.component.html`, `src/app/pages/empty/empty.component.scss` | Replace the current placeholder headings and explanatory copy with a sparse, design-system-aligned empty state container that introduces no new business content. |
| 3.3 | Add focused unit coverage for the empty screen | `src/app/pages/empty/empty.component.spec.ts` | Verify the start screen renders without the banner and without legacy placeholder copy regressions. |

#### Phase 4: Update end-to-end coverage for the redesigned screens
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Align login e2e expectations with the new UI | `tests/e2e/login.spec.ts`, `tests/e2e/login-auth.spec.ts` | Keep assertions on auth flow and validation, but update any brittle visual/content expectations so they match the redesigned login screen. |
| 4.2 | Assert post-login navigation still lands on `/empty` without a banner | `tests/e2e/login.spec.ts`, `tests/e2e/admin-login.spec.ts` | Update post-login checks to verify redirect persistence and banner removal rather than the current placeholder `/empty` headings/text. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/shared/components/wizard-card/wizard-card.component.ts` | MODIFY | Add optional inputs for a bannerless shell variant while preserving default behavior. |
| `src/app/shared/components/wizard-card/wizard-card.component.html` | MODIFY | Conditionally render header/hero sections based on new inputs. |
| `src/app/shared/components/wizard-card/wizard-card.component.scss` | MODIFY | Support spacing/states for the new shell variant. |
| `src/styles.scss` | MODIFY | Extend shared responsive/layout tokens and common styles used by the redesigned auth/start screens. |
| `src/app/pages/login/login.component.html` | MODIFY | Replace current simple markup with the final legacy-inspired, design-system-aligned login structure. |
| `src/app/pages/login/login.component.scss` | MODIFY | Implement mobile-first responsive styling for the updated login screen. |
| `src/app/pages/login/login.component.spec.ts` | MODIFY | Update unit tests to reflect the new markup while preserving behavior assertions. |
| `src/app/pages/empty/empty.component.html` | MODIFY | Remove banner usage and replace placeholder copy with a sparse start-screen template. |
| `src/app/pages/empty/empty.component.scss` | MODIFY | Style the bannerless empty route so it remains intentional across breakpoints. |
| `src/app/pages/empty/empty.component.spec.ts` | CREATE | Add unit coverage for banner removal and sparse empty-view rendering. |
| `tests/e2e/login.spec.ts` | MODIFY | Update e2e checks for redesigned login and new `/empty` expectations. |
| `tests/e2e/login-auth.spec.ts` | MODIFY | Keep login-flow assertions aligned with the updated page structure. |
| `tests/e2e/admin-login.spec.ts` | MODIFY | Verify admin login still redirects to `/empty` and that the banner is absent after authentication. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm run test -- --watch=false`
3. [ ] New tests cover requirements for login presentation, validation preservation, `/empty` redirect behavior, and banner removal
4. [ ] Playwright tests pass with `npm run e2e`
5. [ ] Manual check: `/login` renders correctly at mobile, tablet, and desktop widths
6. [ ] Manual check: successful login still navigates to `/empty`, refresh preserves session, and no banner/hero appears on the start screen