# SDLC-110: Na liście procesów przesuń kolumnę PH przed eMail PH

## Specification

### Overview
Zadanie wprowadza wyłącznie zmianę kolejności prezentacji dwóch istniejących kolumn na standardowej liście procesów. W aktualnej implementacji tabela renderuje kolumnę `Email PH` przed `PH`; po zmianie kolumna `PH` ma być widoczna bezpośrednio przed `Email PH`, bez modyfikacji nazw, danych, sortowania, filtrowania ani logiki ładowania listy.

### User Stories
- US-001: As a użytkownik listy procesów, I want widzieć kolumnę `PH` przed `eMail PH`, so that dane kontaktowe są prezentowane w oczekiwanej kolejności biznesowej.
  - Given użytkownik otwiera standardową listę procesów / When tabela zostanie wyrenderowana / Then kolumna `PH` znajduje się bezpośrednio przed kolumną `eMail PH`
- US-002: As a użytkownik biznesowy, I want zmiana dotyczyła tylko układu tabeli, so that obecne działanie listy pozostaje bez regresji.
  - Given lista procesów działa z filtrowaniem, sortowaniem i paginacją / When zmieni się kolejność kolumn / Then zachowanie tych funkcji pozostaje bez zmian
- US-003: As a użytkownik korzystający z powiązanych prezentacji danych, I want ta sama kolejność była zachowana wszędzie tam, gdzie obie kolumny występują, so that układ danych jest spójny.
  - Given istnieje widok prezentujący oba pola `PH` i `eMail PH` / When widok zostanie pokazany po wdrożeniu / Then `PH` poprzedza `eMail PH`

### Functional Requirements
- FR-001: Standardowa lista procesów musi renderować kolumnę `PH` bezpośrednio przed kolumną `eMail PH`.
- FR-002: Zmiana musi objąć zarówno nagłówki tabeli, jak i kolejność komórek w każdym wierszu.
- FR-003: Wartości wyświetlane w polach `phName` i `phEmail` muszą pozostać bez zmian.
- FR-004: Nie wolno zmieniać modeli danych, kontraktów serwisu ani mechanizmu pobierania listy.
- FR-005: Sortowanie, filtrowanie, paginacja i link do szczegółów procesu muszą działać identycznie jak przed zmianą.
- FR-006: Testy komponentu muszą potwierdzać nową kolejność kolumn w renderowanej tabeli.
- FR-007: Jeśli w repo istnieją inne renderowane widoki tej samej tabeli, powinny używać tej samej kolejności; jeśli takich implementacji brak, zakres kodowy w tym repo ogranicza się do głównej listy procesów.

### Edge Cases
- EC-001: Zmiana kolejności nie może spowodować zamiany danych między kolumnami; `PH` nadal ma pokazywać `item.phName`, a `eMail PH` nadal `item.phEmail`.
- EC-002: Jeśli testy opierają się na indeksach kolumn, muszą zostać zaktualizowane do nowego układu.
- EC-003: Przy pustej liście lub stanie błędu nowa kolejność nie może wpływać na obsługę empty/error state.
- EC-004: Jeśli w przyszłości kolumny będą renderowane warunkowo, `PH` i `eMail PH` nadal powinny zachować wzajemną kolejność.

### Success Criteria
- [ ] Na `/processes` nagłówek `PH` jest wyświetlany przed `Email PH`
- [ ] W każdym wierszu wartość `phName` jest renderowana przed wartością `phEmail`
- [ ] Dane w obu kolumnach pozostają niezmienione względem obecnej implementacji
- [ ] Istniejące funkcje listy procesów działają bez regresji
- [ ] Test komponentu pokrywa wymaganą kolejność kolumn

### Open Questions
- [NEEDS CLARIFICATION: Jira wymaga zachowania tej samej kolejności także w eksporcie i innych powiązanych widokach, ale w aktualnym repo nie widać implementacji eksportu ani dodatkowego widoku listy procesów — czy oczekiwane są zmiany także poza tym repo?]

---

## Implementation Plan

### Technical Approach
Zmiana powinna zostać wykonana minimalnie i lokalnie, zgodnie z obecną strukturą Angular 17 standalone components.

Aktualny kod:
- `src/app/features/processes/process-list/process-list.component.ts` renderuje tabelę inline w szablonie komponentu.
- W nagłówku i wierszach kolejność jest obecnie: `Email PH` → `PH`.
- Dane pochodzą z istniejącego `ProcessListService`, ale ticket nie wymaga żadnej zmiany w serwisie ani modelach.

Wykorzystane źródła Confluence:
- Funkcjonalny kontekst listy procesów: https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD
- Scenariusze E2E dla listy procesów: https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios
- Nie znaleziono relewantnych konwencji/ADR w skonfigurowanej przestrzeni `SDLC`.
- Nie znaleziono istniejącej usługi/API do ponownego użycia w `Services`; zadanie dotyczy wyłącznie warstwy prezentacji w repo.

Decyzje implementacyjne:
1. Nie zmieniać `ProcessListService`, `process-list.models.ts` ani `process-list.mock.ts`, ponieważ logika danych i kontrakty pozostają bez zmian.
2. Zmodyfikować tylko kolejność dwóch kolumn w `process-list.component.ts`:
   - w `<thead>` zamienić miejscami nagłówki `PH` i `Email PH`
   - w `<tbody>` zamienić miejscami odpowiadające im komórki `item.phName` i `item.phEmail`
3. Rozszerzyć `process-list.component.spec.ts` o jawny test kolejności nagłówków i/lub kolejności komórek w wierszu, aby zabezpieczyć ticket przed regresją.
4. Ponieważ repo nie zawiera osobnej implementacji eksportu ani drugiego widoku tej tabeli, plan zmian w kodzie obejmuje wyłącznie główny widok `/processes`; ewentualne zmiany poza tym repo wymagają doprecyzowania.

### Task Breakdown

#### Phase 1: Zmiana kolejności kolumn w widoku listy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zidentyfikować aktualne miejsca renderowania obu kolumn | `src/app/features/processes/process-list/process-list.component.ts` | Potwierdzić bieżący układ `Email PH` przed `PH` w sekcjach nagłówka i wiersza. |
| 1.2 | Zamienić miejscami nagłówki kolumn | `src/app/features/processes/process-list/process-list.component.ts` | Przenieść `<th>PH</th>` bezpośrednio przed `<th>Email PH</th>` w inline template. |
| 1.3 | Zamienić miejscami komórki danych | `src/app/features/processes/process-list/process-list.component.ts` | Przenieść `{{ item.phName }}` przed `{{ item.phEmail }}` tak, aby dane odpowiadały nowemu nagłówkowi. |
| 1.4 | Zweryfikować brak wpływu na pozostałe kolumny i akcje | `src/app/features/processes/process-list/process-list.component.ts` | Upewnić się, że indeksy pozostałych kolumn, badge statusu, obserwowane i link szczegółów pozostają spójne. |

#### Phase 2: Pokrycie testami regresji
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać test kolejności nagłówków | `src/app/features/processes/process-list/process-list.component.spec.ts` | Sprawdzić, że `PH` występuje przed `Email PH` w wyrenderowanym `thead`. |
| 2.2 | Dodać lub zaktualizować test kolejności danych w wierszu | `src/app/features/processes/process-list/process-list.component.spec.ts` | Potwierdzić, że dla przykładowego wiersza komórka z `phName` poprzedza komórkę z `phEmail`. |
| 2.3 | Uruchomić istniejące testy komponentu | `src/app/features/processes/process-list/process-list.component.spec.ts` | Zweryfikować, że obecne testy ładowania, filtrowania, sortowania i paginacji nie wymagają dodatkowych zmian. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Zamiana kolejności kolumn `PH` i `Email PH` w nagłówku oraz wierszach tabeli. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Dodanie regresyjnych asercji potwierdzających nową kolejność kolumn. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Otwórz `/processes` i potwierdź, że nagłówek `PH` jest przed `Email PH`
5. [ ] Sprawdź pierwszy wiersz tabeli i potwierdź, że wartość `phName` jest przed `phEmail`
6. [ ] Potwierdź, że filtrowanie, sortowanie i paginacja nadal działają bez zmian
7. [ ] Potwierdź, że link `Szczegóły` nadal prowadzi do `/processes/:id`