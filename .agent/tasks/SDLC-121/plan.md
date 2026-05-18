# SDLC-121: Waluty na ekranie kwitariuszy

## Specification

### Overview
Dodanie na ekranie listy kwitariuszy wyboru waluty prezentacji danych obok istniejących filtrów. Widok ma startować z walutą `PLN`, a po przełączeniu na `USD` lub `EUR` ma natychmiast przeliczać wszystkie widoczne kwoty wyłącznie w warstwie prezentacji, korzystając z średniego kursu NBP z ostatniej dostępnej tabeli A. Dla walut obcych ekran ma także pokazywać metadane kursu: kurs, numer tabeli i datę kursu.

### User Stories
- US-001: As a użytkownik listy kwitariuszy, I want wybrać walutę prezentacji danych, so that mogę analizować kwoty w PLN, USD albo EUR bez ręcznych przeliczeń.
  - Given użytkownik otwiera ekran listy kwitariuszy / When widok zostaje załadowany / Then pole wyboru „Lista” jest widoczne obok filtrów i ma domyślną wartość `PLN`
- US-002: As a użytkownik listy kwitariuszy, I want po zmianie waluty zobaczyć od razu przeliczone kwoty, so that wszystkie wartości na ekranie są spójne z wybraną walutą.
  - Given użytkownik jest na liście kwitariuszy / When wybierze `USD` albo `EUR` / Then wszystkie widoczne wartości kwotowe na liście są przeliczone według ostatniej dostępnej tabeli A NBP
- US-003: As a użytkownik listy kwitariuszy, I want widzieć podstawę przeliczenia, so that wiem jaki kurs został zastosowany.
  - Given użytkownik wybierze `USD` albo `EUR` / When przeliczenie zakończy się sukcesem / Then ekran pokazuje kurs średni, oznaczenie tabeli A i datę kursu
- US-004: As a użytkownik listy kwitariuszy, I want żeby wybór waluty nie był zapamiętywany po opuszczeniu ekranu, so that każdy nowy wejściowy widok startował od `PLN`.
  - Given użytkownik opuści ekran listy / When wróci do niego ponownie / Then lista otwiera się ponownie z walutą `PLN`

### Functional Requirements
- FR-001: Na ekranie `src/app/features/kwitariusze/kwitariusze.html` należy dodać kontrolkę wyboru waluty opisaną jako „Lista” obok obecnych filtrów.
- FR-002: Kontrolka musi obsługiwać dokładnie wartości `PLN`, `USD`, `EUR`.
- FR-003: Domyślną walutą po wejściu na widok ma być `PLN`.
- FR-004: Zmiana waluty na `USD` lub `EUR` musi uruchamiać runtime’owe pobranie kursu z realnej integracji NBP udokumentowanej na stronie Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-005: Runtime default integracji ma używać publicznego endpointu latest table A NBP: `GET https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json`, bez autoryzacji, z odpowiedzią będącą tablicą zawierającą obiekt tabeli z polami `table`, `no`, `effectiveDate`, `rates[]`; kurs dla waluty docelowej ma być odczytywany z wpisu `rates[]` po `code` (`USD`/`EUR`) i polu `mid`.
- FR-006: Przeliczenie ma dotyczyć wszystkich wartości kwotowych widocznych na ekranie listy kwitariuszy, w aktualnym zakresie co najmniej:
  - `Kwota (z odsetkami)` = `baseAmount + interest`
  - `odsetki`
- FR-007: Dane źródłowe w `KwitariuszService` i modelu `Kwitariusz` pozostają w PLN; przeliczenie dotyczy wyłącznie prezentacji.
- FR-008: Dla `PLN` ekran ma pokazywać wartości źródłowe bez sekcji metadanych kursu.
- FR-009: Dla `USD` i `EUR` ekran ma pokazywać sekcję informacyjną z:
  - zastosowanym kursem średnim,
  - numerem tabeli NBP,
  - datą kursu.
- FR-010: Stan wybranej waluty ma być utrzymywany wyłącznie w obrębie instancji komponentu listy; nie wolno zapisywać go do `localStorage`, cookies, profilu użytkownika ani do rootowego serwisu współdzielonego między trasami.
- FR-011: Sortowanie kolumny kwotowej musi pozostać zgodne z aktualnie prezentowaną walutą, tj. bazować na przeliczonej wartości widocznej dla użytkownika.
- FR-012: Obsługa kursów nie może wprowadzać mocków jako fallbacku runtime; mockowane odpowiedzi mogą być użyte wyłącznie w testach jednostkowych.

### Edge Cases
- EC-001: Jeżeli pobranie kursu NBP dla `USD`/`EUR` zakończy się błędem, widok powinien pokazać komunikat błędu i nie prezentować częściowo przeliczonych, niespójnych danych.
- EC-002: Po przełączeniu z `USD`/`EUR` z powrotem na `PLN` ekran ma natychmiast wrócić do wartości źródłowych i ukryć metadane kursu.
- EC-003: Wielokrotne przełączanie między `USD` i `EUR` w ramach jednej sesji widoku może używać cache’u w pamięci komponentu, ale po opuszczeniu trasy stan ma zostać utracony.
- EC-004: Jeżeli odpowiedź latest table A nie zawiera żądanego kodu waluty, system powinien potraktować to jak błąd integracji i nie wykonywać niejawnego fallbacku.
- EC-005: Zaokrąglenie kwot prezentowanych użytkownikowi powinno pozostać spójne z obecną konwencją ekranu, tj. 2 miejsca po przecinku dla kwot, przy zachowaniu dokładności kursu z odpowiedzi NBP.
- EC-006: Jeżeli użytkownik zmieni walutę przed zakończeniem poprzedniego requestu, komponent powinien honorować tylko wynik ostatnio wybranej waluty.

### Success Criteria
- [ ] Na liście kwitariuszy widoczna jest kontrolka „Lista” z opcjami `PLN`, `USD`, `EUR`
- [ ] Po wejściu na ekran aktywna jest waluta `PLN`
- [ ] Po wyborze `USD` wszystkie widoczne kwoty na liście są przeliczone według ostatniej tabeli A NBP
- [ ] Po wyborze `EUR` wszystkie widoczne kwoty na liście są przeliczone według ostatniej tabeli A NBP
- [ ] Dla `USD` i `EUR` widoczne są kurs, numer tabeli i data kursu
- [ ] Po powrocie na ekran lista znów startuje w `PLN`
- [ ] Dane w `KwitariuszService` pozostają niezmienione i przechowywane w PLN
- [ ] Testy pokrywają logikę integracji kursów i renderowanie listy po zmianie waluty

### Open Questions
- Brak.

---

## Implementation Plan

### Technical Approach
Repozytorium jest aplikacją Angular 21 w architekturze standalone components z lokalnym stanem opartym o `signal()` i prostymi serwisami w `src/app/core/services`. Lista kwitariuszy (`src/app/features/kwitariusze/kwitariusze.ts`) dziś renderuje dane z `KwitariuszService`, który przechowuje mockowe rekordy w PLN (`baseAmount`, `interest`) i nie korzysta jeszcze z HTTP. Komponent sam formatuje kwoty przez `formatAmount()` i ustawia `MatTableDataSource` przez `effect()`.

Plan powinien zachować ten wzorzec:
- źródłowe rekordy nadal pozostają w `KwitariuszService`,
- stan wyboru waluty musi być lokalny dla `KwitariuszeComponent`, aby po zejściu z trasy resetował się do `PLN`,
- integracja kursowa powinna zostać wydzielona do nowego stateless serwisu HTTP.

Reużywana integracja:
- Runtime default dla kursów walut należy oprzeć o stronę Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- Implementacja ma używać realnego endpointu NBP latest table A: `GET https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json`
- Oczekiwany kształt odpowiedzi runtime:
  ```ts
  type NbpTableResponse = Array<{
    table: 'A';
    no: string;
    effectiveDate: string;
    rates: Array<{
      currency: string;
      code: string;
      mid: number;
    }>;
  }>;
  ```
- Auth: brak
- Wartość przeliczenia: dla danych bazowych w PLN kwotę w walucie obcej liczymy jako `pln / mid`

Brak dodatkowej wiedzy domenowej:
- Nie znaleziono istotnych stron domenowych w przestrzeni `SDLC`, które wprowadzałyby dodatkowe ADR-y, słownik lub ograniczenia dla tej funkcji.

Najważniejsze decyzje:
1. **Nie dodawać stanu waluty do `KwitariuszService`**  
   Serwis jest `providedIn: 'root'`, więc utrzymałby wybór po zmianie trasy, co łamie wymaganie resetu po opuszczeniu ekranu.
2. **Dodać `CurrencyRateService` jako integrację HTTP tylko do kursów**  
   Pozwala to zachować obecny mockowy charakter danych biznesowych, ale spełnić wymaganie realnego runtime dla kursów.
3. **Przeliczać w komponencie/widoku, nie nadpisywać modeli źródłowych**  
   Dzięki temu nie zmieniamy `baseAmount` ani `interest` w serwisie i zachowujemy zgodność z AC o braku zmian danych źródłowych.
4. **Dodać `sortingDataAccessor` dla kolumny `amount`**  
   Widoczna wartość po zmianie waluty musi być spójna z sortowaniem tabeli.
5. **Użyć testowych fixture HTTP tylko w warstwie testów**  
   Bez plików mock JSON w produkcyjnym kodzie ścieżki runtime.

### Task Breakdown

#### Phase 1: Integracja kursów NBP
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Włączyć HTTP w aplikacji | `src/app/app.config.ts` | Dodać `provideHttpClient()` do globalnych providerów, bo repo obecnie nie udostępnia `HttpClient` |
| 1.2 | Zdefiniować typy odpowiedzi NBP | `src/app/core/models/currency-rate.model.ts` | Utworzyć modele dla latest table A oraz uproszczony view-model kursu używany przez ekran kwitariuszy |
| 1.3 | Dodać serwis integracyjny kursów | `src/app/core/services/currency-rate.service.ts` | Utworzyć serwis z metodą pobierającą latest table A z NBP, mapującą odpowiedź do kursu dla `USD`/`EUR`, bez fallbacku runtime |
| 1.4 | Dodać testy integracji serwisu | `src/app/core/services/currency-rate.service.spec.ts` | Pokryć poprawne mapowanie `table/no/effectiveDate/mid`, obsługę braku waluty i błędów HTTP z użyciem `HttpTestingController` |

#### Phase 2: Logika ekranu listy kwitariuszy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać lokalny stan waluty w komponencie | `src/app/features/kwitariusze/kwitariusze.ts` | Wprowadzić `selectedCurrency`, `rateMeta`, `isRateLoading`, `rateError` i opcjonalny cache w pamięci komponentu |
| 2.2 | Podłączyć pobieranie kursu na zmianę waluty | `src/app/features/kwitariusze/kwitariusze.ts` | Dla `USD`/`EUR` wywoływać `CurrencyRateService`, anulować/ignorować stare odpowiedzi i czyścić metadane po powrocie do `PLN` |
| 2.3 | Dodać helpery przeliczeń i formatowania | `src/app/features/kwitariusze/kwitariusze.ts` | Zastąpić obecne `formatAmount()` wersją zależną od wybranej waluty oraz dodać helper `convertAmount(plnValue)` i `displayTotalAmount(row)` |
| 2.4 | Urealnić sortowanie kolumny kwotowej | `src/app/features/kwitariusze/kwitariusze.ts` | Skonfigurować `sortingDataAccessor` tak, by kolumna `amount` sortowała po aktualnie przeliczonej wartości |
| 2.5 | Zachować reset przy opuszczeniu ekranu | `src/app/features/kwitariusze/kwitariusze.ts` | Upewnić się, że żaden stan waluty nie jest wynoszony do serwisów root/localStorage; ponowne utworzenie komponentu ma startować od `PLN` |

#### Phase 3: Zmiany UI listy kwitariuszy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać kontrolkę „Lista” obok filtrów | `src/app/features/kwitariusze/kwitariusze.html` | Umieścić nowy selektor w rzędzie `.filters`, zgodnie z istniejącym stylem ekranu |
| 3.2 | Dodać sekcję informacji o kursie | `src/app/features/kwitariusze/kwitariusze.html` | Pod filtrami lub nad tabelą renderować kurs, tabelę i datę tylko dla `USD`/`EUR` |
| 3.3 | Obsłużyć loading/error kursów | `src/app/features/kwitariusze/kwitariusze.html` | Dodać lekki komunikat ładowania i błędu, bez blokowania całego ekranu |
| 3.4 | Przełączyć renderowanie kwot na przeliczone wartości | `src/app/features/kwitariusze/kwitariusze.html` | W kolumnie `amount` użyć helperów komponentu zamiast bezpośredniego `totalAmount(row)` i starego `formatAmount()` |
| 3.5 | Dodać styl selektora i panelu kursowego | `src/app/features/kwitariusze/kwitariusze.scss` | Rozszerzyć istniejący system klas filtrów o spójny wygląd kontrolki waluty i sekcji metadanych kursu |

#### Phase 4: Testy komponentu
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Dodać testy komponentu listy | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Sprawdzić domyślny `PLN`, żądanie kursu po wyborze `USD`/`EUR`, render metadanych kursu i powrót do wartości źródłowych przy `PLN` |
| 4.2 | Zweryfikować brak trwałej persystencji | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Potwierdzić, że nowa instancja komponentu startuje z `PLN` i bez kursu |
| 4.3 | Dodać asercje błędu integracji | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Potwierdzić zachowanie UI przy błędzie NBP lub braku kursu dla kodu waluty |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Dodanie `provideHttpClient()` |
| `src/app/core/models/currency-rate.model.ts` | CREATE | Typy odpowiedzi NBP i view-model kursu |
| `src/app/core/services/currency-rate.service.ts` | CREATE | Realna integracja HTTP z latest table A NBP |
| `src/app/core/services/currency-rate.service.spec.ts` | CREATE | Testy mapowania i błędów integracji |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Lokalny stan waluty, przeliczenia, pobieranie kursów, sortowanie |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Selektor „Lista”, panel kursu, komunikaty loading/error, render przeliczonych kwot |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style dla selektora waluty i informacji o kursie |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | CREATE | Testy komponentu listy dla przełączania walut |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` przechodzi po dodaniu `provideHttpClient()` i nowych plików
5. [ ] `ng test` potwierdza testy `CurrencyRateService` oraz `KwitariuszeComponent`
6. [ ] Manualnie na `/rozliczenia/kwitariusze`:
   - [ ] domyślnie widoczne jest `PLN`
   - [ ] po wyborze `USD` kwoty zmieniają się i pojawia się metadana kursu
   - [ ] po wyborze `EUR` kwoty zmieniają się i pojawia się metadana kursu
   - [ ] po powrocie do `PLN` znikają metadane kursu
   - [ ] po opuszczeniu trasy i wejściu ponownie aktywne jest `PLN`