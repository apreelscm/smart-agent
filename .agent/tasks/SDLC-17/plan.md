# SDLC-17: Na ekranie listy ofert należy zmienić nazwę przycisku “Reset” na “Wyczyść”

## Specification

### Overview
To zadanie wprowadza wyłącznie zmianę tekstu przycisku czyszczenia filtrów na ekranie listy ofert. Obecnie przycisk w pasku akcji widoku ofert ma etykietę `Reset`; po zmianie powinien wyświetlać `Wyczyść`, bez modyfikowania jego działania, położenia, stylów ani warunków aktywności.

### User Stories
- US-001: As a sales agent, I want the filter reset button on the offers list to be labeled `Wyczyść`, so that the wording matches the expected Polish UI copy.
  - Given ekran listy ofert jest otwarty
  - When użytkownik patrzy na pasek akcji nad listą
  - Then widzi przycisk z etykietą `Wyczyść`

### Functional Requirements
- FR-001: Na ekranie listy ofert przycisk resetowania filtrów musi wyświetlać etykietę `Wyczyść` zamiast `Reset`.
- FR-002: Przycisk musi pozostać powiązany z istniejącą metodą `clearAllFilters()`.
- FR-003: Przycisk musi zachować obecny stan disabled/enabled oparty o `filtersChanged()`.
- FR-004: Przycisk musi pozostać w sekcji `toolbar__actions` bez zmian układu.
- FR-005: Istniejąca klasa `clear-filters-button` oraz obecne inline style przycisku muszą zostać zachowane.
- FR-006: Test jednostkowy widoku ofert musi zostać zaktualizowany tak, aby sprawdzał nową etykietę `Wyczyść`.

### Edge Cases
- EC-001: Gdy żadne filtry nie są aktywne i przycisk jest wyłączony, nadal powinien wyświetlać tekst `Wyczyść`.
- EC-002: Gdy filtry są aktywne, kliknięcie przycisku `Wyczyść` nadal musi przywracać wartości domyślne: pusty tekst wyszukiwania, status `ALL`, produkt `ALL`, pole sortowania `ISSUE_DATE` i kierunek `DESC`.
- EC-003: Stara etykieta `Reset` nie może pozostać w template ani w oczekiwaniach testowych dla tego przycisku.
- EC-004: Tekst pustego stanu `wyczyść filtry` nie jest częścią tego zadania i nie powinien być zmieniany, ponieważ ticket dotyczy wyłącznie nazwy przycisku.

### Success Criteria
- [ ] Na stronie listy ofert widoczny jest przycisk z etykietą `Wyczyść`
- [ ] Napis `Reset` nie jest już renderowany dla tego przycisku
- [ ] Kliknięcie przycisku nadal uruchamia istniejące czyszczenie filtrów
- [ ] Stan aktywności przycisku pozostaje bez zmian
- [ ] Test komponentu ofert przechodzi z nową etykietą

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 20 oparta o standalone components i PrimeNG, z testami jednostkowymi uruchamianymi przez Karma/Jasmine zgodnie z `package.json`. Ekran listy ofert jest zaimplementowany w:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Aktualny stan kodu:
- w `offers-home-page.component.html` przycisk `clear-filters-button` ma dziś `label="Reset"`,
- przycisk używa istniejącej obsługi `(click)="clearAllFilters()"`,
- aktywność przycisku zależy od computed signal `filtersChanged()` zdefiniowanego w `offers-home-page.component.ts`,
- test `should render clear filters button with Reset label` w `offers-home-page.component.spec.ts` sprawdza obecny napis `Reset`.

Wzorzec w repo potwierdza użycie etykiety `Wyczyść` na podobnym ekranie polis w `src/app/features/policies/pages/policies-home-page.component.html`, więc zmiana na ekranie ofert powinna być spójna z istniejącym UI.

Wynik analizy Confluence:
- No existing service/API documentation relevant to this UI copy change was found in the configured `Services` space.
- No relevant domain guidance or naming convention was found in the configured `SDLC` space.

To zadanie nie wymaga zmian w serwisach, repozytoriach, modelach ani integracjach. Zakres powinien zostać ograniczony do warstwy template i istniejącego testu komponentu.

### Task Breakdown

#### Phase 1: Update offers list button copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change button label in offers toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić atrybut `label` przycisku `clear-filters-button` z `Reset` na `Wyczyść`, bez naruszania click handlera, disabled bindingu, klasy i stylów. |
| 1.2 | Preserve existing reset logic | `src/app/features/offers/pages/offers-home-page.component.ts` | Nie wprowadzać zmian w `clearAllFilters()` ani `filtersChanged()`; jedynie potwierdzić, że obecna logika pozostaje właściwa dla nowej etykiety. |
| 1.3 | Update spec assertions for new label | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zmienić nazwę testu i asercje tak, aby oczekiwały tekstu `Wyczyść` oraz potwierdzały brak starego napisu `Reset`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Zmiana widocznej etykiety przycisku czyszczenia filtrów z `Reset` na `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Aktualizacja testu renderowania przycisku do nowej etykiety `Wyczyść`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements