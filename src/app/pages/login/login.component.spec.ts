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

  it('renders the login form with username and masked password fields', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;
    const passwordInput = nativeElement.querySelector(
      '[data-testid="login-password-input"]',
    ) as HTMLInputElement;

    expect(nativeElement.querySelector('[data-testid="login-form"]')).not.toBeNull();
    expect(nativeElement.querySelector('[data-testid="login-username-input"]')).not.toBeNull();
    expect(passwordInput).not.toBeNull();
    expect(passwordInput.type).toBe('password');
    expect(nativeElement.querySelector('[data-testid="login-submit-button"]')).not.toBeNull();
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

  it('shows an invalid credentials error when the backend returns 401', () => {
    authService.login.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 401, statusText: 'Unauthorized' })),
    );

    component.loginForm.setValue({
      username: 'admin',
      password: 'wrong',
    });

    component.onSubmit();
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).textContent).toContain(
      'Nieprawidłowy login lub hasło.',
    );
  });

  it('shows a generic sign-in error for non-401 failures', () => {
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
  });

  it('blocks duplicate submissions while a login request is in flight', () => {
    const pendingResponse = new Subject<AuthCurrentUser>();
    authService.login.and.returnValue(pendingResponse.asObservable());

    component.loginForm.setValue({
      username: 'admin',
      password: 'admin',
    });

    component.onSubmit();
    component.onSubmit();

    expect(authService.login).toHaveBeenCalledTimes(1);

    pendingResponse.next(currentUser);
    pendingResponse.complete();
  });
});
