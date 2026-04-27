# SDLC-100: Na ekranie listy ofert okres ochrony powinien byc przed kolumna Aktualizacja

## Specification

### Overview
Zadanie koryguje kolejność pól metadanych na ekranie `/offers`. W aktualnej implementacji komponent `src/app/features/offers/pages/offers-home-page.component.html` renderuje w siatce `offer-row__meta-grid` najpierw blok „Aktualizacja”, a dopiero po nim „Okres ochrony”. Zmiana ma odwrócić tę kolejność tak, aby „Okres ochrony” był prezentowany przed „Aktualizacja” dla każdej oferty.

Zakres jest wyłącznie prezentacyjny:
- bez zmian w logice wyliczania okresu ochrony,
- bez zmian w modelu `Offer`,
- bez zmian w repozytoriach danych i mockach runtime,
- bez zmian w filtrowaniu, sortowaniu i akcjach ofert.

### User Stories
- US-001: As a sales agent, I want to see the protection period before the update field on the offers list, so that the more business-relevant date range is visible earlier in the offer metadata.
  - Given użytkownik otworzył ekran listy ofert
  - When lista ofert zostanie wyrenderowana
  - Then pole „Okres ochrony” jest pokazane przed polem „Aktualizacja” w każdym wierszu oferty

- US-002: As a sales agent, I want the reordered metadata layout to stay consistent after filtering the list, so that the screen remains predictable regardless of visible results.
  - Given użytkownik zawęzi listę ofert filtrami
  - When na ekranie pozostaną tylko wybrane oferty
  - Then każda widoczna oferta nadal pokazuje „Okres ochrony” przed „Aktualizacja”

### Functional Requirements
- FR-001: Na ekranie `/offers` blok metadanych „Okres ochrony” musi być renderowany przed blokiem „Aktualizacja”.
- FR-002: Zmiana musi dotyczyć wszystkich ofert renderowanych przez `OffersHomePageComponent`, niezależnie od typu produktu (`MOTOR`, `CROP`).
- FR-003: Istniejąca wartość `protectionPeriodLabel` oraz jej formatowanie muszą pozostać bez zmian.
- FR-004: Zmiana nie może wpływać na działanie filtrów, sortowania, liczników, statusów i menu akcji.
- FR-005: Układ responsywny zdefiniowany w `offers-home-page.component.scss` musi pozostać poprawny po zmianie kolejności bloków.
- FR-006: Testy regresyjne muszą potwierdzać nową kolejność pól.

### Edge Cases
- EC-001: Gdy lista jest przefiltrowana do pojedynczej oferty, kolejność metadanych nadal musi być poprawna.
- EC-002: Gdy lista jest pusta i renderuje się `empty-state`, zmiana nie może wprowadzać błędów w widoku.
- EC-003: Na mniejszych breakpointach, gdzie `offer-row__meta-grid` zmienia liczbę kolumn, kolejność DOM i kolejność odczytu nadal muszą zachować „Okres ochrony” przed „Aktualizacja”.
- EC-004: Oferty crop i motor muszą zachować identyczną kolejność wspólnych pól metadanych mimo różnic w treści pierwszego bloku.

### Success Criteria
- [ ] Na ekranie `/offers` każda oferta pokazuje „Okres ochrony” przed „Aktualizacja”.
- [ ] Wartość „Okres ochrony” pozostaje taka sama jak przed zmianą.
- [ ] Zmiana nie wymaga modyfikacji `Offer`, `OffersRepository` ani plików `public/mock/*.json`.
- [ ] Test jednostkowy potwierdza kolejność etykiet w siatce metadanych.
- [ ] Test e2e potwierdza nową kolejność na działającym ekranie.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Ekran ofert jest zbudowany jako standalone Angular component w `src/app/features/offers/pages/offers-home-page.component.ts` z template w `src/app/features/offers/pages/offers-home-page.component.html`. Logika wyliczania okresu ochrony już istnieje:
- `protectionPeriodStart`
- `protectionPeriodEnd`
- `protectionPeriodLabel`

Dlatego zadanie nie wymaga zmian w TypeScript ani warstwie danych. Wystarczy zmienić kolejność dwóch sąsiednich bloków w sekcji `.offer-row__meta-grid` w template:
1. pozostawić blok „Okres ochrony” bez zmian treści,
2. przenieść go przed blok „Aktualizacja”.

Aktualny SCSS w `src/app/features/offers/pages/offers-home-page.component.scss` już obsługuje 5-elementową siatkę (`grid-template-columns: repeat(5, minmax(0, 1fr))`), więc nie ma potrzeby zmiany layoutu, jeśli po prostym przestawieniu bloków widok pozostaje poprawny.

Wzorzec repozytorium wskazuje, że ekran `/offers` korzysta z danych z `OffersRepository`, ale to zadanie nie zmienia ani źródła danych, ani mapowania modelu. `src/app/core/models/offer/offer.model.ts` i `src/app/core/repositories/offers.repository.ts` powinny pozostać nietknięte.

Wnioski z Confluence:
- Strona domenowa [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) potwierdza kontekst aplikacji `smart-agent` i wspiera utrzymanie zmiany lokalnie na ekranie listy ofert.
- W przestrzeni `Services` nie znaleziono istniejącej usługi ani API powiązanych z tym wymaganiem. No existing notification/service/API pattern relevant to reuse in this task.
- Ponieważ zadanie jest wyłącznie frontendowe, nie wprowadzamy żadnej nowej integracji runtime.

### Task Breakdown

#### Phase 1: Reorder metadata blocks in the offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zlokalizować kolejność pól metadanych | `src/app/features/offers/pages/offers-home-page.component.html` | Potwierdzić obecny układ w `.offer-row__meta-grid`: „Aktualizacja” przed „Okres ochrony”. |
| 1.2 | Przenieść blok „Okres ochrony” przed „Aktualizacja” | `src/app/features/offers/pages/offers-home-page.component.html` | Zamienić kolejność dwóch bloków metadanych bez zmiany ich zawartości i bindingów. |
| 1.3 | Zachować istniejącą logikę i stylowanie | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/pages/offers-home-page.component.scss` | Nie zmieniać `protectionPeriodLabel` ani układu siatki, o ile po reorderze widok pozostaje poprawny. |

#### Phase 2: Add regression coverage for the new order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Rozszerzyć test jednostkowy komponentu | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Dodać asercję sprawdzającą, że w renderze pierwszego `offer-row__meta-grid` etykieta „Okres ochrony” występuje przed „Aktualizacja”. |
| 2.2 | Zachować istniejące testy ochrony okresu | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Pozostawić i ewentualnie uzupełnić aktualne testy renderowania `protectionPeriodLabel`, aby upewnić się, że reorder nie zmienia wartości. |
| 2.3 | Rozszerzyć test Playwright | `tests/e2e/offers-protection-period.spec.ts` | Dodać weryfikację kolejności etykiet/metadanych w pierwszym widocznym `offer-row`, aby potwierdzić zmianę na działającym ekranie. |

#### Phase 3: Validate no unintended UI regression
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Zweryfikować brak zmian w danych i repozytoriach | `src/app/core/models/offer/offer.model.ts`, `src/app/core/repositories/offers.repository.ts` | Utrzymać brak modyfikacji poza warstwą widoku i testów. |
| 3.2 | Sprawdzić zachowanie po filtrowaniu | `tests/e2e/offers-protection-period.spec.ts` | Potwierdzić, że po użyciu wyszukiwarki nadal zachowana jest nowa kolejność pól dla pozostałej oferty. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Przestawi kolejność bloków „Okres ochrony” i „Aktualizacja” w `offer-row__meta-grid`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Doda test regresyjny potwierdzający kolejność pól metadanych. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Rozszerzy test e2e o weryfikację nowej kolejności na ekranie `/offers`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Otworzyć `/offers` i potwierdzić, że „Okres ochrony” jest widoczny przed „Aktualizacja” w każdym `offer-row`
5. [ ] Potwierdzić, że tekst `protectionPeriodLabel` nie zmienił formatu ani wartości
6. [ ] Przefiltrować listę i sprawdzić, że kolejność pól pozostaje poprawna