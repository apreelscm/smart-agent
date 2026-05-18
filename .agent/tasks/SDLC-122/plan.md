# SDLC-122: Na ekranie listy kwitariuszy przesun kolumne ze statusem przed kolumne z Kwota (z odsetkami)

## Specification

### Overview
Zadanie dotyczy wyłącznie warstwy prezentacji na ekranie listy kwitariuszy pod ścieżką `/rozliczenia/kwitariusze`. Celem jest poprawa czytelności tabeli poprzez zmianę kolejności kolumn tak, aby kolumna **Status** była wyświetlana bezpośrednio przed kolumną **Kwota (z odsetkami)**, bez zmiany danych, logiki biznesowej, filtrów ani zachowania istniejących akcji.

### User Stories
- US-001: As a użytkownik przeglądający listę kwitariuszy, I want widzieć kolumnę statusu przed kolumną kwoty, so that szybciej oceniam stan pozycji przed analizą wartości.
  - Given użytkownik otwiera ekran listy kwitariuszy / When tabela zostaje wyrenderowana / Then kolumna **Status** jest widoczna bezpośrednio przed **Kwota (z odsetkami)**

- US-002: As a użytkownik systemu, I want zmiana dotyczyła tylko układu kolumn, so that dane, sortowanie i pozostałe funkcje listy pozostają bez regresji.
  - Given lista zawiera dane i aktywne mechanizmy tabeli / When zmienia się kolejność kolumn / Then wartości w kolumnach, akcje wiersza i obecne sortowanie działają jak dotychczas

### Functional Requirements
- FR-001: Na ekranie listy kwitariuszy kolejność renderowania kolumn musi zostać zmieniona tak, aby `status` był wyświetlany bezpośrednio przed `amount`.
- FR-002: Etykiety kolumn pozostają bez zmian: **Status** oraz **Kwota (z odsetkami)**.
- FR-003: Zawartość komórek obu kolumn pozostaje bez zmian, w tym badge statusu oraz sposób prezentacji kwoty całkowitej i odsetek.
- FR-004: Zmiana musi być ograniczona do ekranu listy kwitariuszy; modele danych i serwis `KwitariuszService` nie wymagają zmian kontraktowych.
- FR-005: Istniejące zachowanie tabeli opartej o Angular Material, w tym aktualnie dostępne sortowanie i akcje wiersza, musi pozostać bez zmian.
- FR-006: Należy dodać regresyjne pokrycie testowe potwierdzające kolejność kolumn.

### Edge Cases
- EC-001: Gdy tabela nie zawiera rekordów, nagłówki nadal powinny być renderowane w nowej kolejności.
- EC-002: Zmiana nie może spowodować duplikacji ani pominięcia żadnej kolumny w `displayedColumns`.
- EC-003: Jeśli użytkownik sortuje po kolumnie **Kwota (z odsetkami)**, samo przestawienie kolumny nie może zmienić zachowania sortowania.
- EC-004: Na węższych viewportach układ tabeli ma zachować dotychczasowy styl; zmieniana jest tylko kolejność kolumn, nie ich stylowanie.

### Success Criteria
- [ ] Na ekranie `/rozliczenia/kwitariusze` kolumna **Status** jest widoczna bezpośrednio przed **Kwota (z odsetkami)**.
- [ ] Dane w kolumnach **Status** i **Kwota (z odsetkami)** są identyczne jak przed zmianą.
- [ ] Żadna kolumna nie została usunięta ani zduplikowana.
- [ ] Istniejące funkcje tabeli nadal działają bez regresji.
- [ ] Testy potwierdzają nową kolejność kolumn.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Repozytorium jest aplikacją Angular 21 opartą o standalone components i Angular Material. Ekran listy kwitariuszy jest zrealizowany w `src/app/features/kwitariusze/kwitariusze.ts` oraz `src/app/features/kwitariusze/kwitariusze.html`, gdzie tabela Material korzysta z `MatTableDataSource` i tablicy `displayedColumns`. W tym wzorcu to właśnie `displayedColumns` steruje kolejnością renderowania nagłówków i komórek w `mat-header-row` oraz `mat-row`.

Najmniejsza i najbezpieczniejsza zmiana to:
1. przestawienie kolejności kluczy w `displayedColumns` z `... 'amount', 'status', ...` na `... 'status', 'amount', ...`,
2. uporządkowanie bloków `<ng-container matColumnDef="...">` w `kwitariusze.html`, aby odpowiadały runtime order i ułatwiały utrzymanie kodu.

Nie należy zmieniać:
- `src/app/core/services/kwitariusz.service.ts`, ponieważ serwis już dostarcza oba pola i zadanie nie wymaga zmian danych,
- `src/app/core/models/kwitariusz.model.ts`, ponieważ kontrakt modelu pozostaje poprawny,
- `src/app/features/kwitariusze/kwitariusze.scss`, o ile po zmianie kolejności nie wystąpi problem wizualny.

Wynik analizy Confluence:
- **Services**: nie znaleziono istniejącej usługi/API dotyczącej kwitariuszy w przestrzeni `Services`, więc zadanie pozostaje zmianą frontendową bez nowej integracji.
- **SDLC**: znaleziono stronę kontekstową **Ekrany**: https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany. Dostarcza ona ogólny kontekst ekranów aplikacji smart-agent, ale nie definiuje szczególnej konwencji dla kolejności kolumn; implementacja powinna więc pozostać zgodna z aktualnym wzorcem komponentów ekranowych w repozytorium.

Dla zabezpieczenia przed regresją należy dodać test komponentu, który:
- sprawdzi kolejność `displayedColumns`,
- opcjonalnie zweryfikuje kolejność tekstów nagłówków wyrenderowanych przez tabelę.

### Task Breakdown

#### Phase 1: Aktualizacja konfiguracji tabeli kwitariuszy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zmiana kolejności kolumn w komponencie | `src/app/features/kwitariusze/kwitariusze.ts` | Zaktualizować `displayedColumns`, aby `status` był umieszczony bezpośrednio przed `amount`, bez zmiany innych kolumn. |
| 1.2 | Uporządkowanie definicji kolumn w szablonie | `src/app/features/kwitariusze/kwitariusze.html` | Przenieść blok `matColumnDef="status"` nad `matColumnDef="amount"` dla spójności z faktyczną kolejnością renderowania; nie zmieniać zawartości komórek ani nagłówków. |

#### Phase 2: Test regresyjny dla kolejności kolumn
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać test komponentu listy kwitariuszy | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Utworzyć test standalone component z `TestBed`, który potwierdzi nową kolejność `displayedColumns` oraz nagłówków tabeli. |
| 2.2 | Zweryfikować brak wpływu na dane | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Sprawdzić, że nagłówki **Status** i **Kwota (z odsetkami)** nadal renderują się poprawnie i bez duplikacji. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Zmiana kolejności `displayedColumns`, która steruje układem tabeli. |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Uporządkowanie definicji kolumn zgodnie z nową kolejnością prezentacji. |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | CREATE | Test regresyjny potwierdzający kolejność kolumn na liście kwitariuszy. |

### Verification Steps
1. [ ] Build succeeds (`npm run build`)
2. [ ] Tests pass (`npm test -- --watch=false`)
3. [ ] New tests cover requirements, w szczególności kolejność **Status** przed **Kwota (z odsetkami)** na ekranie `/rozliczenia/kwitariusze`