# SDLC-19: Na ekranie listy ofert należy zmienić nazwę przycisku “Reset” na “Wyczyść2”

## Specification

### Overview
To zadanie wprowadza drobną zmianę tekstową w interfejsie listy ofert: przycisk służący do czyszczenia filtrów ma zmienić etykietę z `Reset` na `Wyczyść2`. Zakres obejmuje tylko warstwę prezentacji i aktualizację testu komponentu, bez zmian w logice filtrowania, stanie komponentu, routingu ani integracjach.

### User Stories
- US-001: As a sales agent, I want the filter-clearing button on the offers list to display `Wyczyść2`, so that the UI matches the wording required by the task.
  - Given ekran listy ofert jest otwarty
  - When patrzę na pasek akcji nad listą ofert
  - Then widzę przycisk z etykietą `Wyczyść2`

### Functional Requirements
- FR-001: Na ekranie listy ofert przycisk czyszczenia filtrów musi wyświetlać etykietę `Wyczyść2`.
- FR-002: Przycisk musi pozostać podłączony do istniejącej metody `clearAllFilters()`.
- FR-003: Przycisk musi zachować istniejący stan aktywności oparty o `filtersChanged()`.
- FR-004: Zmiana musi dotyczyć wyłącznie etykiety przycisku, bez zmiany jego klasy CSS, stylów inline, położenia i zachowania.
- FR-005: Test jednostkowy komponentu listy ofert musi zostać zaktualizowany tak, aby weryfikował nową etykietę oraz brak starej etykiety `Reset`.

### Edge Cases
- EC-001: Gdy żadne filtry nie są aktywne i przycisk jest disabled, nadal powinien wyświetlać tekst `Wyczyść2`.
- EC-002: Gdy filtry są aktywne, kliknięcie przycisku `Wyczyść2` nadal musi resetować: `searchTerm` do pustego ciągu, `selectedStatus` do `ALL`, `selectedProduct` do `ALL`, `selectedSortField` do `ISSUE_DATE`, a `selectedSortDirection` do `DESC`.
- EC-003: W widoku i w testach nie powinien pozostać stary napis `Reset` dla tego przycisku.

### Success Criteria
- [ ] Na stronie listy ofert widoczny jest przycisk z etykietą `Wyczyść2`
- [ ] Przycisk nadal wywołuje istniejące czyszczenie filtrów
- [ ] Stan enabled/disabled przycisku nie zmienia się względem obecnego działania
- [ ] Napis `Reset` nie jest już renderowany dla tego przycisku
- [ ] Test komponentu przechodzi po aktualizacji etykiety

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 20 oparta o standalone components i PrimeNG, co widać po `package.json` oraz implementacji ekranu ofert. Funkcjonalność listy ofert znajduje się w:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Aktualna implementacja już zawiera kompletną logikę resetowania filtrów:
- metoda `clearAllFilters()` w `offers-home-page.component.ts`,
- computed signal `filtersChanged` sterujący disabled state,
- przycisk w `offers-home-page.component.html` z klasą `clear-filters-button`,
- istniejący test renderowania etykiety w `offers-home-page.component.spec.ts`.

W związku z tym implementacja powinna być minimalna i zgodna z istniejącym wzorcem z wcześniejszego zadania udokumentowanego w `.agent/tasks/SDLC-15/plan.md`, gdzie analogicznie planowano zmianę samej etykiety tego samego przycisku. Tutaj należy wykonać kolejną zmianę copy na aktualnie wymagane `Wyczyść2`.

Wynik kroku Confluence:
- No existing service/API documentation relevant to this UI copy-only task was found in Services space: https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+text+%7E+%22reset%22+OR+title+%7E+%22ofert%22%29+AND+space+in+%28Services%29
- No relevant domain guidance or naming convention was found in SDLC space: https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22lista+ofert%22+OR+text+%7E+%22ofert%22+OR+text+%7E+%22ui%22+OR+text+%7E+%22button%22%29+AND+space+in+%28SDLC%29

Ponieważ nie znaleziono żadnych istniejących usług ani integracji dotyczących tego zadania, plan nie zakłada zmian w repozytoriach, serwisach ani runtime APIs. Mocki w `public/mock` nie są częścią zakresu tego zadania i nie wymagają modyfikacji.

### Task Breakdown

#### Phase 1: Update offers list button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change visible label in offers toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić atrybut `label` przycisku `clear-filters-button` z `Reset` na `Wyczyść2`, bez zmiany istniejących bindingów, stylów i akcji kliknięcia. |
| 1.2 | Preserve current reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Nie wprowadzać zmian w `clearAllFilters()` ani `filtersChanged`; jedynie potwierdzić, że obecna logika pozostaje właściwa dla nowej etykiety. |
| 1.3 | Update unit test expectations | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zmienić nazwę i asercje istniejącego testu tak, aby oczekiwał tekstu `Wyczyść2` oraz potwierdzał brak tekstu `Reset`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Aktualizacja etykiety przycisku czyszczenia filtrów na `Wyczyść2`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Dostosowanie testu komponentu do nowej etykiety i usunięcia starej wartości oczekiwanej. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements