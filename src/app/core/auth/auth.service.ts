import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';

export interface AuthLoginRequest {
  username: string;
  password: string;
}

export interface AuthApiUser {
  username: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  sellerNumber?: string | null;
  auwId?: number | null;
  roles?: string[];
}

export interface AuthLoginResponse {
  token: string;
  refreshToken?: string;
  user: AuthApiUser;
}

export interface AuthCurrentUser {
  username: string;
  name: string;
  role: string | null;
  email: string | null;
  phone: string;
  auwId: number | null;
  sellerNumber: string | null;
  roles: string[];
}

const AUTH_STORAGE_KEY = 'auth.currentUser';
const AUTH_LOGIN_ENDPOINT = '/api/auth/login';
const AUTH_LOGOUT_ENDPOINT = '/api/auth/logout';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly currentUserSignal = signal<AuthCurrentUser | null>(this.readStoredUser());

  readonly currentUser = this.currentUserSignal.asReadonly();

  login(credentials: AuthLoginRequest): Observable<AuthCurrentUser> {
    return this.http.post<AuthLoginResponse>(AUTH_LOGIN_ENDPOINT, credentials).pipe(
      map((response) => {
        const user = this.normalizeLoginResponse(response);

        if (!user) {
          throw new Error('Invalid auth response');
        }

        return user;
      }),
      tap((user) => this.storeUser(user)),
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(AUTH_LOGOUT_ENDPOINT, null).pipe(
      tap(() => this.clearAuthState()),
      catchError((error) => {
        this.clearAuthState();
        return throwError(() => error);
      }),
    );
  }

  isAuthenticated(): boolean {
    return this.currentUserSignal() !== null;
  }

  getCurrentUser(): AuthCurrentUser | null {
    return this.currentUserSignal();
  }

  private storeUser(user: AuthCurrentUser): void {
    this.currentUserSignal.set(user);

    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(user));
    }
  }

  private clearAuthState(): void {
    this.currentUserSignal.set(null);

    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(AUTH_STORAGE_KEY);
    }
  }

  private readStoredUser(): AuthCurrentUser | null {
    if (typeof localStorage === 'undefined') {
      return null;
    }

    const storedValue = localStorage.getItem(AUTH_STORAGE_KEY);

    if (!storedValue) {
      return null;
    }

    try {
      const parsedValue = JSON.parse(storedValue) as Partial<AuthCurrentUser> & {
        role?: string | null;
      };

      const normalizedUser = this.normalizeStoredUser(parsedValue);

      if (!normalizedUser) {
        localStorage.removeItem(AUTH_STORAGE_KEY);
      }

      return normalizedUser;
    } catch {
      localStorage.removeItem(AUTH_STORAGE_KEY);
      return null;
    }
  }

  private normalizeLoginResponse(response: AuthLoginResponse | null | undefined): AuthCurrentUser | null {
    if (!response || typeof response.token !== 'string' || response.token.trim().length === 0) {
      return null;
    }

    return this.mapApiUserToCurrentUser(response.user);
  }

  private normalizeStoredUser(
    candidate: Partial<AuthCurrentUser> & { role?: string | null },
  ): AuthCurrentUser | null {
    const username = this.normalizeRequiredString(candidate.username);

    if (!username) {
      return null;
    }

    const roles = this.normalizeRoles(candidate.roles);
    const legacyRole = this.normalizeOptionalString(candidate.role);
    const normalizedRoles = roles.length > 0 ? roles : legacyRole ? [legacyRole] : [];

    return {
      username,
      name: this.normalizeOptionalString(candidate.name) ?? username,
      role: legacyRole ?? normalizedRoles[0] ?? null,
      email: this.normalizeOptionalString(candidate.email),
      phone: this.normalizeOptionalString(candidate.phone) ?? '',
      auwId: typeof candidate.auwId === 'number' && Number.isFinite(candidate.auwId) ? candidate.auwId : null,
      sellerNumber: this.normalizeOptionalString(candidate.sellerNumber),
      roles: normalizedRoles,
    };
  }

  private mapApiUserToCurrentUser(user: AuthApiUser | null | undefined): AuthCurrentUser | null {
    const username = this.normalizeRequiredString(user?.username);

    if (!username) {
      return null;
    }

    const firstName = this.normalizeOptionalString(user?.firstName) ?? '';
    const lastName = this.normalizeOptionalString(user?.lastName) ?? '';
    const fullName = [firstName, lastName].filter(Boolean).join(' ').trim();
    const roles = this.normalizeRoles(user?.roles);

    return {
      username,
      name: fullName || username,
      role: roles[0] ?? null,
      email: this.normalizeOptionalString(user?.email),
      phone: '',
      auwId: typeof user?.auwId === 'number' && Number.isFinite(user.auwId) ? user.auwId : null,
      sellerNumber: this.normalizeOptionalString(user?.sellerNumber),
      roles,
    };
  }

  private normalizeRequiredString(value: unknown): string | null {
    if (typeof value !== 'string') {
      return null;
    }

    const normalizedValue = value.trim();
    return normalizedValue.length > 0 ? normalizedValue : null;
  }

  private normalizeOptionalString(value: unknown): string | null {
    if (typeof value !== 'string') {
      return null;
    }

    const normalizedValue = value.trim();
    return normalizedValue.length > 0 ? normalizedValue : null;
  }

  private normalizeRoles(roles: unknown): string[] {
    if (!Array.isArray(roles)) {
      return [];
    }

    return roles
      .filter((role): role is string => typeof role === 'string')
      .map((role) => role.trim())
      .filter((role) => role.length > 0);
  }
}
