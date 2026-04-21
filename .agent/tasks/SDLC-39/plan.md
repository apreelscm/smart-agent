# SDLC-39: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Wdrożenie wielowalutowej prezentacji i wprowadzania kwot w aplikacji Smart Agent dla walut `PLN`, `EUR`, `USD`, przy zachowaniu obecnego modelu danych: wartości biznesowe i runtime pozostają utrwalane w `PLN`, a waluta obca działa jako warstwa prezentacji oraz wejścia użytkownika.

Zmiana obejmie istniejące widoki, które dziś renderują kwoty wyłącznie przez twardo zakodowane formatowanie `PLN` w szablonach Angulara:
- lista ofert,
- lista polis,
- panel klientów,
- raporty,
- kreator oferty i jego podsumowania.

### User Stories
- US-001: As an agent, I want to switch the current view between PLN, EUR, and USD, so that I can discuss premiums and totals in the customer’s contract currency.
  - Given a view with monetary values / When I switch currency from PLN to EUR or USD / Then all amounts on that view are converted consistently, formatted correctly, and the applied rate is visible.

- US-002: As an agent, I want to enter supported monetary inputs in EUR or USD, so that I can work with partner pricing without manually converting to PLN.
  - Given an editable money field in the offer flow / When I choose EUR or USD and enter a value / Then the UI shows the approximate PLN equivalent and the persisted/runtime value remains PLN.

- US-003: As a sales manager, I want reports and customer summaries to use one selected currency for the whole page, so that comparisons stay readable and there is no mixed-currency table.
  - Given a report or customer summary / When I change the currency / Then all totals, rows, KPIs, and summaries on that page update to the same currency.

### Functional Requirements
- FR-001: Every view covered by the ticket must expose a consistent currency switcher with options `PLN | EUR | USD`.
- FR-002: Each covered view must initialize in `PLN`.
- FR-003: Switching currency must recalculate every monetary value rendered on the active view using one exchange-rate snapshot.
- FR-004: `PLN` formatting must remain visually unchanged from current behavior.
- FR-005: `EUR` and `USD` values must render with currency symbols and two decimal places; `PLN` must keep the current zero-decimal presentation.
- FR-006: When a foreign currency is selected, the view must display the applied rate and its effective date.
- FR-007: Editable money inputs in scope must allow selecting input currency and must persist converted `PLN` values into existing state/services.
- FR-008: Exchange rates must be fetched at runtime from the documented NBP integration in Confluence, using the real API as default runtime source: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-009: Runtime integration must use the documented NBP table endpoint `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, read `effectiveDate` and `rates[].{ code, mid }`, and use `EUR`/`USD` rates without any mock JSON fallback in production code.
- FR-010: The NBP integration must assume no authentication for the runtime call; if deployment requires proxying or URL override, that must be handled as configuration, not as a code-path fallback.
- FR-011: If exchange-rate retrieval fails or the required foreign rates are unavailable, the affected view must force `PLN`, disable foreign-currency entry, and show a clear user message.
- FR-012: Reports and summary tables must never mix currencies within the same rendered view state.

### Edge Cases
- EC-001: NBP response is available but missing `EUR` or `USD` rate — foreign currencies are disabled and the view falls back to `PLN`.
- EC-002: NBP call fails or times out — the view remains usable in `PLN`, with a visible degradation message.
- EC-003: User selected `EUR`/`USD`, then rates become unavailable on refresh/navigation — the view resets to `PLN`.
- EC-004: Empty or invalid foreign-currency input value — no conversion is persisted, validation message is shown, existing `PLN` value remains unchanged.
- EC-005: Rounding differences appear after conversion — display values follow presentation rules, but persisted values remain normalized `PLN`.
- EC-006: Navigation to a different page resets currency to `PLN`, in line with the Jira default-per-view behavior.
- EC-007: Aggregated views with totals and row values must use the same rate snapshot for the whole page render cycle.
- EC-008: Existing localStorage runtime offers/policies remain in `PLN` and must not require migration.

### Success Criteria
- [ ] Covered pages render a visible `PLN | EUR | USD` switcher and start in `PLN`
- [ ] All currently hardcoded `PLN` displays on covered pages convert consistently after switching currency
- [ ] Current `PLN` display remains unchanged versus existing UI
- [ ] Foreign-currency views show applied rate and effective date
- [ ] Editable money inputs in scope accept `EUR`/`USD` and persist converted `PLN` values
- [ ] On NBP unavailability, foreign currencies are disabled and the UI degrades to `PLN` with a clear message
- [ ] Unit tests cover conversion, formatting, and fallback behavior
- [ ] E2E smoke for currency switching passes on at least one covered page

### Open Questions
- [NEEDS CLARIFICATION: the current codebase exposes only discount-style monetary inputs in the offer flow; there is no existing editable premium field in `variants-step-page`/`crop-variants-step-page`. Should SDLC-39 apply foreign-currency entry only to existing editable money fields, or should it also introduce a new directly editable premium input in the wizard?]

---

## Implementation Plan

### Technical Approach
Repozytorium jest aplikacją Angular 20 opartą o standalone components, sygnały (`signal`, `computed`, `toSignal`) i prosty wzorzec repository w `src/app/core/repositories/*.ts`. Widoki renderują kwoty bezpośrednio w szablonach przez twardo wpisane pipe’y `currency: 'PLN'`, np.:
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/policies/pages/policies-home-page.component.html`
- `src/app/features/customers/pages/customers-page.component.html`
- `src/app/features/reports/pages/reports-page.component.html`
- `src/app/features/offer-wizard/pages/*.html`

Obecny model danych przechowuje wartości w PLN:
- `src/app/core/models/common/money.model.ts` ma `currency: 'PLN'`
- `src/app/core/models/policy/policy.model.ts` trzyma premie jako liczby
- `src/app/core/repositories/sales-flow-runtime.repository.ts` zapisuje runtime offers/policies do localStorage bez warstwy walutowej

Dlatego implementacja powinna **zachować persistence w PLN** i dołożyć osobną warstwę:
1. pobrania kursów,
2. konwersji `PLN <-> EUR/USD`,
3. formatowania,
4. lokalnego stanu wybranego widoku.

Plan reuse’uje istniejącą, udokumentowaną integrację kursową z Confluence: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut). Runtime defaultem będzie realne wywołanie NBP:
- endpoint: `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`
- auth: brak
- response: tablica tabel, z której pobieramy `effectiveDate` oraz `rates[].code` i `rates[].mid`
- używane kody: `EUR`, `USD`

Nie należy używać `public/mock/*.json` jako fallbacku runtime dla kursów. Mocki są dopuszczalne wyłącznie w testach (`HttpTestingController` / fixture responses).

W przestrzeni DOMAIN (`SDLC`) nie znaleziono dodatkowych konwencji ani ADR-ów ograniczających projekt, więc plan opiera się na aktualnych wzorcach repozytorium.

Kluczowe decyzje:
- **brak migracji danych** — wszystkie wartości biznesowe i runtime dalej zapisują się jako PLN;
- **view-scoped currency state** — wybór waluty resetuje się per widok, zamiast być globalną preferencją;
- **shared currency utilities** — zamiast powielać konwersję w każdej stronie, dodać wspólne repozytorium kursów, serwis konwersji, pipe wyświetlający i komponent przełącznika;
- **wspólna degradacja błędów** — ten sam mechanizm fallback do PLN dla wszystkich stron.

### Task Breakdown

#### Phase 1: Currency integration foundation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add currency domain types | `src/app/core/models/common/display-currency.model.ts`, `src/app/core/models/exchange-rate/exchange-rate.model.ts` | Dodać typy dla `PLN/EUR/USD`, snapshotu kursów i danych potrzebnych do konwersji bez zmiany istniejącego modelu persistence w PLN. |
| 1.2 | Add configurable NBP endpoint token | `src/app/core/config/exchange-rates.token.ts` | Zdefiniować token/konfigurację z domyślnym base URL NBP, aby endpoint runtime był jawny i ewentualnie nadpisywalny deploymentowo. |
| 1.3 | Implement runtime NBP repository | `src/app/core/repositories/exchange-rates.repository.ts` | Utworzyć repository oparte na `HttpClient`, zgodne ze stylem istniejących repositories, pobierające tabelę A z NBP i mapujące `EUR`/`USD` + `effectiveDate`. |
| 1.4 | Implement currency conversion service | `src/app/core/services/currency.service.ts` | Dodać serwis/fasadę z metodami `convertFromPln`, `convertToPln`, `formatDigits`, `foreignCurrencyAvailable`, spójnym fallbackiem do PLN i cache’em per sesja/aplikacja. |
| 1.5 | Add unit coverage for NBP mapping/fallback | `src/app/core/repositories/exchange-rates.repository.spec.ts`, `src/app/core/services/currency.service.spec.ts` | Przetestować mapowanie NBP, brak auth, obsługę błędów, brakujących kursów i wymuszenie fallbacku. |

#### Phase 2: Shared UI and presentation layer
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add reusable view-scoped currency store | `src/app/core/services/currency-view.store.ts` | Dodać lokalny store sygnałowy trzymający wybraną walutę dla pojedynczego widoku, startujący zawsze w `PLN`. |
| 2.2 | Add shared switcher component | `src/app/shared/ui/currency-switcher/currency-switcher.component.ts`, `src/app/shared/ui/currency-switcher/currency-switcher.component.html`, `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | Zaimplementować spójny przełącznik `PLN | EUR | USD` z disabled state dla walut obcych przy braku kursów. |
| 2.3 | Add shared exchange-rate note component | `src/app/shared/ui/currency-rate-note/currency-rate-note.component.ts`, `src/app/shared/ui/currency-rate-note/currency-rate-note.component.html`, `src/app/shared/ui/currency-rate-note/currency-rate-note.component.scss` | Dodać jednolity komponent pokazujący kurs i datę albo komunikat degradacji do PLN. |
| 2.4 | Add shared display pipe for PLN-backed amounts | `src/app/shared/pipes/display-money.pipe.ts`, `src/app/shared/pipes/display-money.pipe.spec.ts` | Zastąpić powtarzane pipe’y `currency: 'PLN'` wspólnym pipe’em, który przyjmuje kwotę bazową w PLN i bieżącą walutę widoku. |
| 2.5 | Add shared money-input component for PLN persistence | `src/app/shared/ui/currency-amount-input/currency-amount-input.component.ts`, `src/app/shared/ui/currency-amount-input/currency-amount-input.component.html`, `src/app/shared/ui/currency-amount-input/currency-amount-input.component.scss` | Zbudować komponent wejściowy z wyborem waluty, normalizacją do PLN, helperem `≈ ... PLN po kursie ...`, walidacją i blokadą EUR/USD przy braku kursów. |

#### Phase 3: Apply multi-currency to covered views
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Integrate offers list currency switching | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.html` | Podpiąć store waluty, przełącznik, informację o kursie oraz zamienić wszystkie prezentacje składek i statystyk na wspólny pipe. |
| 3.2 | Integrate policies list currency switching | `src/app/features/policies/pages/policies-home-page.component.ts`, `src/app/features/policies/pages/policies-home-page.component.html` | Przeliczyć `annualPremium`, `monthlyPremium`, kafle i dialogi w jednej walucie widoku, z fallbackiem do PLN. |
| 3.3 | Integrate customer panel currency switching | `src/app/features/customers/pages/customers-page.component.ts`, `src/app/features/customers/pages/customers-page.component.html` | Zmienić przypis klienta, listę ofert/polis i szczegóły klienta na jednolitą walutę widoku. |
| 3.4 | Integrate reports currency switching | `src/app/features/reports/pages/reports-page.component.ts`, `src/app/features/reports/pages/reports-page.component.html` | Przeliczyć KPI, trend, strukturę linii biznesowych i inne agregaty po jednym snapshotcie kursów dla całego raportu. |
| 3.5 | Integrate wizard shell and summary displays | `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.ts`, `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.html`, `src/app/features/offer-wizard/pages/summary-step-page.component.ts`, `src/app/features/offer-wizard/pages/summary-step-page.component.html` | Zapewnić wspólny, route-scoped stan waluty w kreatorze, przeliczyć podsumowania boczne, dialogi i ekran podsumowania. |
| 3.6 | Integrate motor variants page | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `src/app/features/offer-wizard/pages/variants-step-page.component.html` | Zamienić prezentację składek linii/wariantów na multi-currency oraz przepiąć istniejące pole zniżki na nowy komponent wejściowy zapisujący wynik w PLN. |
| 3.7 | Integrate crop variants page | `src/app/features/offer-wizard/pages/crop-variants-step-page.component.ts`, `src/app/features/offer-wizard/pages/crop-variants-step-page.component.html` | Analogicznie wprowadzić multi-currency dla składek działek, dodatków i pola zniżki. |
| 3.8 | Review other offer-flow money fields | `src/app/features/offer-wizard/pages/vehicle-step-page.component.ts`, `src/app/features/offer-wizard/pages/vehicle-step-page.component.html` | Zweryfikować istniejące editable money fields w kreatorze; jeśli występują w widoku końcowym po clarif, przepiąć je na komponent walutowy bez zmiany modelu zapisu. |

#### Phase 4: Regression protection and verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add page-level/component tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/features/reports/pages/reports-page.component.spec.ts` | Pokryć zmianę waluty, poprawne formatowanie i fallback do PLN na reprezentatywnych stronach listy i raportów. |
| 4.2 | Add wizard input tests | `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts`, `src/app/features/offer-wizard/pages/crop-variants-step-page.component.spec.ts` | Przetestować zapis zniżek w EUR/USD -> PLN, helper kursowy i blokadę wejść przy braku kursów. |
| 4.3 | Add end-to-end scenario | `tests/e2e/currency-switching.spec.ts` | Dodać scenariusz przełączenia waluty i widoczności kursu na co najmniej jednym widoku oraz degradacji do PLN przy błędzie integracji. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/display-currency.model.ts` | CREATE | Typy walut widoku i wejścia użytkownika |
| `src/app/core/models/exchange-rate/exchange-rate.model.ts` | CREATE | Model snapshotu kursów z `effectiveDate` |
| `src/app/core/config/exchange-rates.token.ts` | CREATE | Konfiguracja runtime endpointu NBP |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Realna integracja HTTP z NBP |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Testy mapowania i błędów integracji |
| `src/app/core/services/currency.service.ts` | CREATE | Konwersja PLN/EUR/USD i fallback do PLN |
| `src/app/core/services/currency.service.spec.ts` | CREATE | Testy konwersji, formatowania i degradacji |
| `src/app/core/services/currency-view.store.ts` | CREATE | Lokalny stan wybranej waluty per widok |
| `src/app/shared/pipes/display-money.pipe.ts` | CREATE | Wspólny pipe do prezentacji kwot bazowych w PLN |
| `src/app/shared/pipes/display-money.pipe.spec.ts` | CREATE | Testy reguł formatowania PLN/EUR/USD |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | UI przełącznika walut |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Szablon przełącznika walut |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Style przełącznika walut |
| `src/app/shared/ui/currency-rate-note/currency-rate-note.component.ts` | CREATE | Komponent informacji o kursie/degradacji |
| `src/app/shared/ui/currency-rate-note/currency-rate-note.component.html` | CREATE | Szablon informacji o kursie |
| `src/app/shared/ui/currency-rate-note/currency-rate-note.component.scss` | CREATE | Style informacji o kursie |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.ts` | CREATE | Walutowy komponent wejściowy zapisujący do PLN |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.html` | CREATE | Szablon walutowego inputu |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.scss` | CREATE | Style walutowego inputu |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Podpięcie stanu waluty i logiki prezentacji |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Switcher, kurs, nowe renderowanie kwot |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Multi-currency dla listy polis i dialogów |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Switcher i renderowanie składek w walucie widoku |
| `src/app/features/customers/pages/customers-page.component.ts` | MODIFY | Obsługa waluty w przypisach i pozycjach klienta |
| `src/app/features/customers/pages/customers-page.component.html` | MODIFY | Switcher i nowe renderowanie przypisów |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Konwersja KPI, trendów i agregatów |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Switcher i renderowanie raportów w jednej walucie |
| `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.ts` | MODIFY | Route-scoped stan waluty dla kreatora |
| `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.html` | MODIFY | Switcher i kurs w podsumowaniach kreatora |
| `src/app/features/offer-wizard/pages/summary-step-page.component.ts` | MODIFY | Multi-currency dla końcowego podsumowania |
| `src/app/features/offer-wizard/pages/summary-step-page.component.html` | MODIFY | Nowe renderowanie składki i linii ochrony |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Walutowy input zniżki i display składek |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Zastąpienie renderów PLN wspólnymi komponentami |
| `src/app/features/offer-wizard/pages/crop-variants-step-page.component.ts` | MODIFY | Walutowy input zniżki i display składek crop |
| `src/app/features/offer-wizard/pages/crop-variants-step-page.component.html` | MODIFY | Switcher/renderowanie wielowalutowe dla crop |
| `src/app/features/offer-wizard/pages/vehicle-step-page.component.ts` | MODIFY | Ewentualne przepięcie istniejących money inputs po potwierdzeniu zakresu |
| `src/app/features/offer-wizard/pages/vehicle-step-page.component.html` | MODIFY | UI dla money inputs w walutach obcych, jeśli wchodzi w zakres |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Testy listy ofert po zmianie waluty |
| `src/app/features/reports/pages/reports-page.component.spec.ts` | CREATE | Testy raportów w jednej walucie |
| `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts` | CREATE | Testy wejść walutowych w wariantach motor |
| `src/app/features/offer-wizard/pages/crop-variants-step-page.component.spec.ts` | CREATE | Testy wejść walutowych w wariantach crop |
| `tests/e2e/currency-switching.spec.ts` | CREATE | E2E dla przełączania walut i fallbacku |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test`
3. [ ] New tests cover NBP mapping, conversion, formatting, fallback to PLN, and foreign-currency inputs
4. [ ] `offers`, `policies`, `customers`, `reports`, and wizard summary screens start in PLN
5. [ ] Switching to EUR/USD updates all visible monetary values on a page consistently
6. [ ] Foreign-currency views show rate + effective date from NBP response
7. [ ] When NBP is unavailable, the UI forces PLN and disables EUR/USD inputs
8. [ ] Runtime data saved through existing repositories/localStorage remains PLN-based