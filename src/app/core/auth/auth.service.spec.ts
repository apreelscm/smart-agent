import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthCurrentUser, AuthService } from './auth.service';

describe('AuthService', () => {
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    localStorage.clear();
  });

  it('restores persisted auth state from localStorage', () => {
    const storedUser: AuthCurrentUser = {
      username: 'admin',
      name: 'Administrator',
      role: 'EUM_ADMINISTRATOR',
      email: 'admin@eumowy.local',
      phone: '',
      auwId: 1,
      sellerNumber: null,
      roles: ['EUM_ADMINISTRATOR'],
    };

    localStorage.setItem('auth.currentUser', JSON.stringify(storedUser));

    const service = TestBed.inject(AuthService);

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.getCurrentUser()).toEqual(storedUser);
  });

  it('restores legacy stored auth state seeded with a single role field', () => {
    localStorage.setItem(
      'auth.currentUser',
      JSON.stringify({
        username: ' admin ',
        name: ' Administrator ',
        role: ' EUM_ADMINISTRATOR ',
        email: ' admin@eumowy.local ',
        phone: ' ',
        auwId: 1,
      }),
    );

    const service = TestBed.inject(AuthService);

    expect(service.getCurrentUser()).toEqual({
      username: 'admin',
      name: 'Administrator',
      role: 'EUM_ADMINISTRATOR',
      email: 'admin@eumowy.local',
      phone: '',
      auwId: 1,
      sellerNumber: null,
      roles: ['EUM_ADMINISTRATOR'],
    });
  });

  it('clears malformed stored auth state safely', () => {
    localStorage.setItem('auth.currentUser', '{"username":');

    const service = TestBed.inject(AuthService);

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.getCurrentUser()).toBeNull();
    expect(localStorage.getItem('auth.currentUser')).toBeNull();
  });

  it('posts credentials to the login endpoint and stores the normalized user', () => {
    const service = TestBed.inject(AuthService);
    let receivedUser: AuthCurrentUser | undefined;

    service.login({ username: 'admin', password: 'admin' }).subscribe((user) => {
      receivedUser = user;
    });

    const request = httpTestingController.expectOne('/api/auth/login');

    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      username: 'admin',
      password: 'admin',
    });

    request.flush({
      token: 'jwt-token',
      user: {
        username: ' admin ',
        firstName: ' Admin ',
        lastName: ' Administrator ',
        email: ' admin@eumowy.local ',
        sellerNumber: ' ADM001 ',
        auwId: 1,
        roles: [' EUM_ADMINISTRATOR '],
      },
    });

    expect(receivedUser).toEqual({
      username: 'admin',
      name: 'Admin Administrator',
      role: 'EUM_ADMINISTRATOR',
      email: 'admin@eumowy.local',
      phone: '',
      auwId: 1,
      sellerNumber: 'ADM001',
      roles: ['EUM_ADMINISTRATOR'],
    });

    expect(service.isAuthenticated()).toBeTrue();
    expect(JSON.parse(localStorage.getItem('auth.currentUser') ?? '{}')).toEqual(receivedUser);
  });

  it('does not persist auth state when login fails', () => {
    const service = TestBed.inject(AuthService);
    let receivedStatus: number | undefined;

    service.login({ username: 'admin', password: 'wrong' }).subscribe({
      next: () => fail('Expected login to fail'),
      error: (error) => {
        receivedStatus = error.status;
      },
    });

    const request = httpTestingController.expectOne('/api/auth/login');
    request.flush(
      { error: 'AUTH_INVALID_CREDENTIALS' },
      {
        status: 401,
        statusText: 'Unauthorized',
      },
    );

    expect(receivedStatus).toBe(401);
    expect(service.isAuthenticated()).toBeFalse();
    expect(localStorage.getItem('auth.currentUser')).toBeNull();
  });

  it('does not persist auth state when the login response shape is invalid', () => {
    const service = TestBed.inject(AuthService);
    let receivedError: unknown;

    service.login({ username: 'admin', password: 'admin' }).subscribe({
      next: () => fail('Expected login to fail for malformed response'),
      error: (error) => {
        receivedError = error;
      },
    });

    const request = httpTestingController.expectOne('/api/auth/login');
    request.flush({
      token: '',
      user: {
        firstName: 'Admin',
      },
    });

    expect(receivedError).toEqual(jasmine.any(Error));
    expect(service.isAuthenticated()).toBeFalse();
    expect(localStorage.getItem('auth.currentUser')).toBeNull();
  });

  it('clears auth state on successful logout', () => {
    localStorage.setItem(
      'auth.currentUser',
      JSON.stringify({
        username: 'admin',
        name: 'Administrator',
        role: 'EUM_ADMINISTRATOR',
        email: 'admin@eumowy.local',
        phone: '',
        auwId: 1,
        sellerNumber: null,
        roles: ['EUM_ADMINISTRATOR'],
      }),
    );

    const service = TestBed.inject(AuthService);

    service.logout().subscribe();

    const request = httpTestingController.expectOne('/api/auth/logout');
    expect(request.request.method).toBe('POST');

    request.flush(null, {
      status: 204,
      statusText: 'No Content',
    });

    expect(service.isAuthenticated()).toBeFalse();
    expect(localStorage.getItem('auth.currentUser')).toBeNull();
  });

  it('clears auth state even when logout fails', () => {
    localStorage.setItem(
      'auth.currentUser',
      JSON.stringify({
        username: 'admin',
        name: 'Administrator',
        role: 'EUM_ADMINISTRATOR',
        email: 'admin@eumowy.local',
        phone: '',
        auwId: 1,
        sellerNumber: null,
        roles: ['EUM_ADMINISTRATOR'],
      }),
    );

    const service = TestBed.inject(AuthService);
    let receivedStatus: number | undefined;

    service.logout().subscribe({
      next: () => fail('Expected logout to fail'),
      error: (error) => {
        receivedStatus = error.status;
      },
    });

    const request = httpTestingController.expectOne('/api/auth/logout');
    request.flush(
      { error: 'AUTH_LOGOUT_FAILED' },
      {
        status: 503,
        statusText: 'Service Unavailable',
      },
    );

    expect(receivedStatus).toBe(503);
    expect(service.isAuthenticated()).toBeFalse();
    expect(localStorage.getItem('auth.currentUser')).toBeNull();
  });
});
