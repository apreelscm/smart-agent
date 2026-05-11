import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    loadComponent: () => import('./core/layout/layout.component').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'processes',
        loadComponent: () =>
          import('./features/processes/process-list/process-list.component').then(
            (m) => m.ProcessListComponent,
          ),
      },
      {
        path: 'processes/:id',
        loadComponent: () =>
          import('./features/processes/process-detail/process-detail.component').then(
            (m) => m.ProcessDetailComponent,
          ),
      },
      {
        path: 'wizard',
        loadComponent: () =>
          import('./features/wizard/wizard-shell/wizard-shell.component').then(
            (m) => m.WizardShellComponent,
          ),
        children: [
          {
            path: 'activity-selection',
            loadComponent: () =>
              import('./features/wizard/steps/activity-selection/activity-selection.component').then(
                (m) => m.ActivitySelectionComponent,
              ),
          },
          {
            path: 'calculator',
            loadComponent: () =>
              import('./features/wizard/steps/calculator/calculator.component').then(
                (m) => m.CalculatorComponent,
              ),
          },
          {
            path: 'panels',
            loadComponent: () =>
              import('./features/wizard/steps/panels/panels.component').then(
                (m) => m.PanelsComponent,
              ),
          },
          {
            path: 'documents',
            loadComponent: () =>
              import('./features/wizard/steps/documents/documents.component').then(
                (m) => m.DocumentsComponent,
              ),
          },
          {
            path: 'signing',
            loadComponent: () =>
              import('./features/wizard/steps/signing/signing.component').then(
                (m) => m.SigningComponent,
              ),
          },
          {
            path: 'submit',
            loadComponent: () =>
              import('./features/wizard/steps/submit/submit.component').then(
                (m) => m.SubmitComponent,
              ),
          },
          { path: '', redirectTo: 'activity-selection', pathMatch: 'full' },
        ],
      },
      { path: '', redirectTo: 'processes', pathMatch: 'full' },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
