# SDLC-102: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Dodanie na ekranie listy ofert `/offers` nowego pola prezentacyjnego „Okres ochrony”, wyświetlanego dla każdej oferty w formacie `yyyy/MM/dd - yyyy/MM/dd`. Wartość ma być liczona dynamicznie od bieżącej daty systemowej używanej w aplikacji w momencie wyświetlenia listy i ma obejmować dokładnie jeden rok kalendarzowy. Zmiana dotyczy wyłącznie warstwy prezentacji na ekranie ofert i nie wymaga zmian w danych oferty ani nowych integracji.

### User Stories
- US-001: As a użytkownik listy ofert, I want widzieć okres ochrony przy każdej ofercie, so that mogę szybko sprawdzić zakres dat ochrony.
  - Given użytkownik otwiera ekran `/offers` / When lista ofert zostaje wyświetlona / Then przy każdej pozycji widoczny jest „Okres ochrony”

- US-002: As a użytkownik systemu, I want aby okres ochrony był liczony jednolicie dla wszystkich ofert, so that prezentowane dane są spójne w ramach jednego wyświetlenia listy.
  - Given lista zawiera wiele ofert / When ekran renderuje listę / Then wszystkie oferty pokazują ten sam zakres liczony od jednej bieżącej daty systemowej

- US-003: As a użytkownik listy ofert, I want aby wartość aktualizowała się po ponownym wejściu na ekran w innym dniu, so that widzę aktualny okres ochrony.
  - Given zmieniła się bieżąca data systemowa / When użytkownik ponownie wyświetli listę ofert / Then wartość „Okres ochrony” zostaje przeliczona od nowej daty

### Functional Requirements
- FR-001: W `src/app/features/offers/pages/offers-home-page.component.html` należy dodać nowe pole o etykiecie `Okres ochrony` dla każdej oferty na liście.
- FR-002: Wartość `Okres ochrony` musi być wyliczana z bieżącej daty systemowej w momencie prezentacji ekranu `/offers`, bez użycia danych z backendu i bez zmian w `Offer`.
- FR-003: Data początkowa musi odpowiadać bieżącej dacie systemowej aplikacji, a data końcowa musi być wyznaczona jako dokładnie 1 rok kalendarzowy później.
- FR-004: Format prezentacji musi być zawsze dokładnie `yyyy/MM/dd - yyyy/MM/dd`.
- FR-005: Wszystkie oferty widoczne na liście w ramach tego samego renderu ekranu muszą pokazywać identyczną wartość `Okres ochrony`.
- FR-006: Pole musi być widoczne niezależnie od statusu oferty, produktu (`MOTOR`, `CROP`) i bez dodatkowych warunków uprawnień w UI.
- FR-007: Implementacja nie może zmieniać istniejących źródeł danych w `OffersRepository`, modelu `Offer` ani logiki filtrowania, sortowania, statusów i akcji oferty.
- FR-008: Implementacja musi pozostać lokalna dla ekranu ofert i wykorzystywać istniejący wzorzec standalone component + helper methods/sygnały obecny w `OffersHomePageComponent`.

### Edge Cases
- EC-001: Jeśli bieżąca data przypada 29 lutego, data końcowa powinna być liczona jako rok kalendarzowy później zgodnie z przyjętą logiką helpera daty i zwracana w tym samym formacie.
- EC-002: Formatowanie nie może zależeć od locale przeglądarki ani `DatePipe`, jeśli mogłoby to zmienić separator lub przesunąć dzień przez strefę czasową.
- EC-003: Jeśli lista ofert jest pusta, ekran nadal działa poprawnie; brak ofert nie generuje osobnej obsługi dla okresu ochrony.
- EC-004: Oferty komunikacyjne i uprawowe muszą pokazywać taki sam okres ochrony, bo źródłem jest wyłącznie bieżąca data systemowa.
- EC-005: Zmiana daty systemowej bez ponownego wejścia na ekran nie musi odświeżać wartości live; kryterium mówi o ponownym wyświetleniu listy.

### Success Criteria
- [ ] Na ekranie `/offers` każda oferta pokazuje pole `Okres ochrony`
- [ ] Dla daty systemowej `2025/05/10` ekran pokazuje `2025/05/10 - 2026/05/10`
- [ ] Wszystkie oferty widoczne tego samego dnia pokazują identyczną wartość okresu ochrony
- [ ] Po ponownym otwarciu listy w innym dniu wartość ulega aktualizacji
- [ ] Nie ma zmian w modelu `Offer`, repozytorium ofert ani integracjach backendowych

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Aktualna implementacja listy ofert w `src/app/features/offers/pages/offers-home-page.component.*` nie jest tabelą PrimeNG, tylko kartową listą wierszy z siatką `offer-row__meta-grid`. Dlatego wymaganie „dodania kolumny” należy zrealizować zgodnie z istniejącym wzorcem UI: jako nowy, piąty element metadanych w każdym wierszu oferty, oznaczony etykietą `Okres ochrony`.

Zakres powinien pozostać wyłącznie w `OffersHomePageComponent`, ponieważ:
- dane są już pobierane przez `OffersRepository`, a nowe pole jest wyłącznie prezentacyjne,
- model `src/app/core/models/offer/offer.model.ts` nie zawiera i nie powinien zawierać tego pola,
- bieżąca logika ekranu centralizuje obliczenia pomocnicze w komponencie (`getPrimaryPremium`, `getOfferHeadlineSubject`, `getCropMetaPrimaryLine`, `summaryTiles`, `filteredOffers`), więc analogicznie należy dodać helpery dla okresu ochrony tutaj, a nie w repozytorium.

Najbezpieczniejsze rozwiązanie:
1. Dodać w `offers-home-page.component.ts` jeden wspólny, prezentacyjny punkt obliczenia okresu ochrony oparty na `new Date()` wykonanym raz dla instancji ekranu.
2. Wydzielić małe helpery do:
   - zbudowania daty końcowej +1 rok,
   - formatowania pojedynczej daty do `yyyy/MM/dd`,
   - zbudowania końcowego stringa zakresu.
3. Użyć tej jednej wartości w template dla każdego wiersza, aby zagwarantować identyczny wynik dla wszystkich ofert w tym samym renderze.
4. Nie używać Angular `date` pipe do tego pola, bo wymagany format ma stałe ukośniki i warto uniknąć ryzyka przesunięcia dnia przez strefę czasową przy konwersjach ISO/UTC.

Wykorzystanie wiedzy z Confluence:
- Dokument domenowy [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) potwierdza, że ekran ofert jest częścią kokpitu sprzedaży i zmiana powinna zostać ograniczona do widoku listy ofert.
- No existing notification/service/API relevant to this task was found in `Services` space — zmiana nie wymaga nowej ani istniejącej integracji usługowej.

### Task Breakdown

#### Phase 1: Logika wyliczania okresu ochrony
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać wspólną wartość okresu ochrony dla ekranu | `src/app/features/offers/pages/offers-home-page.component.ts` | Wprowadzić readonly property lub signal wyliczany z bieżącej daty systemowej raz przy tworzeniu komponentu, tak aby wszystkie oferty korzystały z tej samej wartości. |
| 1.2 | Wydzielić helpery dat | `src/app/features/offers/pages/offers-home-page.component.ts` | Dodać prywatne helpery np. do formatowania `yyyy/MM/dd`, dodania 1 roku kalendarzowego oraz budowy końcowego tekstu zakresu. |
| 1.3 | Zachować zakres tylko w warstwie prezentacji | `src/app/features/offers/pages/offers-home-page.component.ts` | Nie modyfikować `Offer`, `OffersRepository` ani obiektów z `filteredOffers()`; używać osobnej wartości prezentacyjnej w komponencie. |

#### Phase 2: Dodanie pola do listy ofert
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać nowy element `Okres ochrony` do siatki metadanych | `src/app/features/offers/pages/offers-home-page.component.html` | Rozszerzyć `offer-row__meta-grid` o nowy blok z etykietą `Okres ochrony` i wartością wyliczoną w komponencie. |
| 2.2 | Zachować spójność dla wszystkich wierszy | `src/app/features/offers/pages/offers-home-page.component.html` | Użyć tej samej wartości w każdej iteracji `@for (offer of filteredOffers())`, bez uzależnienia od konkretnej oferty. |
| 2.3 | Nie zmieniać istniejących akcji i pól | `src/app/features/offers/pages/offers-home-page.component.html` | Dodać nowe pole bez naruszania obecnych elementów: statusów, wariantu, kanału, aktualizacji, składki i akcji `p-splitbutton`. |

#### Phase 3: Dostosowanie layoutu i responsywności
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Rozszerzyć grid metadanych z 4 do 5 pól | `src/app/features/offers/pages/offers-home-page.component.scss` | Zmienić układ `.offer-row__meta-grid`, aby pomieścił nowe pole w desktopie bez rozbijania obecnego layoutu. |
| 3.2 | Utrzymać zachowanie mobilne | `src/app/features/offers/pages/offers-home-page.component.scss` | Zachować istniejące breakpointy, w których `.offer-row__meta-grid` przechodzi do jednej kolumny przy `width <= 980px`. |
| 3.3 | Dopasować czytelność dłuższej wartości dat | `src/app/features/offers/pages/offers-home-page.component.scss` | W razie potrzeby skorygować spacing lub szerokości, aby `yyyy/MM/dd - yyyy/MM/dd` mieściło się estetycznie w nowym polu. |

#### Phase 4: Testy jednostkowe i regresja
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Dodać test renderowania nowego pola | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Rozszerzyć istniejący spec o asercję, że każda oferta na liście pokazuje etykietę `Okres ochrony` i oczekiwany zakres. |
| 4.2 | Zamockować bieżącą datę systemową | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Użyć `jasmine.clock()` lub równoważnego mechanizmu, aby sprawdzić przypadek `2025/05/10 -> 2026/05/10`. |
| 4.3 | Zweryfikować jednolitość wartości dla wszystkich ofert | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Dodać test potwierdzający, że wiele wierszy renderuje dokładnie ten sam tekst okresu ochrony. |
| 4.4 | Zweryfikować ponowne wyświetlenie z nową datą | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Odtworzyć komponent z inną zamockowaną datą i potwierdzić, że wyświetlana wartość aktualizuje się po ponownym utworzeniu widoku. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Dodanie wspólnej logiki wyliczania i formatowania okresu ochrony na potrzeby listy ofert. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Dodanie nowego pola `Okres ochrony` do `offer-row__meta-grid` dla każdej oferty. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Dostosowanie siatki i spacingu do dodatkowego pola metadanych. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Rozszerzenie testów komponentu o scenariusze dynamicznego okresu ochrony opartego na dacie systemowej. |

### Verification Steps
1. [ ] `npm run build` succeeds
2. [ ] `npm test -- --watch=false` passes
3. [ ] New tests verify the exact value `2025/05/10 - 2026/05/10` for a mocked system date
4. [ ] The `/offers` screen shows `Okres ochrony` for every rendered offer
5. [ ] Existing filters, sorting, currency presentation, and offer actions still work unchanged