# SDLC-114: Na liście procesów przesuń kolumnę PH za eMail PH tylko na ekranie listy procesow

## Specification

### Overview
Zmiana dotyczy wyłącznie warstwy prezentacji na ekranie listy procesów. Obecnie tabela w `ProcessListComponent` renderuje kolumnę **PH** przed kolumną **Email PH**. Zadanie polega na odwróceniu tej kolejności tak, aby **PH** było wyświetlane bezpośrednio po **Email PH**, bez zmian w danych, logice filtrowania, sortowania, paginacji ani na innych ekranach.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę PH bezpośrednio po kolumnie Email PH, so that układ informacji odpowiada oczekiwanej kolejności prezentacji.
  - Given użytkownik otwiera ekran listy procesów / When tabela zostanie wyrenderowana / Then kolumna Email PH jest widoczna przed kolumną PH, a wartości w obu kolumnach pozostają bez zmian

### Functional Requirements
- FR-001: Na ekranie listy procesów kolejność nagłówków tabeli musi zostać zmieniona tak, aby `Email PH` poprzedzał `PH`.
- FR-002: W każdym wierszu tabeli komórka z `item.phEmail` musi być renderowana bezpośrednio przed komórką z `item.phName`.
- FR-003: Zmiana ma dotyczyć tylko `src/app/features/processes/process-list/process-list.component.ts`; inne ekrany i widoki nie mogą zmienić kolejności kolumn.
- FR-004: Nazwy kolumn, mapowanie danych (`phEmail`, `phName`) oraz logika pobierania listy procesów pozostają bez zmian.
- FR-005: Sortowanie, filtrowanie, paginacja, ładowanie danych, stany pusty/błąd oraz linki do szczegółów muszą działać tak jak przed zmianą.
- FR-006: Testy jednostkowe i E2E muszą zostać zaktualizowane tak, aby weryfikowały nową kolejność `Email PH` -> `PH`.

### Edge Cases
- EC-001: Na breakpointach responsywnych, gdzie część kolumn jest ukrywana przez style komponentu, `Email PH` i `PH` nadal powinny zachować właściwą kolejność DOM/renderingu, jeśli obie są widoczne.
- EC-002: Jeżeli tabela nie zawiera wyników albo występuje błąd ładowania, zmiana kolejności kolumn nie może wpływać na rendering stanów `empty-state` i `error-alert`.
- EC-003: Zmiana nie może przypadkowo odwrócić wartości danych, tj. email ma nadal renderować `item.phEmail`, a nazwa PH ma nadal renderować `item.phName`.

### Success Criteria
- [ ] Na ekranie listy procesów nagłówek `PH` jest widoczny bezpośrednio po nagłówku `Email PH`
- [ ] W każdym wierszu wartość `phName` jest renderowana bezpośrednio po `phEmail`
- [ ] Filtrowanie, sortowanie, paginacja i przejście do szczegółów działają bez regresji
- [ ] Zmiana nie wpływa na inne ekrany ani warstwę danych
- [ ] Testy unit i E2E potwierdzają nową kolejność kolumn

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Zmiana powinna zostać wykonana lokalnie w standalone komponencie `src/app/features/processes/process-list/process-list.component.ts`, który zawiera inline template i inline styles. Tabela listy procesów jest zdefiniowana bez osobnego partiala ani modelu kolumn, więc najbezpieczniejsze i najmniejsze zakresowo rozwiązanie to bezpośrednie przestawienie dwóch sąsiadujących elementów:

- w `<thead>` zamienić miejscami nagłówki `PH` i `Email PH`
- w `<tbody>` zamienić miejscami komórki `{{ item.phName }}` i `{{ item.phEmail }}`

Bez zmian pozostają:
- `ProcessListService` w `src/app/features/processes/process-list/process-list.service.ts`
- modele w `process-list.models.ts`
- mechanizmy filtrowania, sortowania, paginacji i obsługi błędów

Plan reuse'uje istniejącą dokumentację funkcjonalną listy procesów z Confluence:
- [F02 - Process List & Filtering (ZRD)](https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD) — jako referencję kontekstu ekranu listy procesów i potwierdzenie, że jest to główny widok do przeglądania, filtrowania, sortowania i paginacji procesów
- [F02 — Process List & Filtering (ZRD) — E2E Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios) — jako referencję zachowania ekranu, które nie może zostać naruszone przez zmianę prezentacyjną

No relevant domain guidance was found in the configured `SDLC` Confluence space.

W repo istnieją już testy kodujące obecną, niepożądaną kolejność:
- `process-list.component.spec.ts` sprawdza dziś `PH` przed `Email PH`
- `tests/e2e/process-list-column-order.spec.ts` ma stałą listę nagłówków z `PH` przed `Email PH`

Dlatego implementacja musi objąć także aktualizację tych asercji, inaczej zmiana UI spowoduje zamierzone złamanie testów. Ze względu na istniejący styl projektu należy utrzymać obecny wzorzec:
- testy jednostkowe oparte o DOM queries z `fixture.nativeElement`
- testy E2E w Playwright z `data-testid` i walidacją `thead th` / `tbody td`

### Task Breakdown

#### Phase 1: Update process list column rendering
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Swap header order in process list table | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić miejscami nagłówki `PH` i `Email PH` w sekcji `<thead>`, zachowując istniejące klasy CSS kolumn |
| 1.2 | Swap row cell order for PH data | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić miejscami komórki `{{ item.phEmail }}` i `{{ item.phName }}` w `<tbody>`, bez zmiany mapowania danych |
| 1.3 | Verify responsive styling compatibility | `src/app/features/processes/process-list/process-list.component.ts` | Sprawdzić, czy istniejące reguły `.col-phEmail` i `.col-phName` nadal poprawnie działają po zmianie kolejności; ewentualnie zaktualizować komentarze stylów, bez zmiany zachowania |

#### Phase 2: Update unit coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update header-order unit test | `src/app/features/processes/process-list/process-list.component.spec.ts` | Zmienić asercję tak, aby test wymagał `Email PH` bezpośrednio przed `PH` |
| 2.2 | Update row-order unit test | `src/app/features/processes/process-list/process-list.component.spec.ts` | Zmienić test wiersza tak, aby `phEmail` występował bezpośrednio przed `phName` |
| 2.3 | Keep regression coverage intact | `src/app/features/processes/process-list/process-list.component.spec.ts` | Pozostawić i uruchomić istniejące testy filtrowania, sortowania, paginacji, błędów i linków do szczegółów jako regresję |

#### Phase 3: Update E2E expectations
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Update expected header sequence | `tests/e2e/process-list-column-order.spec.ts` | Zmienić oczekiwaną tablicę nagłówków na sekwencję z `Email PH` przed `PH` |
| 3.2 | Update first-row column assertions | `tests/e2e/process-list-column-order.spec.ts` | Zmienić indeksowe asercje Playwright tak, aby w pierwszym wierszu email PH poprzedzał PH |
| 3.3 | Re-run behavior regression in same E2E suite | `tests/e2e/process-list-column-order.spec.ts` | Zweryfikować, że druga część scenariusza nadal potwierdza sortowanie, filtrowanie, paginację i nawigację do detalu |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Przestawienie kolejności kolumn `Email PH` i `PH` w nagłówku oraz wierszach tabeli |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Aktualizacja testów jednostkowych do nowej kolejności kolumn |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Aktualizacja testu Playwright do nowej kolejności nagłówków i komórek |

### Verification Steps
1. [ ] Build succeeds: `npm run build`
2. [ ] Unit tests pass, w szczególności `src/app/features/processes/process-list/process-list.component.spec.ts`
3. [ ] E2E tests pass, w szczególności `tests/e2e/process-list-column-order.spec.ts`
4. [ ] New tests cover requirements: nagłówki i wartości wiersza potwierdzają kolejność `Email PH` -> `PH`
5. [ ] Manual smoke check na `/processes` potwierdza brak regresji w filtrowaniu, sortowaniu, paginacji i linkach `Szczegóły`