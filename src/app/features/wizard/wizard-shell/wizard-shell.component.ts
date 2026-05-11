import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

interface WizardStep {
  path: string;
  label: string;
}

@Component({
  selector: 'app-wizard-shell',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="row">
      <div class="col-12 mb-3">
        <nav class="nav nav-pills">
          @for (step of steps; track step.path) {
            <a class="nav-link" [routerLink]="step.path" routerLinkActive="active">
              {{ step.label }}
            </a>
          }
        </nav>
      </div>
      <div class="col-12">
        <div class="card">
          <div class="card-body">
            <router-outlet />
          </div>
        </div>
      </div>
    </div>
  `,
})
export class WizardShellComponent {
  steps: WizardStep[] = [
    { path: 'activity-selection', label: '1. Czynności' },
    { path: 'calculator', label: '2. Kalkulator' },
    { path: 'panels', label: '3. Dane' },
    { path: 'documents', label: '4. Dokumenty' },
    { path: 'signing', label: '5. Podpisy' },
    { path: 'submit', label: '6. Wysłanie' },
  ];
}
