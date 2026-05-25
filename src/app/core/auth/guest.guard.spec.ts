import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  provideRouter,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { AuthService } from './auth.service';
import { guestGuard } from './guest.guard';

describe('guestGuard', () => {
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
      guestGuard({} as ActivatedRouteSnapshot, { url } as RouterStateSnapshot),
    );
  }

  it('allows navigation for guests', () => {
    authService.isAuthenticated.and.returnValue(false);

    const result = runGuard('/login');

    expect(result).toBeTrue();
  });

  it('redirects authenticated users opening /login to /empty', () => {
    authService.isAuthenticated.and.returnValue(true);

    const result = runGuard('/login');

    expect(router.serializeUrl(result as UrlTree)).toBe('/empty');
  });
});
