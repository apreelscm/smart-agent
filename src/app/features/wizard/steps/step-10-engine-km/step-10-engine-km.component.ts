import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleDataService } from '../../../../core/services/vehicle-data.service';

@Component({
  selector: 'app-step-10-engine-km',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-10-engine-km.component.html',
})
export class Step10EngineKmComponent {
  private draft = inject(PolicyDraftService);
  private vehicleData = inject(VehicleDataService);
  private router = inject(Router);

  private v = this.draft.draft().vehicle;
  selected = this.v.engineKM;

  options = toSignal(
    this.vehicleData.getEngineKMs(this.v.make, this.v.model, this.v.fuelType, this.v.engineCC),
    { initialValue: [] as number[] }
  );

  next() {
    this.draft.patchVehicle({ engineKM: this.selected, version: '', versionValue: 0, totalValue: 0 });
    this.router.navigate(['/kalkulator/wersja']);
  }
}
