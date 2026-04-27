# SDLC-103: Na liście ofert kolumna z okresem ochrony powinna byc przed kolumna Aktualizacja.

## Specification

### Overview
Zmiana porządku prezentacji metadanych na ekranie `/offers`, tak aby pole `Okres ochrony` było wyświetlane przed polem `Aktualizacja` w każdym wierszu oferty. Zakres zadania dotyczy wyłącznie warstwy UI listy ofert i ma zachować istniejącą logikę wyliczania okresu ochrony, filtrowania, sortowania oraz akcji dostępnych dla oferty.

### User Stories
- US-001: As a użytkownik listy ofert, I want widzieć `Okres ochrony` przed `Aktualizacja`, so that kolejność informacji na liście jest zgodna z oczekiwanym układem.
  - Given użytkownik otwiera ekran `/offers` / When renderowana jest lista ofert / Then blok `Okres ochrony` pojawia się przed blokiem `Aktualizacja`

- US-002: As a użytkownik systemu, I want aby zmiana dotyczyła wszystkich widocznych ofert, so that układ listy jest spójny na całym ekranie.
  - Given na liście widocznych jest wiele ofert / When ekran renderuje kolejne wiersze / Then każdy wiersz zachowuje tę samą kolejność pól metadanych

### Functional Requirements
- FR-001: W `src/app/features/offers/pages/offers-home-page.component.html` blok `Okres ochrony` musi zostać umieszczony przed blokiem `Aktualizacja` w sekcji `offer-row__meta-grid`.
- FR-002: Zmiana ma zachować istniejącą treść pola `Okres ochrony`, w tym aktualny format zakresu dat i opis pomocniczy.
- FR-003: Zmiana ma zachować istniejącą treść pola `Aktualizacja`, w tym prezentację daty i godziny.
- FR-004: Kolejność pól metadanych w wierszu oferty po zmianie musi być spójna z aktualnym układem komponentu i wynosić: `Pojazd/Uprawy`, `Kanał`, `Wariant`, `Okres ochrony`, `Aktualizacja`.
- FR-005: Implementacja nie może zmieniać logiki w `OffersRepository`, modelu `Offer` ani helperów odpowiedzialnych za wyliczanie `protectionPeriod`, jeśli nie jest to konieczne dla samego przestawienia układu.
- FR-006: Layout desktopowy i responsywny musi pozostać czytelny po zmianie kolejności, bez pogorszenia prezentacji dłuższej wartości `yyyy/MM/dd - yyyy/MM/dd`.

### Edge Cases
- EC-001: Na ekranach desktopowych szerokość kolumn musi nadal wspierać dłuższą wartość `Okres ochrony`; po zmianie kolejności pole nie może zostać przypadkowo ściśnięte bardziej niż obecnie.
- EC-002: Na breakpointach mobilnych, gdzie `offer-row__meta-grid` przechodzi do jednej kolumny, kolejność sekcji nadal musi pozostać zgodna z nowym wymaganiem.
- EC-003: Jeśli lista ofert jest pusta, zmiana kolejności nie wpływa na empty state i nie wymaga dodatkowej obsługi.
- EC-004: Zmiana nie może wpłynąć na istniejące testy związane z walutą prezentacji, ochroną okresu ani akcjami oferty poza konieczną aktualizacją oczekiwań dotyczących kolejności DOM.

### Success Criteria
- [ ] Na ekranie `/offers` `Okres ochrony` jest widoczny przed `Aktualizacja` w każdym wierszu oferty
- [ ] Wartość `Okres ochrony` pozostaje bez zmian względem obecnej implementacji
- [ ] Wartość `Aktualizacja` pozostaje bez zmian względem obecnej implementacji
- [ ] Układ listy pozostaje czytelny na desktopie i mobile
- [ ] Testy potwierdzają nową kolejność pól metadanych

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Aktualna lista ofert jest renderowana w `src/app/features/offers/pages/offers-home-page.component.html` jako kartowy układ z siatką metadanych `offer-row__meta-grid`, a nie jako klasyczna tabela. Dlatego wymaganie “kolumna przed kolumną” należy zrealizować przez zmianę kolejności bloków w tej siatce.

W repozytorium istnieje już logika okresu ochrony:
- `protectionPeriod` jest obliczane w `src/app/features/offers/pages/offers-home-page.component.ts`
- pole jest już renderowane w `offers-home-page.component.html`
- style siatki są zdefiniowane w `src/app/features/offers/pages/offers-home-page.component.scss`

Najmniejsza, zgodna z obecnym kodem zmiana to:
1. przenieść blok `offer-row__protection-period` przed blok `Aktualizacja` w HTML,
2. skorygować układ CSS tak, by szeroka kolumna nadal przypadała na `Okres ochrony`, a nie na `Aktualizacja`,
3. rozszerzyć testy jednostkowe i e2e o asercję kolejności pól.

Wykorzystanie wiedzy z Confluence:
- Dokument domenowy [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) potwierdza, że ekran ofert jest elementem kokpitu sprzedaży i zmiana powinna pozostać lokalna dla widoku listy ofert.
- No relevant existing service/API for this UI ordering change was found in the `Services` space — zadanie nie wymaga nowej ani istniejącej integracji usługowej.

### Task Breakdown

#### Phase 1: Przestawienie kolejności pól w widoku ofert
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Przenieść blok `Okres ochrony` przed `Aktualizacja` | `src/app/features/offers/pages/offers-home-page.component.html` | Zmienić kolejność elementów wewnątrz `offer-row__meta-grid`, tak aby `offer-row__protection-period` był renderowany przed sekcją `Aktualizacja`. |
| 1.2 | Zachować istniejącą zawartość pól | `src/app/features/offers/pages/offers-home-page.component.html` | Nie zmieniać bindingów `protectionPeriod` ani formatterów daty/godziny dla `offer.updatedAt`; modyfikacja ma dotyczyć wyłącznie układu. |

#### Phase 2: Dostosowanie układu siatki
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dopasować szerokości kolumn po zmianie kolejności | `src/app/features/offers/pages/offers-home-page.component.scss` | Zmienić `grid-template-columns` lub przypisanie szerokiej kolumny tak, aby `Okres ochrony` nadal miało wystarczającą szerokość po przesunięciu przed `Aktualizacja`. |
| 2.2 | Zachować responsywność | `src/app/features/offers/pages/offers-home-page.component.scss` | Utrzymać obecne breakpointy i nową kolejność bloków także w widoku jednokolumnowym dla `width <= 980px`. |
| 2.3 | Utrzymać czytelność wartości dat | `src/app/features/offers/pages/offers-home-page.component.scss` | Zweryfikować, czy `offer-row__protection-period-value` nadal nie łamie się niepożądanie na desktopie i zachowuje obecne zachowanie mobilne. |

#### Phase 3: Testy regresyjne kolejności
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Rozszerzyć test komponentu o kolejność etykiet | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Dodać asercję sprawdzającą kolejność etykiet metadanych w pierwszym wierszu oferty, z `Okres ochrony` przed `Aktualizacja`. |
| 3.2 | Zachować istniejące testy okresu ochrony | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Utrzymać obecne testy wartości `protectionPeriod`, aktualizując je tylko wtedy, gdy selektory lub oczekiwania zależą od starego porządku DOM. |
| 3.3 | Dodać/regresyjnie rozszerzyć test e2e | `tests/e2e/offers-protection-period.spec.ts` | Uzupełnić scenariusz Playwright o sprawdzenie, że w pierwszym `offer-row__meta-grid` etykieta `Okres ochrony` występuje przed etykietą `Aktualizacja`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Przestawienie kolejności bloków `Okres ochrony` i `Aktualizacja` w siatce metadanych oferty. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Korekta szerokości/układu kolumn po zmianie kolejności, aby `Okres ochrony` pozostało czytelne. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Dodanie testu jednostkowego potwierdzającego nową kolejność pól metadanych. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Rozszerzenie testu e2e o weryfikację kolejności `Okres ochrony` przed `Aktualizacja`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Na `/offers` pierwszy widoczny wiersz ma kolejność metadanych: `Pojazd/Uprawy`, `Kanał`, `Wariant`, `Okres ochrony`, `Aktualizacja`
5. [ ] Wartość `Okres ochrony` nadal pokazuje ten sam zakres co przed zmianą
6. [ ] Layout listy ofert pozostaje czytelny na desktopie i mobile