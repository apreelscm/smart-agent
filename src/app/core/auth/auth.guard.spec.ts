import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  provideRouter,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { AuthService } from './auth.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let router: Router;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['isAuthenticated']);

    TestBed.configureTestingModule({
      providers: [provideRouter([]), { provide: AuthService, useValue: authService }],
    });

    router = TestBed.inject(Router);
  });

  function runGuard(url: string): boolean | UrlTree {
    return TestBed.runInInjectionContext(() =>
      authGuard({} as ActivatedRouteSnapshot, { url } as RouterStateSnapshot),
    );
  }

  it('allows navigation for authenticated users', () => {
    authService.isAuthenticated.and.returnValue(true);

    const result = runGuard('/empty');

    expect(result).toBeTrue();
  });

  it('redirects unauthenticated users from /empty to /login', () => {
    authService.isAuthenticated.and.returnValue(false);

    const result = runGuard('/empty');

    expect(router.serializeUrl(result as UrlTree)).toBe('/login');
  });

  it('redirects unauthenticated users from other guarded routes to /login', () => {
    authService.isAuthenticated.and.returnValue(false);

    const result = runGuard('/design');

    expect(router.serializeUrl(result as UrlTree)).toBe('/login');
  });
});
