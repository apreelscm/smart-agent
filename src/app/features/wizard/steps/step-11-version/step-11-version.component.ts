import { Component, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleDataService, VehicleVersion } from '../../../../core/services/vehicle-data.service';

@Component({
  selector: 'app-step-11-version',
  standalone: true,
  imports: [WizardCardComponent, DecimalPipe],
  templateUrl: './step-11-version.component.html',
})
export class Step11VersionComponent {
  private draft = inject(PolicyDraftService);
  private vehicleData = inject(VehicleDataService);
  private router = inject(Router);

  private v = this.draft.draft().vehicle;
  selectedName = this.v.version;
  selectedValue = this.v.versionValue;

  versions = toSignal(
    this.vehicleData.getVersions(this.v.make, this.v.model, this.v.engineCC, this.v.engineKM),
    { initialValue: [] as VehicleVersion[] }
  );

  constructor() {
    effect(() => {
      const list = this.versions();
      if (list.length === 1 && !this.selectedName) this.select(list[0]);
    });
  }

  select(ver: VehicleVersion) {
    this.selectedName = ver.name;
    this.selectedValue = ver.eurotaxValue;
  }

  next() {
    this.draft.patchVehicle({ version: this.selectedName, versionValue: this.selectedValue, totalValue: this.selectedValue });
    this.router.navigate(['/kalkulator/wyposazenie']);
  }
}
