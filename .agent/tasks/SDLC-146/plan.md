# SDLC-146: Mapka

## Specification

### Overview
Add a “Pokaż na mapie” action on the visit details screen (`/portal/wizyta/:id`) so a user can view the facility location in a popup without leaving the current page. The popup should show an OpenStreetMap-based map centered on the facility, a single marker for the resolved address, and additional facility information already available on the visit details screen. When geocoding fails, the UI must show a clear error state instead of an empty or incorrect map.

### User Stories
- US-001: As a patient viewing visit details, I want to click “Pokaż na mapie” next to the facility address, so that I can quickly see where the facility is located.
  - Given a stationary visit with a facility address / When the details page loads / Then a “Pokaż na mapie” action is visible near the address
  - Given the action is visible / When I click “Pokaż na mapie” / Then a popup opens without navigating away from the visit details screen

- US-002: As a patient, I want the popup to show the correct facility on an OpenStreetMap map, so that I can confirm I am looking at the right place.
  - Given the popup is opened / When geocoding succeeds for the current visit address / Then the map is centered on that location and shows a single marker
  - Given the popup is opened / When the popup content is rendered / Then I see the facility name and full address alongside the map

- US-003: As a patient, I want a clear fallback when the address cannot be resolved, so that I am not shown a broken or misleading map.
  - Given the current visit address cannot be geocoded / When I click “Pokaż na mapie” / Then the popup shows a readable error message and no empty map

### Functional Requirements
- FR-001: On `src/app/pages/portal/wizyta/wizyta.component.html`, add a “Pokaż na mapie” action within the facility/address info block for mappable in-person visits.
- FR-002: The action must not trigger route navigation or page reload; it opens a modal/popup on top of the current details page.
- FR-003: The popup must display an OpenStreetMap-based embedded map centered on the facility location with a single marker.
- FR-004: Facility coordinates must be resolved on demand from the current visit address every time the user opens the popup.
- FR-005: The popup must display additional facility context already present on the page, at minimum facility name and full address.
- FR-006: When geocoding is in progress, the popup must show a loading state instead of a blank map area.
- FR-007: When geocoding returns no result or fails, the popup must show a user-friendly error message and must not render an empty/incorrect map.
- FR-008: The popup must include a close control and return the user to the unchanged visit details context.
- FR-009: Telemedicine visits and visits without a valid physical facility context must not expose the “Pokaż na mapie” action.
- FR-010: The implementation must follow the existing standalone Angular component pattern already used in `wizyta.component.ts`, `opieka.component.ts`, and `portal-layout.component.ts`.

### Edge Cases
- EC-001: Telemedicine visit (`type` contains `telemedyczna`) — hide the map action entirely.
- EC-002: Missing or blank address for a stationary visit — hide the action or immediately present the popup error state; do not attempt to render a map.
- EC-003: Geocoding returns an empty result set — show a clear “nie udało się pokazać lokalizacji” message with facility details and no iframe/map.
- EC-004: Geocoding request errors or times out — show the same user-facing error state and allow the user to close the popup.
- EC-005: User clicks the action repeatedly while lookup is in progress — ignore duplicate opens or keep a single in-flight request/state.
- EC-006: Address contains Polish characters/special characters — geocoding request must encode the query correctly.
- EC-007: User closes the popup after success or failure — the background page state remains unchanged and scroll/context are preserved.

### Success Criteria
- [ ] The visit details screen shows “Pokaż na mapie” for physical visits with a facility address
- [ ] Clicking the action opens a popup on the same page
- [ ] The popup renders an OpenStreetMap-based view with one marker for successful geocoding
- [ ] The popup shows facility name and full address
- [ ] Failed geocoding shows a readable error state and no broken map
- [ ] Telemedicine visits do not show the map action
- [ ] Unit and e2e tests cover the new behavior

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Implement the feature directly in the visit-details area around `src/app/pages/portal/wizyta/wizyta.component.*`, using the repo’s current Angular 21 standalone-component style and signal-based state management.

Codebase patterns to follow:
- `WizytaComponent` already uses `input()` and `computed()` for route-driven view state.
- `OpiekaComponent` uses Angular signals for local UI state toggles.
- `PortalLayoutComponent` provides the page shell and should remain unchanged.
- Shared visual tokens and button/link styles already exist in `src/styles.scss` (`.sz-card`, `.sz-btn`, `.sz-btn-outline`, `.sz-link`), so the modal should reuse them instead of introducing a new UI library.

Confluence discovery results:
- No existing map/geocoding service was found in the configured `Services` space.
- No relevant visit-detail/modal/domain convention was found in the configured `SDLC` space.
- Therefore, no reusable documented internal service is available; a new feature-local integration is required.

Runtime integration default:
- Use live OpenStreetMap-compatible services at runtime, not a mock JSON fallback.
- Geocoding request default: `GET https://nominatim.openstreetmap.org/search?format=jsonv2&limit=1&q={encodedAddress}`
- Map embed default: `https://www.openstreetmap.org/export/embed.html?...&marker={lat},{lon}`
- Mock data/files are forbidden in the production map flow; test doubles may be used only in unit/e2e tests.
- If deployment CSP is enforced, allow:
  - `connect-src https://nominatim.openstreetmap.org`
  - `frame-src https://www.openstreetmap.org`

Recommended implementation shape:
- Create a feature-local service, e.g. `src/app/pages/portal/wizyta/visit-map.service.ts`, responsible for:
  - geocoding the address,
  - normalizing the first result into lat/lon,
  - building a safe OpenStreetMap embed URL with a small bounding box around the point.
- Keep the modal markup local to `wizyta.component.html` because there is no existing shared modal component in the repo and the scope is limited to this page.
- Add signal-based UI state in `wizyta.component.ts`:
  - whether the modal is open,
  - loading state,
  - resolved map URL,
  - failure message.
- Reuse existing visit data from `src/app/pages/portal/visits.data.ts`; no new runtime data source is needed for facility metadata.
- Hide the map action for telemedicine by reusing the existing `isTelemedicine` computed state.

This avoids adding heavyweight dependencies like Leaflet for a single-page popup requirement, keeps bundle churn low, and fits the repo’s current simple mockup architecture.

### Task Breakdown

#### Phase 1: Add map lookup service and UI state
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create feature-local geocoding service | `src/app/pages/portal/wizyta/visit-map.service.ts` | Add an injectable service wrapping `HttpClient` to call Nominatim with the current visit address and return the first matching lat/lon result. |
| 1.2 | Build OpenStreetMap embed URL helper | `src/app/pages/portal/wizyta/visit-map.service.ts` | Add helper logic to calculate a small bbox around the coordinates and generate the OSM embed URL with a single marker. |
| 1.3 | Extend visit detail component state | `src/app/pages/portal/wizyta/wizyta.component.ts` | Inject the new service and add signals/computed values for `canShowMap`, `isMapModalOpen`, `isMapLoading`, `mapError`, and `mapUrl`. |
| 1.4 | Implement open/close handlers | `src/app/pages/portal/wizyta/wizyta.component.ts` | Add methods to open the modal, request geocoding on demand, handle success/failure, and close/reset modal state without changing route context. |

#### Phase 2: Render the map action and popup
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add “Pokaż na mapie” action near facility address | `src/app/pages/portal/wizyta/wizyta.component.html` | Render the action only for non-telemedicine visits with a usable address, colocated with the existing facility info block. |
| 2.2 | Add popup markup with facility details | `src/app/pages/portal/wizyta/wizyta.component.html` | Add modal structure with dialog semantics, close button, loading state, success state (iframe), and error state. Include facility name and full address. |
| 2.3 | Add test-friendly selectors | `src/app/pages/portal/wizyta/wizyta.component.html` | Add `data-testid` attributes for the action button, modal, map frame container, and error message to stabilize tests. |
| 2.4 | Style the popup and map area | `src/app/pages/portal/wizyta/wizyta.component.scss` | Add overlay, dialog, responsive layout, loading/error states, and inline action styling using existing `sz-*` design tokens. |

#### Phase 3: Verify behavior with automated tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add unit tests for visit detail map behavior | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Cover button visibility, telemedicine suppression, modal open/close flow, loading state, success rendering, and graceful failure behavior. |
| 3.2 | Mock HTTP geocoding in unit tests | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Use Angular HTTP testing utilities to verify the address-based Nominatim request and return fixture responses only in test code. |
| 3.3 | Add e2e happy-path coverage | `tests/e2e/visit-map.spec.ts` | Open a physical visit, intercept the Nominatim call, click “Pokaż na mapie”, and assert the popup and facility details render correctly. |
| 3.4 | Add e2e failure-path coverage if lightweight | `tests/e2e/visit-map.spec.ts` | Optionally intercept geocoding with an empty result/error and assert the user-facing error state, if it can be done without modifying demo visit data. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/pages/portal/wizyta/visit-map.service.ts` | CREATE | Feature-local service for runtime geocoding and OSM embed URL generation |
| `src/app/pages/portal/wizyta/wizyta.component.ts` | MODIFY | Add map modal state, computed visibility rules, and open/close/geocode handlers |
| `src/app/pages/portal/wizyta/wizyta.component.html` | MODIFY | Add “Pokaż na mapie” action and popup markup with loading/success/error states |
| `src/app/pages/portal/wizyta/wizyta.component.scss` | MODIFY | Add styling for the inline map action, modal overlay, dialog layout, and responsive map container |
| `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | CREATE | Unit tests for button visibility, modal behavior, geocoding success, and failure handling |
| `tests/e2e/visit-map.spec.ts` | CREATE | Playwright coverage for the new popup flow on the visit details page |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm run test -- --runInBand` or repo-standard Angular/Vitest command
3. [ ] Playwright tests pass with `npm run e2e`
4. [ ] Visiting `/portal/wizyta/w1` shows “Pokaż na mapie”
5. [ ] Clicking the action opens the modal and shows facility details plus an OSM map
6. [ ] Visiting `/portal/wizyta/w2` does not show the map action
7. [ ] Mocked geocoding failure shows a clear error message and no empty map