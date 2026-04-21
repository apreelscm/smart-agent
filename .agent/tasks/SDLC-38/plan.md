# SDLC-38: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Rozszerzenie modelu kwot i sposobu ich prezentacji w aplikacji tak, aby system poprawnie obsługiwał wartości pieniężne w walutach PLN, EUR i USD zamiast obecnego, sztywnego założenia `PLN`. Zmiana obejmie warstwę modeli domenowych, widoki list ofert i podsumowań oraz przygotowanie danych referencyjnych/moków w repozytorium tak, aby UI renderował właściwą walutę dla każdej kwoty.

### User Stories
- US-001: As a agent, I want to see premium amounts in the actual offer currency, so that I do not misread foreign-currency offers as PLN.
  - Given oferta zawiera kwotę z walutą EUR lub USD / When otwieram listę ofert lub podsumowanie / Then system wyświetla kwotę z poprawnym symbolem i formatem waluty.
- US-002: As a developer, I want the money model to support multiple currencies, so that kolejne funkcje nie były oparte na założeniu, że każda kwota jest w PLN.
  - Given model `Money` jest używany w wariantach, ratach i liniach polisowych / When zapisuję lub odczytuję dane / Then typ systemowy dopuszcza PLN, EUR i USD bez obejść typu `as const`.
- US-003: As a tester, I want representative mock data with foreign currencies, so that I can verify end-to-end rendering and calculations for PLN, EUR and USD.
  - Given dane ofert zawierają różne waluty / When uruchamiam aplikację / Then widać co najmniej przykłady dla PLN, EUR i USD.

### Functional Requirements
- FR-001: Model `Money` musi obsługiwać waluty `PLN | EUR | USD`.
- FR-002: Wszystkie miejsca korzystające z `Money.currency` muszą kompilować się bez założenia, że waluta zawsze wynosi `PLN`.
- FR-003: Widok listy ofert musi wyświetlać składkę używając waluty przypisanej do danej oferty/wybranego planu płatności, a nie stałej `PLN`.
- FR-004: Widok dialogu zmiany statusu oferty musi wyświetlać składkę w rzeczywistej walucie oferty.
- FR-005: Widok podsumowania w wizardzie musi wyświetlać składkę całkowitą i linie ochrony w walucie przypisanej do danych oferty.
- FR-006: Dane mock muszą zawierać przykłady kwot w PLN, EUR i USD, zachowując spójność waluty wewnątrz pojedynczej oferty/wariantu/planu płatności.
- FR-007: Obliczenia arytmetyczne w istniejących komponentach mogą nadal operować na `amount`, ale muszą zakładać spójność waluty w obrębie jednej kalkulacji/oferty.
- FR-008: Jeśli ekran agreguje wiele ofert w jedną metrykę i nie posiada logiki przewalutowania, musi pozostać w walucie domyślnej lub zostać ograniczony do danych PLN z jawnym opisem implementacyjnym, zamiast mieszać kwoty różnych walut.

### Edge Cases
- EC-001: Jeśli oferta nie ma `selectedPaymentPlan`, UI powinien pobrać walutę z `variants[0]?.totalPremium.currency` tak samo jak dziś pobiera kwotę.
- EC-002: Jeśli dane są niepełne i brak waluty w obiekcie źródłowym, system powinien użyć istniejącego typu `Money` jako kontraktu obowiązkowego i nie dodawać fallbacku runtime na sztywne `PLN`.
- EC-003: Dla ekranów agregujących oferty w różnej walucie nie wolno sumować wartości bez przewalutowania; należy ograniczyć agregację do jednolitej waluty albo oznaczyć brak agregacji międzywalutowej.
- EC-004: Dla ofert crop i motor implementacja musi działać identycznie, bo oba produkty używają wspólnego modelu `Money`.
- EC-005: Rekalkulacja dodatków w `OfferWizardStateService` musi zachować walutę istniejącej linii/variantu zamiast wpisywać literalnie `PLN`, jeśli oferta źródłowa jest w EUR lub USD.

### Success Criteria
- [ ] Typ `Money` dopuszcza PLN, EUR i USD.
- [ ] Lista ofert renderuje kwoty zgodnie z walutą oferty.
- [ ] Dialog zmiany statusu renderuje kwoty zgodnie z walutą oferty.
- [ ] Podsumowanie wizarda renderuje składki i linie ochrony zgodnie z walutą oferty.
- [ ] Mock data zawiera przykłady ofert z EUR i USD.
- [ ] Projekt buduje się bez błędów typów po zmianie modelu walut.
- [ ] Istniejące testy i/lub nowe testy pokrywają obsługę wielu walut.

### Open Questions
- [NEEDS CLARIFICATION: czy metryka „Średnia składka” na liście ofert ma liczyć wyłącznie oferty w PLN, czy zostać ukryta/oznaczona przy mieszanych walutach?]

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 20 z standalone components i PrimeNG. Dane ofert są obecnie ładowane z moków przez `src/app/core/repositories/offers.repository.ts`, a model waluty jest zdefiniowany centralnie w `src/app/core/models/common/money.model.ts`. Obecnie model ogranicza walutę do `'PLN'`, a widoki w `src/app/features/offers/pages/offers-home-page.component.html` oraz `src/app/features/offer-wizard/pages/summary-step-page.component.html` używają pipe `currency` ze stałą `PLN`, co bezpośrednio blokuje obsługę EUR i USD.

W przestrzeni Services znaleziono istniejącą dokumentację integracji kursów walut:
- [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
- [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Te strony dokumentują istniejące źródło kursów walut i należy je traktować jako runtime default dla przyszłego przewalutowania/integracji. Jednak dla tego tasku zakres repozytorium i obecnego kodu wskazuje na obsługę prezentacji oraz modelu walut, nie na wdrożenie runtime przewalutowania. Plan więc reuse’uje wiedzę z Confluence jako kierunek dla przyszłych agregacji międzywalutowych i nie proponuje żadnych mock JSON jako runtime fallback dla kursów. Jednocześnie w skonfigurowanej przestrzeni DOMAIN `SDLC` nie znaleziono istotnych ADR/convention dla tego zagadnienia.

Kluczowe decyzje:
1. **Uogólnić typ `Money`** przez wprowadzenie unii walut `PLN | EUR | USD`.
2. **Dodać pomocnicze metody pobierające pełny obiekt Money**, zamiast przekazywać do szablonu samo `amount`. To pozwoli używać `currency` pipe z dynamicznym kodem waluty.
3. **Zachować istniejące obliczenia numeryczne lokalnie w obrębie jednej oferty**, bo tam waluta jest spójna.
4. **Nie sumować bezrefleksyjnie kwot z różnych walut** na ekranach portfelowych. Najbardziej wrażliwy punkt to `summaryTiles` w `offers-home-page.component.ts`, szczególnie kafel „Średnia składka”.
5. **Zaktualizować mock data** tak, aby przynajmniej część ofert i powiązanych struktur (`selectedPaymentPlan`, `variants`, `installments`, `policyLines`, `premiumDelta`) używała EUR i USD. Dzięki temu UI będzie realnie weryfikowalny.
6. **Zachować zgodność z obecnym wzorcem repo**: logika w komponencie, brak dodatkowej warstwy serwisowej, minimalne zmiany zakresowe.

### Task Breakdown

#### Phase 1: Uogólnienie modelu domenowego walut
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Rozszerzyć typ waluty | `src/app/core/models/common/money.model.ts` | Zmienić `currency: 'PLN'` na dedykowany union type, np. `type SupportedCurrency = 'PLN' | 'EUR' | 'USD'`, a następnie użyć go w `Money`. |
| 1.2 | Zweryfikować zgodność modeli pośrednich | `src/app/core/models/offer/offer-variant.model.ts`, `src/app/core/models/offer/offer.model.ts` | Potwierdzić, że modele korzystające z `Money` nie wymagają dalszych zmian strukturalnych, a jedynie nowego typu. |
| 1.3 | Usunąć twarde ograniczenia typów w logice rekalkulacji | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | Zastąpić miejsca z `currency: 'PLN'` i `'PLN' as const` logiką dziedziczenia waluty z istniejącego wariantu/linii lub pomocniczą stałą typu wspieranego. |

#### Phase 2: Dynamiczne renderowanie walut w UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać helpery zwracające pełny `Money` dla ofert | `src/app/features/offers/pages/offers-home-page.component.ts` | Obok `getPrimaryPremium()` dodać metodę typu `getPrimaryPremiumMoney(offer)` zwracającą `Money`, aby szablon znał zarówno `amount`, jak i `currency`. |
| 2.2 | Zmienić renderowanie składki na liście ofert | `src/app/features/offers/pages/offers-home-page.component.html` | Zastąpić `currency: 'PLN'` dynamicznym `currency: getPrimaryPremiumMoney(offer).currency`. |
| 2.3 | Zmienić renderowanie składki w dialogu statusów | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.ts` | Użyć tej samej metody helperowej w sekcji dialogu przejścia statusu. |
| 2.4 | Dodać helpery walutowe w podsumowaniu wizarda | `src/app/features/offer-wizard/pages/summary-step-page.component.ts` | Wprowadzić metody np. `discountedPremiumMoney()` i `lineCurrency(line)` lub pobieranie waluty z `selectedVariant().totalPremium.currency`. |
| 2.5 | Zmienić renderowanie składki w podsumowaniu wizarda | `src/app/features/offer-wizard/pages/summary-step-page.component.html` | Zastąpić stałe `PLN` dynamicznym kodem waluty dla składki głównej i każdej linii ochrony. |

#### Phase 3: Obsługa agregacji i danych wielowalutowych
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Zabezpieczyć agregację „Średnia składka” przed mieszaniem walut | `src/app/features/offers/pages/offers-home-page.component.ts` | Zmodyfikować `summaryTiles`, aby liczyło średnią wyłącznie dla ofert w jednej walucie referencyjnej (najbezpieczniej PLN) lub wyświetlało neutralny komunikat, zgodnie z decyzją po doprecyzowaniu. |
| 3.2 | Wprowadzić przykładowe oferty w EUR i USD | `public/mock/offers.json` | Zmienić wybrane oferty i wszystkie ich zagnieżdżone kwoty na EUR i USD, zachowując spójność w obrębie konkretnej oferty. |
| 3.3 | Zweryfikować template crop | `public/mock/offers-crop-template.json` | Jeśli template zawiera `Money`, upewnić się, że struktura pozostaje zgodna z rozszerzonym typem i może przyjąć inne waluty bez błędów. |

#### Phase 4: Testy i walidacja
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Dodać/rozszerzyć testy komponentów dla renderowania waluty | `src/app/features/offers/pages/offers-home-page.component.spec.ts` (CREATE), `src/app/features/offer-wizard/pages/summary-step-page.component.spec.ts` (CREATE) | Dodać testy sprawdzające renderowanie PLN/EUR/USD na bazie fixture data lub stubów komponentu/repozytorium. |
| 4.2 | Dodać test modelu/serwisu dla rekalkulacji waluty | `src/app/features/offer-wizard/state/offer-wizard-state.service.spec.ts` (CREATE lub MODIFY jeśli istnieje) | Zweryfikować, że rekalkulacje dodatków i rat nie wymuszają `PLN` dla oferty w EUR/USD. |
| 4.3 | Wykonać regresję smoke | `tests/e2e/smoke.spec.ts` | W razie potrzeby rozszerzyć smoke test o asercję obecności symboli walut na głównych ekranach, bez wychodzenia poza scope tasku. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Rozszerzenie typu walut z PLN do PLN/EUR/USD. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Dodanie helperów `Money` oraz korekta agregacji wielowalutowej. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Dynamiczne renderowanie walut na liście ofert i w dialogu statusów. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.ts` | MODIFY | Dynamiczne pobieranie waluty dla składki końcowej i linii ochrony. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.html` | MODIFY | Dynamiczne renderowanie waluty w podsumowaniu. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Usunięcie założeń, że każda przeliczana kwota ma walutę PLN. |
| `public/mock/offers.json` | MODIFY | Dodanie realistycznych danych ofertowych w EUR i USD. |
| `public/mock/offers-crop-template.json` | MODIFY | Utrzymanie zgodności template z obsługą wielu walut, jeśli zawiera `Money`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Testy renderowania waluty na liście ofert. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.spec.ts` | CREATE | Testy renderowania waluty w podsumowaniu wizarda. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.spec.ts` | CREATE/MODIFY | Testy zachowania waluty w rekalkulacjach. |
| `tests/e2e/smoke.spec.ts` | MODIFY | Lekka regresja e2e dla wielowalutowego UI, jeśli potrzebna. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements