# SDLC-135: Mozliwosc podania PESEL

## Specification

### Overview
Dodanie na ekranie `Kilka informacji o Tobie` (`/kalkulator/kto-ty`) wyboru sposobu podania danych o dacie urodzenia: ręcznie przez pole `Data urodzenia` albo pośrednio przez numer `PESEL`. Domyślnie aktywny ma być tryb `PESEL`. Dla poprawnego PESEL aplikacja ma lokalnie zwalidować numer, zdekodować z niego datę urodzenia zgodnie ze standardem PESEL, wyświetlić ją w istniejącym polu daty jako wartość tylko do odczytu oraz zapisać w modelu procesu zarówno `dateOfBirth`, jak i sam `pesel`.

### User Stories
- US-001: As a user, I want to choose whether I provide my birth date directly or via PESEL, so that I can use the input method most convenient for me.
  - Given I open `Kilka informacji o Tobie` / When the screen renders / Then the `PESEL` mode is selected by default and only one input mode is active at a time.

- US-002: As a user, I want a valid PESEL to automatically fill my birth date, so that I do not need to enter the same information twice.
  - Given I am in `PESEL` mode / When I enter a valid 11-digit PESEL with a correct checksum and encoded date / Then the `Data urodzenia` field is auto-filled and cannot be edited manually.

- US-003: As a business process, I want to keep using `dateOfBirth` in the existing flow while also persisting PESEL separately, so that downstream steps receive consistent data without changing the current birth-date contract.
  - Given I continue from `Kilka informacji o Tobie` / When the form is submitted / Then the draft stores `personalInfo.dateOfBirth` and, if PESEL mode was used, also stores `personalInfo.pesel`.

- US-004: As a user, I want invalid PESEL input to be rejected clearly, so that I cannot continue with inconsistent data.
  - Given I am in `PESEL` mode / When the PESEL is incomplete, non-numeric, has an invalid checksum, or encodes an impossible date / Then I see a validation message, `Data urodzenia` stays empty, and the next step remains blocked.

### Functional Requirements
- FR-001: Extend the personal-info step to support two mutually exclusive modes: `dateOfBirth` and `pesel`.
- FR-002: For a new empty draft, the default mode on `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` is `pesel`.
- FR-003: The existing manual `Data urodzenia` entry path must remain available and keep the current HTML date input format.
- FR-004: In `PESEL` mode, the UI must show a dedicated PESEL input field and keep the existing `Data urodzenia` field visible as the decoded output.
- FR-005: In `PESEL` mode, the `Data urodzenia` field must be non-editable by the user and reflect only the value decoded from a valid PESEL.
- FR-006: PESEL validation must cover at least: numeric characters only, exact length `11`, valid checksum, and valid encoded birth date including century decoding.
- FR-007: When the PESEL value changes from one valid number to another valid number, the decoded `Data urodzenia` value must update immediately.
- FR-008: When the PESEL input is invalid, the component must clear any previously decoded birth date and show an inline validation message near the PESEL field.
- FR-009: The `next` button must remain disabled until `firstName`, `lastName`, and the active mode’s required birth data are valid.
- FR-010: Switching from `PESEL` mode to manual `Data urodzenia` mode must clear the PESEL value and any date derived from it.
- FR-011: Switching from manual `Data urodzenia` mode to `PESEL` mode must clear the manually entered date.
- FR-012: Persist the new data in `PolicyDraftService` by extending `PolicyDraft.personalInfo` with `pesel` and the selected input mode so the step can hydrate correctly when revisited.
- FR-013: The existing process contract must continue to use `personalInfo.dateOfBirth` as the primary birth-date field for downstream flow logic.
- FR-014: If the user used `PESEL` mode, `personalInfo.pesel` must be saved as a separate value in the draft for downstream use.
- FR-015: The implementation must be fully local to the frontend; no external PESEL validation service may be introduced for runtime behavior.

### Edge Cases
- EC-001: A PESEL with correct length but invalid checksum must be rejected and must not populate `dateOfBirth`.
- EC-002: A PESEL with a valid checksum but impossible encoded date must be rejected and must not populate `dateOfBirth`.
- EC-003: Partial PESEL input must keep the form invalid and must not leave a stale decoded birth date visible.
- EC-004: Returning to `/kalkulator/kto-ty` after saving progress should restore the previously selected mode and matching saved values from `PolicyDraftService`, consistent with the repo’s existing draft-hydration pattern.
- EC-005: Switching modes must remove stale validation errors from the now-inactive mode.
- EC-006: If a valid PESEL is replaced with an invalid one, the previously decoded birth date must be cleared immediately.
- EC-007: Century decoding must support PESEL month offsets correctly, not only `19xx` dates.
- EC-008: Manual `Data urodzenia` entry behavior must remain unchanged when `dateOfBirth` mode is active.

### Success Criteria
- [ ] The `Kilka informacji o Tobie` screen shows a choice between `Data urodzenia` and `PESEL`
- [ ] `PESEL` is the default mode for a new empty draft
- [ ] A valid PESEL auto-fills `Data urodzenia`
- [ ] In `PESEL` mode, `Data urodzenia` cannot be edited manually
- [ ] Invalid PESEL shows a field-level validation message
- [ ] The form cannot advance while active PESEL input is invalid
- [ ] Switching modes clears values from the previously active mode
- [ ] `personalInfo.dateOfBirth` remains populated for downstream flow
- [ ] `personalInfo.pesel` is persisted separately when PESEL mode is used
- [ ] Existing manual birth-date entry still works unchanged

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Implement the change in the existing standalone Angular step component `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts`, following the same repo pattern already used across the wizard: component-local form state initialized from `PolicyDraftService`, then persisted on `next()`.

Relevant codebase patterns to reuse:
- `step-02-personal-info.component.ts` currently keeps primitive local fields (`firstName`, `lastName`, `dateOfBirth`) and patches the shared draft on submit.
- `src/app/core/models/policy-draft.model.ts` is the single source of truth for cross-step state and should carry the new `pesel` and mode fields.
- `src/app/core/services/policy-draft.service.ts` already exposes `patchPersonalInfo(...)`, so persistence can stay within the existing service contract.
- The repo favors small, testable logic units for business rules, e.g. `src/app/core/services/exchange-rate.service.ts` with `exchange-rate.service.spec.ts`; PESEL validation/decoding should follow the same separation by moving the algorithm into a pure utility module rather than embedding it directly in the component.
- Existing Playwright coverage under `tests/e2e` is the right place to verify end-to-end wizard behavior.

Confluence findings:
- No existing PESEL-related service/API was found in the configured `Services` space — proposing local frontend validation/decoding only.
- Domain context found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the wizard/screen-driven flow and supports keeping the change scoped to the `Kilka informacji o Tobie` step.

Key design decisions:
- Add `personalInfo.pesel` and `personalInfo.birthInputMode` to the draft model so the step can preserve state when revisited, matching existing service-backed wizard behavior.
- Keep `personalInfo.dateOfBirth` as the downstream-compatible field regardless of input mode.
- Introduce a pure `pesel` utility for checksum validation and date decoding; this keeps the component focused on UI state and makes century/date edge cases easy to unit test.
- Reuse global `.radio-option` styling from `src/styles.scss` for the mode switch rather than introducing PrimeNG or a new shared component.
- Add a small local SCSS file for read-only state, inline error text, and mode-specific layout because `step-02-personal-info` currently has no dedicated stylesheet.
- Do not modify unrelated later steps unless needed for compilation; persisting the new fields in `PolicyDraftService` satisfies the “save and pass further” requirement in the current architecture.

### Task Breakdown

#### Phase 1: Extend the draft model and isolate PESEL business logic
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Extend personal-info draft state | `src/app/core/models/policy-draft.model.ts` | Add `pesel` and `birthInputMode` to `PolicyDraft['personalInfo']`; update `emptyDraft()` and `seedDraft()` defaults to keep the model consistent. |
| 1.2 | Add PESEL utility module | `src/app/core/utils/pesel.util.ts` | Create pure helpers for digit/length/checksum validation and birth-date decoding with century handling. |
| 1.3 | Cover PESEL rules with unit tests | `src/app/core/utils/pesel.util.spec.ts` | Add focused tests for valid PESELs, checksum failures, non-digit input, invalid dates, and century decoding cases. |

#### Phase 2: Update the “Kilka informacji o Tobie” step
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add mode-aware component state | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Introduce local fields for `birthInputMode`, `pesel`, decoded/validated state, and inline validation message, initialized from the draft. |
| 2.2 | Implement mode switching behavior | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Add methods to switch between manual date and PESEL modes, clearing stale values from the previously active mode exactly as required by the ticket. |
| 2.3 | Implement PESEL-driven date population | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | On PESEL changes, validate via the utility, populate `dateOfBirth` only when valid, clear it when invalid, and keep the `valid` getter mode-aware. |
| 2.4 | Persist expanded personal info | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Update `next()` to patch `firstName`, `lastName`, `dateOfBirth`, `pesel`, and `birthInputMode` into `PolicyDraftService` before navigating. |
| 2.5 | Add UI for mode selection and PESEL field | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | Insert a two-option selector for `Data urodzenia` / `PESEL`, show the PESEL input only in PESEL mode, and keep the date field visible in both modes. |
| 2.6 | Make date field read-only in PESEL mode | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | Bind the existing date input so it becomes non-editable when PESEL mode is active while still displaying the decoded date value. |
| 2.7 | Add inline validation and hints | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | Render a PESEL validation message and a short helper/read-only hint so the UX explains why the date field cannot be edited in PESEL mode. |
| 2.8 | Add step-specific styling | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | Create local styles for mode selector spacing, inline error state, and read-only field appearance; wire the stylesheet through the component metadata. |

#### Phase 3: Verify user behavior end to end
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add E2E coverage for default PESEL mode | `tests/e2e/personal-info-pesel.spec.ts` | Verify `/kalkulator/kto-ty` opens with PESEL mode active, PESEL field visible, and next button blocked until required data is valid. |
| 3.2 | Add E2E coverage for valid PESEL flow | `tests/e2e/personal-info-pesel.spec.ts` | Verify valid PESEL input auto-populates the date field, keeps it non-editable, allows navigation, and preserves data after step submit. |
| 3.3 | Add E2E coverage for invalid/switching flows | `tests/e2e/personal-info-pesel.spec.ts` | Verify invalid PESEL shows an error and blocks submit, and switching between modes clears stale values from the inactive mode. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/policy-draft.model.ts` | MODIFY | Extend `personalInfo` with `pesel` and input-mode state; update draft factories. |
| `src/app/core/utils/pesel.util.ts` | CREATE | Centralize PESEL validation and birth-date decoding logic. |
| `src/app/core/utils/pesel.util.spec.ts` | CREATE | Unit-test checksum, date decoding, and invalid input scenarios. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | MODIFY | Add mode handling, PESEL decoding, validation state, and expanded draft persistence. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | MODIFY | Add mode selector, PESEL field, read-only date behavior, and validation messaging. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | CREATE | Add local styling for selector, errors, and read-only state. |
| `tests/e2e/personal-info-pesel.spec.ts` | CREATE | End-to-end verification of PESEL mode, manual mode, validation, and clearing behavior. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` succeeds without type errors after extending `PolicyDraft['personalInfo']`
5. [ ] `npm run test` covers the PESEL utility edge cases:
   - valid checksum
   - invalid checksum
   - non-numeric input
   - invalid encoded date
   - century decoding
6. [ ] `npm run e2e` verifies:
   - default PESEL mode on `/kalkulator/kto-ty`
   - valid PESEL auto-fills date of birth
   - invalid PESEL blocks navigation
   - switching modes clears stale values
   - manual date-of-birth mode still works
7. [ ] Manual check confirms:
   - the date field is non-editable in PESEL mode
   - the date field remains editable in manual mode
   - saved step state rehydrates correctly when navigating back to `kalkulator/kto-ty`