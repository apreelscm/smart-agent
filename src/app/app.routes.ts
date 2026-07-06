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
  // Klikalne makiety portalu pacjenta Świat Zdrowia (dostępne bez logowania — do przeglądu).
  { path: 'portal', redirectTo: 'portal/pulpit', pathMatch: 'full' },
  {
    path: 'portal/pulpit',
    loadComponent: () =>
      import('./pages/portal/pulpit/pulpit.component').then((m) => m.PulpitComponent),
  },
  {
    path: 'portal/opieka',
    loadComponent: () =>
      import('./pages/portal/opieka/opieka.component').then((m) => m.OpiekaComponent),
  },
  {
    path: 'portal/oferta',
    loadComponent: () =>
      import('./pages/portal/oferta/oferta.component').then((m) => m.OfertaComponent),
  },
  {
    path: 'portal/umow-wizyte',
    loadComponent: () =>
      import('./pages/portal/umow-wizyte/umow-wizyte.component').then(
        (m) => m.UmowWizyteComponent,
      ),
  },
  {
    path: 'portal/wizyta/:id',
    loadComponent: () =>
      import('./pages/portal/wizyta/wizyta.component').then((m) => m.WizytaComponent),
  },
];
