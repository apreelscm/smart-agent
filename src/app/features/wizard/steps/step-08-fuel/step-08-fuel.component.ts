import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { FuelType } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-08-fuel',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-08-fuel.component.html',
})
export class Step08FuelComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  selected: FuelType = this.draft.draft().vehicle.fuelType;
  options: FuelType[] = ['benzyna', 'diesel', 'elektryczny', 'gaz'];

  next() {
    this.draft.patchVehicle({ fuelType: this.selected, engineCC: 0, engineKM: 0, version: '', versionValue: 0, totalValue: 0 });
    this.router.navigate(['/kalkulator/pojemnosc']);
  }
}
