# SDLC-139: Dodaj testowe konto admin

## Specification

### Overview
Zadanie dostarcza nowego użytkownika mock `admin` z hasłem `admin` w istniejącym mechanizmie mockowego logowania Angulara, tak aby frontend mógł działać bez backendu i umożliwiać testowanie scenariuszy administracyjnych. Nowe konto ma być odrębnym wpisem, przygotowanym na wzór obecnego użytkownika `mmelissa` z `src/app/core/auth/auth.mock.interceptor.ts`, bez modyfikowania istniejących rekordów.

### User Stories
- US-001: As a frontend developer, I want to log in as `admin` in mock mode, so that I can test administrative flows without a backend.
  - Given aplikacja działa z istniejącym mock auth / When podam `admin/admin` na ekranie logowania / Then zostanę poprawnie zalogowany i przekierowany na chronioną trasę

- US-002: As a tester, I want the `admin` account to mirror the access profile of the existing admin-like mock user, so that admin views render without missing-role errors.
  - Given istnieje wzorcowy użytkownik `mmelissa` w danych mock / When tworzony jest nowy rekord `admin` / Then dziedziczy wymagane role i atrybuty potrzebne frontendowi, ale pozostaje osobnym kontem

- US-003: As a project team, I want existing mock users to keep working unchanged, so that adding `admin` does not regress current login scenarios.
  - Given istnieją już rekordy `askonieczny` i `mmelissa` / When dodany zostanie `admin` / Then obecne konta nadal logują się tak jak wcześniej

### Functional Requirements
- FR-001: Należy dodać rekord użytkownika `admin` do istniejącego źródła mock auth w `src/app/core/auth/auth.mock.interceptor.ts`.
- FR-002: Dla użytkownika `admin` należy skonfigurować hasło `admin`.
- FR-003: Login `admin` musi działać przez istniejący kontrakt auth używany przez frontend: `POST /api/auth/login`.
- FR-004: Nowy rekord musi zwracać odpowiedź zgodną z obecnym typem `AuthLoginResponse` z `src/app/core/auth/auth.service.ts`.
- FR-005: Użytkownik `admin` musi być odrębnym kontem, tj. mieć własny `username` i własny token/subject, bez nadpisywania wpisu `mmelissa`.
- FR-006: Dane dostępu i profil uprawnień użytkownika `admin` muszą być oparte na istniejącym rekordzie `mmelissa`, w szczególności w zakresie `roles` oraz innych pól użytkownika wykorzystywanych przez frontend.
- FR-007: Dodanie `admin` nie może zmieniać zachowania istniejących rekordów `askonieczny` i `mmelissa`.
- FR-008: Istniejący ekran logowania w `src/app/pages/login/login.component.ts` oraz `src/app/pages/login/login.component.html` nie wymaga zmian funkcjonalnych; nowy użytkownik ma działać w obecnym flow.
- FR-009: Należy dodać/uzupełnić testy potwierdzające, że `admin/admin` działa end-to-end w aktualnym mockowym mechanizmie logowania.
- FR-010: Należy zachować obecny runtime auth contract opisany przez usługę F01 i zakodowany w repozytorium: frontend nadal używa `POST /api/auth/login` i `POST /api/auth/logout`; zadanie rozszerza wyłącznie zestaw danych mock, nie wprowadza nowego fallbacku runtime.

### Edge Cases
- EC-001: Jeśli użytkownik poda `admin` z błędnym hasłem, aplikacja powinna nadal zwrócić standardowy błąd 401 z obecnego mechanizmu mock.
- EC-002: Jeśli użytkownik poda `admin/admin` ze spacjami wokół wartości, formularz powinien zadziałać poprawnie po trimowaniu przez `login.component.ts`.
- EC-003: Rekord `mmelissa` musi pozostać niezmieniony; dodanie `admin` nie może polegać na zamianie loginu w istniejącym wpisie.
- EC-004: Jeśli frontend opiera wyświetlanie na `roles[0]`, rekord `admin` musi zachować role w takiej formie, aby `AuthService.mapApiUserToCurrentUser()` wyliczył poprawne `role`.
- EC-005: Istniejące scenariusze logowania dla innych mock userów nie mogą przestać działać po rozszerzeniu mapy `MOCK_AUTH_USERS`.

### Success Criteria
- [ ] W `src/app/core/auth/auth.mock.interceptor.ts` istnieje osobny rekord `admin`
- [ ] Logowanie `admin/admin` kończy się sukcesem przez obecny flow `/api/auth/login`
- [ ] Odpowiedź dla `admin` jest zgodna z `AuthLoginResponse`
- [ ] Użytkownik `admin` zachowuje wymagane role/atrybuty wzorowane na `mmelissa`
- [ ] Rekord `mmelissa` pozostaje niezmieniony
- [ ] Istniejący mock users nadal działają bez regresji
- [ ] Testy potwierdzają logowanie `admin/admin` oraz brak wpływu na istniejące konta

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Repozytorium ma już gotowy, spójny mechanizm mock auth:

- kontrakt auth w `src/app/core/auth/auth.service.ts`
- interceptowanie runtime requestów w `src/app/core/auth/auth.mock.interceptor.ts`
- rejestrację interceptora w `src/app/app.config.ts`
- login UI w `src/app/pages/login/login.component.ts`
- e2e w `tests/e2e/login.spec.ts`, `tests/e2e/login-auth.spec.ts`, `tests/e2e/login-session.spec.ts`

Dlatego implementacja powinna być minimalna i data-driven: **rozszerzyć istniejącą mapę `MOCK_AUTH_USERS`, bez zmiany flow logowania, routingu, guardów ani modelu auth**.

Reuse z Confluence:
- F01 — Authentication & Session Management: https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management
- F01 — Authentication & Session — Test Scenarios: https://apreel.atlassian.net/wiki/spaces/Services/pages/174653441/F01+Authentication+Session+Test+Scenarios

Te strony potwierdzają, że docelowy runtime default pozostaje oparty o istniejący kontrakt uwierzytelniania/sesji. W kodzie odpowiada mu już:
- `POST /api/auth/login`
- `POST /api/auth/logout`
- request body `{ username, password }`
- response mapowana do `AuthLoginResponse` / `AuthCurrentUser`

Dla tego zadania nie należy wprowadzać nowego mock JSON ani równoległego mechanizmu logowania. Zmiana ma ograniczyć się do rozszerzenia obecnego, już zarejestrowanego mock interceptora o nowy rekord `admin`, nadal obsługujący ten sam kontrakt endpointów.

Istotne obserwacje z repo:
- `src/app/core/auth/auth.mock.interceptor.ts` jest jedynym źródłem danych mock dla logowania.
- Aktualna mapa zawiera `askonieczny` i `mmelissa`; rekord `mmelissa` jest najbliższym wzorcem dla nowego konta administracyjnego.
- `src/app/core/auth/auth.service.ts` wylicza `role` z pierwszego elementu `roles`, więc kluczowe jest zachowanie poprawnego `roles[]` w odpowiedzi.
- `src/app/pages/login/login.component.ts` trimuje dane wejściowe i nie wymaga zmian, by obsłużyć `admin/admin`.
- `tests/e2e/login.spec.ts` jest najlepszym miejscem do potwierdzenia, że nowe konto działa przez pełny flow UI.
- Pozostałe e2e (`login-auth.spec.ts`, `login-session.spec.ts`) już pokrywają dotychczasowe mock users, więc pomagają wykryć regresję bez dodatkowych zmian zakresu.

Podejście implementacyjne:
1. W `auth.mock.interceptor.ts` skopiować strukturę odpowiedzi użytkownika `mmelissa`.
2. Dodać nowy wpis `admin` z hasłem `admin`.
3. Nadpisać pola tożsamościowe nowego konta (`username`, token subject, ewentualnie display fields), ale zachować pola dostępowe wzorowane na `mmelissa` (`roles` i inne atrybuty potrzebne frontendowi).
4. Nie modyfikować istniejącego wpisu `mmelissa`.
5. Dodać test jednostkowy/interceptorowy lub e2e potwierdzający logowanie `admin/admin`.
6. Zaktualizować co najmniej jeden scenariusz Playwright tak, aby acceptance path używał nowego konta `admin`.

No relevant domain guidance was found in the configured `SDLC` Confluence space.

### Task Breakdown

#### Phase 1: Rozszerzenie źródła danych mock auth
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zidentyfikować rekord wzorcowy | `src/app/core/auth/auth.mock.interceptor.ts` | Użyć istniejącego wpisu `mmelissa` jako źródła ról i atrybutów potrzebnych frontendowi. |
| 1.2 | Dodać nowy rekord `admin` | `src/app/core/auth/auth.mock.interceptor.ts` | Dopisać do `MOCK_AUTH_USERS` osobny wpis z hasłem `admin`, odpowiedzią zgodną z `AuthLoginResponse` i własnym `username/token`. |
| 1.3 | Zachować separację kont | `src/app/core/auth/auth.mock.interceptor.ts` | Upewnić się, że dodanie `admin` nie modyfikuje rekordu `mmelissa` ani logiki obsługi pozostałych użytkowników. |
| 1.4 | Nie zmieniać kontraktu auth | `src/app/core/auth/auth.service.ts`, `src/app/app.config.ts` | Pozostawić bez zmian istniejące endpointy i rejestrację interceptora; zadanie dotyczy danych, nie infrastruktury auth flow. |

#### Phase 2: Pokrycie testami i regresja
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dodać test dla nowego mock usera | `src/app/core/auth/auth.mock.interceptor.spec.ts` | Utworzyć współlokowany test sprawdzający, że `admin/admin` zwraca 200 oraz profil zgodny z oczekiwanym wzorcem po `mmelissa`. |
| 2.2 | Zabezpieczyć brak regresji dla wzorca | `src/app/core/auth/auth.mock.interceptor.spec.ts` | Dodać asercję, że rekord `mmelissa` nadal loguje się z niezmienioną odpowiedzią po dodaniu `admin`. |
| 2.3 | Potwierdzić flow UI dla `admin` | `tests/e2e/login.spec.ts` | Zmienić happy-path login na `admin/admin` lub dodać dedykowany scenariusz Playwright, który loguje nowym kontem i weryfikuje przekierowanie na `/empty` oraz utrzymanie sesji po odświeżeniu. |
| 2.4 | Utrzymać istniejące scenariusze innych użytkowników | `tests/e2e/login-auth.spec.ts`, `tests/e2e/login-session.spec.ts` | Pozostawić obecne testy z dotychczasowymi użytkownikami jako regresję potwierdzającą, że rozszerzenie `MOCK_AUTH_USERS` niczego nie psuje. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/auth/auth.mock.interceptor.ts` | MODIFY | Dodać nowy rekord `admin` wzorowany na `mmelissa`, bez zmiany istniejących użytkowników i bez naruszenia kontraktu auth |
| `src/app/core/auth/auth.mock.interceptor.spec.ts` | CREATE | Dodać testy jednostkowe/interceptorowe dla logowania `admin/admin` oraz regresji `mmelissa` |
| `tests/e2e/login.spec.ts` | MODIFY | Potwierdzić w Playwright, że nowe konto `admin` działa przez pełny flow logowania i sesji |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `admin/admin` logs in successfully from `/login`
5. [ ] Successful login redirects to `/empty`
6. [ ] Refresh after `admin` login preserves session
7. [ ] `mmelissa` still logs in unchanged
8. [ ] Invalid password for `admin` still returns standard invalid-credentials behavior