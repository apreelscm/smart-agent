# SDLC-144: Mapka

## Specification

### Overview
This task adds a “Pokaż na mapie” action to the visit details screen at `src/app/pages/portal/wizyta/` so a user can view the facility location in a popup without leaving the current page. The action is available only for non-telemedicine visits and only when the app has enough location data to render the facility on OpenStreetMap.

### User Stories
- US-001: As a patient viewing an in-person visit, I want to open the facility location from the visit details page, so that I can quickly confirm where the appointment takes place.
  - Given I am on `/portal/wizyta/:id` for a stationary visit with location data / When I click “Pokaż na mapie” / Then a popup opens with an OpenStreetMap map and the facility marker.

- US-002: As a patient viewing a remote consultation, I do not want to see a map action, so that the UI stays relevant to the visit type.
  - Given I am on `/portal/wizyta/:id` for a telemedicine visit / When the page renders / Then the “Pokaż na mapie” action is not shown.

- US-003: As a mobile or desktop user, I want the map popup to fit my screen, so that I can close it and continue using the visit details page easily.
  - Given I open the map popup on desktop or mobile / When the modal appears / Then the map scales to the viewport and closing it returns me to the same visit details screen.

### Functional Requirements
- FR-001: Add a visible “Pokaż na mapie” action in `src/app/pages/portal/wizyta/wizyta.component.html` near the facility address block.
- FR-002: The action must be rendered only when the visit is not telemedicine and the app has enough data to locate the facility.
- FR-003: The map must open in a modal/popup rendered within the visit details page, without route navigation away from `/portal/wizyta/:id`.
- FR-004: The modal must display an OpenStreetMap view with a single marker for the facility.
- FR-005: The map must load lazily only after the user opens the popup.
- FR-006: If visit coordinates are available, they must be used as the primary source for marker placement.
- FR-007: If coordinates are missing but a real postal address exists, the app must resolve coordinates from the existing visit address before rendering the map.
- FR-008: The action must not be shown for visits where neither coordinates nor a renderable address are available.
- FR-009: Closing the popup must keep the user on the visit details page with the existing scroll/context intact.
- FR-010: The implementation must reuse the existing `Visit` detail data source in `src/app/pages/portal/visits.data.ts` and must not introduce unrelated route or layout changes.
- FR-011: The solution must follow the existing standalone Angular component pattern already used by `WizytaComponent`, `OpiekaComponent`, and `PortalLayoutComponent`.

### Edge Cases
- EC-001: For telemedicine visits such as `w2`, the facility map action must not render.
- EC-002: For a visit with a blank or non-geocodable address and no coordinates, the map action must not be available.
- EC-003: If geocoding returns no results, the page must not crash; the modal should not render a broken map state.
- EC-004: Reopening the modal for the same visit should not trigger unnecessary duplicate geocoding if the location was already resolved during the session.
- EC-005: If the modal is closed while the map is loading, the visit details page must remain usable and stable.
- EC-006: The modal layout must remain usable on narrow mobile widths and must not overflow horizontally.

### Success Criteria
- [ ] Stationary visits with valid location data show “Pokaż na mapie” next to the facility address.
- [ ] Clicking the action opens a popup with an OpenStreetMap view and a facility marker.
- [ ] Telemedicine visits do not show the map action.
- [ ] The map is instantiated only after the popup opens.
- [ ] Closing the popup keeps the user on the same visit details page.
- [ ] Unit and e2e coverage verify action visibility, modal behavior, and telemedicine exclusion.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This repository is an Angular 21 standalone-component app with route-level lazy loading in `src/app/app.routes.ts` and local page state managed with `signal()`/`computed()`, as seen in `src/app/pages/portal/opieka/opieka.component.ts` and `src/app/pages/portal/wizyta/wizyta.component.ts`. The visit details page is currently driven entirely by `findVisit()` from `src/app/pages/portal/visits.data.ts`, so this feature should stay local to the `wizyta` page and its data model.

Confluence discovery:
- No existing map/location service, REST endpoint, or OpenAPI reference relevant to this ticket was found in the configured `Services` space.
- No relevant ADR, glossary entry, or UI/domain convention was found in the configured `SDLC` space.
- Therefore, no reusable internal service was identified in Confluence; this feature should be implemented as a new frontend-only capability within the repo.

Recommended implementation:
- Keep the feature scoped to `src/app/pages/portal/wizyta/`.
- Extend the `Visit` model in `src/app/pages/portal/visits.data.ts` with optional geographic coordinates for deterministic demo data.
- Add a small feature-local service, e.g. `src/app/pages/portal/wizyta/visit-location.service.ts`, responsible for:
  - resolving coordinates from the visit,
  - preferring existing coordinates,
  - optionally geocoding the address through OpenStreetMap Nominatim when coordinates are absent,
  - building the embeddable OpenStreetMap URL for the modal iframe.
- Render the popup directly inside `wizyta.component.html` rather than introducing a shared modal abstraction, because there is no existing modal component under `src/app/shared/components/` and the ticket is limited to the visit details page.
- Use an iframe-based OpenStreetMap embed (`https://www.openstreetmap.org/export/embed.html`) with a computed `bbox` and `marker` parameter. This keeps the implementation lightweight and avoids adding a new map rendering dependency just for one modal.
- Use on-demand geocoding only as a fallback when a stationary visit lacks coordinates. Runtime default for this repo should still prefer real coordinates stored with the visit data.
- No mock JSON runtime fallback should be introduced for the map itself. Existing `VISITS` demo data remains the page’s current source of truth, and any geocoding fallback should call the real OpenStreetMap/Nominatim endpoint directly when needed.

Runtime integration defaults:
- OpenStreetMap embed: `https://www.openstreetmap.org/export/embed.html?...`
- Address geocoding fallback: `https://nominatim.openstreetmap.org/search?format=jsonv2&limit=1&q=...`
- Auth: none required.
- Secrets/credentials: none required.

Codebase-specific rationale:
- `WizytaComponent` already computes derived state from route input; add modal-open and map-load state with signals there.
- Reuse existing portal styling classes from `src/styles.scss` (`sz-card`, `sz-link`, `sz-btn-outline`) and extend only `src/app/pages/portal/wizyta/wizyta.component.scss`.
- Keep all route behavior in `src/app/app.routes.ts` unchanged.
- Avoid changing unrelated pages or shared layout components.

### Task Breakdown

#### Phase 1: Extend visit data with location support
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add coordinates to the visit model | `src/app/pages/portal/visits.data.ts` | Extend `Visit` with optional geographic fields, e.g. `coordinates: { lat: number; lon: number }`, to support marker placement. |
| 1.2 | Seed demo visits with facility coordinates | `src/app/pages/portal/visits.data.ts` | Add coordinates for existing stationary/demo facility visits such as `w1`, `w3`, and `w4`, while leaving telemedicine data unchanged. |

#### Phase 2: Add map location resolution service
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Create a feature-local map/location service | `src/app/pages/portal/wizyta/visit-location.service.ts` | Implement location resolution logic that prefers visit coordinates and falls back to geocoding the visit address with `HttpClient`. |
| 2.2 | Build embeddable OpenStreetMap URL | `src/app/pages/portal/wizyta/visit-location.service.ts` | Add helper logic to generate the iframe URL with `bbox`, `layer=mapnik`, and `marker=lat,lon`. |
| 2.3 | Add light in-memory caching for resolved addresses | `src/app/pages/portal/wizyta/visit-location.service.ts` | Prevent repeated geocoding for the same address when a user reopens the modal during one app session. |

#### Phase 3: Update the visit details component
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add map modal state to `WizytaComponent` | `src/app/pages/portal/wizyta/wizyta.component.ts` | Introduce signals/computed state for modal visibility, loading state, embed URL, and whether the action should render. |
| 3.2 | Trigger lazy location loading on click | `src/app/pages/portal/wizyta/wizyta.component.ts` | Implement an `openMap()` flow that resolves location only when the user requests the map. |
| 3.3 | Add close behavior | `src/app/pages/portal/wizyta/wizyta.component.ts` | Implement `closeMap()` and cleanup/reset rules so closing the popup preserves the page state. |
| 3.4 | Render the map action by the facility address | `src/app/pages/portal/wizyta/wizyta.component.html` | Add a “Pokaż na mapie” button/link under the existing address block only when the visit is eligible. |
| 3.5 | Render the modal markup and iframe lazily | `src/app/pages/portal/wizyta/wizyta.component.html` | Add an `@if` block for the popup, loading state, close control, and embedded map iframe. |
| 3.6 | Keep telemedicine behavior unchanged | `src/app/pages/portal/wizyta/wizyta.component.html` | Ensure the action is excluded when `isTelemedicine()` is true. |

#### Phase 4: Add responsive styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Style the new address action | `src/app/pages/portal/wizyta/wizyta.component.scss` | Add spacing and visual treatment for the “Pokaż na mapie” action under the facility address. |
| 4.2 | Style the modal overlay and content | `src/app/pages/portal/wizyta/wizyta.component.scss` | Add popup container, backdrop, header, iframe wrapper, and mobile/desktop sizing rules. |
| 4.3 | Add loading/error visual states | `src/app/pages/portal/wizyta/wizyta.component.scss` | Provide simple loading/error presentation consistent with the existing portal look and feel. |

#### Phase 5: Add automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add unit tests for location resolution | `src/app/pages/portal/wizyta/visit-location.service.spec.ts` | Verify coordinate-first behavior, fallback geocoding request shape, no-result handling, and embed URL generation. |
| 5.2 | Add component tests for visit-details map behavior | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Verify action visibility for stationary vs telemedicine visits, lazy modal opening, iframe rendering, and close behavior. |
| 5.3 | Add e2e coverage for the visit details map | `tests/e2e/visit-map.spec.ts` | Verify that a stationary visit shows the action and opens/closes the popup, while a telemedicine visit does not expose the action. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/pages/portal/visits.data.ts` | MODIFY | Extend the visit model with optional location coordinates and enrich stationary demo visits. |
| `src/app/pages/portal/wizyta/visit-location.service.ts` | CREATE | Encapsulate coordinate resolution, fallback geocoding, caching, and OpenStreetMap embed URL creation. |
| `src/app/pages/portal/wizyta/wizyta.component.ts` | MODIFY | Add map modal state and lazy map-loading logic to the visit details component. |
| `src/app/pages/portal/wizyta/wizyta.component.html` | MODIFY | Add the “Pokaż na mapie” action and popup/modal markup with lazy iframe rendering. |
| `src/app/pages/portal/wizyta/wizyta.component.scss` | MODIFY | Add styles for the address action, modal, and responsive map layout. |
| `src/app/pages/portal/wizyta/visit-location.service.spec.ts` | CREATE | Add unit coverage for map location resolution and URL generation. |
| `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | CREATE | Add unit coverage for visit-details map visibility and modal behavior. |
| `tests/e2e/visit-map.spec.ts` | CREATE | Add end-to-end coverage for stationary and telemedicine visit map behavior. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes successfully
5. [ ] `npm run test -- --watch=false` passes with the new `visit-location` and `wizyta` specs
6. [ ] `npm run e2e` passes with the added visit map scenario
7. [ ] Manual check: `/portal/wizyta/w1` shows “Pokaż na mapie”, opens a popup, and renders an OpenStreetMap iframe with the facility marker
8. [ ] Manual check: `/portal/wizyta/w2` does not show the map action
9. [ ] Manual check: closing the popup keeps the user on the same visit details screen on both desktop and mobile widths