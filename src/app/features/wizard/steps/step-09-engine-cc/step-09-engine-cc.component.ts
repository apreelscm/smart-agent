import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleDataService } from '../../../../core/services/vehicle-data.service';

@Component({
  selector: 'app-step-09-engine-cc',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-09-engine-cc.component.html',
})
export class Step09EngineCcComponent {
  private draft = inject(PolicyDraftService);
  private vehicleData = inject(VehicleDataService);
  private router = inject(Router);

  private v = this.draft.draft().vehicle;
  selected = this.v.engineCC;

  options = toSignal(
    this.vehicleData.getEngineCCs(this.v.make, this.v.model, this.v.fuelType),
    { initialValue: [] as number[] }
  );

  next() {
    this.draft.patchVehicle({ engineCC: this.selected, engineKM: 0, version: '', versionValue: 0, totalValue: 0 });
    this.router.navigate(['/kalkulator/moc']);
  }
}
