# SDLC-111: Na liście procesów przesuń kolumnę PH za eMail PH

## Specification

### Overview
Zadanie wprowadza wyłącznie zmianę kolejności prezentacji dwóch istniejących kolumn na widoku listy procesów pod ścieżką `/processes`. Obecna implementacja w repo renderuje kolumnę `PH` przed `Email PH`; po zmianie `PH` ma być wyświetlana bezpośrednio po `Email PH`, bez zmian w danych, nazwach pól modelu, filtrowaniu, sortowaniu, paginacji ani nawigacji do szczegółów procesu.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę `PH` bezpośrednio po `eMail PH`, so that dane są prezentowane w oczekiwanej kolejności biznesowej.
  - Given użytkownik otwiera listę procesów / When tabela zostanie wyrenderowana / Then kolumna `PH` znajduje się bezpośrednio za kolumną `eMail PH`
- US-002: As a użytkownik biznesowy, I want aby zmiana dotyczyła tylko układu tabeli, so that zawartość listy i jej zachowanie pozostają bez regresji.
  - Given lista procesów korzysta z istniejącego filtrowania, sortowania i paginacji / When zmieni się kolejność kolumn / Then pozostałe funkcje działają tak jak dotychczas

### Functional Requirements
- FR-001: Na widoku `/processes` nagłówek kolumny `PH` musi być renderowany bezpośrednio po nagłówku `Email PH`.
- FR-002: W każdym wierszu tabeli komórka z `item.phName` musi być renderowana bezpośrednio po komórce z `item.phEmail`.
- FR-003: Zmiana musi objąć tylko tabelę listy procesów w `ProcessListComponent`.
- FR-004: Nazwy kolumn, wartości danych, sortowanie, filtrowanie, paginacja, stan pusty, stan błędu i link `Szczegóły` muszą pozostać bez zmian.
- FR-005: Nie wolno zmieniać modeli danych ani logiki `ProcessListService`.
- FR-006: Testy jednostkowe muszą potwierdzać nową kolejność `Email PH` → `PH`.
- FR-007: Test Playwright dla listy procesów musi zostać zaktualizowany do nowej kolejności kolumn.

### Edge Cases
- EC-001: Zmiana kolejności nie może zamienić zawartości kolumn; `Email PH` nadal pokazuje `item.phEmail`, a `PH` nadal pokazuje `item.phName`.
- EC-002: Pozostałe kolumny muszą zachować dotychczasową kolejność, przesuwa się tylko relacja między `Email PH` i `PH`.
- EC-003: Testy oparte na indeksach kolumn muszą zostać zaktualizowane, aby nie zgłaszały fałszywych regresji po zmianie UI.
- EC-004: Stany `empty-state` i `error-alert` nie mogą zostać naruszone przez zmianę kolejności komórek tabeli.

### Success Criteria
- [ ] Na `/processes` nagłówek `PH` jest widoczny bezpośrednio po `Email PH`
- [ ] W wierszach tabeli wartość `phName` jest renderowana bezpośrednio po `phEmail`
- [ ] Żadna inna kolumna nie zmienia położenia
- [ ] Filtrowanie, sortowanie, paginacja i przejście do szczegółów nadal działają
- [ ] Unit testy i test E2E potwierdzają nową kolejność kolumn

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Repo to Angular 17 z standalone components, a lista procesów jest renderowana inline w `src/app/features/processes/process-list/process-list.component.ts`. To jest jedyne miejsce w repo, gdzie widoczna jest tabela listy procesów, więc zmiana powinna być lokalna i minimalna: przestawienie dwóch sąsiednich kolumn w `<thead>` i odpowiadających im komórek w `<tbody>`.

Aktualny stan kodu:
- `ProcessListComponent` renderuje obecnie układ `PH` → `Email PH`.
- `process-list.component.spec.ts` zawiera testy, które jawnie potwierdzają obecną kolejność `PH` przed `Email PH`.
- `tests/e2e/process-list-column-order.spec.ts` również sprawdza obecny układ i będzie wymagał aktualizacji.
- `ProcessListService` korzysta dziś z `MOCK_PROCESS_LIST_ITEMS`, ale ticket nie dotyczy integracji ani warstwy danych, więc serwis i modele pozostają bez zmian.

Wyniki Step 2 / Confluence:
- Funkcjonalny kontekst listy procesów znaleziono w Services: [F02 - Process List & Filtering (ZRD)](https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD)
- Kontekst testowy listy procesów znaleziono w Services: [F02 — Process List & Filtering (ZRD) — E2E Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios)
- W przestrzeni `SDLC` nie znaleziono relewantnych ADR-ów ani konwencji ograniczających implementację.
- W wynikach `Services` nie znaleziono jawnie opisanej usługi REST/OpenAPI wymagającej integracji dla tego ticketu; zadanie pozostaje zmianą prezentacyjną w UI, więc nie wprowadza zmian runtime integration.

Kluczowe decyzje:
1. Zmienić wyłącznie kolejność renderowania kolumn w `ProcessListComponent`.
2. Nie modyfikować `process-list.models.ts`, `process-list.service.ts` ani `process-list.mock.ts`, ponieważ zakres nie obejmuje danych ani kontraktów.
3. Zaktualizować istniejące testy zamiast dodawać nowy tor implementacyjny, zgodnie z obecnym stylem repo.
4. Zachować obecną etykietę nagłówka `Email PH` tak jak w kodzie, bo Jira wymaga wyłącznie zmiany kolejności, nie zmiany nazewnictwa.

### Task Breakdown

#### Phase 1: Zmiana kolejności kolumn w komponencie listy procesów
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zidentyfikować sekcje nagłówka i wiersza odpowiedzialne za `Email PH` i `PH` | `src/app/features/processes/process-list/process-list.component.ts` | Potwierdzić bieżący układ `PH` przed `Email PH` w inline template. |
| 1.2 | Przestawić nagłówki tabeli | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić miejscami `<th scope="col">PH</th>` i `<th scope="col">Email PH</th>`, tak aby `PH` było bezpośrednio po `Email PH`. |
| 1.3 | Przestawić komórki danych w każdym wierszu | `src/app/features/processes/process-list/process-list.component.ts` | Zamienić kolejność `<td>{{ item.phName }}</td>` i `<td>{{ item.phEmail }}</td>` bez zmiany mapowania danych. |
| 1.4 | Zweryfikować brak wpływu na resztę tabeli | `src/app/features/processes/process-list/process-list.component.ts` | Sprawdzić, że kolumny statusu, dat, uwag, obserwowanych i akcji pozostają w tych samych pozycjach względnych poza przesunięciem `PH`. |

#### Phase 2: Aktualizacja testów regresyjnych
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zmienić test kolejności nagłówków | `src/app/features/processes/process-list/process-list.component.spec.ts` | Odwrócić asercje tak, aby `Email PH` występował przed `PH`, najlepiej jako relacja bezpośredniego sąsiedztwa. |
| 2.2 | Zmienić test kolejności danych w wierszu | `src/app/features/processes/process-list/process-list.component.spec.ts` | Potwierdzić, że `phEmail` znajduje się przed `phName` i obie wartości pozostają poprawne. |
| 2.3 | Zaktualizować test Playwright dla układu kolumn | `tests/e2e/process-list-column-order.spec.ts` | Zmienić oczekiwania dla nagłówków i pierwszego wiersza na układ `Email PH` → `PH`. |
| 2.4 | Zachować istniejące scenariusze funkcjonalne | `src/app/features/processes/process-list/process-list.component.spec.ts`, `tests/e2e/process-list-column-order.spec.ts` | Upewnić się, że testy filtrowania, sortowania, paginacji i przejścia do szczegółów nadal odzwierciedlają niezmienione zachowanie listy. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Przestawienie kolejności renderowania kolumn `Email PH` i `PH` w nagłówku oraz wierszach tabeli. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Aktualizacja testów jednostkowych sprawdzających kolejność nagłówków i komórek w wierszach. |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Aktualizacja testu Playwright do nowego układu `Email PH` przed `PH`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Uruchomić `ng test --watch=false` i potwierdzić przejście testów komponentu listy procesów
5. [ ] Uruchomić `npx playwright test tests/e2e/process-list-column-order.spec.ts` i potwierdzić nową kolejność w E2E
6. [ ] Otworzyć `/processes` i sprawdzić, że nagłówek `PH` jest bezpośrednio po `Email PH`
7. [ ] Sprawdzić pierwszy wiersz tabeli i potwierdzić, że email PH jest wyświetlony przed nazwą PH
8. [ ] Sprawdzić, że sortowanie, filtrowanie, paginacja i link `Szczegóły` nadal działają bez zmian