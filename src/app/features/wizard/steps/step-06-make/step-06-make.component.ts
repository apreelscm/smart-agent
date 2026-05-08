import { Component, inject, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleDataService, VehicleBrand } from '../../../../core/services/vehicle-data.service';

@Component({
  selector: 'app-step-06-make',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-06-make.component.html',
})
export class Step06MakeComponent {
  private draft = inject(PolicyDraftService);
  private vehicleData = inject(VehicleDataService);
  private router = inject(Router);

  selected = signal(this.draft.draft().vehicle.make);
  search = signal('');

  allBrands = toSignal(this.vehicleData.getBrands(), { initialValue: [] as VehicleBrand[] });
  popularBrands = computed(() => this.allBrands().filter(b => b.popular));

  filteredByLetter = computed(() => {
    const q = this.search().toLowerCase();
    const brands = this.allBrands().filter(b => !b.popular || q);
    const filtered = q ? brands.filter(b => b.name.toLowerCase().includes(q)) : brands;
    const groups: Record<string, string[]> = {};
    filtered.forEach(b => {
      const letter = b.name[0].toUpperCase();
      (groups[letter] ??= []).push(b.name);
    });
    return Object.entries(groups).sort(([a], [b]) => a.localeCompare(b));
  });

  select(name: string) { this.selected.set(name); }

  next() {
    this.draft.patchVehicle({ make: this.selected(), model: '', fuelType: 'benzyna', engineCC: 0, engineKM: 0, version: '', versionValue: 0, totalValue: 0 });
    this.router.navigate(['/kalkulator/model']);
  }
}
