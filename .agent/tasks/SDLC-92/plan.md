# SDLC-92: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
Dodanie na ekranie `Przygotowane oferty` nowego pola „Okres ochrony” prezentowanego dla każdej oferty na liście. Wartość ma być wyliczana dynamicznie na podstawie bieżącej daty systemowej w momencie wejścia na listę ofert, zaczynać się o `00:00`, kończyć po roku o `23:59` i być wyświetlana w formacie `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.

### User Stories
- US-001: As a użytkownik listy ofert, I want widzieć okres ochrony przy każdej ofercie, so that mogę szybko sprawdzić zakres ochrony bez otwierania szczegółów.
  - Given użytkownik ma dostęp do ekranu listy ofert / When otwiera listę ofert / Then przy każdej ofercie widzi pole „Okres ochrony” z zakresem dat wyliczonym z bieżącej daty systemowej.
- US-002: As a użytkownik systemu, I want aby informacja była widoczna dla wszystkich ról korzystających z listy ofert, so that widok pozostaje spójny niezależnie od użytkownika.
  - Given użytkownik ma uprawnienie do wejścia na listę ofert / When lista się renderuje / Then pole „Okres ochrony” jest widoczne bez dodatkowych warunków uprawnień.

### Functional Requirements
- FR-001: Na widoku `src/app/features/offers/pages/offers-home-page.component.html` należy dodać nowe pole „Okres ochrony” dla każdego rekordu oferty.
- FR-002: Nowe pole ma zostać umieszczone bezpośrednio przed istniejącym polem „Aktualizacja” w siatce `offer-row__meta-grid`.
- FR-003: Wartość pola ma być identycznie liczona dla wszystkich ofert wyświetlonych w danym renderze listy i bazować wyłącznie na bieżącej dacie systemowej w chwili wyświetlenia ekranu.
- FR-004: Początek zakresu ma być normalizowany do początku dnia bieżącej daty systemowej, tj. `00:00`.
- FR-005: Koniec zakresu ma odpowiadać dacie po 1 roku i godzinie `23:59`.
- FR-006: Wartość ma być prezentowana jako jeden ciąg tekstowy w formacie `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- FR-007: Zmiana ma pozostać wyłącznie w warstwie prezentacji; nie należy rozszerzać modelu `Offer`, kontraktów repozytoriów ani danych mockowych.
- FR-008: Istniejące filtrowanie, sortowanie, akcje ofert i przejścia statusów na ekranie ofert nie mogą ulec zmianie.

### Edge Cases
- EC-001: Jeżeli użytkownik otworzy listę ofert o dowolnej godzinie w ciągu dnia, początek zakresu nadal musi być pokazany jako `00:00`, a nie aktualna godzina.
- EC-002: Jeżeli lista zostanie otwarta pod koniec miesiąca lub roku, zakres nadal musi być wyliczony jako data + 1 rok oraz `23:59`, bez błędów formatowania.
- EC-003: Długa wartość zakresu nie może psuć układu `offer-row__meta-grid`; tekst powinien się mieścić lub zawijać w kontrolowany sposób.
- EC-004: Brak lub zmiana danych samej oferty nie wpływa na wyliczenie okresu ochrony, bo źródłem jest wyłącznie czas systemowy.
- EC-005: Wartość musi używać notacji 24-godzinnej `HH:mm`, zgodnie z doprecyzowaniem w komentarzu Jira.

### Success Criteria
- [ ] Na liście ofert widoczne jest nowe pole „Okres ochrony” dla każdej oferty.
- [ ] Pole jest umieszczone bezpośrednio przed polem „Aktualizacja”.
- [ ] Każda oferta pokazuje zakres w formacie `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] Zakres zaczyna się od bieżącej daty systemowej z godziną `00:00`.
- [ ] Zakres kończy się po 1 roku z godziną `23:59`.
- [ ] Zmiana nie wymaga modyfikacji API, repozytoriów danych ani plików `public/mock/*.json`.
- [ ] Istniejące funkcje listy ofert działają bez regresji.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Aplikacja jest zbudowana w Angular 20 z komponentami standalone, sygnałami (`signal`, `computed`, `toSignal`) i PrimeNG. Ekran ofert jest obsługiwany przez `OffersHomePageComponent`, który już renderuje metadane oferty w układzie siatki `offer-row__meta-grid` w plikach:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

Zmiana powinna reuse’ować istniejący wzorzec: dodać kolejny blok metadanych w `offer-row__meta-grid`, zamiast przebudowywać ekran na tabelę. To najbliższy odpowiednik „kolumny” w obecnym UI.

Wnioski z wcześniejszego discovery:
- API/SERVICE: nie znaleziono istniejącej usługi ani API dla tej funkcji w przestrzeni `Services` — brak integracji do reuse, więc zmiana pozostaje czysto prezentacyjna.
- DOMAIN: znaleziono kontekst produktowy na stronie `Ekrany` w przestrzeni `SDLC`, potwierdzający że lista ofert jest głównym ekranem pracy agenta: https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany

Repo już pobiera oferty przez `OffersRepository` z `mock/offers.json`, ale ten ticket nie wymaga żadnej zmiany źródła danych. Zgodnie z zakresem nie należy dodawać żadnego nowego runtime mocka ani fallbacku JSON. Wartość „Okres ochrony” ma być liczona lokalnie na froncie z zegara systemowego podczas renderowania listy.

Rekomendowane rozwiązanie:
1. Wydzielić czystą funkcję pomocniczą do wyliczenia i formatowania zakresu ochrony, aby logika dat była testowalna poza komponentem.
2. Obliczyć jedną wartość zakresu dla bieżącego renderu listy ofert i używać jej dla wszystkich rekordów na ekranie.
3. Wstawić nowy blok metadanych przed sekcją „Aktualizacja”.
4. Dostosować SCSS, aby nowa pozycja z długim zakresem dat pozostała czytelna w istniejącej siatce.
5. Dodać test jednostkowy dla logiki dat oraz test e2e potwierdzający render i pozycję pola.

### Task Breakdown

#### Phase 1: Implementacja logiki okresu ochrony
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Dodać util do wyliczania zakresu ochrony | `src/app/features/offers/utils/protection-period.util.ts` | Utworzyć czystą funkcję budującą zakres od początku bieżącego dnia (`00:00`) do daty po roku z godziną `23:59`, zwracającą gotowy tekst w formacie `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`. |
| 1.2 | Podpiąć logikę do komponentu ofert | `src/app/features/offers/pages/offers-home-page.component.ts` | Zaimportować util i obliczyć jedną wartość `protectionPeriodLabel` dla renderu listy; nie rozszerzać `Offer` ani repozytorium. |
| 1.3 | Wyrenderować nowe pole w widoku ofert | `src/app/features/offers/pages/offers-home-page.component.html` | Dodać blok „Okres ochrony” w `offer-row__meta-grid` bezpośrednio przed istniejącym blokiem „Aktualizacja”. |
| 1.4 | Dostosować układ do nowego pola | `src/app/features/offers/pages/offers-home-page.component.scss` | Zaktualizować układ siatki i styl wartości zakresu tak, aby długi ciąg dat był czytelny i nie łamał layoutu. |

#### Phase 2: Pokrycie testami
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać test jednostkowy dla formatu i obliczeń | `src/app/features/offers/utils/protection-period.util.spec.ts` | Zweryfikować co najmniej przypadek standardowy i normalizację czasu do `00:00` / `23:59` niezależnie od godziny wejściowej. |
| 2.2 | Dodać test e2e dla widoku listy ofert | `tests/e2e/offers-protection-period.spec.ts` | Sprawdzić obecność pola „Okres ochrony” na ekranie ofert, położenie przed „Aktualizacja” oraz zgodność wartości z oczekiwanym wzorcem formatu. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/utils/protection-period.util.ts` | CREATE | Czysta, testowalna logika wyliczania i formatowania okresu ochrony. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Obliczenie wartości okresu ochrony dla renderu listy i podanie jej do szablonu. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Dodanie pola „Okres ochrony” przed blokiem „Aktualizacja”. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Korekta układu i stylu siatki metadanych dla nowego pola. |
| `src/app/features/offers/utils/protection-period.util.spec.ts` | CREATE | Testy jednostkowe logiki dat i formatowania. |
| `tests/e2e/offers-protection-period.spec.ts` | CREATE | Test e2e dla renderu i położenia nowego pola na liście ofert. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Wejście na `/offers` pokazuje pole „Okres ochrony” przy każdym rekordzie
5. [ ] Pole „Okres ochrony” jest wyrenderowane bezpośrednio przed „Aktualizacja”
6. [ ] Wartość ma format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`
7. [ ] Początek zakresu zawsze kończy się `00:00`, a koniec `23:59`
8. [ ] Filtrowanie, sortowanie, akcje split button i dialogi ofert działają bez regresji