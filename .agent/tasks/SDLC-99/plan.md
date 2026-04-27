# SDLC-99: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Zadanie rozszerza ekran `/offers` o nową kolumnę/pole prezentujące okres ochrony dla każdej oferty w formacie `yyyy/MM/dd - yyyy/MM/dd`. W aktualnej implementacji lista ofert jest renderowana jako karta z siatką metadanych, a nie klasyczna tabela, więc „kolumna” zostanie dodana jako kolejny element w istniejącej siatce `offer-row__meta-grid`, spójny wizualnie z pozostałymi polami.

Wartość ma być wyliczana po stronie UI na podstawie bieżącej daty z momentu wyświetlenia ekranu i ma obejmować dokładnie 1 rok. Zmiana dotyczy wyłącznie widoku listy ofert i nie wymaga zmian w danych oferty, repozytorium ani integracji backendowej.

### User Stories
- US-001: Jako agent sprzedaży przeglądający listę ofert, chcę widzieć okres ochrony przy każdej ofercie, żeby szybko porównać zakres czasowy ofert bez przechodzenia do szczegółów.
  - Given użytkownik otworzył ekran listy ofert
  - When lista ofert zostanie wyświetlona
  - Then przy każdej ofercie widoczny jest okres ochrony w stałym formacie dat

- US-002: Jako użytkownik biznesowy, chcę aby okres ochrony był liczony dynamicznie od dnia wyświetlenia ekranu i trwał dokładnie 1 rok, żeby informacja była spójna i jednoznaczna.
  - Given ekran listy ofert został otwarty w konkretnym dniu
  - When komponent wylicza okres ochrony
  - Then data początkowa odpowiada „dzisiaj”, a data końcowa jest równa dacie początkowej powiększonej o 1 rok

### Functional Requirements
- FR-001: Na ekranie listy ofert należy dodać nowe pole „Okres ochrony” w sekcji metadanych każdej oferty.
- FR-002: Wartość pola ma być wyliczana w komponencie `OffersHomePageComponent`, bez polegania na polach `offer.contractData.coverageStartDate` i `offer.contractData.coverageEndDate`.
- FR-003: Data początkowa ma być równa bieżącej dacie z momentu utworzenia/wyrenderowania ekranu listy ofert.
- FR-004: Data końcowa ma być wyliczana jako 1 rok od daty początkowej.
- FR-005: Format prezentacji ma być zawsze `yyyy/MM/dd - yyyy/MM/dd`, niezależnie od ustawień regionalnych przeglądarki.
- FR-006: Ta sama data początkowa i końcowa ma być prezentowana dla wszystkich ofert widocznych podczas jednego renderu ekranu.
- FR-007: Dodanie nowego pola nie może zmieniać działania obecnych filtrów, sortowania, liczników, statusów ani akcji oferty.
- FR-008: Zmiana ma pozostać ograniczona do widoku listy ofert; bez zmian w repozytorium danych, modelu `Offer` i plikach `public/mock/*.json`.

### Edge Cases
- EC-001: Jeżeli użytkownik otworzy ekran tuż przed północą, wszystkie oferty w bieżącym renderze powinny używać tej samej daty początkowej, a nie przeliczać się per wiersz.
- EC-002: Dla dat typu 29 lutego logika dodawania 1 roku powinna zwrócić poprawną datę kalendarzową końcową, z przycięciem do ostatniego poprawnego dnia miesiąca docelowego.
- EC-003: Format nie może zależeć od `LOCALE_ID = 'pl-PL'` ani od pipe’ów formatujących wg strefy czasowej w sposób przesuwający dzień.
- EC-004: Dodanie pola nie może psuć układu `offer-row__meta-grid` dla ofert komunikacyjnych i crop.
- EC-005: Brak danych ofertowych dotyczących ochrony nie jest błędem, ponieważ okres jest wyliczany wyłącznie prezentacyjnie od bieżącej daty.

### Success Criteria
- [ ] Na ekranie `/offers` każda oferta pokazuje nowe pole „Okres ochrony”.
- [ ] Wartość jest prezentowana dokładnie w formacie `yyyy/MM/dd - yyyy/MM/dd`.
- [ ] Data początkowa odpowiada bieżącej dacie z dnia wyświetlenia ekranu.
- [ ] Data końcowa jest wyliczana jako 1 rok od daty początkowej.
- [ ] Format jest identyczny dla wszystkich rekordów i nie zależy od ustawień regionalnych przeglądarki.
- [ ] Dodanie nowego pola nie wpływa na istniejące filtry, sortowanie, liczniki i akcje oferty.
- [ ] Nowe testy potwierdzają renderowanie i wyliczanie zakresu dat.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Ekran listy ofert jest zaimplementowany w `src/app/features/offers/pages/offers-home-page.component.ts` jako standalone Angular component oparty o `signal`, `computed` i `toSignal`. Dane są pobierane przez `OffersRepository`, ale wymaganie Jira oraz komentarz zgłaszającego jednoznacznie wskazują, że okres ochrony ma być liczony od „dzisiaj” na ekranie listy ofert, więc nie należy opierać się na istniejących polach `contractData.coverageStartDate` / `coverageEndDate`, mimo że występują w `public/mock/offers.json`.

Zmiana będzie wyłącznie prezentacyjna:
- bez modyfikacji `OffersRepository`,
- bez modyfikacji `Offer` w `src/app/core/models/offer/offer.model.ts`,
- bez modyfikacji mocków runtime.

Najmniej inwazyjne miejsce na nową „kolumnę” to istniejąca siatka `offer-row__meta-grid` w `src/app/features/offers/pages/offers-home-page.component.html`, która już prezentuje cztery pola metadanych. Zostanie tam dodany piąty blok z etykietą „Okres ochrony”.

Aby spełnić wymaganie „dzisiaj w momencie wyświetlenia ekranu”, komponent powinien wyliczyć zakres dat raz przy inicjalizacji instancji komponentu i udostępnić gotowy string do renderowania. Nie należy używać metody, która woła `new Date()` dla każdego wiersza lub przy każdym przebiegu change detection. Najprostsze rozwiązanie:
- utworzyć prywatne pole startu okresu wyliczone raz przy tworzeniu komponentu,
- dodać prywatny helper dodający 1 rok kalendarzowy,
- dodać prywatny helper formatujący datę ręcznie do `yyyy/MM/dd`,
- wystawić `protected readonly protectionPeriodLabel`.

To podejście:
- eliminuje zależność od locale przeglądarki,
- unika przesunięć dnia wynikających z pipe’a `date`,
- zachowuje identyczną wartość dla wszystkich ofert w danym renderze.

W warstwie stylów `src/app/features/offers/pages/offers-home-page.component.scss` trzeba dopasować układ siatki do dodatkowego pola, zachowując responsywność obecną na breakpointach.

Wnioski z Confluence:
- W przestrzeni DOMAIN znaleziono stronę [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), która opisuje aplikację `smart-agent` z perspektywy ekranów i potwierdza, że zmiana powinna pozostać lokalna dla ekranu listy ofert.
- W przestrzeni API/SERVICE `Services` nie znaleziono istniejącej usługi ani endpointu dotyczącego tego wymagania. Nie jest potrzebna nowa integracja; zmiana pozostaje frontendowa.
- Brak istniejącej usługi w `Services` oznacza, że plan nie wprowadza żadnej nowej integracji ani runtime fallback opartego o mock JSON.

### Task Breakdown

#### Phase 1: Dodać logikę wyliczania okresu ochrony
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać stały zakres dat dla ekranu | `src/app/features/offers/pages/offers-home-page.component.ts` | Wprowadzić prywatne/helperowe funkcje do: normalizacji bieżącej daty, dodania 1 roku kalendarzowego i sformatowania zakresu do `yyyy/MM/dd - yyyy/MM/dd`. |
| 1.2 | Udostępnić gotową wartość do template | `src/app/features/offers/pages/offers-home-page.component.ts` | Dodać `protected readonly protectionPeriodLabel` wyliczane raz przy inicjalizacji komponentu. |
| 1.3 | Zachować zakres zmian tylko w UI | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/core/models/offer/offer.model.ts`, `src/app/core/repositories/offers.repository.ts` | Nie modyfikować modelu oferty ani repozytorium; w planie implementacji jawnie oprzeć render na dacie systemowej, nie na danych oferty. |

#### Phase 2: Dodać nowe pole do listy ofert i dopasować layout
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać blok „Okres ochrony” do metadanych oferty | `src/app/features/offers/pages/offers-home-page.component.html` | W `offer-row__meta-grid` dodać nowy blok z etykietą i sformatowaną wartością `protectionPeriodLabel`. |
| 2.2 | Dostosować responsywną siatkę metadanych | `src/app/features/offers/pages/offers-home-page.component.scss` | Zmienić układ `offer-row__meta-grid`, aby piąte pole było czytelne na desktopie i nadal poprawnie zwijało się na mniejszych szerokościach. |
| 2.3 | Zabezpieczyć czytelność długiego stringa dat | `src/app/features/offers/pages/offers-home-page.component.scss` | W razie potrzeby doprecyzować styl `strong`/konkretnego bloku, aby zakres dat nie powodował łamania layoutu. |

#### Phase 3: Dodać testy komponentu
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Utworzyć spec komponentu listy ofert | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Dodać testy standalone component z podstawionymi stubami repozytoriów i routera. |
| 3.2 | Zamrozić datę systemową w testach | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Użyć kontrolowanego czasu, aby potwierdzić deterministyczny wynik formatowania. |
| 3.3 | Zweryfikować render i format | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Sprawdzić obecność etykiety „Okres ochrony” oraz stringa w formacie `yyyy/MM/dd - yyyy/MM/dd`. |
| 3.4 | Zweryfikować brak regresji widoku | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Potwierdzić, że komponent nadal renderuje oferty i istniejące elementy listy po dodaniu nowego pola. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Doda logikę wyliczania i formatowania okresu ochrony wyliczanego od bieżącej daty ekranu. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Doda nowe pole „Okres ochrony” do siatki metadanych każdej oferty. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Dostosuje layout i responsywność po dodaniu piątego pola metadanych. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Doda testy komponentu dla renderowania i poprawnego wyliczania okresu ochrony. |

### Verification Steps
1. [ ] Build succeeds (`npm run build`)
2. [ ] Tests pass (`npm run test -- --watch=false --browsers=ChromeHeadless`)
3. [ ] New tests cover requirements
4. [ ] Ekran `/offers` pokazuje nowe pole „Okres ochrony” dla każdej oferty
5. [ ] Wartość ma format `yyyy/MM/dd - yyyy/MM/dd`
6. [ ] Data początkowa odpowiada dniowi renderu komponentu
7. [ ] Data końcowa jest równa 1 rok od daty początkowej
8. [ ] Układ listy ofert pozostaje czytelny dla desktop i breakpointów już obsługiwanych w `offers-home-page.component.scss`