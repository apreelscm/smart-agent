import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-05-year',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-05-year.component.html',
})
export class Step05YearComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  selected = this.draft.draft().vehicle.year || 2022;

  years = Array.from({ length: 15 }, (_, i) => 2024 - i);

  next() {
    this.draft.patchVehicle({ year: this.selected });
    this.router.navigate(['/kalkulator/marka']);
  }
}
