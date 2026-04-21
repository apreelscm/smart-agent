# SDLC-41: Wielowalutowość na liście ofert

## Specification

### Overview
Zmiana wprowadza przełącznik waluty prezentacji na widoku listy ofert (`/offers`), tak aby agent mógł oglądać kwoty w PLN, EUR albo USD bez modyfikowania danych źródłowych. PLN pozostaje domyślne przy każdym wejściu na widok. Kursy mają być pobierane runtime z zewnętrznej usługi kursowej, a przy niedostępności tej usługi widok ma pozostać w PLN z wyłączonymi opcjami EUR/USD i jasnym komunikatem.

### User Stories
- US-001: As a sales agent, I want to switch the offers list between PLN, EUR, and USD, so that I can discuss offers in the customer’s contract currency.
  - Given the agent opens the offers list
  - When the page loads
  - Then the list starts in PLN and no persisted data is changed

- US-002: As a sales agent, I want all displayed monetary values on the offers list to change together, so that I do not see mixed currencies in one view.
  - Given the offers list is visible and exchange rates are available
  - When the agent switches from PLN to EUR or USD
  - Then all rendered list amounts and list-level summaries are recalculated and formatted in the selected currency

- US-003: As a sales agent, I want to know which exchange rate was used, so that I can explain the displayed values to the customer.
  - Given the list is displayed in EUR or USD
  - When the conversion is applied
  - Then the UI shows the applied PLN/rate and effective date

- US-004: As a sales agent, I want the screen to remain usable when the exchange-rate service is unavailable, so that I can still work in PLN.
  - Given the exchange-rate service cannot be reached
  - When the offers list loads
  - Then PLN remains available, EUR/USD are disabled, and the UI explains why

### Functional Requirements
- FR-001: Add a currency switcher to `src/app/features/offers/pages/offers-home-page.component.html` with options `PLN | EUR | USD`.
- FR-002: The switcher must default to `PLN` on every fresh entry to `/offers`; the choice must not be persisted in local storage, profile settings, or offer data.
- FR-003: The offers list must fetch current EUR and USD rates at runtime from the documented NBP integration in Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- FR-004: Runtime default integration must call the real NBP endpoint `GET https://api.nbp.pl/api/exchangerates/tables/A?format=json`, with no auth, and map the returned table A payload to EUR/USD rates plus `effectiveDate`.
- FR-005: The conversion source of truth remains PLN; no offer, policy, wizard, or runtime storage record may be rewritten when display currency changes.
- FR-006: All monetary values currently rendered on the offers list page must use the selected display currency consistently, including:
  - row premium in the `premium-box`
  - list-level money summary in `summaryTiles` (`Średnia składka`)
- FR-007: Currency formatting must follow the Jira rules:
  - PLN: existing PLN display preserved, no grosze (`1.0-0`)
  - EUR/USD: 2 decimal places (`1.2-2`)
  - locale remains `pl-PL`
- FR-008: When EUR/USD is selected, the page must display the applied rate and effective date in a discreet but explicit place near the switcher or results metadata.
- FR-009: When the FX service is unavailable or rates are missing, the component must force `PLN`, disable EUR/USD actions, and show a clear user message.
- FR-010: The implementation must reuse the existing repository pattern in `src/app/core/repositories` and the existing signal/computed pattern already used in `OffersHomePageComponent`.
- FR-011: Mock JSON files under `public/mock` may not be used as runtime fallback for exchange rates; they are allowed only in tests.

### Edge Cases
- EC-001: If the NBP request fails during page load, the page still renders offers in PLN and the switcher explains that EUR/USD are unavailable.
- EC-002: If the agent is on EUR/USD and a refresh/re-entry occurs, the page starts again in PLN.
- EC-003: If the NBP payload does not include one of the expected currencies (`EUR` or `USD`), that foreign-currency mode remains unavailable and PLN stays active.
- EC-004: Crop offers have premium amounts but may not expose other insurance-sum fields on this screen; only values actually rendered on the list page are converted.
- EC-005: Summary counts and non-money metadata must remain unchanged when currency changes.
- EC-006: Changing currency must not trigger navigation, persistence, or status transitions.
- EC-007: Unit and e2e tests must stub the NBP HTTP call; production code must not fall back to a local mock file.

### Success Criteria
- [ ] `/offers` shows a visible `PLN | EUR | USD` switcher
- [ ] Entering `/offers` always starts in PLN
- [ ] Switching to EUR/USD converts all rendered money values on the page consistently
- [ ] PLN formatting remains visually unchanged from current behavior
- [ ] EUR/USD formatting uses 2 decimal places in `pl-PL`
- [ ] The applied exchange rate and effective date are shown in EUR/USD mode
- [ ] If FX is unavailable, EUR/USD are disabled and PLN remains usable
- [ ] No offer data is written or mutated because of currency switching
- [ ] Automated tests cover success and service-unavailable paths

### Open Questions
- [NEEDS CLARIFICATION: Czy w ramach tego ticketu obecny widok `/offers` ma zacząć renderować „sumę ubezpieczenia” w wierszu oferty? Aktualny szablon `offers-home-page.component.html` pokazuje tylko składkę i kafelek `Średnia składka`; jeśli trzeba dodać nową pozycję „suma ubezpieczenia”, należy wskazać, którą wartość wybrać dla wariantów zawierających wiele pól `SUM_INSURED`.]

---

## Implementation Plan

### Technical Approach
Widok listy ofert jest już zbudowany w oparciu o Angular signals i `computed()` w `src/app/features/offers/pages/offers-home-page.component.ts`, a dane HTTP są pobierane przez lekkie repozytoria w `src/app/core/repositories`. Najmniej inwazyjne rozwiązanie to dołożenie osobnego repozytorium kursów walut i utrzymanie całej logiki prezentacyjnej w `OffersHomePageComponent`, bez zmiany przepływu danych ofert.

Reużyta integracja:
- **NBP exchange rates API** z Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- Runtime default:
  - endpoint: `GET https://api.nbp.pl/api/exchangerates/tables/A?format=json`
  - auth: brak (publiczne API)
  - expected shape: tablica tabel A, z polami typu `effectiveDate` oraz `rates[]`, gdzie każda pozycja zawiera co najmniej `code` i `mid`
  - mapping używany w UI: `EUR -> mid`, `USD -> mid`, `effectiveDate -> data kursu`
- Konwersja: ponieważ dane ofert są zapisane w PLN, przeliczenie do EUR/USD będzie wykonywane jako `amountPln / mid`.

Wnioski z repo:
- `src/app/app.config.ts` ma już `provideHttpClient()`, więc dodatkowa konfiguracja HTTP nie jest potrzebna.
- `src/app/core/repositories/offers.repository.ts` pokazuje aktualny wzorzec repozytorium HTTP; nowe repo kursowe powinno go naśladować.
- `src/app/features/offers/pages/offers-home-page.component.ts` centralizuje filtrowanie, sortowanie, summary tiles i rendering pomocniczy; tam należy dodać stan `selectedCurrency`, odczyt kursów i helpery formatujące.
- `src/app/core/models/common/money.model.ts` dziś ogranicza walutę do `'PLN'`; warto rozszerzyć typ do `'PLN' | 'EUR' | 'USD'`, żeby typy wspierały obiekty prezentacyjne bez naruszania istniejących danych.
- `public/mock/offers.json` pozostaje źródłem mockowych ofert, ale **nie** może zostać użyte jako runtime fallback dla kursów FX.
- W przestrzeni DOMAIN `SDLC` nie znaleziono dodatkowych ADR-ów ani konwencji ograniczających projekt; plan opiera się na wzorcach repozytorium.

Implementacyjnie:
1. dodać `ExchangeRatesRepository` w `core/repositories`, które mapuje odpowiedź NBP do małego modelu UI;
2. w `OffersHomePageComponent` dodać:
   - `selectedCurrency` signal
   - `exchangeRatesState` signal z obsługą success/unavailable
   - helpery `convertAmountFromPln`, `formatAmount`, `rateCaption`, `currencyDisabled`
3. podmienić obecny twardy `currency: 'PLN'` pipe w `offers-home-page.component.html` na render oparty o helper formatujący;
4. dodać przełącznik waluty i komunikat o kursie / niedostępności w toolbarze lub `results-meta`;
5. przetestować mapowanie NBP i zachowanie UI w wariancie success/failure.

### Task Breakdown

#### Phase 1: Dodać integrację kursów walut
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Rozszerzyć typ waluty | `src/app/core/models/common/money.model.ts` | Zmienić `Money.currency` z `'PLN'` na union wspierający `PLN`, `EUR`, `USD`, bez zmiany istniejących danych mockowych. |
| 1.2 | Dodać repozytorium kursów | `src/app/core/repositories/exchange-rates.repository.ts` | Utworzyć nowe repozytorium Angular `Injectable`, używające `HttpClient` do wywołania NBP table A endpoint i mapowania `effectiveDate`, `EUR`, `USD`. |
| 1.3 | Dodać test repozytorium | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Zweryfikować `HttpTestingController`, poprawny request do NBP, mapowanie payloadu i obsługę brakujących kursów. |

#### Phase 2: Wprowadzić wielowalutową prezentację na liście ofert
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać stan wybranej waluty | `src/app/features/offers/pages/offers-home-page.component.ts` | Dodać `selectedCurrency` jako signal z domyślnym `PLN`; nie zapisywać wyboru do `localStorage`. |
| 2.2 | Załadować kursy do komponentu | `src/app/features/offers/pages/offers-home-page.component.ts` | Wstrzyknąć `ExchangeRatesRepository`, pobrać kursy przez `toSignal`, zmapować stan `ready/unavailable`, i wymuszać `PLN` przy błędzie. |
| 2.3 | Dodać helpery konwersji i formatowania | `src/app/features/offers/pages/offers-home-page.component.ts` | Dodać metody/computed do przeliczania z PLN na EUR/USD, formatowania wg `pl-PL`, budowania podpisu kursu i sprawdzania dostępności walut. |
| 2.4 | Przeliczyć summary tile z kwotą | `src/app/features/offers/pages/offers-home-page.component.ts` | Zmienić `summaryTiles`, aby `Średnia składka` używała wybranej waluty, zachowując obecny sposób liczenia średniej. |
| 2.5 | Przeliczyć kwoty w wierszu oferty | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.ts` | Zastąpić obecne użycie Angular `currency` pipe o stałym `'PLN'` renderem zależnym od `selectedCurrency`. |
| 2.6 | Dodać przełącznik waluty i komunikaty | `src/app/features/offers/pages/offers-home-page.component.html` | Umieścić trzy przyciski/toggle `PLN | EUR | USD`, pokazać caption z kursem przy EUR/USD i komunikat o niedostępności przy awarii FX. |
| 2.7 | Dostosować styling i responsywność | `src/app/features/offers/pages/offers-home-page.component.scss` | Dodać style dla switchera, stanu aktywnego/disabled i podpisu kursowego, zgodne z istniejącym układem toolbaru. |

#### Phase 3: Zabezpieczyć zachowanie testami
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać test komponentu | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Przetestować: start w PLN, przeliczenie do EUR/USD, widoczność kursu, oraz wymuszenie PLN przy błędzie repozytorium. |
| 3.2 | Dodać test e2e dla przełącznika | `tests/e2e/offers-currency-switcher.spec.ts` | Dodać scenariusz Playwright z przechwyceniem requestu do `https://api.nbp.pl/**`, aby deterministycznie sprawdzić zmianę waluty i komunikat fallbackowy. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Rozszerzenie typu waluty dla obiektów prezentacyjnych. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Repozytorium runtime integrujące się z NBP API. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Test mapowania i requestu do NBP endpointu. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Stan waluty, pobieranie kursów, konwersja i formatowanie kwot, fallback do PLN. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Switcher waluty, caption kursowy, disabled state dla EUR/USD, render kwot zależny od waluty. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Stylowanie switchera i komunikatów FX w obecnym layoucie toolbaru. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Testy komponentu dla success/failure ścieżki FX. |
| `tests/e2e/offers-currency-switcher.spec.ts` | CREATE | Test end-to-end dla interakcji użytkownika z przełącznikiem walut. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `/offers` starts in PLN after every page entry
5. [ ] EUR/USD become selectable only when NBP rates load successfully
6. [ ] Premium row values and `Średnia składka` update immediately after currency switch
7. [ ] EUR/USD mode shows applied rate and effective date
8. [ ] Simulated FX failure forces PLN and shows a clear disabled-state message