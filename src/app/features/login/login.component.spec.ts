import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthService, User } from '../../core/services/auth.service';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const authenticatedUser: User = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
  };

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>('AuthService', ['login']);
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);
    routerSpy.navigate.and.returnValue(Promise.resolve(true));

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function setCredentials(username: string, password: string): void {
    const usernameInput: HTMLInputElement = fixture.nativeElement.querySelector('#username');
    const passwordInput: HTMLInputElement = fixture.nativeElement.querySelector('#password');

    usernameInput.value = username;
    usernameInput.dispatchEvent(new Event('input'));

    passwordInput.value = password;
    passwordInput.dispatchEvent(new Event('input'));

    fixture.detectChanges();
  }

  async function submitForm(): Promise<void> {
    const form: HTMLFormElement = fixture.nativeElement.querySelector('form');

    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();

    await fixture.whenStable();
    fixture.detectChanges();
  }

  it('renders a masked password field', () => {
    const passwordInput: HTMLInputElement = fixture.nativeElement.querySelector('#password');

    expect(passwordInput.type).toBe('password');
  });

  it('authenticates and navigates to the default route on successful submit', async () => {
    authServiceSpy.login.and.returnValue(Promise.resolve(authenticatedUser));
    setCredentials('admin', 'admin');

    await submitForm();

    expect(authServiceSpy.login).toHaveBeenCalledWith('admin', 'admin');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
    expect(component.errorMessage).toBe('');
  });

  it('shows an error and stays on the login page after failed submit', async () => {
    authServiceSpy.login.and.returnValue(Promise.reject(new Error('Błędny login lub hasło.')));
    setCredentials('invalid', 'credentials');

    await submitForm();

    expect(authServiceSpy.login).toHaveBeenCalledWith('invalid', 'credentials');
    expect(routerSpy.navigate).not.toHaveBeenCalled();
    expect(component.errorMessage).toBe('Błędny login lub hasło.');

    const errorAlert: HTMLElement | null = fixture.nativeElement.querySelector('.alert-danger');
    expect(errorAlert?.textContent).toContain('Błędny login lub hasło.');
  });

  it('clears the previous error and allows retrying login', async () => {
    authServiceSpy.login.and.returnValues(
      Promise.reject(new Error('Błędny login lub hasło.')),
      Promise.resolve(authenticatedUser),
    );

    setCredentials('wrong', 'wrong');
    await submitForm();

    expect(component.errorMessage).toBe('Błędny login lub hasło.');

    setCredentials('admin', 'admin');
    await submitForm();

    expect(authServiceSpy.login.calls.allArgs()).toEqual([
      ['wrong', 'wrong'],
      ['admin', 'admin'],
    ]);
    expect(component.errorMessage).toBe('');
    expect(fixture.nativeElement.querySelector('.alert-danger')).toBeNull();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });
});
