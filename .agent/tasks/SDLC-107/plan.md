# SDLC-107: Na liscie ofert usun z listy wynikow kolumne z 'Okres ochrony'

## Specification

### Overview
Usunięcie sekcji meta „Okres ochrony” ze standardowej listy ofert na ekranie `Przygotowane oferty`, tak aby uprościć widok i nie wyświetlać już ani etykiety, ani wyliczanej wartości okresu ochrony dla żadnego wiersza. Zmiana dotyczy wyłącznie warstwy prezentacji i testów tego ekranu; bez zmian w danych ofert, API i logice biznesowej ofert.

### User Stories
- US-001: As a user przeglądający standardową listę wyników ofert, I want kolumna „Okres ochrony” była niewidoczna, so that lista jest prostsza i bardziej czytelna.
  - Given użytkownik otwiera ekran `Przygotowane oferty` / When lista ofert zostaje wyrenderowana / Then etykieta „Okres ochrony” i jej wartość nie są widoczne w żadnym wierszu
- US-002: As a user systemu, I want pozostałe informacje o ofercie zachowały poprawny układ po usunięciu kolumny, so that mogę dalej analizować wyniki bez regresji wizualnej i funkcjonalnej.
  - Given lista zawiera oferty komunikacyjne i uprawowe / When ekran wyświetla meta-dane wiersza / Then pozostałe pola meta renderują się poprawnie, bez pustego miejsca po usuniętej kolumnie

### Functional Requirements
- FR-001: Ekran `src/app/features/offers/pages/offers-home-page.component.html` nie może renderować bloku meta z etykietą `Okres ochrony`.
- FR-002: Komponent `OffersHomePageComponent` nie może utrzymywać już logiki wyliczania i formatowania `coveragePeriodLabel`, jeśli była używana wyłącznie przez usuwaną sekcję UI.
- FR-003: Siatka `.offer-row__meta-grid` musi zostać dostosowana z układu 5-kolumnowego do układu zgodnego z 4 widocznymi polami meta, aby nie pozostawiać pustej kolumny.
- FR-004: Lista ofert ma pozostać w pełni funkcjonalna: filtrowanie, sortowanie po istniejących polach, zmiana waluty, przejścia statusów i nawigacja do oferty nie mogą ulec regresji.
- FR-005: Testy jednostkowe i e2e muszą zostać zaktualizowane tak, by potwierdzały brak „Okresu ochrony” oraz nowy porządek pól meta.

### Edge Cases
- EC-001: Dla ofert komunikacyjnych i uprawowych sekcja „Okres ochrony” nie może pojawić się w żadnym wariancie renderowania.
- EC-002: Po zmianie waluty prezentacji (PLN/EUR/USD) wiersze ofert nie mogą ponownie wyświetlać usuniętej sekcji.
- EC-003: W wąskim widoku responsywnym układ `.offer-row__meta-grid` ma nadal renderować się poprawnie, bez pustych miejsc i bez oczekiwania na piąte pole.
- EC-004: Jeśli testy lub helpery nadal zakładają obecność 5 etykiet meta, muszą zostać dostosowane do 4 etykiet.

### Success Criteria
- [ ] Na ekranie `Przygotowane oferty` nie ma etykiety `Okres ochrony` ani odpowiadającej jej wartości w żadnym wierszu
- [ ] Układ meta-danych oferty nie pozostawia pustej kolumny po usuniętym elemencie
- [ ] Istniejące funkcje listy ofert działają bez regresji
- [ ] Testy unit i e2e potwierdzają brak usuniętej sekcji

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Zmiana jest lokalna dla frontendu Angular i koncentruje się na `OffersHomePageComponent`, który obecnie renderuje listę ofert jako karty z siatką `.offer-row__meta-grid`. Obecna implementacja nie korzysta z tabeli PrimeNG, tylko z własnego układu kartowego w HTML/CSS, więc „kolumna” z opisu Jira odpowiada jednemu z pięciu bloków meta w `offers-home-page.component.html`.

Plan:
- usunąć z HTML cały blok meta:
  - etykietę `Okres ochrony`
  - wartość `{{ coveragePeriodLabel() }}`
  - opis pomocniczy `Wyliczony na moment otwarcia listy`
- uprościć klasę `OffersHomePageComponent` przez usunięcie nieużywanej po tej zmianie logiki:
  - `coveragePeriodSnapshot`
  - computed `coveragePeriodLabel`
  - helpery `normalizeToLocalCalendarDay`, `getProtectionPeriodEndDate`, `formatCoverageDate`
- dostosować SCSS:
  - `.offer-row__meta-grid` z `repeat(5, minmax(0, 1fr))` na `repeat(4, minmax(0, 1fr))`
  - breakpointi pozostają zgodne z istniejącym wzorcem responsywności
- zaktualizować testy:
  - unit testy komponentu: zamiast asercji obecności okresu ochrony, asercje jego braku i nowego układu meta
  - e2e: zaktualizować test porządku pól meta oraz test dedykowany `offers-protection-period.spec.ts`, aby weryfikował brak tej sekcji

Wzorce kodowe do zachowania:
- standalone Angular component z `signal`/`computed`
- istniejące testy Jasmine + `HttpTestingController`
- Playwright e2e z selektorami opartymi o klasy `.offer-row`, `.offer-row__meta-grid`, `.offer-row__meta-label`

Confluence:
- Nie znaleziono istniejącego serwisu/API w przestrzeni `Services` relewantnego dla tego zadania — zmiana pozostaje wyłącznie frontendowa.
- W przestrzeni domenowej znaleziono ogólny kontekst ekranów aplikacji: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). Dokument potwierdza, że ekran ofert jest elementem przepływu sprzedażowego, ale nie narzuca dodatkowych ograniczeń technicznych dla tej zmiany.

Dodatkowa obserwacja z kodu:
- W bieżącej implementacji nie ma osobnego sortowania po „Okresie ochrony”; dostępne sortowanie dotyczy tylko `ISSUE_DATE` i `VALID_TO`. W praktyce oznacza to, że zakres usunięcia interakcji sprowadza się do likwidacji samego pola prezentacyjnego i aktualizacji testów, bez zmian w mechanizmie sortowania.

### Task Breakdown

#### Phase 1: Remove protection period from offers list UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Usuń blok meta „Okres ochrony” z widoku listy ofert | `src/app/features/offers/pages/offers-home-page.component.html` | Skasować cały `<div>` renderujący etykietę, wartość i opis okresu ochrony między sekcjami `Wariant` i `Aktualizacja`. |
| 1.2 | Usuń nieużywaną logikę wyliczania okresu ochrony | `src/app/features/offers/pages/offers-home-page.component.ts` | Usunąć `coveragePeriodSnapshot`, `coveragePeriodLabel` i prywatne helpery używane wyłącznie przez usunięty blok UI. |
| 1.3 | Dostosuj układ siatki meta-danych do 4 pól | `src/app/features/offers/pages/offers-home-page.component.scss` | Zmienić desktopowy układ `.offer-row__meta-grid` z 5 na 4 kolumny, aby nie zostawiać pustego miejsca po usuniętym bloku. |

#### Phase 2: Update unit tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zastąp testy obecności okresu ochrony testami jego braku | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Usunąć asercje sprawdzające `coveragePeriodLabel()` i obecność `Okres ochrony`; dodać sprawdzenie, że tekst nie występuje po renderze. |
| 2.2 | Zaktualizuj test porządku meta-danych | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Zmienić oczekiwaną listę etykiet z `['Pojazd', 'Kanał', 'Wariant', 'Okres ochrony', 'Aktualizacja']` na układ 4-elementowy bez usuniętej etykiety. |
| 2.3 | Zachowaj pozostałe testy regresyjne bez zmian funkcjonalnych | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Upewnić się, że testy walut i pozostałych funkcji nadal przechodzą po usunięciu sekcji. |

#### Phase 3: Update e2e coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Zmień e2e porządku meta-danych | `tests/e2e/offers-meta-order.spec.ts` | Zaktualizować liczbę oczekiwanych etykiet meta z 5 do 4 oraz kolejność po usunięciu `Okres ochrony`. |
| 3.2 | Przepisz e2e dedykowany ochronie na weryfikację braku sekcji | `tests/e2e/offers-protection-period.spec.ts` | Zamiast potwierdzać obecność okresu ochrony w każdym wierszu, sprawdzać że tekst `Okres ochrony` nie występuje na liście ofert, także po zmianie waluty i w scenariuszach responsywnych jeśli pozostaną. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Usunięcie bloku prezentującego „Okres ochrony” z meta-danych wiersza oferty |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Usunięcie nieużywanej logiki wyliczania okresu ochrony |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Dostosowanie układu siatki meta-danych do 4 kolumn |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Aktualizacja testów jednostkowych pod brak „Okresu ochrony” i nowy układ meta |
| `tests/e2e/offers-meta-order.spec.ts` | MODIFY | Aktualizacja testu kolejności meta-danych po usunięciu pola |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Zmiana testów e2e z obecności okresu ochrony na potwierdzenie jego braku |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements