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
      map((response) => this.mapApiUserToCurrentUser(response.user)),
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

  private normalizeStoredUser(
    candidate: Partial<AuthCurrentUser> & { role?: string | null },
  ): AuthCurrentUser | null {
    if (typeof candidate.username !== 'string' || candidate.username.trim().length === 0) {
      return null;
    }

    const roles = Array.isArray(candidate.roles)
      ? candidate.roles.filter((role): role is string => typeof role === 'string' && role.trim().length > 0)
      : typeof candidate.role === 'string' && candidate.role.trim().length > 0
        ? [candidate.role]
        : [];

    return {
      username: candidate.username,
      name:
        typeof candidate.name === 'string' && candidate.name.trim().length > 0
          ? candidate.name
          : candidate.username,
      role:
        typeof candidate.role === 'string' && candidate.role.trim().length > 0
          ? candidate.role
          : roles[0] ?? null,
      email: typeof candidate.email === 'string' ? candidate.email : null,
      phone: typeof candidate.phone === 'string' ? candidate.phone : '',
      auwId: typeof candidate.auwId === 'number' ? candidate.auwId : null,
      sellerNumber:
        typeof candidate.sellerNumber === 'string' || candidate.sellerNumber === null
          ? candidate.sellerNumber ?? null
          : null,
      roles,
    };
  }

  private mapApiUserToCurrentUser(user: AuthApiUser): AuthCurrentUser {
    const firstName = typeof user.firstName === 'string' ? user.firstName.trim() : '';
    const lastName = typeof user.lastName === 'string' ? user.lastName.trim() : '';
    const fullName = [firstName, lastName].filter(Boolean).join(' ').trim();
    const roles = Array.isArray(user.roles) ? user.roles : [];

    return {
      username: user.username,
      name: fullName || user.username,
      role: roles[0] ?? null,
      email: user.email ?? null,
      phone: '',
      auwId: user.auwId ?? null,
      sellerNumber: user.sellerNumber ?? null,
      roles,
    };
  }
}
