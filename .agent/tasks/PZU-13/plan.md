# PZU-13: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Wdrożenie wielowalutowej prezentacji i wprowadzania kwot w aplikacji Smart Agent dla zakresu MVP: PLN, EUR, USD. System ma nadal utrwalać dane wyłącznie w PLN, natomiast użytkownik ma móc:
- przełączać widok kwot w ofertach, polisach i raportach,
- wprowadzać kwoty składek w kreatorze oferty w EUR/USD,
- widzieć zastosowany kurs i pomocniczą równowartość w PLN,
- otrzymać bezpieczną degradację do PLN przy niedostępności usługi kursowej.

### User Stories
- US-001: As an agent, I want to switch visible amounts between PLN, EUR, and USD on offers and policies, so that I can discuss pricing in the customer’s contract currency.
  - Given a supported view with monetary values / When I select EUR or USD / Then all amounts on that view are recalculated consistently and shown in the selected currency with the applied rate.

- US-002: As an agent, I want to enter premium amounts in EUR or USD in the offer wizard, so that I can work from partner price lists without manual conversion.
  - Given the variants step is open and exchange rates are available / When I choose EUR or USD and type a value / Then I see the entered amount in that currency and an immediate approximate PLN equivalent based on the current rate.

- US-003: As a sales manager, I want reports to be displayed in one chosen currency, so that I can present consistent portfolio totals to stakeholders.
  - Given the reports page is open / When I switch currency / Then KPI tiles, trend values, and business-line totals are shown in a single selected currency.

- US-004: As a user, I want the system to fall back safely to PLN if the exchange-rate service is unavailable, so that I avoid misleading foreign-currency calculations.
  - Given exchange rates cannot be loaded / When I open a covered view / Then PLN remains available, foreign-currency selection is disabled or reset, and the UI explains why.

### Functional Requirements
- FR-001: Introduce a shared currency domain model supporting exactly `PLN | EUR | USD`.
- FR-002: Replace the current single-currency `Money` model in `src/app/core/models/common/money.model.ts` so that app-level monetary values can be represented in any supported currency.
- FR-003: Add a dedicated exchange-rate integration that uses the documented NBP API as the runtime default, based on Confluence page [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-004: The runtime integration must fetch current average rates for EUR and USD from NBP; production code must not use a mock JSON fallback for exchange rates.
- FR-005: Add a shared currency/exchange-rate service responsible for:
  - loading rates,
  - exposing availability state,
  - converting values between PLN and EUR/USD,
  - formatting amounts per currency,
  - exposing rate metadata for UI display.
- FR-006: Every covered view must default to PLN when opened.
- FR-007: Currency selection is contextual to the current view and is not persisted across screens in MVP.
- FR-008: Add a unified currency switcher component or shared UI pattern reusable in offers, policies, reports, and offer wizard.
- FR-009: On offers list (`src/app/features/offers/pages/offers-home-page.component.*`), all visible premiums and premium-derived summary tiles must switch between PLN/EUR/USD consistently.
- FR-010: On policies list (`src/app/features/policies/pages/policies-home-page.component.*`), annual and monthly premium values must switch between PLN/EUR/USD consistently.
- FR-011: On reports page (`src/app/features/reports/pages/reports-page.component.*`), KPI values, monthly trend values, and business-line totals must switch between PLN/EUR/USD consistently using one currency per whole page.
- FR-012: On the offer wizard variants step (`src/app/features/offer-wizard/pages/variants-step-page.component.*`), the user must be able to select input currency for editable premium/discount-related amounts in scope and enter a value in EUR/USD.
- FR-013: When a user enters a premium value in EUR/USD, the system must convert it to PLN immediately and store/update the draft offer in PLN only.
- FR-014: The UI must display a helper text near edited amounts in foreign currency showing the approximate PLN equivalent and the applied rate date.
- FR-015: When a persisted PLN amount is re-opened and shown in EUR/USD, the display must use the current rate, not any historical input value.
- FR-016: PLN formatting must remain visually identical to the current behavior across existing screens.
- FR-017: EUR and USD formatting must use two decimal places; PLN should remain at the current zero-decimal display standard where already used.
- FR-018: Each foreign-currency view must show the applied rate and date in a discrete but explicit way.
- FR-019: If exchange rates are unavailable, all affected views must force PLN presentation and show a clear informational message.
- FR-020: If exchange rates are unavailable, EUR/USD input controls in the offer wizard must be disabled.
- FR-021: Runtime repositories that derive or persist premium values, including `SalesFlowRuntimeRepository`, must continue to store and compute persisted business values in PLN.
- FR-022: Unit tests must cover conversion logic, formatting rules, rate-unavailable degradation, and at least one representative component integration path.

### Edge Cases
- EC-001: If only one of EUR/USD rates is unavailable, the UI should treat foreign-currency mode as unavailable for the whole switcher and fall back to PLN to avoid partial behavior.
- EC-002: If the NBP API responds but lacks one expected currency code, the app should mark rates unavailable and disable EUR/USD.
- EC-003: If a view is already open in EUR/USD and the rate refresh fails, the view should reset to PLN and show an informational message rather than keeping stale foreign values silently.
- EC-004: Entering `0`, empty input, or invalid numeric text in foreign-currency entry fields should not corrupt persisted PLN data.
- EC-005: Reopening an offer entered previously in USD may display a slightly different USD amount than originally typed; the UI must explain that foreign presentation is based on the current rate.
- EC-006: Summary tiles and totals must never mix currencies within one view.
- EC-007: Currency switching must not mutate source offer/policy/report data objects; it is a presentation-layer transformation except for explicit foreign-currency data entry in forms.
- EC-008: Existing mock-backed repositories for offers/policies/reference data may remain for their current data sources, but exchange-rate loading must not introduce a runtime mock fallback.
- EC-009: If the reports date range yields no data, currency switcher and rate label should still behave consistently without errors.
- EC-010: Local-storage runtime data created by `SalesFlowRuntimeRepository` must remain compatible after the `Money` model broadens beyond PLN.

### Success Criteria
- [ ] A shared supported-currency model exists and is used consistently in monetary types.
- [ ] NBP exchange rates are loaded at runtime via HTTP integration documented in Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- [ ] Offers page defaults to PLN and can switch all displayed premiums to EUR/USD.
- [ ] Policies page defaults to PLN and can switch all displayed premiums to EUR/USD.
- [ ] Reports page defaults to PLN and can switch KPIs, trend values, and line totals to EUR/USD consistently.
- [ ] Variants step allows entering covered monetary values in EUR/USD while persisting PLN only.
- [ ] Foreign-currency helper text shows PLN equivalent, rate, and rate date.
- [ ] PLN display remains unchanged for existing users who do not switch currency.
- [ ] When rates are unavailable, affected views degrade to PLN and EUR/USD entry is disabled with a clear message.
- [ ] Automated tests cover conversion, formatting, and degraded behavior.

### Open Questions
- [NEEDS CLARIFICATION: which exact variants-step fields are in scope for foreign-currency data entry beyond premium display and discount-related values? The Jira explicitly mentions premium entry, but the current `variants-step-page` does not expose a direct editable premium field.]

---

## Implementation Plan

### Technical Approach
Aplikacja jest Angular 20 + standalone components + signals. Dane ofert, polis i referencji są pobierane przez proste repozytoria HTTP (`OffersRepository`, `PoliciesRepository`, `ReferenceDataRepository`), a runtime state jest trzymany przez signals i localStorage w `SalesFlowRuntimeRepository`. Obecny model pieniędzy jest sztucznie ograniczony do PLN w `src/app/core/models/common/money.model.ts`, a ekrany renderują kwoty bezpośrednio przez `CurrencyPipe` z zakodowanym `'PLN'`.

Plan zakłada dołożenie wspólnej warstwy walutowej i odseparowanie:
1. **persisted business value** = zawsze PLN,
2. **view/input currency** = PLN/EUR/USD zależnie od kontekstu widoku.

Reuse over rebuild:
- Integracja kursowa zostanie oparta na istniejącej dokumentacji Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- Nie znaleziono dodatkowych ADR/convention pages w przestrzeni DOMAIN `PZU`.
- Repo nie ma jeszcze wspólnego serwisu walutowego ani istniejącej integracji kursowej, więc zostanie dodany nowy komponent infrastrukturalny zgodny ze stylem repozytoriów Angular service + signals.

#### Integration default
Zgodnie z Confluence runtime default ma korzystać z publicznego API NBP, nie z pliku mock.
Implementacja powinna użyć endpointu tabeli kursów średnich NBP dla tabeli A, np.:
- `GET https://api.nbp.pl/api/exchangerates/tables/A?format=json`
albo równoważnie per waluta, jeśli tak wskazuje dokumentacja page snippet.
Serwis ma z odpowiedzi wyciągnąć kursy dla `EUR` i `USD`, datę publikacji tabeli i używać ich jako źródła prawdy dla przeliczeń:
- PLN -> EUR/USD: `amountPln / rate`
- EUR/USD -> PLN: `amountForeign * rate`

#### Codebase alignment
- `toSignal(...)`, `signal(...)`, `computed(...)` są dominującym wzorcem w feature pages.
- Ekrany list używają lokalnych helperów do prezentacji, więc do walut należy dodać helpery/computed zamiast przepisywać cały flow danych.
- `SalesFlowRuntimeRepository` już operuje na runtime-normalized danych; trzeba dopilnować, by zapisywane `Offer`/`Policy` nadal zawierały wartości biznesowe w PLN.
- `CurrencyPipe` jest obecnie używany bezpośrednio w template; dla spójności wielowalutowej lepiej wprowadzić wspólny formatter/helper lub custom pipe bazujący na nowym currency facade, tak aby nie mnożyć ręcznego ustawiania digitsInfo/rate handling w każdym template.

#### Proposed architecture additions
1. **Core currency domain**
   - `supported-currency.model.ts`
   - rozszerzenie `Money` do `currency: SupportedCurrency`
   - typy dla `ExchangeRateTable`, `ExchangeRateAvailability`, `ConvertedMoneyView`

2. **Exchange-rate integration**
   - `src/app/core/repositories/exchange-rates.repository.ts`
   - HTTP call do NBP API
   - mapowanie odpowiedzi do modelu wewnętrznego

3. **Currency application service**
   - `src/app/core/services/currency.service.ts`
   - ładowanie kursów,
   - sygnały: selected currency per host view instance albo stateless API + local component signal,
   - helpery `convertFromPln`, `convertToPln`, `formatMoney`, `rateLabel`, `isForeignCurrencyAvailable`

4. **Reusable UI**
   - np. `src/app/shared/ui/currency-switcher/...`
   - wejścia: selected currency, enabled state, availability message, rate label
   - użycie na offers/policies/reports/variants

5. **Feature integration**
   - offers/policies/reports: presentation-only conversion
   - variants step: dual representation for in-scope editable values (foreign input + PLN persistence)

6. **Degraded mode**
   - przy braku kursów selected currency reset do `PLN`
   - komunikat inline przy switcherze
   - brak runtime mock fallback

### Task Breakdown

#### Phase 1: Add shared currency and exchange-rate infrastructure
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Broaden money domain | `src/app/core/models/common/money.model.ts`, `src/app/core/models/index.ts` | Replace PLN-only `Money` type with a shared supported-currency union and export it through model barrel if needed. |
| 1.2 | Add currency-specific models | `src/app/core/models/common/supported-currency.model.ts`, `src/app/core/models/common/exchange-rate.model.ts` | Define `SupportedCurrency`, NBP-derived rate metadata, and internal conversion types. |
| 1.3 | Add NBP repository | `src/app/core/repositories/exchange-rates.repository.ts` | Implement runtime HTTP integration against documented NBP API endpoint from Confluence, mapping EUR/USD rates and publication date. |
| 1.4 | Add currency service/facade | `src/app/core/services/currency.service.ts` | Centralize rate loading, availability state, conversion helpers, formatting rules, rate labels, and fallback logic. |
| 1.5 | Register dependencies | `src/app/app.config.ts` | Keep `provideHttpClient()` usage; only adjust providers if a custom pipe/service setup requires it. |

#### Phase 2: Add reusable presentation helpers
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add reusable formatter pipe/helper | `src/app/shared/pipes/money-display.pipe.ts` or `src/app/shared/pipes/converted-money.pipe.ts` | Encapsulate display formatting so templates stop hardcoding `'PLN'` and digit rules. |
| 2.2 | Add currency switcher component | `src/app/shared/ui/currency-switcher/currency-switcher.component.ts`, `.html`, `.scss` | Build a reusable segmented control/button group for `PLN | EUR | USD`, with disabled state and rate info slot/message. |
| 2.3 | Add shared messaging style | `src/styles.scss` or feature SCSS files | Introduce a consistent subtle inline style for exchange-rate info and degraded-mode notices. |

#### Phase 3: Integrate multi-currency presentation into offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add per-view currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add `selectedCurrency` signal defaulting to `PLN`, subscribe/read exchange-rate availability, and reset on unavailability. |
| 3.2 | Convert offer summaries | `src/app/features/offers/pages/offers-home-page.component.ts` | Replace `getPrimaryPremium()`/summary tile amount logic with conversion from persisted PLN to selected view currency. |
| 3.3 | Update offers template | `src/app/features/offers/pages/offers-home-page.component.html` | Insert currency switcher and rate label; replace direct PLN `currency` pipes with shared formatter/helper output. |
| 3.4 | Add degraded behavior | `src/app/features/offers/pages/offers-home-page.component.ts`, `.html` | Force/show PLN when rates unavailable and surface a clear explanatory message near the switcher. |

#### Phase 4: Integrate multi-currency presentation into policies list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add per-view currency state | `src/app/features/policies/pages/policies-home-page.component.ts` | Add contextual selected currency signal defaulting to PLN and availability handling. |
| 4.2 | Convert annual/monthly premiums | `src/app/features/policies/pages/policies-home-page.component.ts` | Wrap `annualPremium` and `monthlyPremium` values in conversion helpers without mutating stored policy data. |
| 4.3 | Update policies template | `src/app/features/policies/pages/policies-home-page.component.html` | Add switcher/rate info and replace hardcoded PLN `currency` pipes for annual/monthly totals. |
| 4.4 | Preserve existing policy actions | `src/app/features/policies/pages/policies-home-page.component.ts` | Ensure copy/renew/cancel/payment flows stay untouched and use persisted PLN data only. |

#### Phase 5: Integrate multi-currency presentation into reports
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add report currency state | `src/app/features/reports/pages/reports-page.component.ts` | Add selected currency signal defaulting to PLN, driven by exchange-rate service. |
| 5.2 | Convert KPI aggregates | `src/app/features/reports/pages/reports-page.component.ts` | Recalculate KPI display values from PLN aggregates into selected currency before formatting. |
| 5.3 | Convert trend and line totals | `src/app/features/reports/pages/reports-page.component.ts` | Apply one selected currency consistently to `monthlyTrend`, business-line totals, and any premium-derived metrics. |
| 5.4 | Update reports template | `src/app/features/reports/pages/reports-page.component.html` | Add switcher/rate info and replace PLN-specific formatting with shared money display helpers. |

#### Phase 6: Integrate foreign-currency input into offer wizard variants step
| # | Task | Files | Description |
|---|------|-------|-------------|
| 6.1 | Identify in-scope editable monetary fields | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `.html` | Map current editable values and wire only Jira-scope fields for foreign-currency input, pending clarification if needed. |
| 6.2 | Add input-currency state | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | Add selected input currency signal default `PLN` and react to rate availability. |
| 6.3 | Add foreign-input helpers | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | Parse EUR/USD input, convert immediately to PLN through currency service, and keep persisted state in PLN. |
| 6.4 | Extend wizard state where needed | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | Ensure state mutations continue storing PLN amounts only; add explicit helpers if premium-related fields need dedicated update methods. |
| 6.5 | Update variants template | `src/app/features/offer-wizard/pages/variants-step-page.component.html` | Add currency switcher near in-scope entry fields, helper text with PLN equivalent and rate date, and explanatory note about current-rate presentation. |
| 6.6 | Disable EUR/USD entry on degraded mode | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `.html` | Lock foreign-currency controls and show a clear message if rates are unavailable. |

#### Phase 7: Compatibility and runtime persistence adjustments
| # | Task | Files | Description |
|---|------|-------|-------------|
| 7.1 | Verify offer/policy model compatibility | `src/app/core/models/offer/offer.model.ts`, `src/app/core/models/policy/policy.model.ts` | Keep persisted aggregates/business amounts in PLN while allowing view conversion through helper layer. |
| 7.2 | Preserve PLN persistence in runtime repo | `src/app/core/repositories/sales-flow-runtime.repository.ts` | Confirm `promoteOfferToPolicy()` and `saveDraftOffer()` continue using PLN amounts and are unaffected by selected view currency. |
| 7.3 | Review mock data assumptions | `public/mock/offers.json`, `public/mock/policies.json` | No runtime exchange-rate mock should be introduced; only confirm existing offer/policy fixtures remain PLN-based. |

#### Phase 8: Automated verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 8.1 | Add unit tests for currency service | `src/app/core/services/currency.service.spec.ts`, `src/app/core/repositories/exchange-rates.repository.spec.ts` | Cover NBP response mapping, conversion formulas, formatting rules, and unavailable-state behavior using HTTP testing fixtures only. |
| 8.2 | Add offers/policies/reports component tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/features/policies/pages/policies-home-page.component.spec.ts`, `src/app/features/reports/pages/reports-page.component.spec.ts` | Validate PLN default, currency switching, and fallback to PLN on rate failure. |
| 8.3 | Add variants-step test coverage | `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts`, `src/app/features/offer-wizard/state/offer-wizard-state.service.spec.ts` | Validate EUR/USD input conversion to PLN, helper text rendering, and disabled state when rates are unavailable. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Expand money type from PLN-only to supported multi-currency domain model. |
| `src/app/core/models/common/supported-currency.model.ts` | CREATE | Define `PLN | EUR | USD` union and related reusable types. |
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Add internal exchange-rate and rate-metadata models. |
| `src/app/core/models/index.ts` | MODIFY | Re-export added currency-related models if needed by features. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Implement NBP API HTTP integration as runtime default based on Confluence documentation. |
| `src/app/core/services/currency.service.ts` | CREATE | Provide conversion, formatting, rate loading, and fallback behavior. |
| `src/app/app.config.ts` | MODIFY | Adjust providers only if required for new shared currency pipe/service setup. |
| `src/app/shared/pipes/money-display.pipe.ts` | CREATE | Centralize formatting and optional conversion-aware display for templates. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | Reusable view-level currency selector component. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Currency selector markup with status/info area. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Styles for switcher and degraded-state messaging. |
| `src/styles.scss` | MODIFY | Add any shared utility styles for exchange-rate info and notices if needed. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add selected currency state, converted summaries, and fallback logic. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency switcher and replace PLN-only formatting in visible premium outputs. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style currency controls and rate-info placement. |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Add selected currency state and converted premium presentation. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Add currency switcher and rate info; replace PLN-only premium rendering. |
| `src/app/features/policies/pages/policies-home-page.component.scss` | MODIFY | Style currency controls and degraded messages. |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Add selected currency state and converted KPI/trend/business-line display values. |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Add report-level currency switcher and replace PLN-only formatting. |
| `src/app/features/reports/pages/reports-page.component.scss` | MODIFY | Style report currency controls and notices. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Add input-currency handling, conversion helpers, and unavailable-state behavior. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Add input currency selector, helper text with PLN equivalent, and explanatory notes. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.scss` | MODIFY | Style wizard currency input controls and helper text. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Preserve PLN persistence and add targeted helpers if premium-edit flow requires dedicated updates. |
| `src/app/core/repositories/sales-flow-runtime.repository.ts` | MODIFY | Ensure runtime persistence remains PLN-based and compatible with the broadened money type. |
| `src/app/core/services/currency.service.spec.ts` | CREATE | Unit-test conversion and availability logic. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit-test NBP API response mapping with HTTP fixtures. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE/MODIFY | Test offers page currency switch and fallback behavior. |
| `src/app/features/policies/pages/policies-home-page.component.spec.ts` | CREATE | Test policies page currency switch and fallback behavior. |
| `src/app/features/reports/pages/reports-page.component.spec.ts` | CREATE | Test report-wide currency consistency and fallback behavior. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts` | CREATE | Test foreign-currency entry and helper rendering. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.spec.ts` | CREATE/MODIFY | Verify PLN-only persistence after foreign-currency input conversion. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements