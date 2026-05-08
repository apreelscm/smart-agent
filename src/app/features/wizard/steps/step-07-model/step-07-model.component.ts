import { Component, inject, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleDataService, VehicleModel } from '../../../../core/services/vehicle-data.service';

@Component({
  selector: 'app-step-07-model',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-07-model.component.html',
})
export class Step07ModelComponent {
  private draft = inject(PolicyDraftService);
  private vehicleData = inject(VehicleDataService);
  private router = inject(Router);

  selected = signal(this.draft.draft().vehicle.model);
  search = signal('');

  allModels = toSignal(
    this.vehicleData.getModels(this.draft.draft().vehicle.make),
    { initialValue: [] as VehicleModel[] }
  );

  filtered = computed(() => {
    const q = this.search().toLowerCase();
    return q ? this.allModels().filter(m => m.name.toLowerCase().includes(q)) : this.allModels();
  });

  next() {
    this.draft.patchVehicle({ model: this.selected(), fuelType: 'benzyna', engineCC: 0, engineKM: 0, version: '', versionValue: 0, totalValue: 0 });
    this.router.navigate(['/kalkulator/paliwo']);
  }
}
