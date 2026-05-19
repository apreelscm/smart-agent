# SDLC-129: Waluty

## Specification

### Overview
Zadanie dostarcza wybór waluty prezentacji kwot na ekranie `/kalkulator/zakres` (`Step15CoverageComponent`), tak aby użytkownik mógł natychmiast przełączać widok składek między PLN, USD i EUR bez zmiany danych źródłowych zapisanych w szkicu polisy. Dla USD i EUR aplikacja ma używać rzeczywistych kursów NBP, pokazywać metadane użytego kursu i obsługiwać fallback do kursu z dnia poprzedniego.

### User Stories
- US-001: As a user selecting coverage, I want to switch displayed premium amounts between PLN, USD, and EUR, so that I can compare costs in my preferred currency.
  - Given użytkownik otworzył ekran „Wybierz zakres” / When wybierze PLN, USD lub EUR / Then wszystkie widoczne kwoty na tym ekranie odświeżają się natychmiast bez dodatkowego zatwierdzania
- US-002: As a user, I want PLN to be the default currency each time I enter the screen, so that the screen always starts from the base business values.
  - Given użytkownik wchodzi na `/kalkulator/zakres` / When komponent się inicjalizuje / Then kontrolka „Waluta” ma domyślnie ustawione PLN
- US-003: As a user, I want to see which NBP rate was used for USD/EUR conversion, so that I can trust the converted values.
  - Given użytkownik wybrał USD lub EUR i kurs został pobrany / When widok zostanie przeliczony / Then ekran pokazuje kurs, numer tabeli NBP i datę kursu
- US-004: As a user, I want failed rate retrieval to leave my current values unchanged, so that the screen never shows inconsistent or partially converted amounts.
  - Given aktualnie widzę kwoty w danej walucie / When pobranie kursu bieżącego i kursu z dnia poprzedniego nie powiedzie się / Then aplikacja pokazuje komunikat błędu i pozostawia dotychczas prezentowane wartości

### Functional Requirements
- FR-001: Na ekranie `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` należy dodać kontrolkę „Waluta” z opcjami `PLN`, `USD`, `EUR`.
- FR-002: Domyślną walutą po każdym wejściu na ekran `/kalkulator/zakres` musi być `PLN`.
- FR-003: Stan wybranej waluty ma być lokalny dla `Step15CoverageComponent`; nie wolno dodawać persystencji do `PolicyDraftService`, `PolicyDraft` ani localStorage.
- FR-004: Dla `PLN` ekran ma wyświetlać bazowe ceny w PLN bez wywołania zewnętrznego API oraz bez sekcji informacji o kursie.
- FR-005: Dla `USD` i `EUR` aplikacja ma pobierać kurs średni NBP z rzeczywistego API w runtime.
- FR-006: Jeśli pobranie kursu bieżącego nie powiedzie się, aplikacja ma wykonać pojedynczy fallback do kursu z dnia poprzedniego.
- FR-007: Jeśli oba wywołania nie powiodą się, ekran ma wyświetlić komunikat o braku możliwości przeliczenia i zachować poprzednio aktywną walutę oraz poprzednio widoczne kwoty.
- FR-008: Wszystkie kwoty na ekranie „Wybierz zakres” muszą być przeliczane tym samym kursem w ramach jednego odświeżenia widoku.
- FR-009: Wszystkie ceny wyświetlane na tym ekranie muszą mieć dokładnie 2 miejsca po przecinku, niezależnie od waluty.
- FR-010: Dla `USD` i `EUR` ekran ma pokazywać użyty kurs (`mid`), numer tabeli NBP (`rates[0].no`) oraz datę kursu (`rates[0].effectiveDate`).
- FR-011: Przeliczenie ma dotyczyć wyłącznie warstwy prezentacji w `Step15CoverageComponent`; zapisywany `coverages.totalPremium` ma pozostać bazową wartością w PLN używaną dalej przez `step-21-review` i kolejne kroki.
- FR-012: Zmiana waluty ma działać natychmiast po wyborze wartości na kontrolce, bez dodatkowego przycisku zatwierdzania.

### Edge Cases
- EC-001: Jeśli użytkownik wybierze tę samą walutę, która jest już aktywna, komponent nie powinien wykonywać kolejnego przeliczenia ani wywołania API.
- EC-002: Jeśli użytkownik przełączy z `USD`/`EUR` z powrotem na `PLN`, komponent powinien natychmiast usunąć informacje o kursie i komunikat błędu.
- EC-003: Jeśli użytkownik przełączy z jednej waluty obcej na drugą, a nowe pobranie kursu się nie powiedzie, selektor powinien wrócić do poprzednio aktywnej waluty, aby nie pokazywać niespójnego stanu.
- EC-004: Suma końcowa na ekranie powinna być liczona z tych samych prezentowanych wartości co pozycje, aby uniknąć rozjazdu o `0,01` po zaokrągleniach.
- EC-005: Ponowne wejście na `/kalkulator/zakres` powinno zawsze inicjalizować widok w `PLN`, nawet jeśli wcześniej na tym ekranie wybrano `USD` lub `EUR`.

### Success Criteria
- [ ] Ekran `/kalkulator/zakres` pokazuje kontrolkę „Waluta” z opcjami `PLN`, `USD`, `EUR`
- [ ] Po wejściu na ekran domyślnie aktywne jest `PLN`
- [ ] Zmiana na `USD` lub `EUR` natychmiast przelicza wszystkie kwoty na ekranie i pokazuje kurs, tabelę oraz datę
- [ ] Przy błędzie kursu bieżącego aplikacja korzysta z fallbacku do dnia poprzedniego
- [ ] Przy błędzie obu wywołań aplikacja nie zmienia aktualnie prezentowanych wartości i pokazuje komunikat
- [ ] Wszystkie kwoty na ekranie są wyświetlane z dokładnością do 2 miejsc po przecinku
- [ ] Zmiana waluty nie modyfikuje zapisanych danych szkicu polisy i nie wpływa na kolejne ekrany poza tym widokiem
- [ ] Po opuszczeniu i ponownym wejściu na ekran waluta wraca do `PLN`

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 21 oparta o standalone components i prosty stan w `PolicyDraftService`. `Step15CoverageComponent` już trzyma lokalną kopię `coverages` i wylicza ceny w getterze `total`, więc najbezpieczniej rozszerzyć ten sam komponent o lokalny stan waluty zamiast rozlewać zmianę na model domenowy. To dobrze wspiera wymaganie „tylko prezentacja na tym ekranie” i „brak zapamiętywania po ponownym wejściu”.

Integrację należy oprzeć na istniejącym opisie usługi z Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut). Runtime default powinien używać rzeczywistego publicznego API NBP:
- bieżący kurs: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`
- fallback do dnia poprzedniego: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/{yyyy-MM-dd}/?format=json`
- auth: brak
- oczekiwany shape odpowiedzi:  
  `{ table, currency, code, rates: [{ no, effectiveDate, mid }] }`

W planie nie należy dodawać żadnego runtime fallbacku do plików JSON w `public/`. Mocki są dopuszczalne wyłącznie w testach (np. Playwright route interception).

Podejście implementacyjne:
- utworzyć nowy serwis HTTP w `src/app/core/services`, analogicznie do `vehicle-data.service.ts`, bo `HttpClient` jest już dostępny globalnie w `src/app/app.config.ts`
- w `Step15CoverageComponent` rozdzielić:
  - **bazowe ceny w PLN** używane do zapisu do draftu
  - **ceny prezentowane** zależne od lokalnie aktywnej waluty i kursu
- nie zmieniać `PolicyDraft`, `PolicyDraftService`, `step-21-review.component.ts/html` ani logiki kolejnych kroków; zapis `totalPremium` pozostaje w PLN
- zastosować lokalny stan komponentu typu `activeCurrency`, `pending/loading`, `exchangeRateInfo`, `conversionError`
- przy błędzie pobrania kursu zachować poprzedni aktywny stan widoku i przywrócić selektor, zamiast zostawiać UI w walucie bez przeliczenia
- użyć istniejących wzorców UI z `step-15-coverage.component.scss` i globalnych klas ze `src/styles.scss` zamiast wprowadzać PrimeNG, bo obecne ekrany używają własnych prostych kontrolek

Wyszukiwanie w przestrzeni domenowej `SDLC` nie zwróciło istotnych ADR-ów ani konwencji powiązanych z tym ekranem — plan opiera się więc na wzorcach bezpośrednio obecnych w repozytorium.

Dodatkowa uwaga wdrożeniowa: ponieważ wywołanie będzie wykonywane z frontendu, środowisko hostujące powinno zezwalać na połączenia HTTPS do `https://api.nbp.pl` (np. CSP / outbound allowlist, jeśli obowiązuje).

### Task Breakdown

#### Phase 1: Integracja z NBP
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać serwis kursów NBP | `src/app/core/services/nbp-exchange-rate.service.ts` | Utworzyć serwis pobierający kursy `USD` i `EUR` z API NBP, mapujący odpowiedź do lekkiego modelu aplikacyjnego (`code`, `mid`, `tableNo`, `effectiveDate`). |
| 1.2 | Zaimplementować fallback do dnia poprzedniego | `src/app/core/services/nbp-exchange-rate.service.ts` | Najpierw wykonać request bieżący, a przy błędzie drugi request dla daty `today - 1 day`; jeśli oba zawiodą, zwrócić spójny błąd do warstwy UI. |
| 1.3 | Ustalić formułę przeliczenia | `src/app/core/services/nbp-exchange-rate.service.ts` | Przeliczać z bazowego PLN do waluty obcej jako `amountPln / mid`, ponieważ `mid` oznacza wartość 1 jednostki waluty w PLN. |

#### Phase 2: Rozszerzenie kroku „Wybierz zakres”
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Zrefaktoryzować model cen w kroku 15 | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Zastąpić rozproszone literały cen bazowymi polami `pricePln` w `mainCoverages` i `addons`, tak aby jedna logika zasilała zarówno prezentację, jak i zapis `totalPremium` w PLN. |
| 2.2 | Dodać lokalny stan waluty i kursu | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Wprowadzić `activeCurrency`, `loading`, `exchangeRateInfo`, `conversionError` oraz handler zmiany waluty działający natychmiast po wyborze. |
| 2.3 | Zabezpieczyć zachowanie przy błędach | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Przy nieudanym pobraniu kursu nie zmieniać aktywnej waluty widoku, zachować poprzednio wyświetlane wartości i ustawić komunikat błędu do renderowania. |
| 2.4 | Dodać kontrolkę „Waluta” i sekcję informacji o kursie | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Dodać nowy blok UI z wyborem `PLN/USD/EUR`, stanem ładowania, komunikatem błędu i metadanymi kursu dla `USD/EUR`. |
| 2.5 | Przełączyć renderowanie cen na format 2-miejscowy | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Wszystkie ceny kart i suma końcowa mają korzystać z jednej funkcji/helpera formatującego kwoty do 2 miejsc po przecinku z kodem waluty. |
| 2.6 | Ostylować nową sekcję | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | Dodać style dla selektora waluty, bloku kursu, loadera i komunikatu błędu w zgodzie z istniejącymi tokenami (`--eh-*`) i układem kart. |
| 2.7 | Zachować zapis draftu bez zmian domenowych | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Metoda `next()` nadal ma zapisywać `promoCode` i `totalPremium` w PLN, bez przekazywania aktywnej waluty do draftu. |

#### Phase 3: Testy regresyjne
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać test e2e dla walut | `tests/e2e/coverage-currency.spec.ts` | Pokryć scenariusze: domyślne `PLN`, natychmiastowe przeliczenie do `USD`, fallback do dnia poprzedniego dla `EUR`, błąd obu wywołań i brak zmiany widocznych wartości. |
| 3.2 | Zamockować NBP tylko w warstwie testowej | `tests/e2e/coverage-currency.spec.ts` | Użyć Playwright `page.route()` do kontrolowania odpowiedzi API NBP w testach; nie dodawać żadnych mocków do kodu produkcyjnego. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/services/nbp-exchange-rate.service.ts` | CREATE | Serwis integracji z publicznym API NBP z fallbackiem do kursu z dnia poprzedniego |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Logika wyboru waluty, pobierania kursu, przeliczania i zachowania lokalnego stanu widoku |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | UI selektora waluty, informacji o kursie, błędu i formatowania cen |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | MODIFY | Style dla nowej kontrolki waluty i sekcji kursowej |
| `tests/e2e/coverage-currency.spec.ts` | CREATE | Testy Playwright dla scenariuszy PLN/USD/EUR, fallbacku i obsługi błędów |

### Verification Steps
1. [ ] `npm run build` succeeds
2. [ ] `npx playwright test tests/e2e/coverage-currency.spec.ts` passes
3. [ ] New tests verify: default `PLN`, immediate `USD/EUR` conversion, previous-day fallback, hidden rate info for `PLN`, and unchanged values after total API failure