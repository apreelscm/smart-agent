# SDLC-18: Na ekranie listy ofert należy zmienić nazwę przycisku “Reset” na “Wyczyść”

## Specification

### Overview
To zadanie wprowadza niewielką zmianę tekstową na ekranie listy ofert: przycisk służący do resetowania filtrów ma wyświetlać etykietę `Wyczyść` zamiast `Reset`. Zmiana ma dotyczyć wyłącznie warstwy prezentacji i powinna zachować obecną logikę działania przycisku, jego stan aktywności, położenie oraz styl.

### User Stories
- US-001: As a sales agent, I want the filter reset button on the offers list to say `Wyczyść`, so that the UI uses the expected Polish wording.
  - Given ekran listy ofert jest otwarty / When użytkownik patrzy na sekcję akcji paska narzędzi / Then widzi przycisk z etykietą `Wyczyść`

### Functional Requirements
- FR-001: Na ekranie listy ofert przycisk resetowania filtrów musi wyświetlać etykietę `Wyczyść` zamiast `Reset`.
- FR-002: Przycisk musi pozostać powiązany z istniejącą metodą `clearAllFilters()`.
- FR-003: Przycisk musi zachować obecny stan disabled/enabled oparty o `filtersChanged()`.
- FR-004: Położenie przycisku w sekcji `toolbar__actions` musi pozostać bez zmian.
- FR-005: Klasa `clear-filters-button` oraz obecne style przycisku muszą zostać zachowane.
- FR-006: Test jednostkowy komponentu listy ofert musi zostać zaktualizowany tak, aby weryfikował nową etykietę.

### Edge Cases
- EC-001: Gdy żadne filtry nie są aktywne i przycisk jest nieaktywny, nadal powinien wyświetlać tekst `Wyczyść`.
- EC-002: Gdy filtry są aktywne, kliknięcie przycisku `Wyczyść` nadal musi przywracać wartości domyślne: pusty `searchTerm`, status `ALL`, produkt `ALL`, sortowanie po `ISSUE_DATE` i kierunek `DESC`.
- EC-003: Zmiana etykiety nie może pozostawić w widoku ani w teście starego napisu `Reset`.

### Success Criteria
- [ ] Na stronie listy ofert widoczny jest przycisk z etykietą `Wyczyść`
- [ ] Napis `Reset` nie jest już renderowany dla tego przycisku
- [ ] Kliknięcie przycisku nadal resetuje filtry przez istniejącą metodę `clearAllFilters()`
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

Na podstawie analizy kodu:
- przycisk znajduje się w `offers-home-page.component.html` w sekcji `toolbar__actions`,
- obecnie ma ustawione `label="Reset"`,
- jego zachowanie jest obsługiwane przez istniejącą metodę `clearAllFilters()` z `offers-home-page.component.ts`,
- stan aktywności jest kontrolowany przez computed signal `filtersChanged`,
- istnieje już test `should render clear filters button with Reset label` w `offers-home-page.component.spec.ts`.

To oznacza, że implementacja powinna być minimalna i ograniczona do warstwy widoku oraz testu:
1. zmiana atrybutu `label` przycisku z `Reset` na `Wyczyść` w template,
2. aktualizacja istniejącego testu jednostkowego, aby oczekiwał `Wyczyść` oraz potwierdzał brak `Reset`.

Wynik odkrycia Confluence:
- No existing service/API documentation relevant to this UI label change was found in Services space.
- No relevant domain guidance or convention was found in SDLC space.

To zadanie nie wymaga zmian w serwisach, integracjach, modelach, repozytoriach ani konfiguracji runtime. Istniejące mock JSON files w `public/mock` nie są częścią tego zakresu i nie wymagają modyfikacji.

### Task Breakdown

#### Phase 1: Update offers list button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change clear filters button label | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić atrybut `label` przycisku `clear-filters-button` z `Reset` na `Wyczyść`, bez zmiany eventu `(click)`, klasy CSS ani stylów. |
| 1.2 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Nie wprowadzać zmian w `clearAllFilters()` ani `filtersChanged`; jedynie potwierdzić, że obecna logika pozostaje właściwa dla nowej etykiety. |
| 1.3 | Update offers page unit test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zmienić nazwę testu i asercje tak, aby oczekiwały tekstu `Wyczyść` i potwierdzały brak starego tekstu `Reset`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Zmiana widocznej etykiety przycisku resetowania filtrów z `Reset` na `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Aktualizacja testu renderowania przycisku do nowej etykiety `Wyczyść`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements