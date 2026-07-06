# SDLC-145: Mapka

## Specification

### Overview
Dodanie na ekranie szczegółów wizyty (`/portal/wizyta/:id`) akcji „Pokaż na mapie” w sekcji adresu placówki. Akcja ma otwierać modal/popup w obrębie aplikacji, bez zmiany trasy i bez przekierowania do zewnętrznego serwisu, z osadzoną mapą OpenStreetMap, znacznikiem lokalizacji placówki oraz podstawowymi danymi identyfikującymi placówkę.

### User Stories
- US-001: As a patient, I want to open the facility location from visit details, so that I can quickly identify where the visit takes place.
  - Given a visit with valid facility coordinates / When I click „Pokaż na mapie” / Then an in-app modal opens with the facility location centered on an OpenStreetMap map
- US-002: As a patient, I want to confirm that the shown map belongs to the correct facility, so that I do not confuse locations.
  - Given the map modal is open / When I review its content / Then I see at least the facility name and full address next to the map
- US-003: As a patient, I want the map feature hidden when location data is missing, so that I do not see a broken action.
  - Given a visit without valid mappable location / When I open visit details / Then the „Pokaż na mapie” action is not rendered

### Functional Requirements
- FR-001: Add a visible „Pokaż na mapie” action in `src/app/pages/portal/wizyta/wizyta.component.html` directly under the facility address block.
- FR-002: Render the action only when the current visit has valid geographic coordinates for the facility.
- FR-003: Opening the action must show an in-app modal/dialog without route navigation or page reload.
- FR-004: The modal must embed an OpenStreetMap view centered on the facility coordinates.
- FR-005: The map must show a single marker for the facility location.
- FR-006: The modal must display at least the facility name and full address from the visit data.
- FR-007: The modal must support closing via an explicit close control and return the user to the same visit details state.
- FR-008: The implementation must remain responsive for desktop and mobile widths.
- FR-009: The feature must not introduce redirects or “open in external maps” links.
- FR-010: The current not-found visit state and telemedicine visit behavior must remain unchanged.

### Edge Cases
- EC-001: Telemedicine visits with no physical coordinates must not show the map action.
- EC-002: Visits with malformed or out-of-range coordinates must behave like missing location data and hide the action.
- EC-003: Reused facility addresses across multiple visits must still open the modal for the current visit only.
- EC-004: If the visit is not found, the existing empty/not-found state must render with no modal markup or map action.
- EC-005: Long facility names or addresses must wrap correctly in the modal on small screens.
- EC-006: Closing the modal must not reset scroll, navigate away, or change the visit route.

### Success Criteria
- [ ] Physical visits with valid coordinates show „Pokaż na mapie” on the visit details page
- [ ] Clicking the action opens an in-app modal with an OpenStreetMap embed and marker
- [ ] The modal shows facility name and full address
- [ ] Telemedicine or non-mappable visits do not show the action
- [ ] The feature works on desktop and mobile layouts
- [ ] Unit and e2e coverage verify visible, hidden, open, and close flows

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The repo is a standalone Angular 21 app with colocated page templates/styles, signal-based local state, and static portal mock data:
- routing is lazy standalone in `src/app/app.routes.ts`
- portal pages use `PortalLayoutComponent`
- state patterns use `input`, `computed`, and `signal` as seen in `src/app/pages/portal/wizyta/wizyta.component.ts` and `src/app/pages/portal/opieka/opieka.component.ts`
- shared visual tokens and utility classes live in `src/styles.scss`

Confluence discovery produced no relevant reusable map/location service or visit-details API in `Services`, and no relevant ADR/convention pages in `SDLC`. Therefore:
- No existing map/location service found in Services space — proposing a direct client-side OpenStreetMap embed for this screen.
- No relevant frontend/domain guidance found in SDLC space — following existing repository patterns instead.

Because this repo is currently a lightweight mock portal backed by `src/app/pages/portal/visits.data.ts`, the lowest-risk implementation is:
1. extend the `Visit` model with optional facility coordinates
2. compute map availability from those coordinates
3. open a local modal from `WizytaComponent`
4. render a real OpenStreetMap runtime embed URL, not a mock JSON/file fallback

Recommended runtime map URL shape:
- `https://www.openstreetmap.org/export/embed.html?bbox={minLng},{minLat},{maxLng},{maxLat}&layer=mapnik&marker={lat},{lng}`

This approach fits the existing app better than adding a full mapping library:
- no new heavy UI pattern beyond one modal
- no Leaflet marker asset handling
- no extra route/state management
- real OSM map at runtime while still keeping the feature inside the app

Implementation details:
- add `location?: { lat: number; lng: number }` to the `Visit` interface in `src/app/pages/portal/visits.data.ts`
- populate coordinates for stacjonarne/diagnostyczne visits and leave telemedicine visits without location
- in `WizytaComponent`, add:
  - `isMapOpen = signal(false)`
  - `canShowMap = computed(...)`
  - `mapEmbedUrl = computed(...)`
  - open/close methods
- place the action beneath `v.address` in the existing placówka info item
- render modal markup only when open, with:
  - `role="dialog"`
  - `aria-modal="true"`
  - visible close button
  - map iframe
  - facility summary block
- keep assertions in tests focused on modal visibility, copy, and iframe URL presence, not external tile loading, so CI remains stable

### Task Breakdown

#### Phase 1: Extend visit data with mappable location
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add facility coordinate model | `src/app/pages/portal/visits.data.ts` | Extend `Visit` with optional `location` object containing `lat` and `lng`. |
| 1.2 | Populate mock visit coordinates | `src/app/pages/portal/visits.data.ts` | Add valid coordinates for existing physical facilities (`w1`, `w3`, `w4`), leave telemedicine visit (`w2`) without location so the hide-action path is covered by real app data. |

#### Phase 2: Add map action and modal to visit details
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add local modal state and URL computation | `src/app/pages/portal/wizyta/wizyta.component.ts` | Introduce `signal`/`computed` state for map visibility, coordinate validation, and OSM embed URL generation from visit coordinates. |
| 2.2 | Render the „Pokaż na mapie” action | `src/app/pages/portal/wizyta/wizyta.component.html` | Add the action directly under the address block, gated by `canShowMap()`. |
| 2.3 | Render modal content | `src/app/pages/portal/wizyta/wizyta.component.html` | Add modal/backdrop markup with embedded OpenStreetMap iframe, facility name, and full address. Include stable selectors/test ids for tests. |
| 2.4 | Style responsive dialog | `src/app/pages/portal/wizyta/wizyta.component.scss` | Add styles for the link/button, overlay, dialog container, map area, info panel, and mobile stacking behavior using the existing `sz-*` look and design tokens. |
| 2.5 | Preserve current page behavior | `src/app/pages/portal/wizyta/wizyta.component.ts`, `src/app/pages/portal/wizyta/wizyta.component.html` | Ensure modal opening does not affect routing, not-found rendering, or existing visit action panel behavior. |

#### Phase 3: Add automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add unit coverage for visit map behavior | `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | Create tests for: action visible for `w1`, action hidden for `w2`, modal opens with facility info, modal closes cleanly. |
| 3.2 | Add e2e regression for the UI flow | `tests/e2e/visit-map.spec.ts` | Add Playwright coverage for opening the modal on `/portal/wizyta/w1` and verifying the action is absent on `/portal/wizyta/w2`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/pages/portal/visits.data.ts` | MODIFY | Add optional facility coordinates and populate existing physical visit entries |
| `src/app/pages/portal/wizyta/wizyta.component.ts` | MODIFY | Add modal state, coordinate validation, and OSM embed URL computation |
| `src/app/pages/portal/wizyta/wizyta.component.html` | MODIFY | Add „Pokaż na mapie” action and in-app modal markup |
| `src/app/pages/portal/wizyta/wizyta.component.scss` | MODIFY | Style the map action and responsive modal |
| `src/app/pages/portal/wizyta/wizyta.component.spec.ts` | CREATE | Unit tests for visible/hidden/open/close map behavior |
| `tests/e2e/visit-map.spec.ts` | CREATE | End-to-end coverage for the visit-details map flow |

### Verification Steps
1. [ ] `npm run build` succeeds
2. [ ] `npm run test` passes, including the new `WizytaComponent` spec
3. [ ] `npm run e2e` passes, including the new visit map scenario
4. [ ] Manual check on `/portal/wizyta/w1` confirms modal opens with map, marker, facility name, and address
5. [ ] Manual check on `/portal/wizyta/w2` confirms „Pokaż na mapie” is not rendered