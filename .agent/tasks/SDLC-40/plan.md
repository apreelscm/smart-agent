# SDLC-40: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Task wprowadza wielowalutową warstwę prezentacji i wprowadzania kwot w aplikacji Smart Agent, przy zachowaniu obecnego modelu danych: wartości biznesowe pozostają utrwalane w PLN. Zmiana obejmuje istniejące widoki portfelowe (`offers`, `policies`, `customers`, `reports`) oraz kreator oferty, tak aby użytkownik mógł:
- przełączać prezentację kwot między PLN, EUR i USD,
- widzieć zastosowany kurs i datę kursu,
- w istniejących polach kwotowych kreatora wprowadzać wartość w EUR/USD i zapisywać ją jako PLN,
- otrzymać bezpieczny fallback do PLN przy niedostępności usługi kursowej.

### User Stories
- US-001: As an agent, I want przełączyć walutę widoku oferty lub polisy, so that mogę rozmawiać z klientem w walucie kontraktu.
  - Given widok oferty/polisy otwarty w PLN / When wybieram EUR lub USD / Then wszystkie kwoty na tym widoku są spójnie przeliczone, poprawnie sformatowane i opatrzone informacją o kursie.

- US-002: As an agent, I want wpisać kwotę w walucie obcej w kreatorze, so that nie muszę ręcznie przeliczać jej do PLN.
  - Given pole kwotowe w kreatorze i dostępne kursy / When wybieram EUR lub USD i wpisuję wartość / Then widzę pomocniczą równowartość w PLN, a zapis do stanu oferty następuje w PLN.

- US-003: As a sales manager, I want oglądać raporty i panel klienta w jednej wybranej walucie, so that mogę porównywać portfel bez mieszania walut.
  - Given raport lub podsumowanie klienta / When zmieniam walutę widoku / Then KPI, tabele i sumy używają jednej wspólnej waluty na całym ekranie.

### Functional Requirements
- FR-001: Aplikacja musi udostępnić przełącznik waluty `{PLN | EUR | USD}` w widokach:
  - `src/app/features/offers/pages/offers-home-page.component.*`
  - `src/app/features/policies/pages/policies-home-page.component.*`
  - `src/app/features/customers/pages/customers-page.component.*`
  - `src/app/features/reports/pages/reports-page.component.*`
  - `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.*` wraz z krokami podsumowania/szczegółów.

- FR-002: Każdy nowy widok musi startować w PLN; wybór waluty nie może być pamiętany między widokami.

- FR-003: Przeliczenie walut musi opierać się na runtime-default integracji z udokumentowanym serwisem kursowym z Confluence:
  - [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
  - [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

- FR-004: Runtime source kursów musi używać publicznego endpointu NBP tabeli A:
  - `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`
  - mapowanie: `effectiveDate` jako data kursu, `rates[].code in ['EUR','USD']`, `rates[].mid` jako kurs PLN dla waluty.
  - Integracja nie wymaga auth.

- FR-005: Wszystkie obecnie zapisane kwoty domenowe pozostają w PLN; warstwa wielowalutowa nie może zmieniać kontraktów `Offer`, `Policy` ani sposobu utrwalania w `SalesFlowRuntimeRepository`.

- FR-006: Formatowanie kwot musi zachować obecny standard dla PLN i dodać obsługę EUR/USD:
  - PLN: bez regresji względem obecnego formatu, 0 miejsc po przecinku,
  - EUR/USD: 2 miejsca po przecinku.

- FR-007: W każdym widoku objętym zmianą użytkownik musi widzieć dyskretną informację o kursie i dacie kursu, gdy aktywna waluta to EUR lub USD.

- FR-008: W kreatorze oferty istniejące edytowalne pola kwotowe muszą wspierać wejście w EUR/USD z natychmiastową informacją pomocniczą o równowartości w PLN oraz zapisem do stanu wyłącznie w PLN.

- FR-009: Widokowe przełączenie waluty musi obejmować wszystkie prezentowane sumy, składki, raty i podsumowania na danym ekranie; nie może wystąpić miks walut w obrębie jednego widoku.

- FR-010: Przy niedostępności usługi kursowej aplikacja musi:
  - wymusić PLN jako jedyną dostępną walutę prezentacji,
  - zablokować wybór EUR/USD w przełączniku,
  - zablokować walutę wejściową EUR/USD w polach edycyjnych,
  - pokazać czytelny komunikat degradacji.

- FR-011: Istniejące mocki w `public/mock/*.json` nie mogą być używane jako runtime fallback dla kursów walut; mogą pozostać wyłącznie źródłem danych ofert/polis i ewentualnie fixture'ami testowymi.

### Edge Cases
- EC-001: Jeśli endpoint NBP zwróci odpowiedź bez EUR lub USD, widok ma zachować się jak przy awarii usługi i wymusić PLN.
- EC-002: Jeśli użytkownik przełączy widok na EUR/USD, a następnie przejdzie do innej strony, nowa strona ma wystartować w PLN.
- EC-003: Jeśli użytkownik zapisze ofertę po wpisaniu kwoty w EUR/USD, po ponownym otwarciu przeliczenie waluty obcej ma być wykonane z bieżącego kursu, nie z historycznej wartości wejściowej.
- EC-004: Jeśli wartość po przeliczeniu daje ułamki groszy/centów, logika ma liczyć na liczbach bazowych, a zaokrąglenie stosować dopiero na etapie prezentacji lub normalizacji zapisu do PLN.
- EC-005: Jeśli widok działa w trybie readonly (np. podgląd oferty z polisy), przełącznik prezentacji ma działać, ale nie może odblokowywać edycji.
- EC-006: Jeśli kursy nie są jeszcze załadowane, UI ma pokazać stan ładowania/disabled zamiast chwilowo wyświetlać błędne przeliczenia.
- EC-007: Jeśli w komponencie są zarówno kwoty `Money.amount`, jak i surowe liczby (`annualPremium`, `monthlyPremium`), wszystkie muszą przejść przez tę samą warstwę prezentacji.

### Success Criteria
- [ ] Widoki ofert, polis, klientów, raportów i kreatora mają przełącznik PLN/EUR/USD startujący w PLN.
- [ ] Wszystkie kwoty na pojedynczym ekranie zmieniają się spójnie po przełączeniu waluty.
- [ ] PLN wygląda dokładnie tak jak przed zmianą.
- [ ] Kreator pozwala wpisać kwotę w EUR/USD w istniejących polach kwotowych i zapisuje wynik w PLN.
- [ ] Użytkownik widzi kurs i datę kursu dla prezentacji nie-PLN.
- [ ] Awaria integracji kursowej wymusza PLN i blokuje EUR/USD z jasnym komunikatem.
- [ ] Nie pojawia się runtime fallback oparty o plik mock JSON dla kursów.
- [ ] Testy automatyczne pokrywają konwersję, degradację do PLN i kluczowe ścieżki UI.

### Open Questions
- [NEEDS CLARIFICATION: w aktualnym motor wizard (`variants-step-page.component.html`) nie ma ręcznego pola wpisania składki, a składka jest wyliczana; czy MVP ma dodać nowe pole ręcznej składki w walucie obcej, czy obsługa wejścia walutowego ma dotyczyć wyłącznie już istniejących edytowalnych pól kwotowych w kreatorze?]

---

## Implementation Plan

### Technical Approach
Repozytorium jest aplikacją Angular 20 opartą o standalone components, `signal`/`computed`, `toSignal`, PrimeNG i prosty podział na:
- dane/integracje w `src/app/core/repositories`,
- modele w `src/app/core/models`,
- współdzielone UI w `src/app/shared/ui`,
- logikę widoków bezpośrednio w komponentach feature.

Plan powinien utrzymać obecny wzorzec:
- nowa integracja kursowa jako singleton repository w `core/repositories`,
- widokowy stan waluty jako osobny serwis używany przez konkretne strony,
- wspólne komponenty/pipes dla prezentacji i wejścia kwotowego zamiast rozproszonego ręcznego przeliczania w szablonach.

Reużycie istniejącej dokumentacji/integracji:
- runtime default dla kursów zostanie oparty o udokumentowaną usługę z Confluence:
  - [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
  - [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- implementacja pobierze tabelę A z `https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, odfiltruje `EUR` i `USD`, a `mid` potraktuje jako kurs do przeliczeń względem PLN.
- brak auth/secrets; nie są wymagane pozycje deploymentowe.
- mock JSON nie będzie używany jako runtime fallback dla kursów.

Wnioski z Confluence DOMAIN:
- Nie znaleziono istotnych ADR/conventions w przestrzeni `SDLC`, więc plan opiera się na wzorcach już obecnych w repozytorium.

Kluczowe decyzje projektowe:
1. **Nie rozszerzać modelu persystencji na wielowalutowy.**  
   `src/app/core/models/common/money.model.ts` pozostaje logicznie PLN-only; wielowalutowość będzie wyłącznie warstwą prezentacji i wejścia. To jest spójne z AC i z aktualnym `SalesFlowRuntimeRepository`.

2. **Dodać osobną warstwę konwersji i formatowania.**  
   Zamiast ręcznie podmieniać dziesiątki użyć `CurrencyPipe`, wprowadzić:
   - `ExchangeRatesRepository` do pobierania snapshotu kursów,
   - `CurrencyPresentationService` do stanu wybranego widoku,
   - `PresentAmountPipe` do spójnego formatowania z PLN/EUR/USD,
   - `CurrencySwitcherComponent` do przełącznika i noty o kursie.

3. **Dodać współdzielony komponent wejścia kwotowego.**  
   W repo są zarówno reactive forms (`vehicle-step-page`) jak i template-driven/signal-based wejścia (`crop-step-page`, `variants-step-page`, `crop-variants-step-page`). Najbezpieczniej dodać jeden standalone `CurrencyAmountInputComponent` z `ControlValueAccessor`, którego model zawsze będzie w PLN, ale UI pozwoli przełączyć walutę wejściową na EUR/USD i pokaże pomocniczy ekwiwalent w PLN.

4. **Zachować zakres wyboru waluty per widok.**  
   Widokowe komponenty (`offers`, `policies`, `customers`, `reports`) dostaną własny provider `CurrencyPresentationService`. Kreator oferty dostanie go na poziomie `NewOfferWizardShellComponent`, aby jeden wybór obowiązywał w całym wizardzie/podglądzie szczegółowym.

5. **Awaria kursów = twardy fallback do PLN.**  
   Jeśli NBP nie odpowie lub nie zwróci EUR/USD, service wyłączy waluty obce i pokaże komunikat. To dotyczy zarówno ekranów prezentacyjnych, jak i komponentu wejściowego.

### Task Breakdown

#### Phase 1: Warstwa kursów i wspólne komponenty walutowe
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać modele walut i snapshotu kursów | `src/app/core/models/common/currency-code.model.ts`, `src/app/core/models/common/exchange-rate.model.ts`, `src/app/core/models/index.ts` | Wprowadzić typy `PLN/EUR/USD` oraz model snapshotu z `effectiveDate` i mapą kursów. |
| 1.2 | Zaimplementować repository integracji NBP | `src/app/core/repositories/exchange-rates.repository.ts` | Dodać `HttpClient`-based repository z runtime default `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, mapowaniem `EUR/USD`, obsługą błędów i `shareReplay(1)`. |
| 1.3 | Dodać view-scoped service stanu waluty | `src/app/core/services/currency-presentation.service.ts` | Service ma trzymać `selectedCurrency`, snapshot kursów, loading/error state, regułę wymuszenia PLN oraz metody konwersji `PLN -> EUR/USD` i `EUR/USD -> PLN`. |
| 1.4 | Dodać wspólny pipe prezentacji kwot | `src/app/shared/pipes/present-amount.pipe.ts` | Pipe ma przyjmować kwotę bazową w PLN i renderować ją wg aktywnej waluty oraz zasad zaokrąglania. |
| 1.5 | Dodać wspólny przełącznik waluty | `src/app/shared/ui/currency-switcher/currency-switcher.component.ts`, `src/app/shared/ui/currency-switcher/currency-switcher.component.html`, `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | Komponent ma wyświetlać `PLN | EUR | USD`, info o kursie, stan loading i komunikat degradacji. |
| 1.6 | Dodać wspólny komponent wejścia kwotowego | `src/app/shared/ui/currency-amount-input/currency-amount-input.component.ts`, `src/app/shared/ui/currency-amount-input/currency-amount-input.component.html`, `src/app/shared/ui/currency-amount-input/currency-amount-input.component.scss` | CVA z wartością modelu w PLN, wyborem waluty wejściowej, live hintem `≈ ... PLN po kursie ...` i blokadą EUR/USD przy awarii kursów. |
| 1.7 | Pokryć foundation testami jednostkowymi | `src/app/core/repositories/exchange-rates.repository.spec.ts`, `src/app/core/services/currency-presentation.service.spec.ts`, `src/app/shared/pipes/present-amount.pipe.spec.ts` | Zweryfikować mapowanie odpowiedzi NBP, reguły fallbacku i formatowanie PLN/EUR/USD. |

#### Phase 2: Wielowalutowa prezentacja w widokach portfelowych
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zintegrować przełącznik i pipe na liście ofert | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Dodać provider serwisu walut, switcher w toolbarze/sekcji nagłówka oraz zastąpić hardcoded `PLN` dla składek, średniej składki i dialogów przejść. |
| 2.2 | Zintegrować przełącznik i pipe na liście polis | `src/app/features/policies/pages/policies-home-page.component.ts`, `src/app/features/policies/pages/policies-home-page.component.html`, `src/app/features/policies/pages/policies-home-page.component.scss` | Przeliczyć `annualPremium`, `monthlyPremium`, kafle i dialog płatności/anulowania bez zmiany danych bazowych. |
| 2.3 | Zintegrować przełącznik i pipe na panelu klienta | `src/app/features/customers/pages/customers-page.component.ts`, `src/app/features/customers/pages/customers-page.component.html`, `src/app/features/customers/pages/customers-page.component.scss` | Przeliczyć przypis całkowity, składki w tabelach oraz podsumowania klienta w jednej wspólnej walucie. |
| 2.4 | Zintegrować przełącznik i pipe w raportach | `src/app/features/reports/pages/reports-page.component.ts`, `src/app/features/reports/pages/reports-page.component.html`, `src/app/features/reports/pages/reports-page.component.scss` | Przeliczyć KPI, trend, strukturę linii biznesowych i statystyki płatności przy zachowaniu jednej waluty dla całego raportu. |

#### Phase 3: Kreator oferty i szczegóły/podgląd
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Włączyć view-level walutę dla całego wizardu | `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.ts`, `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.html`, `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.scss` | Umieścić `CurrencyPresentationService` na poziomie shell, dodać switcher i notę kursową dla całego kreatora/podglądu readonly. |
| 3.2 | Przeliczyć sidebar i podsumowanie wizardu | `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.html`, `src/app/features/offer-wizard/pages/summary-step-page.component.ts`, `src/app/features/offer-wizard/pages/summary-step-page.component.html` | Objąć walutą prezentacji składkę, linie ochrony, raty, dodatki i sumę ubezpieczenia. |
| 3.3 | Dodać wejście walutowe do motor variants | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `src/app/features/offer-wizard/pages/variants-step-page.component.html` | Zastąpić ręczne `discountInput` komponentem wejścia walutowego, zachować zapis zniżki w PLN, przeliczyć tabele rat i wariantów w prezentacji. |
| 3.4 | Dodać wejście walutowe do crop calculation step | `src/app/features/offer-wizard/pages/crop-step-page.component.ts`, `src/app/features/offer-wizard/pages/crop-step-page.component.html`, `src/app/features/offer-wizard/pages/crop-step-page.component.scss` | Objąć istniejące pola kwotowe (np. `pricePerUnit`, claim history i prezentację sum ubezpieczenia) wspólną warstwą wejścia/prezentacji; model pozostaje w PLN. |
| 3.5 | Dodać wejście walutowe do crop variants | `src/app/features/offer-wizard/pages/crop-variants-step-page.component.ts`, `src/app/features/offer-wizard/pages/crop-variants-step-page.component.html` | Zastąpić ręczne parsowanie zniżki komponentem wejścia walutowego, przeliczyć składki działek, dodatki i raty wg waluty widoku. |
| 3.6 | Zachować niezmienny zapis stanu oferty | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | W razie potrzeby doprecyzować normalizację kwot przy zapisie, ale bez wprowadzania persystencji EUR/USD do stanu oferty. |

#### Phase 4: Testy i regresja
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Dodać testy komponentowe / integracyjne dla kluczowych ekranów | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/features/policies/pages/policies-home-page.component.spec.ts`, `src/app/features/reports/pages/reports-page.component.spec.ts` | Zweryfikować przełączanie waluty, notę kursową i wymuszenie PLN przy błędzie repository. |
| 4.2 | Dodać e2e smoke dla scenariusza walutowego | `tests/e2e/currency-support.spec.ts` | Pokryć przełączenie PLN→EUR na portfelu/raporcie oraz degradację do PLN po zasymulowanym błędzie kursów. |
| 4.3 | Przejść regresję istniejących przepływów | `tests/e2e/smoke.spec.ts` | W razie potrzeby rozszerzyć istniejący smoke test o obecność root view i brak regresji startu aplikacji. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/currency-code.model.ts` | CREATE | Typ waluty prezentacji/wejścia. |
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Snapshot kursów EUR/USD względem PLN. |
| `src/app/core/models/index.ts` | MODIFY | Eksport nowych modeli walutowych. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Integracja runtime z API NBP. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Testy mapowania i błędów integracji. |
| `src/app/core/services/currency-presentation.service.ts` | CREATE | View-scoped stan wybranej waluty i logika fallbacku do PLN. |
| `src/app/core/services/currency-presentation.service.spec.ts` | CREATE | Testy przełączania waluty i degradacji. |
| `src/app/shared/pipes/present-amount.pipe.ts` | CREATE | Spójne przeliczanie i formatowanie kwot z PLN do waluty widoku. |
| `src/app/shared/pipes/present-amount.pipe.spec.ts` | CREATE | Testy formatowania PLN/EUR/USD. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | UI przełącznika waluty. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Template przełącznika i noty o kursie. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Style przełącznika. |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.ts` | CREATE | Reusable input kwotowy z modelem w PLN. |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.html` | CREATE | Template wejścia z selektorem waluty i hintem PLN. |
| `src/app/shared/ui/currency-amount-input/currency-amount-input.component.scss` | CREATE | Style dla wejścia walutowego. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Provider serwisu walut, importy shared UI/pipe. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Switcher waluty i dynamiczne renderowanie kwot. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Layout dla switchera i noty kursowej. |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Integracja widokowego stanu waluty. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Dynamiczne kwoty polis i rata/miesięczna składka. |
| `src/app/features/policies/pages/policies-home-page.component.scss` | MODIFY | Dostosowanie toolbar/header pod switcher. |
| `src/app/features/customers/pages/customers-page.component.ts` | MODIFY | Integracja przeliczania przypisu i pozycji klienta. |
| `src/app/features/customers/pages/customers-page.component.html` | MODIFY | Switcher oraz wielowalutowe podsumowania klienta. |
| `src/app/features/customers/pages/customers-page.component.scss` | MODIFY | Style elementów walutowych. |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Provider waluty i binding danych raportowych do nowej warstwy prezentacji. |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Switcher, nota kursowa i dynamiczne KPI/trendy. |
| `src/app/features/reports/pages/reports-page.component.scss` | MODIFY | Układ dla sekcji walutowej raportu. |
| `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.ts` | MODIFY | Udostępnienie jednego stanu waluty dla całego wizardu. |
| `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.html` | MODIFY | Switcher i wielowalutowe podsumowanie w aside/dialogu. |
| `src/app/features/offer-wizard/pages/new-offer-wizard-shell.component.scss` | MODIFY | Style dla walutowego headera wizardu. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.ts` | MODIFY | Logika podsumowania zgodna z walutą widoku. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.html` | MODIFY | Wielowalutowa prezentacja składki i linii ochrony. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Podmiana ręcznej logiki zniżki na reusable input walutowy. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Wielowalutowe warianty, raty i pole kwotowe. |
| `src/app/features/offer-wizard/pages/crop-step-page.component.ts` | MODIFY | Obsługa istniejących pól kwotowych z normalizacją do PLN. |
| `src/app/features/offer-wizard/pages/crop-step-page.component.html` | MODIFY | Wejście walutowe i przeliczona prezentacja sum/odszkodowań. |
| `src/app/features/offer-wizard/pages/crop-step-page.component.scss` | MODIFY | Style dla nowych kontrolek walutowych. |
| `src/app/features/offer-wizard/pages/crop-variants-step-page.component.ts` | MODIFY | Refaktor zniżki i prezentacji premium na nową warstwę walutową. |
| `src/app/features/offer-wizard/pages/crop-variants-step-page.component.html` | MODIFY | Wielowalutowe składki działek, dodatki i raty. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Utrzymanie zapisu kwot wyłącznie w PLN po wejściu EUR/USD. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Test przełącznika i spójnego przeliczania listy ofert. |
| `src/app/features/policies/pages/policies-home-page.component.spec.ts` | CREATE | Test przełącznika i fallbacku listy polis. |
| `src/app/features/reports/pages/reports-page.component.spec.ts` | CREATE | Test wielowalutowego raportu bez miksu walut. |
| `tests/e2e/currency-support.spec.ts` | CREATE | E2E dla scenariusza walut i degradacji do PLN. |

### Verification Steps
1. [ ] Build succeeds (`npm run build`)
2. [ ] Tests pass (`npm test`)
3. [ ] New tests cover requirements
4. [ ] `offers`, `policies`, `customers`, `reports` startują w PLN i poprawnie przełączają się na EUR/USD
5. [ ] Kreator oferty pokazuje helper PLN przy wejściu EUR/USD i zapisuje wartości w PLN
6. [ ] Symulowany błąd `ExchangeRatesRepository` wymusza PLN i blokuje EUR/USD
7. [ ] Brak użycia `public/mock/*` jako runtime source kursów walut