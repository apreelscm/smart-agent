import { Injectable } from '@angular/core';

export interface User {
  username: string;
  name: string;
  role: 'EUM_PH_BZOS' | 'EUM_ZRD' | 'EUM_ADMINISTRATOR';
  email: string;
  phone: string;
  auwId: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private static readonly STORAGE_KEY = 'auth.currentUser';
  private static readonly AUTO_LOGIN_DISABLED_KEY = 'auth.autoLoginDisabled';
  private static readonly DEFAULT_DEMO_USER: User = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
  };

  private currentUser: User | null = null;

  constructor() {
    this.currentUser = this.hydrateUserFromStorage() ?? this.hydrateDefaultDemoUser();
  }

  get user(): User | null {
    return this.currentUser;
  }

  get isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  get isZrd(): boolean {
    return this.currentUser?.role === 'EUM_ZRD';
  }

  get isPhBzos(): boolean {
    return this.currentUser?.role === 'EUM_PH_BZOS';
  }

  async login(username: string, password: string): Promise<User> {
    if (username !== 'admin' || password !== 'admin') {
      this.clearAuthState();
      throw new Error('Błędny login lub hasło.');
    }

    this.setCurrentUser(AuthService.DEFAULT_DEMO_USER);

    return this.currentUser as User;
  }

  logout(): void {
    this.clearAuthState({ disableAutoLogin: true });
  }

  private hydrateUserFromStorage(): User | null {
    const storedUser = this.storage?.getItem(AuthService.STORAGE_KEY);

    if (!storedUser) {
      return null;
    }

    try {
      const parsedUser: unknown = JSON.parse(storedUser);

      if (this.isUser(parsedUser)) {
        return parsedUser;
      }
    } catch {
      // Ignore malformed stored auth state and treat the user as logged out.
    }

    this.storage?.removeItem(AuthService.STORAGE_KEY);

    return null;
  }

  private hydrateDefaultDemoUser(): User | null {
    if (!this.storage || this.storage.getItem(AuthService.AUTO_LOGIN_DISABLED_KEY) === 'true') {
      return null;
    }

    return { ...AuthService.DEFAULT_DEMO_USER };
  }

  private setCurrentUser(user: User): void {
    this.currentUser = user;
    this.storage?.removeItem(AuthService.AUTO_LOGIN_DISABLED_KEY);
    this.storage?.setItem(AuthService.STORAGE_KEY, JSON.stringify(user));
  }

  private clearAuthState(options?: { disableAutoLogin?: boolean }): void {
    this.currentUser = null;
    this.storage?.removeItem(AuthService.STORAGE_KEY);

    if (options?.disableAutoLogin) {
      this.storage?.setItem(AuthService.AUTO_LOGIN_DISABLED_KEY, 'true');
    } else {
      this.storage?.removeItem(AuthService.AUTO_LOGIN_DISABLED_KEY);
    }
  }

  private isUser(value: unknown): value is User {
    if (!value || typeof value !== 'object') {
      return false;
    }

    const candidate = value as Partial<User>;

    return (
      typeof candidate.username === 'string' &&
      typeof candidate.name === 'string' &&
      (candidate.role === 'EUM_PH_BZOS' ||
        candidate.role === 'EUM_ZRD' ||
        candidate.role === 'EUM_ADMINISTRATOR') &&
      typeof candidate.email === 'string' &&
      typeof candidate.phone === 'string' &&
      typeof candidate.auwId === 'number'
    );
  }

  private get storage(): Storage | null {
    return typeof localStorage === 'undefined' ? null : localStorage;
  }
}
