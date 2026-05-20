# SDLC-130: Mozliwosc podania PESEL

## Specification

### Overview
Rozszerzenie kroku `kalkulator/kto-ty` o drugi sposób podania informacji o dacie urodzenia: użytkownik ma móc wybrać między ręcznym wpisaniem daty urodzenia a podaniem numeru PESEL. W trybie PESEL aplikacja ma lokalnie zwalidować numer, odczytać z niego datę urodzenia, wyświetlić ją w istniejącym polu daty jako wartość tylko do odczytu oraz zapisać PESEL w szkicu procesu tak, aby był dostępny w dalszych krokach.

### User Stories
- US-001: As a użytkownik, I want wybrać sposób podania informacji o dacie urodzenia, so that mogę uzupełnić formularz wygodniej.
  - Given ekran „Kilka informacji o Tobie” jest otwarty / When wybieram „Data urodzenia” albo „PESEL” / Then aktywna jest tylko jedna metoda wprowadzania danych.
- US-002: As a użytkownik, I want po wpisaniu poprawnego PESEL zobaczyć automatycznie uzupełnioną datę urodzenia, so that nie muszę wpisywać tych samych danych dwa razy.
  - Given wybrany jest tryb „PESEL” / When wpiszę poprawny 11-cyfrowy numer PESEL / Then pole daty urodzenia pokazuje zdekodowaną datę i jest tylko do odczytu.
- US-003: As a system, I want zapisać PESEL podany w kroku personal info, so that kolejne kroki procesu mogą odczytać tę wartość ze wspólnego draftu.
  - Given formularz jest poprawny w trybie „PESEL” / When użytkownik przechodzi dalej / Then `PolicyDraftService` przechowuje PESEL razem z pozostałymi danymi `personalInfo`.

### Functional Requirements
- FR-001: Krok `src/app/features/wizard/steps/step-02-personal-info` musi prezentować kontrolkę wyboru metody podania danych: `Data urodzenia` lub `PESEL`.
- FR-002: Domyślną metodą musi pozostać ręczne podanie daty urodzenia, aby zachować obecne zachowanie seeded/nowego draftu.
- FR-003: Model `PolicyDraft.personalInfo` musi zostać rozszerzony o zapis wybranej metody oraz numeru PESEL.
- FR-004: W trybie `Data urodzenia` formularz wymaga `firstName`, `lastName` i ręcznie wprowadzonego `dateOfBirth`.
- FR-005: W trybie `PESEL` formularz wymaga `firstName`, `lastName` i poprawnego numeru PESEL; pole `dateOfBirth` pozostaje widoczne, ale nieedytowalne.
- FR-006: PESEL musi być walidowany lokalnie bez integracji z zewnętrznym API: dokładnie 11 cyfr, poprawna suma kontrolna, poprawna data zakodowana wg reguł PESEL wraz z kodowaniem stulecia.
- FR-007: Po podaniu poprawnego PESEL system musi wyliczyć datę urodzenia w formacie zgodnym z istniejącym polem `<input type="date">` (`yyyy-MM-dd`) i zapisać ją jako kanoniczne `personalInfo.dateOfBirth`.
- FR-008: Po zmianie metody dane z poprzedniego trybu muszą zostać wyczyszczone:
  - `Data urodzenia -> PESEL`: czyści ręcznie wpisaną datę,
  - `PESEL -> Data urodzenia`: czyści PESEL oraz datę wyliczoną z PESEL.
- FR-009: Przycisk „Dalej” musi pozostać zablokowany, dopóki aktywna metoda nie ma kompletnych i poprawnych danych.
- FR-010: Po przejściu dalej wartość `personalInfo.pesel` i `personalInfo.inputMethod` musi być dostępna w dalszych krokach przez istniejący wspólny stan `PolicyDraftService`.

### Edge Cases
- EC-001: PESEL ma 11 cyfr, ale błędną sumę kontrolną — data nie jest wyliczana, pokazuje się komunikat walidacyjny, przejście dalej jest zablokowane.
- EC-002: PESEL ma poprawną długość i checksum, ale koduje niemożliwą datę (np. 31 lutego) — wartość jest traktowana jako błędna.
- EC-003: Użytkownik zacznie wpisywać PESEL i przełączy tryb — dane z nieaktywnej metody są czyszczone, aby nie pozostał niespójny stan draftu.
- EC-004: Użytkownik wraca na krok po zapisaniu danych — formularz odtwarza zapisany tryb, PESEL i/lub datę zgodnie z draftem.
- EC-005: Seeded draft zawiera tylko datę urodzenia z wcześniejszego modelu — migracja wartości domyślnych zachowuje tryb `Data urodzenia`, a `pesel` startuje jako pusty string.
- EC-006: W trybie PESEL użytkownik wpisuje znaki inne niż cyfry — wejście powinno być sanitizowane do cyfr albo walidowane jako niepoprawne bez próby dekodowania daty.

### Success Criteria
- [ ] Krok „Kilka informacji o Tobie” pozwala przełączyć się między trybem daty urodzenia i PESEL.
- [ ] W trybie PESEL poprawny numer automatycznie uzupełnia `dateOfBirth` i blokuje ręczną edycję tego pola.
- [ ] Niepoprawny PESEL pokazuje błąd i uniemożliwia przejście do następnego kroku.
- [ ] Zmiana trybu czyści dane z poprzedniego sposobu wprowadzania.
- [ ] `PolicyDraftService` zapisuje PESEL, wybraną metodę i wynikową datę urodzenia w `personalInfo`.
- [ ] Dotychczasowy scenariusz ręcznego wpisania daty działa bez regresji.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Implementacja powinna pozostać zgodna z obecnym wzorcem repozytorium:
- standalone Angular components,
- template-driven forms przez `FormsModule`,
- lokalny stan komponentu + prosty getter `valid`,
- zapis do wspólnego draftu przez `PolicyDraftService`,
- nawigacja przez `Router`.

Aktualny krok `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` jest bardzo prosty i zapisuje tylko `firstName`, `lastName`, `dateOfBirth`. Najmniejsza bezpieczna zmiana to:
1. rozszerzyć model `personalInfo`,
2. dodać czystą, testowalną utilkę do walidacji/dekodowania PESEL,
3. rozbudować komponent kroku o przełącznik trybu, stan błędu i logikę czyszczenia danych,
4. dodać testy dla algorytmu i przepływu UI.

Wiedza z Confluence:
- Brak istniejącej usługi/API związanej z PESEL w przestrzeni `Services` — implementacja będzie lokalna po stronie klienta, bez nowej integracji runtime.
- Kontekst domenowy znaleziony w `SDLC`: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). Plan zachowuje istniejący wizardowy flow i styl ekranu zamiast wprowadzania nowego wzorca UI.

Decyzje projektowe:
- `personalInfo.dateOfBirth` pozostaje polem kanonicznym używanym dalej w procesie.
- `personalInfo.pesel` przechowuje wartość źródłową tylko wtedy, gdy użytkownik wybrał tryb PESEL.
- `personalInfo.inputMethod` pozwala odtworzyć UI po powrocie na krok i jednoznacznie określać aktywną walidację.
- Dekodowanie PESEL będzie działać lokalnie w utilce, bez mocków runtime i bez zewnętrznego JSON fallbacku.
- Dla izolacji stylów nowy wygląd przełącznika i komunikatu błędu powinien trafić do lokalnego pliku SCSS komponentu zamiast rozlewać się do `src/styles.scss`.
- Ponieważ repo ma Playwright (`tests/e2e`) i zależność `vitest`, ale nie ma skonfigurowanego targetu `test` w `angular.json`, najbezpieczniej:
  - algorytm PESEL pokryć lekkim testem utilki,
  - zachowanie formularza pokryć testem Playwright.

### Task Breakdown

#### Phase 1: Rozszerzenie modelu draftu i logiki PESEL
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać typ metody wejścia | `src/app/core/models/policy-draft.model.ts` | Wprowadzić union/type dla sposobu podania daty, np. `BirthDateInputMethod = 'dateOfBirth' | 'pesel'`. |
| 1.2 | Rozszerzyć `personalInfo` | `src/app/core/models/policy-draft.model.ts` | Dodać pola `inputMethod` i `pesel` do `PolicyDraft.personalInfo`; zaktualizować `emptyDraft()` i `seedDraft()` tak, by domyślnie zachować tryb ręcznej daty. |
| 1.3 | Dodać utilkę PESEL | `src/app/core/utils/pesel.util.ts` | Zaimplementować funkcje: normalizacja cyfr, walidacja długości/checksum, dekodowanie daty z obsługą stuleci, zwrot `yyyy-MM-dd` albo `null`. |
| 1.4 | Pokryć utilkę testami | `src/app/core/utils/pesel.util.spec.ts` | Dodać przypadki: poprawny PESEL, błędna checksum, nieprawidłowa data, dekodowanie różnych stuleci, odrzucenie niedozwolonych znaków/długości. |

#### Phase 2: Rozbudowa kroku „Kilka informacji o Tobie”
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Rozszerzyć stan komponentu | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Zainicjalizować `inputMethod`, `pesel`, `peselError`; odczytywać wartości z `PolicyDraftService.draft().personalInfo`. |
| 2.2 | Dodać logikę przełączania trybu | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Dodać metody czyszczące dane poprzedniego trybu zgodnie z AC i odtwarzające poprawny stan przy powrocie na krok. |
| 2.3 | Dodać logikę walidacji i dekodowania | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Na zmianie PESEL wywoływać utilkę, aktualizować `dateOfBirth`, ustawiać komunikat błędu i przeliczać `valid` zależnie od aktywnego trybu. |
| 2.4 | Zaktualizować zapis do draftu | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | W `next()` zapisać `firstName`, `lastName`, `dateOfBirth`, `pesel`, `inputMethod` przez `patchPersonalInfo(...)` i zachować istniejącą nawigację do `/kalkulator/rejestracja`. |
| 2.5 | Rozbudować szablon kroku | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | Dodać kontrolkę wyboru trybu w stylu istniejących `radio-option`, warunkowe pole PESEL, pole daty z trybem editable/read-only oraz komunikat walidacyjny. |
| 2.6 | Dodać lokalne style kroku | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | Ostylować przełącznik metod, readonly state pola daty oraz error text bez wpływu na inne kroki. |
| 2.7 | Podpiąć style do komponentu | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Dodać `styleUrl` w metadanych komponentu, zgodnie z innymi krokami mającymi własny SCSS. |

#### Phase 3: Testy przepływu i regresji
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać scenariusz ręcznej daty | `tests/e2e/step-02-personal-info.spec.ts` | Zweryfikować, że domyślnie aktywny jest obecny wariant, można wpisać datę ręcznie i przejść dalej. |
| 3.2 | Dodać scenariusz PESEL | `tests/e2e/step-02-personal-info.spec.ts` | Zweryfikować przełączenie na PESEL, auto-uzupełnienie `dateOfBirth`, readonly pole daty i przejście dalej po poprawnym numerze. |
| 3.3 | Dodać scenariusz błędu i czyszczenia | `tests/e2e/step-02-personal-info.spec.ts` | Sprawdzić blokadę „Dalej” dla błędnego PESEL oraz czyszczenie danych przy zmianie trybu w obie strony. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/policy-draft.model.ts` | MODIFY | Rozszerzenie modelu `personalInfo` o `inputMethod` i `pesel`, aktualizacja pustego i seeded draftu. |
| `src/app/core/utils/pesel.util.ts` | CREATE | Czysta logika walidacji i dekodowania daty urodzenia z PESEL. |
| `src/app/core/utils/pesel.util.spec.ts` | CREATE | Testy utilki PESEL dla checksum, dat i stuleci. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | MODIFY | Stan formularza, przełączanie trybów, walidacja, zapis do draftu. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | MODIFY | UI wyboru metody, pole PESEL, readonly data urodzenia, komunikaty błędów. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | CREATE | Lokalne style dla przełącznika i stanów walidacyjnych kroku. |
| `tests/e2e/step-02-personal-info.spec.ts` | CREATE | E2E dla ścieżki manualnej, ścieżki PESEL i regresji przełączania. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements