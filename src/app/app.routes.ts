import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { guestGuard } from './core/auth/guest.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'empty',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/empty/empty.component').then((m) => m.EmptyComponent),
  },
  {
    path: 'design',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/design/design.component').then((m) => m.DesignComponent),
  },
];
