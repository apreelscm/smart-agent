# SDLC-112: Na liście procesów przesuń kolumnę PH przed eMail PH tylko na ekranie listy procesow

## Specification

### Overview
Zadanie przywraca oczekiwaną kolejność prezentacji dwóch istniejących kolumn na ekranie listy procesów pod ścieżką `/processes`: kolumna `PH` ma być wyświetlana bezpośrednio przed kolumną `Email PH`. Zakres biznesowy z Jira obejmuje także eksporty, wydruki i raporty oparte o listę procesów, ale w aktualnym repozytorium nie znaleziono implementacji tych funkcji, więc plan dla tego repo obejmuje ekran listy procesów oraz testy regresyjne.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę `PH` przed `eMail PH`, so that układ danych jest zgodny z oczekiwanym porządkiem biznesowym.
  - Given użytkownik otwiera ekran listy procesów / When tabela zostanie wyrenderowana / Then kolumna `PH` jest bezpośrednio przed kolumną `Email PH`
- US-002: As a użytkownik korzystający z eksportów, wydruków i raportów opartych o listę procesów, I want zachowaną tę samą kolejność kolumn, so that prezentacja danych jest spójna między kanałami.
  - Given istnieje eksport, wydruk lub raport oparty o listę procesów / When dane zostaną wygenerowane / Then `PH` jest prezentowane przed `eMail PH`
- US-003: As a użytkownik operacyjny, I want aby zmiana nie wpływała na filtrowanie, sortowanie, paginację i nawigację, so that mogę nadal pracować bez regresji funkcjonalnych.
  - Given użytkownik korzysta z istniejących funkcji listy / When kolejność kolumn zostanie zmieniona / Then pozostałe zachowania pozostają bez zmian

### Functional Requirements
- FR-001: Na ekranie `/processes` nagłówek kolumny `PH` musi być renderowany bezpośrednio przed nagłówkiem `Email PH`.
- FR-002: W każdym wierszu tabeli komórka z `item.phName` musi być renderowana bezpośrednio przed komórką z `item.phEmail`.
- FR-003: Nazwy kolumn, mapowanie danych, sortowanie, filtrowanie, paginacja, stan pusty, stan błędu i link `Szczegóły` muszą pozostać bez zmian.
- FR-004: Pozostałe kolumny muszą zachować obecną kolejność; zmienia się wyłącznie relacja `PH` ↔ `Email PH`.
- FR-005: Eksporty, wydruki i raporty oparte o listę procesów powinny stosować tę samą kolejność `PH` przed `eMail PH`, ale w bieżącym repozytorium nie znaleziono ich implementacji.
- FR-006: Testy jednostkowe muszą potwierdzać nową kolejność nagłówków i komórek danych.
- FR-007: Test E2E musi potwierdzać nową kolejność kolumn oraz brak regresji w podstawowych interakcjach listy.

### Edge Cases
- EC-001: Zmiana kolejności nie może zamienić zawartości kolumn; `PH` nadal pokazuje `item.phName`, a `Email PH` nadal pokazuje `item.phEmail`.
- EC-002: Testy oparte na indeksach kolumn muszą zostać zaktualizowane, aby nie zgłaszały fałszywych regresji po zmianie kolejności.
- EC-003: Sortowalne kolumny sąsiadujące z `PH` i `Email PH` nie mogą zmienić działania ani etykiet.
- EC-004: Przy pustych wynikach i stanie błędu zmiana kolejności nie może wpływać na renderowanie pozostałych sekcji komponentu.
- EC-005: Jeśli eksporty/wydruki/raporty istnieją poza tym repozytorium, ich brak w kodzie nie może być błędnie interpretowany jako zakończona implementacja pełnego zakresu Jira.

### Success Criteria
- [ ] Na `/processes` kolumna `PH` jest widoczna bezpośrednio przed `Email PH`
- [ ] Dane `phName` i `phEmail` pozostają niezmienione, zmienia się tylko ich pozycja
- [ ] Żadna inna kolumna nie zmienia położenia
- [ ] Filtrowanie, sortowanie, paginacja i przejście do szczegółów nadal działają
- [ ] Testy jednostkowe i E2E przechodzą z nową kolejnością kolumn
- [ ] Zidentyfikowano, że w tym repo nie ma kodu eksportów/wydruków/raportów do zmiany

### Open Questions
- [NEEDS CLARIFICATION: Gdzie utrzymywana jest implementacja eksportów, wydruków i raportów opartych o listę procesów? W aktualnym repozytorium nie ma plików ani ścieżek odpowiadających za te funkcje, więc tutaj można zaimplementować tylko zmianę na ekranie `/processes`.]

---

## Implementation Plan

### Technical Approach
Repozytorium to Angular 17 ze standalone components. Ekran listy procesów jest zdefiniowany inline w `src/app/features/processes/process-list/process-list.component.ts`, a testy regresyjne znajdują się w:
- `src/app/features/processes/process-list/process-list.component.spec.ts`
- `tests/e2e/process-list-column-order.spec.ts`

Aktualny stan kodu już renderuje kolejność `Email PH` → `PH`, co widać w tabeli komponentu oraz w testach, które jawnie to potwierdzają. Aby zrealizować SDLC-112, należy odwrócić tę relację do `PH` → `Email PH` zarówno w `<thead>`, jak i w `<tbody>`, bez zmian w `process-list.models.ts`, `process-list.service.ts` i `process-list.mock.ts`, ponieważ ticket nie dotyczy danych ani logiki pobierania.

Wyniki Step 2 / Confluence:
- Znaleziono funkcjonalny opis istniejącej listy procesów w Services: [F02 - Process List & Filtering (ZRD)](https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD)
- Znaleziono scenariusze testowe dla listy procesów w Services: [F02 — Process List & Filtering (ZRD) — E2E Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios)
- W przestrzeni `SDLC` nie znaleziono relewantnych ADR-ów ani konwencji ograniczających implementację.
- Nie znaleziono istniejącej usługi REST/OpenAPI do integracji dla tego ticketu; zadanie pozostaje zmianą prezentacyjną.
- Strona Services wspomina o obsłudze report generation dialog, ale w aktualnym repozytorium brak implementacji eksportów/wydruków/raportów, więc plan dla tego kodu obejmuje wyłącznie ekran `/processes` i testy.

Podejście:
1. Zmienić wyłącznie kolejność renderowania dwóch istniejących kolumn w `ProcessListComponent`.
2. Zachować istniejące bindingi `item.phName` i `item.phEmail`.
3. Zaktualizować testy jednostkowe i Playwright, które obecnie utrwalają przeciwną kolejność.
4. Nie modyfikować modeli, mocków ani serwisu, bo ich kontrakty nie zmieniają się w tym zadaniu.
5. Jawnie odnotować lukę repozytoryjną względem eksportów/wydruków/raportów jako zależność poza tym kodbase.

### Task Breakdown

#### Phase 1: Aktualizacja układu kolumn na ekranie listy procesów
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zlokalizować bieżący układ kolumn | `src/app/features/processes/process-list/process-list.component.ts` | Potwierdzić, że template renderuje obecnie `Email PH` przed `PH` i że zmiana dotyczy wyłącznie tych dwóch kolumn. |
| 1.2 | Przestawić nagłówki tabeli | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić kolejność `<th scope="col">Email PH</th>` i `<th scope="col">PH</th>`, tak aby `PH` było bezpośrednio przed `Email PH`. |
| 1.3 | Przestawić komórki danych w wierszach | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić kolejność `<td>{{ item.phEmail }}</td>` i `<td>{{ item.phName }}</td>` bez zmiany wartości renderowanych w kolumnach. |
| 1.4 | Zachować niezmieniony układ pozostałych kolumn | `src/app/features/processes/process-list/process-list.component.ts` | Zweryfikować, że status, daty, format dokumentów, uwagi, obserwowane i akcje pozostają w niezmienionej kolejności względnej. |

#### Phase 2: Aktualizacja testów jednostkowych
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zmienić asercję nagłówków | `src/app/features/processes/process-list/process-list.component.spec.ts` | Zaktualizować test, aby potwierdzał `PH` bezpośrednio przed `Email PH`, zamiast odwrotnej kolejności. |
| 2.2 | Zmienić asercję danych w wierszu | `src/app/features/processes/process-list/process-list.component.spec.ts` | Potwierdzić, że `phName` występuje bezpośrednio przed `phEmail`, a oba pola nadal odpowiadają właściwym wartościom modelu. |
| 2.3 | Utrzymać istniejące testy zachowania listy | `src/app/features/processes/process-list/process-list.component.spec.ts` | Pozostawić testy filtrowania, sortowania, paginacji, empty state, error state i nawigacji bez zmian poza ewentualnym wpływem indeksów kolumn. |

#### Phase 3: Aktualizacja testów E2E
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Zmienić oczekiwaną listę nagłówków | `tests/e2e/process-list-column-order.spec.ts` | Zaktualizować `expectedHeaders`, tak aby `PH` poprzedzało `Email PH`. |
| 3.2 | Zmienić indeksy i opis scenariusza | `tests/e2e/process-list-column-order.spec.ts` | Dostosować wyliczenia `phHeaderIndex` i `phEmailHeaderIndex` oraz nazwy testów do nowej kolejności. |
| 3.3 | Potwierdzić brak regresji funkcjonalnej | `tests/e2e/process-list-column-order.spec.ts` | Zachować istniejące sprawdzenia filtrowania, czyszczenia filtrów, sortowania, paginacji i przejścia do szczegółów po zmianie kolumn. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Zmiana kolejności renderowania kolumn `PH` i `Email PH` w nagłówku oraz wierszach tabeli. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Aktualizacja testów jednostkowych do nowej kolejności `PH` przed `Email PH`. |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Aktualizacja testu Playwright i oczekiwanych indeksów kolumn. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Uruchomić `npm run build` i potwierdzić brak błędów kompilacji
5. [ ] Uruchomić `ng test --watch=false` i potwierdzić przejście testów `ProcessListComponent`
6. [ ] Uruchomić `npx playwright test tests/e2e/process-list-column-order.spec.ts` i potwierdzić nową kolejność kolumn
7. [ ] Otworzyć `/processes` i sprawdzić ręcznie, że nagłówek `PH` jest bezpośrednio przed `Email PH`
8. [ ] Sprawdzić pierwszy wiersz tabeli i potwierdzić, że wartość `phName` jest przed `phEmail`
9. [ ] Potwierdzić, że filtrowanie, sortowanie, paginacja i link `Szczegóły` nadal działają
10. [ ] Potwierdzić w przeglądzie repo, że brak jest kodu eksportów/wydruków/raportów do zmiany w tym zakresie