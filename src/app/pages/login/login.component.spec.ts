import { HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of, Subject, throwError } from 'rxjs';
import { AuthCurrentUser, AuthService } from '../../core/auth/auth.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let router: Router;
  let authService: jasmine.SpyObj<AuthService>;

  const currentUser: AuthCurrentUser = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
    sellerNumber: null,
    roles: ['EUM_ADMINISTRATOR'],
  };

  beforeEach(async () => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['login', 'isAuthenticated']);
    authService.isAuthenticated.and.returnValue(false);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [provideRouter([]), { provide: AuthService, useValue: authService }],
    }).compileComponents();

    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('renders only the login form controls without wizard or intro content', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;
    const loginForm = nativeElement.querySelector('[data-testid="login-form"]') as HTMLFormElement;
    const usernameInput = nativeElement.querySelector(
      '[data-testid="login-username-input"]',
    ) as HTMLInputElement;
    const passwordInput = nativeElement.querySelector(
      '[data-testid="login-password-input"]',
    ) as HTMLInputElement;
    const submitButton = nativeElement.querySelector(
      '[data-testid="login-submit-button"]',
    ) as HTMLButtonElement;

    expect(nativeElement.querySelector('app-wizard-card')).toBeNull();
    expect(nativeElement.querySelector('.login-intro')).toBeNull();
    expect(nativeElement.querySelector('h1')).toBeNull();
    expect(nativeElement.textContent).not.toContain('Zaloguj się do systemu');
    expect(nativeElement.textContent).not.toContain('Dane dostępowe');
    expect(nativeElement.textContent).not.toContain(
      'Po zalogowaniu od razu przejdziesz do ekranu startowego.',
    );
    expect(loginForm).not.toBeNull();
    expect(loginForm.getAttribute('aria-label')).toBe('Formularz logowania');
    expect(usernameInput).not.toBeNull();
    expect(passwordInput).not.toBeNull();
    expect(submitButton).not.toBeNull();
    expect(nativeElement.querySelector('[data-testid="login-error-message"]')).toBeNull();
  });

  it('renders a masked password field', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;
    const passwordInput = nativeElement.querySelector(
      '[data-testid="login-password-input"]',
    ) as HTMLInputElement;

    expect(passwordInput).not.toBeNull();
    expect(passwordInput.type).toBe('password');
  });

  it('shows validation feedback and does not submit whitespace-only values', () => {
    component.loginForm.setValue({
      username: '   ',
      password: '   ',
    });

    component.onSubmit();
    fixture.detectChanges();

    expect(authService.login).not.toHaveBeenCalled();
    expect(component.loginForm.controls.username.touched).toBeTrue();
    expect(component.loginForm.controls.password.touched).toBeTrue();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Podaj login.');
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Podaj hasło.');
  });

  it('submits trimmed credentials and redirects to /empty after a successful login', async () => {
    authService.login.and.returnValue(of(currentUser));
    spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));

    component.loginForm.setValue({
      username: ' admin ',
      password: ' admin ',
    });

    component.onSubmit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(authService.login).toHaveBeenCalledWith({
      username: 'admin',
      password: 'admin',
    });
    expect(router.navigateByUrl).toHaveBeenCalledWith('/empty');
  });

  it('shows the invalid credentials error when the backend returns 401', () => {
    spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    authService.login.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' })),
    );

    component.loginForm.setValue({
      username: 'admin',
      password: 'wrong',
    });

    component.onSubmit();
    fixture.detectChanges();

    const errorMessage = (fixture.nativeElement as HTMLElement).querySelector(
      '[data-testid="login-error-message"]',
    );

    expect(errorMessage?.textContent).toContain('Nieprawidłowy login lub hasło.');
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });

  it('shows a generic technical error for non-401 failures', () => {
    spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    authService.login.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 503, statusText: 'Service Unavailable' })),
    );

    component.loginForm.setValue({
      username: 'admin',
      password: 'admin',
    });

    component.onSubmit();
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).textContent).toContain(
      'Nie udało się zalogować. Spróbuj ponownie później.',
    );
    expect(router.navigateByUrl).not.toHaveBeenCalled();
  });

  it('blocks duplicate submissions while a login request is in flight', () => {
    const pendingResponse = new Subject<AuthCurrentUser>();
    const nativeElement = fixture.nativeElement as HTMLElement;
    const submitButton = nativeElement.querySelector(
      '[data-testid="login-submit-button"]',
    ) as HTMLButtonElement;

    spyOn(router, 'navigateByUrl').and.returnValue(Promise.resolve(true));
    authService.login.and.returnValue(pendingResponse.asObservable());

    component.loginForm.setValue({
      username: 'admin',
      password: 'admin',
    });

    component.onSubmit();
    fixture.detectChanges();
    component.onSubmit();

    expect(authService.login).toHaveBeenCalledTimes(1);
    expect(component.isSubmitting()).toBeTrue();
    expect(submitButton.disabled).toBeTrue();
    expect(submitButton.textContent).toContain('Trwa logowanie...');

    pendingResponse.next(currentUser);
    pendingResponse.complete();
    fixture.detectChanges();

    expect(component.isSubmitting()).toBeFalse();
  });
});
