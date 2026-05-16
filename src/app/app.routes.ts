import { Routes } from '@angular/router';
import { ShellComponent } from './layout/shell/shell';

export const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      { path: '', redirectTo: 'kokpit', pathMatch: 'full' },
      {
        path: 'kokpit',
        loadComponent: () => import('./features/dashboard/dashboard').then(m => m.DashboardComponent),
      },
      {
        path: 'zdjecia-do-polisy',
        loadComponent: () => import('./features/policy-photos/policy-photos').then(m => m.PolicyPhotosComponent),
      },
      {
        path: 'polisy-do-wznowienia',
        loadComponent: () => import('./features/policy-renewals/policy-renewals').then(m => m.PolicyRenewalsComponent),
      },
      {
        path: 'polisy/:id',
        loadComponent: () => import('./features/polisy/polisa-detail/polisa-detail').then(m => m.PolisaDetailComponent),
      },
      {
        path: 'polisy',
        loadComponent: () => import('./features/polisy/polisy').then(m => m.PolisyComponent),
      },
      {
        path: 'oferty',
        loadComponent: () => import('./features/oferty/oferty').then(m => m.OfertyComponent),
      },
      {
        path: 'rozliczenia/noty-prowizyjne/:id/metryczka',
        loadComponent: () => import('./features/noty-prowizyjne/nota-metryczka/nota-metryczka').then(m => m.NotaMetryczkaComponent),
      },
      {
        path: 'rozliczenia/noty-prowizyjne/:id',
        loadComponent: () => import('./features/noty-prowizyjne/nota-detail/nota-detail').then(m => m.NotaDetailComponent),
      },
      {
        path: 'rozliczenia/noty-prowizyjne',
        loadComponent: () => import('./features/noty-prowizyjne/noty-prowizyjne').then(m => m.NotyProwizyjneComponent),
      },
      {
        path: 'rozliczenia/wykazy/nowy',
        loadComponent: () => import('./features/wykazy/new-wykaz/new-wykaz').then(m => m.NewWykazComponent),
      },
      {
        path: 'rozliczenia/wykazy/:id',
        loadComponent: () => import('./features/wykazy/wykaz-detail/wykaz-detail').then(m => m.WykazDetailComponent),
      },
      {
        path: 'rozliczenia/wykazy',
        loadComponent: () => import('./features/wykazy/wykazy').then(m => m.WykazyComponent),
      },
      {
        path: 'rozliczenia/kwitariusze/nowy',
        loadComponent: () => import('./features/kwitariusze/new-kwitariusz/new-kwitariusz').then(m => m.NewKwitariuszComponent),
      },
      {
        path: 'rozliczenia/kwitariusze',
        loadComponent: () => import('./features/kwitariusze/kwitariusze').then(m => m.KwitariuszeComponent),
      },
      {
        path: 'rozliczenia/kwitariusze/:id',
        loadComponent: () => import('./features/kwitariusze/kwitariusz-detail/kwitariusz-detail').then(m => m.KwitariuszDetailComponent),
      },
      {
        path: 'rozliczenia/kwitariusze/:id/edytuj',
        loadComponent: () => import('./features/kwitariusze/kwitariusz-edit/kwitariusz-edit').then(m => m.KwitariuszEditComponent),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
