# SDLC-15: Na ekranie listy ofert należy zmienić nazwę przycisku “Wyczyść” na “Resetuj”

## Specification

### Overview
To zadanie zmienia wyłącznie widoczną etykietę przycisku resetowania filtrów na ekranie listy ofert. Obecny przycisk oznaczony jako `Wyczyść` ma zostać przemianowany na `Resetuj`, bez zmiany jego położenia, stylu, warunku aktywności ani logiki `clearAllFilters()`.

### User Stories
- US-001: As a sales agent, I want the filter reset button on the offers list to say `Resetuj`, so that the wording matches expected UI terminology.
  - Given ekran listy ofert jest otwarty
  - When patrzę na sekcję akcji paska narzędzi
  - Then widzę przycisk z etykietą `Resetuj`

### Functional Requirements
- FR-001: Na ekranie listy ofert przycisk resetowania filtrów musi wyświetlać etykietę `Resetuj` zamiast `Wyczyść`.
- FR-002: Przycisk musi pozostać powiązany z istniejącą metodą `clearAllFilters()`.
- FR-003: Przycisk musi zachować obecny stan disabled/enabled oparty o `filtersChanged()`.
- FR-004: Położenie przycisku w `toolbar__actions` musi pozostać bez zmian.
- FR-005: Istniejące style przycisku, w tym bieżące inline styles oraz klasa `clear-filters-button`, muszą pozostać bez zmian, o ile nie są wymagane do samej zmiany tekstu.
- FR-006: Testy komponentu ofert muszą zostać zaktualizowane tak, aby weryfikowały nową etykietę.

### Edge Cases
- EC-001: Gdy żadne filtry nie są aktywne i przycisk jest nieaktywny, nadal powinien wyświetlać tekst `Resetuj`.
- EC-002: Gdy filtry są aktywne, kliknięcie `Resetuj` nadal musi przywracać wartości domyślne: pusty search term, status `ALL`, produkt `ALL`, sortowanie po `ISSUE_DATE` i kierunek `DESC`.
- EC-003: Zmiana etykiety nie może pozostawić w widoku ani testach starego napisu `Wyczyść`.

### Success Criteria
- [ ] Na stronie listy ofert widoczny jest przycisk z etykietą `Resetuj`
- [ ] Napis `Wyczyść` nie jest już renderowany dla tego przycisku
- [ ] Kliknięcie przycisku nadal wywołuje istniejące resetowanie filtrów
- [ ] Stan aktywności przycisku pozostaje bez zmian
- [ ] Test komponentu ofert przechodzi z nową etykietą

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 20 oparta o standalone components i PrimeNG. Ekran listy ofert jest zaimplementowany w:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

W bieżącej implementacji:
- logika resetowania filtrów już istnieje w metodzie `clearAllFilters()`,
- warunek aktywności przycisku już istnieje jako computed signal `filtersChanged`,
- template zawiera przycisk z klasą `clear-filters-button` i etykietą `Wyczyść`,
- test jednostkowy już sprawdza renderowany tekst tego przycisku.

Dlatego zmiana powinna być minimalna i ograniczona do:
1. podmiany wartości atrybutu `label` w template z `Wyczyść` na `Resetuj`,
2. aktualizacji istniejącego testu w `offers-home-page.component.spec.ts`, aby oczekiwał `Resetuj` i nie oczekiwał starej wartości.

Wynik odkrycia Confluence:
- No existing service/API documentation relevant to this UI label change was found in [Services](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+title+%7E+%22oferty%22%29+AND+space+in+%28Services%29).
- No relevant domain guidance or convention was found in [SDLC](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+title+%7E+%22wyczy%C5%9B%C4%87%22+OR+title+%7E+%22resetuj%22%29+AND+space+in+%28SDLC%29).

To zadanie nie wymaga zmian w serwisach, modelach, repozytoriach ani integracjach runtime.

### Task Breakdown

#### Phase 1: Update offers list button copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change button label in offers toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić atrybut `label` przycisku `clear-filters-button` z `Wyczyść` na `Resetuj`, bez naruszania istniejących bindingów i stylów. |
| 1.2 | Keep current reset behavior unchanged | `src/app/features/offers/pages/offers-home-page.component.ts` | Zweryfikować, że metoda `clearAllFilters()` oraz computed `filtersChanged` nie wymagają żadnych zmian implementacyjnych. |
| 1.3 | Update component spec for new label | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zaktualizować test renderujący przycisk tak, aby oczekiwał tekstu `Resetuj` oraz potwierdzał brak starej etykiety `Wyczyść`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Zmiana widocznej etykiety przycisku resetowania filtrów na `Resetuj`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Aktualizacja asercji testowych do nowej etykiety przycisku. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements