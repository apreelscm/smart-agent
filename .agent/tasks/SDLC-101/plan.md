# SDLC-101: Waluty

## Specification

### Overview
Dodanie na ekranie `/offers` możliwości wyboru waluty prezentacji składek pomiędzy EUR i USD, pobrania bieżącego kursu z usługi kursowej i przeliczenia składek ofert widocznych na liście bez modyfikowania danych źródłowych w PLN. Ekran ma też pokazywać kurs użyty do przeliczenia oraz zachować ostatnio poprawnie wyświetlone wartości, jeśli pobranie kursu się nie powiedzie.

### User Stories
- US-001: As a użytkownik listy ofert, I want wybrać EUR albo USD jako walutę prezentacji, so that mogę porównywać oferty w preferowanej walucie.
  - Given użytkownik jest na ekranie listy ofert i składki źródłowe są w PLN / When wybiera EUR albo USD / Then wszystkie widoczne składki są pokazane w wybranej walucie

- US-002: As a użytkownik listy ofert, I want aby wszystkie widoczne składki były liczone tym samym kursem, so that porównanie ofert jest spójne.
  - Given na liście jest wiele ofert / When użytkownik zmienia walutę / Then wszystkie widoczne składki są przeliczane według jednego bieżącego kursu pobranego dla tej operacji

- US-003: As a użytkownik listy ofert, I want widzieć kurs użyty do przeliczenia, so that wiem, na jakiej podstawie pokazano wartości.
  - Given kurs został poprawnie pobrany / When przeliczenie się powiedzie / Then ekran pokazuje kurs i datę kursu użytego do prezentacji

- US-004: As a użytkownik listy ofert, I want zachować dotychczasowe wartości przy błędzie integracji, so that ekran nie pokazuje częściowo przeliczonych danych.
  - Given użytkownik ma aktualnie widoczne składki / When pobranie kursu kończy się błędem albo nie zwraca kursu / Then wartości na ekranie pozostają bez zmian i widoczny jest komunikat błędu

### Functional Requirements
- FR-001: Ekran `src/app/features/offers/pages/offers-home-page.component.*` musi udostępnić selektor waluty prezentacji z opcjami `EUR` i `USD`, przy zachowaniu domyślnej prezentacji w `PLN` do momentu wyboru.
- FR-002: Zmiana waluty musi wywołać pobranie bieżącego kursu z udokumentowanej usługi kursowej NBP opisanej w Confluence: [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut) oraz [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-003: Runtime default integracji musi używać rzeczywistego endpointu `GET https://api.nbp.pl/api/exchangerates/rates/A/{targetCurrency}/?format=json` dla `EUR` i `USD`, bez mocka w ścieżce produkcyjnej.
- FR-004: Integracja musi mapować odpowiedź NBP w kształcie zawierającym co najmniej `code`, `table`, `rates[0].mid`, `rates[0].effectiveDate`; do przeliczenia PLN -> waluta docelowa należy użyć `Math.round(amountPln / mid)`.
- FR-005: Przeliczenie musi dotyczyć wyłącznie prezentacji składek na ekranie listy ofert, tj. wartości wyprowadzanych z `filteredOffers()` i powiązanych elementów UI na stronie ofert, bez mutowania obiektów `Offer`.
- FR-006: Wszystkie przeliczone kwoty muszą być zaokrąglane do 0 miejsc po przecinku i oznaczane kodem/symbolem wybranej waluty.
- FR-007: Ekran musi pokazywać kurs użyty do przeliczenia po poprawnym pobraniu danych, wraz z informacją o walucie docelowej i dacie kursu.
- FR-008: Przy błędzie HTTP, pustej odpowiedzi lub braku `rates[0].mid` system nie może zmieniać aktualnie prezentowanych wartości i musi wyświetlić komunikat błędu.
- FR-009: Każda kolejna zmiana pomiędzy EUR i USD musi wykonywać nowe pobranie kursu; wybór nie jest utrwalany po opuszczeniu ekranu.
- FR-010: Implementacja ma zachować istniejący wzorzec repozytoriów HTTP z `src/app/core/repositories/*.repository.ts` oraz sygnałów/computed signals używanych w `OffersHomePageComponent`.

### Edge Cases
- EC-001: Jeśli pierwsza próba wyboru EUR/USD zakończy się błędem, ekran pozostaje w domyślnej prezentacji PLN i pokazuje błąd.
- EC-002: Jeśli użytkownik ma już aktywną prezentację np. EUR i pobranie USD zakończy się błędem, ekran pozostaje na ostatnio poprawnej prezentacji EUR.
- EC-003: Jeśli API zwróci odpowiedź bez `rates[0]` albo z pustym `mid`, należy potraktować to jako błąd biznesowy i nie przeliczać danych.
- EC-004: Jeśli oferta nie ma `selectedPaymentPlan.totalPremium`, należy dalej używać istniejącego fallbacku do `variants[0]?.totalPremium`.
- EC-005: Jeśli lista po filtracji jest pusta, selektor waluty i informacja o kursie mogą pozostać dostępne, ale ekran nie może zgłaszać błędu samego braku ofert.
- EC-006: Jeśli użytkownik szybko zmienia wybór waluty, UI powinno zablokować kolejne żądanie do czasu zakończenia bieżącego albo zapewnić, że tylko ostatni poprawny wynik aktualizuje stan ekranu.

### Success Criteria
- [ ] Na `/offers` użytkownik może wybrać EUR albo USD i zobaczyć przeliczone składki wszystkich aktualnie widocznych ofert.
- [ ] Wszystkie prezentowane kwoty po przeliczeniu są zaokrąglone do pełnych wartości i opisane właściwą walutą.
- [ ] Ekran pokazuje kurs NBP użyty do przeliczenia oraz datę kursu.
- [ ] Przy błędzie pobrania kursu wartości na ekranie nie zmieniają się, a użytkownik widzi komunikat błędu.
- [ ] Dane źródłowe `Offer` pozostają w PLN i nie są nadpisywane prezentacyjnymi wartościami EUR/USD.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Implementacja powinna zostać osadzona bezpośrednio w istniejącym ekranie ofert, bo repo korzysta już ze standalone components, Angular signals i prostych repozytoriów HTTP (`OffersRepository`, `ReferenceDataRepository`). Najmniejsza i zgodna z aktualnym stylem zmiana to:

1. **Dodać nowe repozytorium HTTP dla kursów walut** w `src/app/core/repositories`, zamiast dopisywać logikę integracji do komponentu.
   - Repozytorium będzie analogiczne do istniejących klas repozytoriów, ale jako runtime default użyje realnej integracji opisanej w Confluence:
     - Strona katalogowa: [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
     - Źródło referencyjne: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
   - Endpoint runtime: `GET https://api.nbp.pl/api/exchangerates/rates/A/{targetCurrency}/?format=json`
   - Request: brak body, path param `targetCurrency` = `EUR | USD`
   - Response mapping: `table`, `code`, `rates[0].mid`, `rates[0].effectiveDate`
   - Auth: brak, publiczne API
   - **Nie dodajemy pliku mock JSON jako fallbacku runtime.** Mockowanie pojawi się tylko w testach przez `HttpTestingController` albo spy repozytorium.

2. **Rozszerzyć logikę `OffersHomePageComponent` o stan prezentacji waluty**, ale bez zmiany źródłowych modeli ofert.
   - Aktualny komponent i tak centralizuje obliczenia listy (`filteredOffers`, `summaryTiles`, `getPrimaryPremium`), więc to najlepsze miejsce na prezentacyjne przeliczenie.
   - Zamiast modyfikować obiekty `Offer`, należy dodać sygnały w stylu:
     - aktywnie zastosowana waluta prezentacji (`PLN | EUR | USD`)
     - aktywny kurs (`mid`, `effectiveDate`, `code`)
     - flaga ładowania
     - komunikat błędu
   - `getPrimaryPremium()` albo pomocnicze metody powinny dalej wyciągać **źródłową** składkę w PLN, a nowa warstwa helperów ma wyliczać kwotę wyświetlaną.

3. **Przeliczanie ograniczyć do bieżącego widoku `/offers`**, zgodnie z Jira i z kontekstem ekranu z dokumentu [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany).
   - W praktyce oznacza to zmianę renderingu na stronie listy ofert, a nie propagację waluty do innych ekranów, wizarda ofert czy danych zapisanych w runtime repository.
   - Ponieważ lista nie ma paginacji po stronie komponentu, “bieżący widok” odpowiada obecnemu wynikowi `filteredOffers()`.

4. **Zaktualizować wszystkie miejsca na ekranie ofert, które pokazują składkę**, aby nie mieszać walut na jednym ekranie.
   - Minimum:
     - premium box w każdym wierszu oferty
     - tile “Średnia składka”, bo jest liczony z widocznych ofert
     - kwota w dialogu przejścia statusu, jeśli pozostaje częścią tego samego widoku
   - Dzięki temu ekran pozostanie spójny po zmianie waluty.

5. **UI dla kursu i błędu dodać inline w istniejącym layoucie strony.**
   - Najprościej: umieścić selektor waluty obok filtrów/toolbara oraz dodać poniżej toolbaru sekcję “kurs użyty do przeliczenia” i komunikat błędu `role="alert"`.
   - To pasuje do obecnej struktury `offers-home-page.component.html` i wymaga tylko lokalnych zmian w SCSS.

6. **Brak relewantnej istniejącej implementacji repozytoryjnej w kodzie dla kursów walut.**
   - W repo znaleziono tylko mock-driven repozytoria ofert i danych referencyjnych; dlatego należy dodać nowe repozytorium, ale oprzeć je na istniejącym wzorcu klas `*.repository.ts`.
   - W przestrzeni SDLC nie znaleziono dodatkowych ADR-ów/specyfikacji walut poza ogólnym kontekstem ekranu ofert z dokumentu `Ekrany`.

### Task Breakdown

#### Phase 1: Integracja z usługą kursową
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać repozytorium kursów walut | `src/app/core/repositories/exchange-rates.repository.ts` | Utworzyć nową klasę repozytorium z `HttpClient`, która pobiera kurs dla `EUR` lub `USD` z endpointu NBP i mapuje odpowiedź do uproszczonego modelu domenowego używanego przez UI. |
| 1.2 | Zdefiniować typy waluty/prezentacji | `src/app/core/repositories/exchange-rates.repository.ts`, `src/app/core/models/common/money.model.ts` | Jeśli implementacja ma zwracać typowane kwoty poza PLN, poszerzyć typ waluty albo zdefiniować lokalne typy pomocnicze bez naruszania źródłowych danych ofert. |
| 1.3 | Pokryć repozytorium testem HTTP | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Zweryfikować poprawny request do `https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`, mapowanie `mid/effectiveDate` oraz obsługę błędnej odpowiedzi. |

#### Phase 2: Logika prezentacji waluty na liście ofert
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać stan waluty prezentacji do komponentu ofert | `src/app/features/offers/pages/offers-home-page.component.ts` | Wprowadzić sygnały dla aktywnej waluty, aktywnego kursu, loadingu i błędu; zachować domyślny stan PLN po wejściu na ekran. |
| 2.2 | Dodać obsługę wyboru EUR/USD i pobierania kursu | `src/app/features/offers/pages/offers-home-page.component.ts` | Podłączyć nowy `ExchangeRatesRepository`, pobierać kurs po zmianie wyboru, aktualizować tylko stan prezentacyjny i zachowywać poprzedni stan przy błędzie. |
| 2.3 | Wydzielić helpery przeliczeń | `src/app/features/offers/pages/offers-home-page.component.ts` | Rozdzielić źródłowe odczytanie składki w PLN od prezentacyjnego formatowania kwoty w PLN/EUR/USD, z zaokrągleniem `1.0-0`. |
| 2.4 | Zaktualizować obliczenia podsumowań | `src/app/features/offers/pages/offers-home-page.component.ts` | Przeliczać tile “Średnia składka” według aktywnej waluty i tego samego kursu co lista, bez zmiany logiki filtrowania. |
| 2.5 | Zachować spójność w dialogu statusowym | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.html` | Jeżeli dialog pokazuje składkę, użyć tych samych helperów prezentacyjnych co wiersze listy. |

#### Phase 3: Zmiany UI na ekranie ofert
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać selektor waluty do toolbara | `src/app/features/offers/pages/offers-home-page.component.html` | Wstawić `p-select` albo równoważny element zgodny z obecnym PrimeNG layoutem, z opcjami `EUR` i `USD` oraz placeholderem/domyslną informacją o PLN. |
| 3.2 | Dodać prezentację kursu i błędu | `src/app/features/offers/pages/offers-home-page.component.html` | Wyświetlić po udanym pobraniu kurs w czytelnej postaci, np. `1 EUR = 4,32 PLN`, oraz datę kursu; przy błędzie pokazać inline alert. |
| 3.3 | Podmienić renderowanie składek na dynamiczne | `src/app/features/offers/pages/offers-home-page.component.html` | Zastąpić sztywne `currency: 'PLN'` dynamiczną walutą wyliczaną przez komponent dla tiles, premium boxów i dialogu. |
| 3.4 | Dostosować style | `src/app/features/offers/pages/offers-home-page.component.scss` | Dodać style dla selektora waluty, bannera kursu, komunikatu błędu i stanów loading/disabled z zachowaniem obecnego układu responsywnego. |

#### Phase 4: Testy komponentu i regresja funkcjonalna
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Dodać testy komponentu ofert | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Pokryć scenariusze: domyślne PLN, poprawne przeliczenie po wyborze EUR, poprawne przeliczenie po wyborze USD, wyświetlenie kursu, zachowanie poprzedniego stanu po błędzie. |
| 4.2 | Użyć mocków tylko w testach | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/core/repositories/exchange-rates.repository.spec.ts` | Stubować repozytorium lub HTTP w testach; nie dodawać `public/mock/exchange-rates.json`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Nowe repozytorium integrujące runtime z publicznym API NBP dla kursów EUR/USD. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Testy repozytorium dla requestu, mapowania odpowiedzi i błędów. |
| `src/app/core/models/common/money.model.ts` | MODIFY | Opcjonalne poszerzenie typowania walut, jeśli będzie użyte w warstwie prezentacyjnej. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Stan waluty prezentacji, pobieranie kursu, helpery przeliczeń, zachowanie błędów i aktualizacja podsumowań. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Selektor waluty, prezentacja kursu, komunikat błędu i dynamiczne renderowanie składek. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style dla nowych elementów UI i ich responsywności. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Testy komponentu dla walut, kursów i zachowania przy błędzie. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements