# SDLC-115: Na liście procesów przesuń kolumnę PH za eMail PH tylko na ekranie listy procesow

## Specification

### Overview
Zadanie wprowadza wyłącznie zmianę prezentacyjną na ekranie listy procesów `/processes`: kolumna `PH` ma zostać przeniesiona bezpośrednio za kolumnę `Email PH`. Zakres nie obejmuje zmian danych, modeli, API, eksportu danych ani zapisanych widoków użytkownika.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę `PH` bezpośrednio po `Email PH`, so that układ tabeli odpowiada oczekiwanej kolejności biznesowej.
  - Given użytkownik otwiera ekran listy procesów / When tabela zostanie wyrenderowana / Then kolumna `PH` jest wyświetlana bezpośrednio za kolumną `Email PH`
- US-002: As a użytkownik operacyjny, I want aby dane w kolumnach `PH` i `Email PH` pozostały bez zmian, so that zmienia się tylko pozycja wizualna kolumn.
  - Given lista procesów zawiera dane `phName` i `phEmail` / When tabela zostanie wyrenderowana po zmianie / Then `phEmail` nadal wyświetla e-mail PH, a `phName` nadal wyświetla nazwę PH
- US-003: As a użytkownik listy procesów, I want aby filtrowanie, sortowanie, paginacja i przejście do szczegółów działały jak dotąd, so that zmiana układu kolumn nie powoduje regresji.
  - Given użytkownik korzysta z istniejących funkcji listy / When wdrożona zostanie nowa kolejność kolumn / Then pozostałe zachowania ekranu pozostają bez zmian

### Functional Requirements
- FR-001: Na ekranie `/processes` nagłówek `PH` musi być renderowany bezpośrednio po nagłówku `Email PH`.
- FR-002: W każdym wierszu tabeli komórka z `item.phName` musi być renderowana bezpośrednio po komórce z `item.phEmail`.
- FR-003: Zmiana ma dotyczyć wyłącznie ekranu listy procesów zaimplementowanego w `src/app/features/processes/process-list/process-list.component.ts`.
- FR-004: Nazwy kolumn i wartości danych pozostają bez zmian; zmienia się wyłącznie kolejność renderowania kolumn `Email PH` i `PH`.
- FR-005: Kolejność wszystkich pozostałych kolumn musi pozostać bez zmian.
- FR-006: Logika filtrowania, sortowania, paginacji, stanów loading/empty/error oraz linków `Szczegóły` nie może zostać zmieniona.
- FR-007: Eksport danych nie może zostać zmieniony przez to zadanie.
- FR-008: Zapisane widoki i ustawienia użytkownika nie mogą zostać zmodyfikowane przez to zadanie.
- FR-009: Testy jednostkowe i E2E muszą zostać zaktualizowane tak, aby weryfikowały nową kolejność kolumn.

### Edge Cases
- EC-001: Zmiana kolejności nie może spowodować zamiany znaczenia kolumn; `Email PH` nadal pokazuje `item.phEmail`, a `PH` nadal pokazuje `item.phName`.
- EC-002: Przy widoku mobilnym i regułach responsywnych obie kolumny muszą pozostać widoczne i sąsiadujące we właściwej kolejności.
- EC-003: Testy oparte na indeksach kolumn muszą zostać zaktualizowane, aby nie raportowały fałszywej regresji.
- EC-004: Przy pustych wynikach i błędzie ładowania brak tabeli nie może wpływać na obsługę stanów `empty` i `error`.
- EC-005: Ponieważ użytkownik nie może samodzielnie zmieniać kolejności kolumn, jedynym źródłem kolejności pozostaje statyczny template komponentu.

### Success Criteria
- [ ] Na `/processes` kolumna `PH` jest widoczna bezpośrednio za `Email PH`
- [ ] Dane w kolumnach `PH` i `Email PH` pozostają identyczne jak przed zmianą
- [ ] Żadna inna kolumna na ekranie listy procesów nie zmienia położenia
- [ ] Filtrowanie, sortowanie, paginacja i przejście do szczegółów działają bez regresji
- [ ] Eksport danych zachowuje dotychczasową kolejność kolumn
- [ ] Testy jednostkowe i E2E przechodzą z nową kolejnością kolumn

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 17 oparta o standalone components. Ekran listy procesów jest zdefiniowany inline w `src/app/features/processes/process-list/process-list.component.ts`, a jego zachowanie jest pokryte testami w:
- `src/app/features/processes/process-list/process-list.component.spec.ts`
- `tests/e2e/process-list-column-order.spec.ts`

Aktualny kod renderuje kolumnę `PH` przed `Email PH`, więc implementacja SDLC-115 będzie polegała na zamianie kolejności dwóch sąsiadujących kolumn w sekcjach `<thead>` i `<tbody>` tego komponentu, bez zmiany modeli, mocków i serwisu.

Dokumentacja znaleziona w Confluence będzie wykorzystana jako kontekst funkcjonalny dla ekranu listy procesów:
- [F02 - Process List & Filtering (ZRD)](https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD)
- [F02 — Process List & Filtering (ZRD) — E2E Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios)

Reuse:
- Plan reuse existing process list screen behavior and test intent described by the above Confluence pages, but only at the UI layout level.
- No existing service/API integration relevant to this ticket was found in the `Services` space beyond the functional documentation above, so no service reuse or integration changes are required.
- No relevant ADRs or frontend conventions were found in the configured `SDLC` domain space.

Codebase patterns to follow:
- Keep changes localized to the inline template in `ProcessListComponent`.
- Preserve existing property bindings (`item.phEmail`, `item.phName`) and CSS class naming (`col-phEmail`, `col-phName`).
- Update the existing Jasmine and Playwright tests rather than creating parallel alternative tests.
- Do not modify `process-list.service.ts` or `process-list.mock.ts`, because column order is not controlled by data access logic.

### Task Breakdown

#### Phase 1: Update process list column rendering
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Confirm current column order source | `src/app/features/processes/process-list/process-list.component.ts` | Verify that the order is currently hardcoded in the inline table template and not derived from configuration. |
| 1.2 | Reorder table headers | `src/app/features/processes/process-list/process-list.component.ts` | Move the `<th>` for `PH` so it appears immediately after the `<th>` for `Email PH`, leaving all other headers in place. |
| 1.3 | Reorder row cells | `src/app/features/processes/process-list/process-list.component.ts` | Move the `<td class="col-phName">{{ item.phName }}</td>` cell to render immediately after `<td class="col-phEmail">{{ item.phEmail }}</td>`. |
| 1.4 | Preserve responsive behavior | `src/app/features/processes/process-list/process-list.component.ts` | Verify that existing responsive CSS for `.col-phEmail` and `.col-phName` still works without selector changes. |

#### Phase 2: Update unit test coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Adjust header-order assertion | `src/app/features/processes/process-list/process-list.component.spec.ts` | Update the unit test so it asserts that `PH` is rendered immediately after `Email PH`. |
| 2.2 | Adjust row-order assertion | `src/app/features/processes/process-list/process-list.component.spec.ts` | Update the row-value test to assert that `phEmail` comes immediately before `phName` in each rendered row while preserving value correctness. |
| 2.3 | Keep existing regression checks intact | `src/app/features/processes/process-list/process-list.component.spec.ts` | Ensure filtering, sorting, pagination, empty-state, error-state and detail-link tests remain valid after the column reordering. |

#### Phase 3: Update E2E regression scenario
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Update E2E scenario title and expectation | `tests/e2e/process-list-column-order.spec.ts` | Change the scenario wording and expected header list so `Email PH` precedes `PH`. |
| 3.2 | Update column index assertions | `tests/e2e/process-list-column-order.spec.ts` | Recalculate `phColumnIndex` and `phEmailColumnIndex` assertions to verify `PH` is directly after `Email PH`. |
| 3.3 | Preserve functional smoke coverage | `tests/e2e/process-list-column-order.spec.ts` | Keep the existing sorting, filtering, pagination and detail-navigation checks unchanged to confirm no regressions. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Reorder `PH` and `Email PH` columns in the process list table template. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Update unit tests to assert the new `Email PH` → `PH` order. |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Update Playwright expectations and assertions for the new column order. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Run `npm run build` successfully
5. [ ] Run `ng test --watch=false` and confirm `ProcessListComponent` specs pass
6. [ ] Run `npx playwright test tests/e2e/process-list-column-order.spec.ts`
7. [ ] Open `/processes` manually and confirm `Email PH` is immediately followed by `PH`
8. [ ] Verify the first rendered row shows the email cell before the PH-name cell
9. [ ] Verify sorting, filtering, pagination and detail navigation still work
10. [ ] Verify no files related to export behavior were changed