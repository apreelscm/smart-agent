# SDLC-147: Mapa

## Specification

### Overview
This task adds a “Pokaż na mapie” action to the visit details screen at `/portal/wizyta/:id`, opening a popup with an embedded OpenStreetMap view centered on the facility’s existing coordinates and showing a single marker for that facility.

The change is limited to the visit details experience implemented in `src/app/pages/portal/wizyta/*`. It must also enforce the clarified rule from Jira comments: if a visit has no stored coordinates, the UI must hide the facility name, facility address, and the “Pokaż na mapie” action.

### User Stories
- US-001: As a patient viewing visit details, I want to open a map from the facility section, so that I can quickly confirm where the visit takes place.
  - Given a visit has facility coordinates / When I click “Pokaż na mapie” / Then a popup opens with an OpenStreetMap view centered on that facility.

- US-002: As a patient, I want to see the facility marked clearly on the map, so that I can recognize the exact visit location.
  - Given the map popup is open / When the map renders / Then a single marker indicates the facility location.

- US-003: As a patient, I want to stay in the visit details context after checking the map, so that I do not lose my place in the application.
  - Given the map popup is open / When I close it / Then I remain on the same visit details page.

- US-004: As a patient, I do not want to see misleading facility details when coordinates are unavailable, so that the screen only shows actionable location data.
  - Given a visit has no coordinates / When the details page renders / Then the facility name, facility address, and “Pokaż na mapie” action are not shown.

### Functional Requirements
- FR-001: The visit details page in `src/app/pages/portal/wizyta/wizyta.component.html` must render a “Pokaż na mapie” action within the facility section only when `visit.coordinates` is available.
- FR-002: Clicking “Pokaż na mapie” must open an in-page popup/modal rather than navigating away from `/portal/wizyta/:id`.
- FR-003: The popup must display an embedded OpenStreetMap map as the runtime default.
- FR-004: The embedded map must be centered using the existing `VisitCoordinates` values from `src/app/pages/portal/visits.data.ts`.
- FR-005: The embedded map must display exactly one marker for the facility location.
- FR-006: The popup must include a visible close control and closing it must only dismiss the popup.
- FR-007: If `visit.coordinates` is missing, the facility information block must not render the facility name, address, or map action.
- FR-008: Existing visit-detail behavior outside map handling must remain unchanged, including route binding via `withComponentInputBinding`, status rendering, preparation tips, and side-panel actions.
- FR-009: The implementation must not introduce geocoding, route planning, editable map state, or multi-marker behavior.
- FR-010: The implementation should add stable selectors or accessible structure suitable for unit/e2e verification of the popup state.

### Edge Cases
- EC-001: Visits without coordinates, including the current telemedicine sample in `VISITS`, must hide the full facility/address block instead of showing partial text.
- EC-002: If coordinates are missing or invalid at runtime, the component should treat them as unavailable and not render the map trigger or popup content.
- EC-003: Opening and closing the popup repeatedly must not alter the current route, visit data, or scroll context beyond the temporary overlay.
- EC-004: Completed and cancelled visits that still have coordinates must show the map action, because the requirement depends on coordinates, not visit status.
- EC-005: The popup should remain usable on narrow screens, with the map constrained to the viewport and a close action always visible.

### Success Criteria
- [ ] On a visit with coordinates, the details page shows “Pokaż na mapie” under the facility address.
- [ ] Clicking the action opens a popup containing an OpenStreetMap embed.
- [ ] The map is initialized so the facility marker is visible immediately.
- [ ] Closing the popup keeps the user on the same `/portal/wizyta/:id` page.
- [ ] On a visit without coordinates, the facility name, facility address, and map action are all hidden.
- [ ] Unit and e2e tests cover both coordinate-present and coordinate-missing scenarios.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This repository is an Angular 21 standalone-component app using route lazy loading in `src/app/app.routes.ts` and signal-based component state in portal screens such as `src/app/pages/portal/opieka/opieka.component.ts` and `src/app/pages/portal/wizyta/wizyta.component.ts`. The visit details screen already resolves data locally through `findVisit(this.id())` from `src/app/pages/portal/visits.data.ts`, so the map feature should extend that existing pattern rather than introducing a new data flow.

Confluence discovery outcome:
- No existing location/map service or visit-location API relevant to this task was found in the configured `Services` space.
- No relevant domain ADR/convention was found in the configured `SDLC` space.
- Therefore, this plan reuses the existing repo-local visit model in `src/app/pages/portal/visits.data.ts`. No Confluence-documented integration is available to reuse for facility coordinates.

Implementation strategy:
- Keep the feature fully local to `WizytaComponent`.
- Add signal/computed state for:
  - whether coordinates are available,
  - whether the map popup is open,
  - the generated OpenStreetMap embed URL.
- Use a lightweight iframe-based OpenStreetMap embed instead of adding a new map library. `package.json` currently has no Leaflet/OpenLayers dependency, and the ticket only needs a read-only single-marker popup. This keeps scope narrow and avoids package churn.
- Generate the iframe URL from the existing WGS84 coordinates already described in `visits.data.ts`, using an OpenStreetMap embed URL shaped like `https://www.openstreetmap.org/export/embed.html?...&marker={lat},{lon}` so the marker is visible on first open.
- Use an in-template overlay/modal for the popup, matching the repo’s current preference for simple standalone components and SCSS-owned UI, rather than introducing a new shared dialog abstraction for a single screen.
- Preserve the existing route and visit data source. No runtime mock fallback will be added for the map; the runtime default is direct OpenStreetMap embedding based on the live coordinates available to the component. Test doubles, if needed, stay in unit/e2e assertions only.
- Keep scope limited to visit details. Do not activate or refactor the separate “Pokaż na mapie” placeholder already present in `src/app/pages/portal/umow-wizyte/umow-wizyte.component.html`.

Expected component-level changes:
- `wizyta.component.ts`
  - add `signal(false)` for popup visibility,
  - add a computed `hasCoordinates`,
  - add a computed/sanitized map URL derived from `visit()?.coordinates`,
  - keep `isTelemedicine()` only where it still affects action buttons.
- `wizyta.component.html`
  - wrap the existing facility info item in a coordinate guard,
  - render the map trigger beneath the address,
  - render a conditional popup with dialog semantics, close button, and iframe.
- `wizyta.component.scss`
  - style the map trigger, modal backdrop, dialog shell, responsive iframe area, and close affordance using the existing `--sz-*` design tokens from `src/styles.scss`.

### Task Breakdown

#### Phase 1: Extend visit-detail state for location-aware rendering
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add coordinate-aware computed state | `src/app/pages/portal/wizyta/wizyta.component.ts` | Introduce computed helpers for `hasCoordinates` and the selected visit coordinates, reusing `findVisit(this.id())`. |
| 1.2 | Add popup visibility state and handlers | `src/app/pages/portal/wizyta/wizyta.component.ts` | Add local open/close methods backed by a signal so the popup is shown without route changes. |
| 1.3 | Build OpenStreetMap embed URL | `src/app/pages/portal/wizyta/wizyta.component.ts` | Generate a sanitized iframe URL from `VisitCoordinates`, including a marker and bounding box sized to keep the point visible immediately. |

#### Phase 2: Update the visit details template
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Hide facility block when coordinates are unavailable | `src/app/pages/portal/wizyta/wizyta.component.html` | Replace the unconditional facility/form info item with an `@if` block so visits without coordinates do not render facility name or address. |
| 2.2 | Add the map action under the address | `src/app/pages/portal/wizyta/wizyta.component.html` | Render a button/link-style “Pokaż na mapie” control only when coordinates exist. |
| 2.3 | Add popup markup with embedded map | `src/app/pages/portal/wizyta/wizyta.component.html` | Add a conditional overlay containing dialog title, close button, and an iframe bound to the computed OpenStreetMap URL. |
| 2.4 | Add stable test hooks | `src/app/pages/portal/wizyta/wizyta.component.html` | Add `data-testid` attributes for the facility block, map trigger, dialog, and iframe to simplify targeted tests. |

#### Phase 3: Style the popup and responsive map layout
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Style the inline map action | `src/app/pages/portal/wizyta/wizyta.component.scss` | Extend the existing info-panel styles so the trigger appears as a secondary text action beneath the address. |
| 3.2 | Style the popup overlay | `src/app/pages/portal/wizyta/wizyta.component.scss` | Add backdrop, dialog container, header, close button, and spacing consistent with the `sz-card` visual language. |
| 3.3 | Make the map viewport responsive | `src/app/pages/portal/wizyta/wizyta.component.scss` | Ensure the iframe keeps a usable aspect ratio and fits smaller screens without overflow. |

#### Phase 4: Add feature tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Create unit coverage for coordinate-present flow | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Verify the map trigger renders for a visit like `w1`, opens the popup, and exposes an OpenStreetMap iframe URL containing the visit coordinates. |
| 4.2 | Create unit coverage for coordinate-missing flow | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Verify a visit like `w2` hides the facility block, address, and map trigger entirely. |
| 4.3 | Add e2e coverage for popup behavior | `tests/e2e/wizyta-map.spec.ts` | Navigate directly to `/portal/wizyta/w1`, open and close the popup, and assert the route remains unchanged. |
| 4.4 | Add e2e coverage for hidden-location behavior | `tests/e2e/wizyta-map.spec.ts` | Navigate to `/portal/wizyta/w2` and assert that facility/location UI is absent when coordinates are missing. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/pages/portal/wizyta/wizyta.component.ts` | MODIFY | Add coordinate-aware state, popup visibility, and OpenStreetMap embed URL generation. |
| `src/app/pages/portal/wizyta/wizyta.component.html` | MODIFY | Add conditional facility rendering, the “Pokaż na mapie” action, and popup markup. |
| `src/app/pages/portal/wizyta/wizyta.component.scss` | MODIFY | Add styles for the map trigger and responsive popup overlay. |
| `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | CREATE | Add unit tests for map visibility, popup behavior, and missing-coordinate hiding rules. |
| `tests/e2e/wizyta-map.spec.ts` | CREATE | Add end-to-end coverage for opening/closing the map popup and the no-coordinates case. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes without template or type errors
5. [ ] `ng test` passes with new `WizytaComponent` coverage
6. [ ] `npm run e2e` passes with the new visit-map scenario
7. [ ] Manual check: `/portal/wizyta/w1` shows “Pokaż na mapie”, opens a popup, and displays an OpenStreetMap iframe with a visible marker
8. [ ] Manual check: closing the popup keeps the user on `/portal/wizyta/w1`
9. [ ] Manual check: `/portal/wizyta/w2` does not show facility name, address, or the map action