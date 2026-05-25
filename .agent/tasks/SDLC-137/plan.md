# SDLC-137: Dodaj ekran logowania z polami na login i hasło.

## Specification

### Overview
Zadanie dostarcza działający ekran logowania dla aplikacji Angular, oparty o już istniejący szkielet autoryzacji w repozytorium. Użytkownik ma widzieć formularz z polami login i hasło, móc zalogować się danymi `admin/admin`, a po sukcesie zostać przekierowany na `/empty`. Dla pustych lub błędnych danych aplikacja ma pozostać na ekranie logowania i pokazać odpowiedni komunikat.

### User Stories
- US-001: As a user, I want to see a login screen with login and password fields, so that I can start authentication.
  - Given I open the application / When the login view loads / Then I see a login field, a password field, and a submit button

- US-002: As a user, I want to log in with `admin/admin`, so that I can access the protected part of the app.
  - Given I am on `/login` / When I submit valid credentials `admin/admin` / Then I am authenticated and redirected to `/empty`

- US-003: As a user, I want clear feedback for invalid or missing credentials, so that I know why login failed.
  - Given I leave fields empty or enter invalid credentials / When I submit the form / Then I stay on `/login` and see validation or authentication feedback

### Functional Requirements
- FR-001: The application shall redirect `/` to `/login` for unauthenticated users.
- FR-002: The login page shall render a form with:
  - a login field,
  - a password field,
  - a submit button.
- FR-003: The password field shall mask entered characters.
- FR-004: The login form shall reject empty or whitespace-only values for both fields.
- FR-005: The component shall submit trimmed credentials through the existing auth abstraction in `src/app/core/auth/auth.service.ts`, not implement authentication inline.
- FR-006: The UI-facing “login” field may continue to map to the backend/request property `username` to stay compatible with the existing code path.
- FR-007: Successful authentication with `admin/admin` shall persist authenticated frontend state and navigate to `/empty`.
- FR-008: Invalid credentials shall keep the user on `/login` and display a standard invalid-credentials message.
- FR-009: Non-401 login failures shall keep the user on `/login` and display a generic sign-in error.
- FR-010: Protected routes such as `/empty` and `/design` shall require authentication.
- FR-011: Visiting `/login` while already authenticated shall redirect the user to `/empty`.

### Edge Cases
- EC-001: User submits whitespace-only values.
  - Expected behavior: values are treated as empty, validation messages are shown, and no login request is sent.
- EC-002: User clicks submit multiple times before the first request finishes.
  - Expected behavior: duplicate submissions are blocked while the request is in flight.
- EC-003: User opens `/design` or `/empty` without being authenticated.
  - Expected behavior: route guard redirects to `/login`.
- EC-004: User refreshes the browser on `/empty` after a successful login.
  - Expected behavior: persisted auth state is restored and the user remains on `/empty`.
- EC-005: User opens `/login` after already being authenticated.
  - Expected behavior: guest guard redirects to `/empty`.
- EC-006: Backend/auth endpoint returns an unexpected server error.
  - Expected behavior: authentication state is not persisted and a generic failure message is shown.

### Success Criteria
- [ ] Opening `/` as a guest redirects to `/login`
- [ ] The login view shows login, password, and submit controls
- [ ] The password input uses `type="password"`
- [ ] Empty or whitespace-only values cannot be used for a successful login
- [ ] Submitting `admin/admin` results in navigation to `/empty`
- [ ] Invalid credentials keep the user on `/login` and show an error
- [ ] Unauthenticated access to `/empty` and `/design` is blocked
- [ ] Unit and e2e tests cover success, validation, and failure flows

### Open Questions
- None

---

## Implementation Plan

### Technical Approach
Repozytorium jest już ustawione pod ten feature: Angular 21, standalone components, lazy routes w `src/app/app.routes.ts`, provider `HttpClient` w `src/app/app.config.ts`, auth core w `src/app/core/auth/`, gotowy `LoginComponent`, `EmptyComponent`, guardy oraz testy jednostkowe i Playwright.

Plan powinien więc **domknąć i ustabilizować istniejącą implementację**, a nie budować równoległy mechanizm logowania.

Kodowe wzorce do ponownego użycia:
- lazy-loaded standalone pages z `src/app/app.routes.ts`
- auth state w `src/app/core/auth/auth.service.ts`
- route guards w `src/app/core/auth/auth.guard.ts` i `src/app/core/auth/guest.guard.ts`
- Reactive Forms i lokalny stan przez signals w `src/app/pages/login/login.component.ts`
- layout karty przez `src/app/shared/components/wizard-card/wizard-card.component.*`
- testy jednostkowe obok plików źródłowych oraz Playwright w `tests/e2e/`

Reuse z Confluence:
- Services space zawiera istniejącą dokumentację auth/session:
  - [F01 — Authentication & Session Management](https://apreel.atlassian.net/wiki/spaces/Services/pages/174555137/F01+Authentication+Session+Management)
  - [F01 — Authentication & Session — Test Scenarios](https://apreel.atlassian.net/wiki/spaces/Services/pages/174653441/F01+Authentication+Session+Test+Scenarios)
- Plan reuse’uje z niej sam wzorzec logowania/sesji: dedykowany auth flow, obsługa sukcesu i błędnych danych, oraz utrzymanie stanu sesji po stronie aplikacji.
- Nie znaleziono istotnych wskazówek domenowych w przestrzeni `SDLC`.
- W pierwszej wersji należy dodać lekki mock backendowych endpointów auth/session, podpięty na poziomie `HttpClient`, tak aby aplikacja frontendowa mogła działać bez dostępnego backendu. Mock ma odwzorowywać minimalny kontrakt potrzebny do logowania i odtworzenia sesji; testy nadal mogą używać dedykowanego mockowania (`HttpTestingController`, Playwright route mocking) tam, gdzie potrzebna jest pełna kontrola scenariusza.

Najważniejsze decyzje:
1. Zachować `AuthService` jako pojedynczy punkt wejścia dla logowania i persystencji stanu.
2. Zachować obecny podział odpowiedzialności: komponent odpowiada za formularz i UX, serwis za auth state, guardy za nawigację.
3. Dostosować i zweryfikować istniejące pliki zamiast tworzyć nowe struktury.
4. Potraktować `admin/admin` jako obowiązkowy scenariusz testowany i wspierany przez flow logowania.
5. Utrzymać minimalny ekran `/empty` jako jednoznaczny target po poprawnym logowaniu.
6. Dodać tymczasową warstwę mock backendu dla endpointów auth/session bez zmiany publicznego kontraktu `AuthService`.

### Task Breakdown

#### Phase 1: Ustabilizowanie warstwy autoryzacji
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Zweryfikować kontrakt logowania w serwisie | `src/app/core/auth/auth.service.ts` | Utrzymać jeden auth entry point, upewnić się że serwis wysyła przycięte wartości `username/password`, normalizuje odpowiedź użytkownika i zapisuje stan tylko po sukcesie. |
| 1.2 | Doprecyzować obsługę persystencji sesji | `src/app/core/auth/auth.service.ts` | Potwierdzić poprawne odtwarzanie `auth.currentUser` po refreshu oraz czyszczenie stanu przy błędnych lub niepoprawnych danych lokalnych. |
| 1.3 | Utrzymać spójne zachowanie guardów | `src/app/core/auth/auth.guard.ts`, `src/app/core/auth/guest.guard.ts`, `src/app/app.routes.ts` | Potwierdzić, że gość trafia na `/login`, authenticated user omija `/login`, a `/empty` i `/design` są chronione. |
| 1.4 | Rozszerzyć testy auth core | `src/app/core/auth/auth.service.spec.ts`, `src/app/core/auth/auth.guard.spec.ts`, `src/app/core/auth/guest.guard.spec.ts` | Dodać lub doprecyzować przypadki dla 401, restore z localStorage, redirectów i braku persystencji po błędzie. |
| 1.5 | Dodać mocki backendowych endpointów auth/session | `src/app/core/auth/mock-auth.interceptor.ts`, `src/app/core/auth/mock-auth.interceptor.spec.ts`, `src/app/app.config.ts` | Dodać lekki mock HTTP przechwytujący endpointy używane przez logowanie i odtwarzanie sesji, tak aby frontend działał bez backendu; odwzorować co najmniej sukces dla `admin/admin`, odpowiedź 401 dla błędnych danych i kontrolowany błąd techniczny dla scenariusza generic error. |

#### Phase 2: Domknięcie UX ekranu logowania
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Dopracować logikę formularza | `src/app/pages/login/login.component.ts` | Zachować Reactive Forms, trimmed validator, blokadę wielokrotnego submitu i kasowanie błędu auth po zmianie formularza. |
| 2.2 | Zweryfikować zgodność widoku z Jira | `src/app/pages/login/login.component.html` | Potwierdzić obecność pola login, pola hasła i przycisku; utrzymać maskowanie hasła, komunikaty walidacyjne i komunikat o błędnych danych. |
| 2.3 | Dopracować stany wizualne | `src/app/pages/login/login.component.scss` | Ujednolicić styling walidacji, disabled state i błędu formularza zgodnie z istniejącym layoutem aplikacji. |
| 2.4 | Potwierdzić stronę docelową po sukcesie | `src/app/pages/empty/empty.component.ts`, `src/app/pages/empty/empty.component.html`, `src/app/pages/empty/empty.component.scss` | Zachować minimalną stronę potwierdzającą sukces logowania i pozwalającą jednoznacznie testować redirect na `/empty`. |

#### Phase 3: Testy regresyjne i e2e
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Uzupełnić testy komponentu login | `src/app/pages/login/login.component.spec.ts` | Sprawdzić render formularza, maskowanie hasła, trimowanie `admin/admin`, 401 error, generic error i blokadę duplikatów submitu. |
| 3.2 | Dopracować scenariusze Playwright | `tests/e2e/login.spec.ts` | Pokryć redirect z `/`, ochronę tras, skuteczne logowanie `admin/admin`, walidację pustych pól i błąd niepoprawnych danych przy domyślnie aktywnej mockowej warstwie auth dla uruchomienia bez backendu. |
| 3.3 | Zaktualizować smoke test pod auth-first app | `tests/e2e/smoke.spec.ts` | Utrzymać prosty test startowy z zasianym auth state, tak aby potwierdzał poprawne ładowanie aplikacji po zalogowaniu. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Rejestracja mockowej warstwy backendowej dla endpointów auth/session w pierwszej wersji |
| `src/app/core/auth/auth.service.ts` | MODIFY | Ustabilizowanie kontraktu logowania, persystencji i normalizacji użytkownika |
| `src/app/core/auth/auth.service.spec.ts` | MODIFY | Testy serwisu auth dla sukcesu, 401, restore i logout/cleanup |
| `src/app/core/auth/auth.guard.ts` | MODIFY | Potwierdzenie ochrony tras dla gości |
| `src/app/core/auth/auth.guard.spec.ts` | MODIFY | Testy redirectu na `/login` i przepuszczania authenticated user |
| `src/app/core/auth/guest.guard.ts` | MODIFY | Potwierdzenie redirectu authenticated user z `/login` |
| `src/app/core/auth/guest.guard.spec.ts` | MODIFY | Testy guest-only behavior |
| `src/app/core/auth/mock-auth.interceptor.ts` | ADD | Mock odpowiedzi endpointów auth/session na potrzeby działania bez backendu |
| `src/app/core/auth/mock-auth.interceptor.spec.ts` | ADD | Testy kontraktu mocka dla sukcesu, 401 i błędu technicznego |
| `src/app/app.routes.ts` | MODIFY | Utrzymanie login-first routing i ochrony `/empty` oraz `/design` |
| `src/app/pages/login/login.component.ts` | MODIFY | Finalizacja logiki formularza logowania |
| `src/app/pages/login/login.component.html` | MODIFY | Finalizacja struktury formularza i komunikatów |
| `src/app/pages/login/login.component.scss` | MODIFY | Dopracowanie stylów stanów formularza |
| `src/app/pages/login/login.component.spec.ts` | MODIFY | Testy jednostkowe ekranu logowania |
| `src/app/pages/empty/empty.component.ts` | MODIFY | Potwierdzenie minimalnego celu po logowaniu |
| `src/app/pages/empty/empty.component.html` | MODIFY | Jasny komunikat sukcesu po auth |
| `src/app/pages/empty/empty.component.scss` | MODIFY | Minimalny layout strony docelowej |
| `tests/e2e/login.spec.ts` | MODIFY | Regresja e2e dla success/failure/validation/protected routes |
| `tests/e2e/smoke.spec.ts` | MODIFY | Smoke test dla uruchomienia aplikacji z auth state |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Aplikacja uruchamia się i obsługuje logowanie bez backendu dzięki mockom auth/session

## Revision History
- Revision 1
  - Reviewer: razorapid
  - Summary of changes made: dodano plan wdrożenia mocków backendowych endpointów auth/session, aktualizację konfiguracji `HttpClient`, testy kontraktu mocka oraz weryfikację działania aplikacji bez backendu