# SDLC-125: Na ekranie listy kwitariuszy przesun kolumne ze statusem przed kolumne z Kwota (z odsetkami)

## Specification

### Overview
Zmiana obejmuje wyłącznie standardowy ekran listy kwitariuszy pod trasą `/rozliczenia/kwitariusze` i polega na przestawieniu kolejności kolumn w tabeli Angular Material tak, aby kolumna `Status` była wyświetlana bezpośrednio przed kolumną `Kwota (z odsetkami)`. Zakres nie obejmuje zmian danych, logiki filtrowania, sortowania, uprawnień, backendu ani innych widoków.

### User Stories
- US-001: As a użytkownik przeglądający standardową listę kwitariuszy, I want widzieć status kwitariusza przed kwotą, so that szybciej oceniam stan dokumentu.
  - Given użytkownik otwiera standardowy widok listy kwitariuszy / When tabela zostanie wyrenderowana / Then kolumna `Status` jest wyświetlana przed kolumną `Kwota (z odsetkami)`
- US-002: As a użytkownik systemu, I want zachować dotychczasowe dane i zachowania listy, so that zmiana dotyczy tylko układu prezentacji.
  - Given lista zawiera dane, filtry i sortowanie / When zmiana zostanie wdrożona / Then wartości kolumn, logika obliczeń i pozostałe funkcje pozostają bez zmian

### Functional Requirements
- FR-001: Standardowa lista kwitariuszy musi renderować kolumny tabeli w kolejności, w której `status` znajduje się bezpośrednio przed `amount`.
- FR-002: Nagłówek kolumny `Status` ma zachować obecną nazwę i sposób renderowania badge'y statusu.
- FR-003: Nagłówek oraz komórki kolumny `Kwota (z odsetkami)` mają zachować obecną nazwę, format i logikę prezentacji kwot z `presentedTotalAmount()` oraz `presentedInterestAmount()`.
- FR-004: Pozostałe kolumny muszą zachować dotychczasową kolejność względem siebie.
- FR-005: Trasa standardowej listy `/rozliczenia/kwitariusze` musi pozostać jedynym miejscem objętym zmianą.
- FR-006: Istniejące zachowania listy oparte na `KwitariuszService.filtered()` i `MatTableDataSource` nie mogą zostać zmienione.

### Edge Cases
- EC-001: Kolumna `Status` nie ma obecnie `mat-sort-header`; po zmianie pozycji nadal pozostaje niesortowalna, a kolumna `Kwota (z odsetkami)` zachowuje obecne sortowanie.
- EC-002: Zmiana kolejności nie może zaburzyć wyrównania sticky header row generowanego przez `matHeaderRowDef`.
- EC-003: Aktywne filtry, licznik wyników i zmiana waluty nie mogą wpływać na kolejność kolumn ani odwrotnie.
- EC-004: Jeżeli testy odwołują się do pozycji nagłówków w tabeli, muszą zostać zaktualizowane tak, aby weryfikowały nowy porządek bez zmiany oczekiwanych danych.

### Success Criteria
- [ ] Po wejściu na `/rozliczenia/kwitariusze` nagłówek `Status` jest widoczny przed `Kwota (z odsetkami)`
- [ ] Komórki statusu nadal wyświetlają te same badge'e dla tych samych rekordów
- [ ] Komórki kwoty nadal wyświetlają te same wartości i format waluty co przed zmianą
- [ ] Pozostałe kolumny zachowują dotychczasową kolejność
- [ ] Testy jednostkowe i e2e potwierdzają nową kolejność bez regresji funkcjonalnej

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Repozytorium to aplikacja Angular 21 oparta o standalone components i Angular Material. Standardowa lista kwitariuszy jest renderowana przez `KwitariuszeComponent` pod trasą zdefiniowaną w `src/app/app.routes.ts`. Sama kolejność kolumn jest kontrolowana przede wszystkim przez tablicę `displayedColumns` w `src/app/features/kwitariusze/kwitariusze.ts`, a zawartość poszczególnych kolumn przez definicje `ng-container matColumnDef` w `src/app/features/kwitariusze/kwitariusze.html`.

W tym zadaniu najbezpieczniejsza zmiana to:
1. przestawić `displayedColumns` tak, aby `status` występował przed `amount`,
2. dla spójności utrzymaniowej dopasować kolejność bloków `matColumnDef` w szablonie HTML do kolejności renderowania,
3. dodać regresyjne testy jednostkowe i e2e w istniejącym stylu repo.

Nie znaleziono żadnych relewantnych materiałów w skonfigurowanych przestrzeniach Confluence:
- API/SERVICE: brak wyników w `Services`
- DOMAIN: brak wyników w `SDLC`

W związku z tym plan opiera się wyłącznie na istniejących wzorcach kodu w repozytorium. Zadanie nie wymaga zmian w `src/app/core/services/kwitariusz.service.ts`, modelach ani integracjach zewnętrznych, ponieważ Jira jednoznacznie ogranicza zakres do warstwy prezentacji UI.

### Task Breakdown

#### Phase 1: Reorder standard kwitariusze table columns
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zmienić kolejność renderowanych kolumn | `src/app/features/kwitariusze/kwitariusze.ts` | Zaktualizować `displayedColumns`, aby kolejność była: `type`, `number`, `policyNumber`, `insuredName`, `issueDate`, `status`, `amount`, `actions`. |
| 1.2 | Utrzymać spójność deklaracji kolumn z nową kolejnością | `src/app/features/kwitariusze/kwitariusze.html` | Przenieść blok `ng-container matColumnDef="status"` nad blok `amount`, bez zmiany treści komórek i nagłówków. |
| 1.3 | Zweryfikować brak potrzeby zmian stylów | `src/app/features/kwitariusze/kwitariusze.scss` | Sprawdzić, czy po przestawieniu kolumn nie występują problemy z układem; modyfikować tylko jeśli pojawi się realna regresja wizualna. |

#### Phase 2: Add regression coverage for column order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać test jednostkowy kolejności nagłówków | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Dodać przypadek sprawdzający kolejność nagłówków tabeli i potwierdzający, że `Status` poprzedza `Kwota (z odsetkami)` przy niezmienionej zawartości danych. |
| 2.2 | Dodać test e2e standardowego widoku listy | `tests/e2e/kwitariusze-column-order.spec.ts` | Utworzyć dedykowany scenariusz Playwright otwierający `/rozliczenia/kwitariusze` i weryfikujący kolejność nagłówków tabeli w standardowym widoku. |
| 2.3 | Użyć istniejących helperów testowych | `tests/e2e/helpers/visual-snapshot.ts` | Nie zmieniać helpera; wykorzystać go w nowym teście zgodnie z istniejącym wzorcem snapshot stepów. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Przestawienie `displayedColumns`, które kontroluje kolejność kolumn tabeli. |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Uporządkowanie definicji `matColumnDef`, aby odpowiadały nowej kolejności prezentacji. |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Dodanie testu jednostkowego zabezpieczającego kolejność `Status` przed `Kwota (z odsetkami)`. |
| `tests/e2e/kwitariusze-column-order.spec.ts` | CREATE | Nowy test Playwright weryfikujący kolejność kolumn na standardowej liście kwitariuszy. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` kończy się bez błędów
5. [ ] `ng test --watch=false` lub równoważny przebieg testów jednostkowych potwierdza nowy test komponentu
6. [ ] `npx playwright test tests/e2e/kwitariusze-column-order.spec.ts` potwierdza kolejność nagłówków na `/rozliczenia/kwitariusze`
7. [ ] Manualnie sprawdzono, że w tabeli `Status` znajduje się przed `Kwota (z odsetkami)`, a wartości statusu i kwoty pozostały bez zmian