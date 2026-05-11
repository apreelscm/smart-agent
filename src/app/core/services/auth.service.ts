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
  private currentUser: User | null = null;

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

  login(username: string, password: string): Promise<User> {
    // TODO: call POST /api/auth/login
    throw new Error('Not implemented');
  }

  logout(): void {
    this.currentUser = null;
    // TODO: call POST /api/auth/logout
  }
}
