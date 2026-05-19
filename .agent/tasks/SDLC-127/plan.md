# SDLC-127: Mozliwosc podania PESEL

## Specification

### Overview
Dodanie na kroku `kalkulator/kto-ty` („Kilka informacji o Tobie”) przełącznika typu toggle, który pozwoli użytkownikowi podać datę urodzenia na dwa sposoby:
- ręcznie w istniejącym polu daty,
- pośrednio przez numer PESEL.

W trybie PESEL system ma lokalnie zwalidować numer, wyliczyć z niego datę urodzenia i wyświetlić ją w polu „Data urodzenia” jako wartość tylko do odczytu. Zmiana ma pozostać spójna z obecnym wzorcem formularzy w aplikacji: standalone Angular components, `[(ngModel)]`, zapis do `PolicyDraftService` przy przejściu dalej.

### User Stories
- US-001: As a użytkownik kalkulatora, I want wybrać sposób podania daty urodzenia, so that mogę wprowadzić dane w wygodniejszy dla siebie sposób.
  - Given jestem na ekranie „Kilka informacji o Tobie” / When przełączę toggle między „Data urodzenia” i „PESEL” / Then formularz aktywuje właściwe pole wejściowe i właściwą walidację

- US-002: As a użytkownik kalkulatora, I want po wpisaniu poprawnego numeru PESEL zobaczyć automatycznie uzupełnioną datę urodzenia, so that nie muszę wpisywać jej ręcznie.
  - Given wybrany jest tryb „PESEL” / When wpiszę poprawny 11-cyfrowy numer PESEL z poprawną sumą kontrolną i poprawną zakodowaną datą / Then pole „Data urodzenia” pokazuje zdekodowaną datę i pozostaje nieedytowalne

- US-003: As a użytkownik kalkulatora, I want dostać czytelny błąd dla niepoprawnego PESEL, so that mogę poprawić dane przed przejściem dalej.
  - Given wybrany jest tryb „PESEL” / When wpiszę pustą, nienumeryczną, zbyt krótką, zbyt długą lub błędną kontrolnie wartość / Then formularz pokazuje komunikat walidacyjny, nie uzupełnia daty urodzenia i blokuje przejście dalej

### Functional Requirements
- FR-001: Krok `src/app/features/wizard/steps/step-02-personal-info` musi prezentować toggle z opcjami „Data urodzenia” i „PESEL”.
- FR-002: Domyślnie aktywny ma pozostać obecny sposób pracy formularza, tj. ręczne podanie daty urodzenia.
- FR-003: W trybie „Data urodzenia” formularz zachowuje obecne pole `input[type="date"]` jako edytowalne.
- FR-004: W trybie „PESEL” formularz pokazuje pole tekstowe na PESEL z ograniczeniem do 11 znaków i wskazaniem wejścia numerycznego (`inputmode="numeric"`).
- FR-005: W trybie „PESEL” pole „Data urodzenia” pozostaje widoczne, ale jest tylko do odczytu i nie przyjmuje ręcznej edycji.
- FR-006: System musi lokalnie walidować PESEL co najmniej pod kątem: wymaganej wartości, samych cyfr, długości 11, poprawnej sumy kontrolnej i poprawnej zakodowanej daty.
- FR-007: Dla poprawnego PESEL system musi zdekodować datę urodzenia zgodnie ze standardem PESEL i zapisać ją w formacie zgodnym z istniejącym polem daty (`YYYY-MM-DD`).
- FR-008: Dla niepoprawnego PESEL system nie może pozostawić w polu daty urodzenia starej wartości wyliczonej z wcześniejszego wpisu.
- FR-009: Przycisk „Dalej” musi być aktywny tylko wtedy, gdy imię, nazwisko oraz dane wymagane dla aktualnie wybranego trybu są poprawne.
- FR-010: Wybrany tryb, PESEL i wynikowa data urodzenia muszą zapisywać się w `PolicyDraftService` przez istniejące `patchPersonalInfo(...)`, aby wracały po ponownym wejściu na krok.
- FR-011: Model `PolicyDraft.personalInfo` musi zostać rozszerzony o dane potrzebne do zapisu wybranego trybu i wpisanego numeru PESEL.
- FR-012: Implementacja nie może wprowadzać zewnętrznej integracji ani runtime mocków; walidacja i dekodowanie mają działać lokalnie w aplikacji.

### Edge Cases
- EC-001: Użytkownik przełącza z trybu ręcznego na PESEL mając już wpisaną datę urodzenia — formularz nie może mylić ręcznej daty z datą wyliczoną; do czasu poprawnego PESEL pole daty powinno być puste lub odświeżone wyłącznie z PESEL.
- EC-002: Użytkownik wpisuje PESEL z poprawną długością, ale błędną sumą kontrolną — pokazany zostaje błąd walidacyjny, a data urodzenia nie jest ustawiana.
- EC-003: Użytkownik wpisuje 11 cyfr, które przechodzą checksum, ale kodują nieistniejącą datę — PESEL traktowany jest jako niepoprawny.
- EC-004: Użytkownik wpisuje PESEL zaczynający się od zera — wartość jest przechowywana jako string, bez utraty wiodących zer.
- EC-005: Użytkownik przełącza z trybu „PESEL” z powrotem na „Data urodzenia” — pole daty staje się ponownie edytowalne i tylko ono podlega walidacji wymagalności.
- EC-006: Użytkownik poprawia błędny PESEL na poprawny bez opuszczania pola — komunikat błędu znika, a data urodzenia odświeża się automatycznie.

### Success Criteria
- [ ] Na ekranie `kalkulator/kto-ty` widoczny jest toggle „Data urodzenia” / „PESEL”.
- [ ] W trybie „Data urodzenia” użytkownik może przejść dalej bez podawania PESEL.
- [ ] W trybie „PESEL” pole daty urodzenia jest nieedytowalne i uzupełnia się automatycznie po poprawnym PESEL.
- [ ] Dla pustego, nienumerycznego, za krótkiego, za długiego lub błędnego kontrolnie PESEL użytkownik widzi błąd i nie może przejść dalej.
- [ ] Po powrocie na krok zapisany wybór trybu i wpisane dane są odtworzone z draftu.
- [ ] Zmiana nie psuje istniejącej nawigacji do `/kalkulator/rejestracja`.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Repozytorium jest aplikacją Angular 21 opartą o standalone components i prosty stan w `PolicyDraftService` (`src/app/core/services/policy-draft.service.ts`). Krok `step-02-personal-info` korzysta obecnie z `FormsModule`, lokalnych właściwości komponentu i `patchPersonalInfo(...)` przy kliknięciu „Dalej”. Tę samą konwencję należy zachować.

Najmniejsza spójna zmiana:
1. rozszerzyć model `personalInfo`,
2. dodać lokalny utility do walidacji/dekodowania PESEL,
3. rozbudować komponent kroku 02 o toggle, logikę walidacji i read-only DOB,
4. dodać regresję e2e w istniejącym katalogu `tests/e2e`.

Istniejące ustalenia i reuse:
- W przestrzeni DOMAIN znaleziono tylko ogólny opis przepływu ekranów: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). Dokument daje kontekst nazewnictwa i ścieżki wizarda, ale nie definiuje szczegółowej walidacji PESEL ani wzorca toggle dla tego ekranu.
- Nie znaleziono istniejącej usługi ani API w przestrzeni `Services` dla walidacji/dekodowania PESEL — proponowana jest lokalna implementacja frontendowa. Nie będzie żadnego runtime fallbacku do mock JSON.

Decyzje techniczne:
- **Nie wprowadzamy zewnętrznej integracji**: ticket i repo nie wskazują backendu dla tego przypadku, a przestrzeń `Services` nie zawiera gotowej usługi.
- **Nie wprowadzamy Reactive Forms**: obecny krok korzysta z `[(ngModel)]`; utrzymanie tego wzorca minimalizuje zakres zmian.
- **Nie wprowadzamy nowego komponentu bibliotecznego PrimeNG dla toggle**: repo używa PrimeNG marginalnie (np. `DialogModule`), a prosty toggle da się zbudować natywnie i ostylować zgodnie z istniejącym wyglądem formularza.
- **Walidację PESEL wydzielamy do utility** w `src/app/core/utils/`, żeby nie zamykać logiki w komponencie i umożliwić późniejsze reuse np. w `step-19-policy-data`, gdzie PESEL też występuje.
- **`PolicyDraftService` pozostaje bez zmian**: obecne `patchPersonalInfo(partial)` już wspiera częściową aktualizację i wystarczy po rozszerzeniu typu modelu.

Szczegóły implementacyjne:
- Rozszerzyć `personalInfo` o:
  - `pesel: string`
  - `birthDateInputMode: 'date' | 'pesel'` (lub analogiczny czytelny union type)
- W `seedDraft()` pozostawić domyślny tryb ręcznej daty, a `personalInfo.pesel` ustawić na pusty string, aby nie wprowadzać potencjalnie niespójnych seeded danych.
- Dodać utility eksportujące funkcje w rodzaju:
  - `validatePesel(pesel: string): PeselValidationResult`
  - `decodeBirthDateFromPesel(pesel: string): string | null`
- W `step-02-personal-info.component.ts`:
  - zainicjalizować `birthDateInputMode`, `pesel`, `dateOfBirth`, `firstName`, `lastName` z draftu,
  - dodać getter `peselError`,
  - dodać metodę reagującą na zmianę PESEL i aktualizującą `dateOfBirth`,
  - dostosować getter `valid`, aby sprawdzał aktywny tryb,
  - w `next()` zapisywać `firstName`, `lastName`, `birthDateInputMode`, `pesel`, `dateOfBirth`.
- W HTML:
  - dodać toggle nad sekcją daty/PESEL,
  - renderować pole PESEL tylko dla trybu PESEL,
  - zachować pole daty zawsze widoczne, ale blokowane w trybie PESEL,
  - wyświetlać komunikat walidacyjny pod polem PESEL.
- Stylowanie umieścić lokalnie w nowym `step-02-personal-info.component.scss`, aby nie rozlewać jednorazowej zmiany do globalnego `src/styles.scss`.
- Testy oprzeć o istniejący Playwright, bo repo ma aktywny katalog `tests/e2e`, natomiast `angular.json` nie ma skonfigurowanego targetu testowego.

### Task Breakdown

#### Phase 1: Model danych i logika PESEL
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Rozszerzyć model personalInfo | `src/app/core/models/policy-draft.model.ts` | Dodać `pesel` i `birthDateInputMode` do `PolicyDraft['personalInfo']`; zaktualizować `emptyDraft()` i `seedDraft()` tak, aby obecny domyślny flow nadal startował od ręcznej daty. |
| 1.2 | Dodać utility do walidacji i dekodowania PESEL | `src/app/core/utils/pesel.util.ts` | Utworzyć czystą funkcję/funkcje sprawdzające cyfry, długość, checksum i poprawność zakodowanej daty oraz zwracające DOB w formacie `YYYY-MM-DD`. |

#### Phase 2: Rozbudowa kroku „Kilka informacji o Tobie”
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać stan trybu wejścia i walidację w komponencie | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | Dodać pola `birthDateInputMode`, `pesel`, getter walidacji i metody obsługi przełączania trybu oraz aktualizacji DOB po PESEL; zachować wzorzec `ngModel` i `patchPersonalInfo(...)`. |
| 2.2 | Rozbudować widok o toggle i komunikaty błędów | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | Wstawić toggle „Data urodzenia / PESEL”, warunkowe pole PESEL, readonly DOB w trybie PESEL oraz komunikat walidacyjny przy błędnym numerze. |
| 2.3 | Dodać lokalne style dla toggle i stanów pól | `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | Ostylować segmented toggle, readonly DOB, helper/error text i spacing zgodnie z obecną estetyką formularzy. |

#### Phase 3: Regresja i weryfikacja zachowania
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Dodać test e2e dla trybu PESEL | `tests/e2e/personal-info-pesel.spec.ts` | Pokryć scenariusze: poprawny PESEL uzupełnia DOB i blokuje edycję; błędny PESEL pokazuje błąd i blokuje przejście; przełączenie z powrotem na ręczną datę odblokowuje pole. |
| 3.2 | Sprawdzić brak regresji istniejącego flow | `tests/e2e/smoke.spec.ts` | Bez zmian w samym pliku, ale uwzględnić uruchomienie smoke testu obok nowego scenariusza, aby potwierdzić brak wpływu na start aplikacji. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/policy-draft.model.ts` | MODIFY | Rozszerzenie `personalInfo` o tryb podania daty i numer PESEL oraz aktualizacja draftów startowych. |
| `src/app/core/utils/pesel.util.ts` | CREATE | Lokalna, wielokrotnego użytku logika walidacji PESEL i dekodowania daty urodzenia. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.ts` | MODIFY | Logika toggle, aktywnej walidacji, readonly DOB i zapisu danych do draftu. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.html` | MODIFY | UI toggle, pole PESEL, readonly DOB i komunikaty walidacyjne. |
| `src/app/features/wizard/steps/step-02-personal-info/step-02-personal-info.component.scss` | CREATE | Lokalne style dla nowego układu formularza i przełącznika. |
| `tests/e2e/personal-info-pesel.spec.ts` | CREATE | Test regresyjny Playwright dla nowego zachowania kroku 02. |

### Verification Steps
1. [ ] `npm run build` kończy się sukcesem.
2. [ ] Wejście na `/kalkulator/kto-ty` nadal renderuje krok bez błędów.
3. [ ] W trybie „Data urodzenia” można wpisać datę ręcznie i przejść do `/kalkulator/rejestracja`.
4. [ ] W trybie „PESEL” poprawny numer automatycznie ustawia datę urodzenia i blokuje jej edycję.
5. [ ] W trybie „PESEL” błędny numer pokazuje komunikat walidacyjny i utrzymuje `nextDisabled`.
6. [ ] Przełączenie z „PESEL” na „Data urodzenia” ponownie odblokowuje `input[type="date"]`.
7. [ ] `npx playwright test tests/e2e/smoke.spec.ts tests/e2e/personal-info-pesel.spec.ts` przechodzi.