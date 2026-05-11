# SDLC-113: Na liście procesów przesuń kolumnę PH za eMail PH tylko na ekranie listy procesow

## Specification

### Overview
Zadanie wprowadza wyłącznie zmianę prezentacyjną na ekranie listy procesów pod ścieżką `/processes`: kolumna `PH` ma zostać przeniesiona bezpośrednio za kolumnę `Email PH`. Zakres nie obejmuje zmian w modelu danych, serwisach, API, logice filtrowania, sortowania, paginacji ani innych ekranach.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę `PH` bezpośrednio po kolumnie `Email PH`, so that powiązane dane kontaktowe są czytane w oczekiwanej kolejności.
  - Given użytkownik otwiera ekran listy procesów / When tabela zostanie wyrenderowana / Then kolumna `PH` jest widoczna bezpośrednio za `Email PH`

### Functional Requirements
- FR-001: Na ekranie `/processes` nagłówek kolumny `Email PH` musi być renderowany bezpośrednio przed nagłówkiem `PH`.
- FR-002: W każdym wierszu tabeli komórka z `item.phEmail` musi być renderowana bezpośrednio przed komórką z `item.phName`.
- FR-003: Kolumna `PH` nadal musi prezentować `item.phName`, a kolumna `Email PH` nadal musi prezentować `item.phEmail`.
- FR-004: Zmiana ma dotyczyć wyłącznie ekranu listy procesów zaimplementowanego w `src/app/features/processes/process-list/process-list.component.ts`.
- FR-005: Pozostałe kolumny tabeli muszą zachować obecną kolejność i etykiety.
- FR-006: Filtrowanie, sortowanie, paginacja, stan pusty, stan błędu i linki `Szczegóły` muszą działać bez zmian.
- FR-007: Testy jednostkowe i E2E muszą potwierdzać nową kolejność kolumn.

### Edge Cases
- EC-001: Zamiana kolejności nie może zamienić zawartości kolumn; tylko pozycja `Email PH` i `PH` ma się zmienić.
- EC-002: Zmiana nie może wpłynąć na indeksy i działanie kolumn sąsiednich, w szczególności `Segment` i `Status`.
- EC-003: Przy pustej liście lub błędzie ładowania brak tabeli nie może powodować regresji w renderowaniu pozostałych sekcji komponentu.
- EC-004: Testy oparte na kolejności nagłówków i komórek muszą zostać zaktualizowane, aby nie raportowały fałszywej regresji.

### Success Criteria
- [ ] Na `/processes` kolumna `PH` jest widoczna bezpośrednio za `Email PH`
- [ ] Wartości `phEmail` i `phName` pozostają przypisane do właściwych rekordów
- [ ] Żadna inna kolumna nie zmienia położenia
- [ ] Filtrowanie, sortowanie, paginacja i przejście do szczegółów nadal działają
- [ ] Test jednostkowy i test E2E potwierdzają nową kolejność kolumn

### Open Questions

---

## Implementation Plan

### Technical Approach
Repozytorium to Angular 17 ze standalone components i inline templates. Ekran listy procesów jest utrzymywany bezpośrednio w `src/app/features/processes/process-list/process-list.component.ts`, więc implementacja powinna ograniczyć się do przestawienia dwóch sąsiadujących kolumn w sekcjach `<thead>` i `<tbody>` tego komponentu.

Aktualny kod renderuje kolejność:
`... Segment | PH | Email PH | Status ...`

Dla SDLC-113 trzeba zmienić ją na:
`... Segment | Email PH | PH | Status ...`

Bez zmiany:
- `ProcessListItem` w `src/app/features/processes/process-list/process-list.models.ts`
- danych mockowych z `src/app/features/processes/process-list/process-list.mock.ts`
- logiki ładowania i filtrowania z `src/app/features/processes/process-list/process-list.service.ts`

Istniejące wzorce testowe w repo:
- test jednostkowy `src/app/features/processes/process-list/process-list.component.spec.ts` sprawdza kolejność nagłówków i kolejność komórek w wierszu,
- test Playwright `tests/e2e/process-list-column-order.spec.ts` sprawdza pełną listę nagłówków oraz podstawowe regresje funkcjonalne.

Wyniki Step 2 / Confluence:
- W przestrzeni `Services` znaleziono opis istniejącej funkcji listy procesów: [F02 - Process List & Filtering (ZRD)](https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD)
- W przestrzeni `Services` znaleziono scenariusze testowe dla listy procesów: [F02 — Process List & Filtering (ZRD) — E2E Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios)
- Nie znaleziono istniejącej usługi REST/API/OpenAPI do wykorzystania dla tego ticketu w `Services`; zadanie pozostaje zmianą wyłącznie prezentacyjną.
- W przestrzeni `SDLC` nie znaleziono relewantnych ADR-ów ani konwencji ograniczających implementację.

Podejście implementacyjne:
1. Przestawić tylko kolejność renderowania `Email PH` i `PH` w tabeli komponentu.
2. Zachować obecne bindingi danych: `item.phEmail` i `item.phName`.
3. Zaktualizować test jednostkowy do nowej relacji `Email PH -> PH`.
4. Zaktualizować test E2E i jego oczekiwaną listę nagłówków.
5. Nie modyfikować serwisu, modeli ani mocków, bo Jira i Confluence wskazują zmianę wyłącznie w UI.

### Task Breakdown

#### Phase 1: Aktualizacja tabeli listy procesów
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zmienić kolejność nagłówków | `src/app/features/processes/process-list/process-list.component.ts` | Przenieść `<th scope="col">PH</th>` za `<th scope="col">Email PH</th>` w inline template. |
| 1.2 | Zmienić kolejność komórek danych | `src/app/features/processes/process-list/process-list.component.ts` | Przenieść `<td>{{ item.phName }}</td>` za `<td>{{ item.phEmail }}</td>` w każdym wierszu tabeli. |
| 1.3 | Zachować pozostały układ tabeli | `src/app/features/processes/process-list/process-list.component.ts` | Zweryfikować, że `Segment`, `Status`, daty, dokumenty, uwagi, obserwowane i akcje pozostają bez zmian. |

#### Phase 2: Aktualizacja testów jednostkowych
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zmienić asercję kolejności nagłówków | `src/app/features/processes/process-list/process-list.component.spec.ts` | Zaktualizować test tak, aby sprawdzał, że indeks `PH` jest równy indeksowi `Email PH + 1`. |
| 2.2 | Zmienić asercję kolejności danych w wierszu | `src/app/features/processes/process-list/process-list.component.spec.ts` | Potwierdzić, że `firstItem.phEmail` występuje bezpośrednio przed `firstItem.phName` i że wartości pozostają poprawne. |
| 2.3 | Utrzymać regresję zachowań listy | `src/app/features/processes/process-list/process-list.component.spec.ts` | Pozostawić istniejące testy filtrowania, czyszczenia filtrów, sortowania, paginacji, empty state, error state i nawigacji. |

#### Phase 3: Aktualizacja testów E2E
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Zmienić oczekiwaną listę nagłówków | `tests/e2e/process-list-column-order.spec.ts` | Zaktualizować tablicę `headers`, aby zawierała `Email PH` przed `PH`. |
| 3.2 | Zmienić asercję indeksów kolumn | `tests/e2e/process-list-column-order.spec.ts` | Dostosować porównanie indeksów tak, by `PH` było bezpośrednio po `Email PH`. |
| 3.3 | Dostosować walidację pierwszego wiersza | `tests/e2e/process-list-column-order.spec.ts` | Sprawdzić, że komórka `Email PH` zawiera adres e-mail, a następna komórka `PH` zawiera nazwę opiekuna. |
| 3.4 | Zachować test regresji funkcjonalnej | `tests/e2e/process-list-column-order.spec.ts` | Utrzymać istniejące kroki dla sortowania, filtrowania, czyszczenia filtrów, paginacji i przejścia do szczegółów. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Zmiana kolejności renderowania kolumn `Email PH` i `PH` w nagłówku i wierszach tabeli. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Aktualizacja testów jednostkowych do nowej kolejności `Email PH` przed `PH`. |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Aktualizacja scenariusza Playwright i oczekiwanej kolejności kolumn. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Uruchomić `npm run build`
5. [ ] Uruchomić `ng test --watch=false`
6. [ ] Uruchomić `npx playwright test tests/e2e/process-list-column-order.spec.ts`
7. [ ] Otworzyć `/processes` i potwierdzić wizualnie kolejność `Email PH` -> `PH`
8. [ ] Sprawdzić pierwszy wiersz tabeli i potwierdzić, że po komórce z adresem e-mail występuje komórka z nazwą PH
9. [ ] Potwierdzić brak regresji dla filtrowania, sortowania, paginacji i linku `Szczegóły`