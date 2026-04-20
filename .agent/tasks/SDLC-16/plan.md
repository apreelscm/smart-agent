# SDLC-16: Na ekranie listy ofert należy zmienić nazwę przycisku “Reset” na “Wyczyść”

## Specification

### Overview
To zadanie przywraca oczekiwaną etykietę przycisku czyszczenia filtrów na ekranie listy ofert. Aktualnie w pasku akcji widoczny jest przycisk `Reset`, a zgodnie z Jira ma on wyświetlać tekst `Wyczyść`. Zmiana dotyczy wyłącznie warstwy prezentacji na ekranie listy ofert i nie powinna modyfikować istniejącej logiki resetowania filtrów, stanu disabled ani układu toolbaru.

### User Stories
- US-001: As a sales agent, I want the filter reset button on the offers list to say `Wyczyść`, so that the UI wording matches the requested label.
  - Given ekran listy ofert jest otwarty
  - When patrzę na sekcję akcji paska narzędzi
  - Then widzę przycisk z etykietą `Wyczyść`

### Functional Requirements
- FR-001: Na ekranie listy ofert przycisk resetowania filtrów musi wyświetlać etykietę `Wyczyść` zamiast `Reset`.
- FR-002: Przycisk musi pozostać powiązany z istniejącą metodą `clearAllFilters()`.
- FR-003: Przycisk musi zachować obecny stan disabled/enabled oparty o `filtersChanged()`.
- FR-004: Położenie przycisku w sekcji `toolbar__actions` musi pozostać bez zmian.
- FR-005: Istniejąca klasa `clear-filters-button`, styling PrimeNG oraz obecne inline styles muszą pozostać bez zmian.
- FR-006: Test komponentu ofert musi zostać zaktualizowany tak, aby weryfikował nową etykietę `Wyczyść` i brak starej etykiety `Reset`.

### Edge Cases
- EC-001: Gdy żadne filtry nie są aktywne i przycisk jest disabled, nadal powinien wyświetlać tekst `Wyczyść`.
- EC-002: Gdy filtry są aktywne, kliknięcie `Wyczyść` nadal musi przywracać wartości domyślne: pusty `searchTerm`, status `ALL`, produkt `ALL`, sortowanie po `ISSUE_DATE` i kierunek `DESC`.
- EC-003: Zmiana etykiety nie może pozostawić w widoku ani teście starego napisu `Reset`.

### Success Criteria
- [ ] Na stronie listy ofert widoczny jest przycisk z etykietą `Wyczyść`
- [ ] Napis `Reset` nie jest już renderowany dla tego przycisku
- [ ] Kliknięcie przycisku nadal wywołuje istniejące resetowanie filtrów
- [ ] Stan aktywności przycisku pozostaje bez zmian
- [ ] Test komponentu ofert przechodzi z nową etykietą

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 20 oparta o standalone components i PrimeNG, z logiką ekranu listy ofert zaimplementowaną w:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Analiza bieżącego kodu pokazuje, że:
- przycisk znajduje się w `offers-home-page.component.html` w sekcji `toolbar__actions`,
- aktualnie ma ustawione `label="Reset"`,
- korzysta z istniejącej metody `(click)="clearAllFilters()"`,
- jego aktywność jest kontrolowana przez `[disabled]="!filtersChanged()"`,
- istniejący test w `offers-home-page.component.spec.ts` sprawdza dokładnie obecną etykietę `Reset`.

Zakres implementacji powinien być minimalny i ograniczony do warstwy widoku oraz testu:
1. podmiana wartości atrybutu `label` z `Reset` na `Wyczyść` w `offers-home-page.component.html`,
2. aktualizacja istniejącego testu jednostkowego, aby oczekiwał `Wyczyść` i potwierdzał brak `Reset`.

To zadanie nie wymaga zmian w:
- `offers-home-page.component.ts`, ponieważ `clearAllFilters()` i `filtersChanged()` już realizują żądane zachowanie,
- repozytoriach, modelach i usługach,
- danych mockowych.

Wynik odkrycia Confluence:
- No existing service/API documentation relevant to this UI label change was found in [Services](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+title+%7E+%22ofert%22%29+AND+space+in+%28Services%29).
- No relevant domain guidance or convention was found in [SDLC](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+text+%7E+%22reset%22+OR+text+%7E+%22wyczy%C5%9B%C4%87%22+OR+title+%7E+%22ofert%22%29+AND+space+in+%28SDLC%29).

Dodatkowy sygnał z repozytorium:
- `.agent/tasks/SDLC-15/plan.md` dokumentuje odwrotną zmianę (`Wyczyść` → `Reset`) dla tego samego przycisku, więc implementacja dla SDLC-16 powinna świadomie odwrócić tamtą modyfikację w tych samych plikach i bez rozszerzania zakresu.

### Task Breakdown

#### Phase 1: Update offers list button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change toolbar button label to `Wyczyść` | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić atrybut `label` przycisku `clear-filters-button` z `Reset` na `Wyczyść`, pozostawiając bez zmian click handler, disabled binding, klasę i style. |
| 1.2 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Zweryfikować, że metoda `clearAllFilters()` oraz computed signal `filtersChanged` nie wymagają zmian, ponieważ ticket dotyczy wyłącznie copy UI. |
| 1.3 | Update unit test expectations | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zaktualizować test renderowania przycisku tak, aby oczekiwał tekstu `Wyczyść` oraz sprawdzał, że `Reset` nie występuje w renderowanym widoku. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Zmiana widocznej etykiety przycisku resetowania filtrów z `Reset` na `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Aktualizacja asercji testowych do nowej etykiety `Wyczyść`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements