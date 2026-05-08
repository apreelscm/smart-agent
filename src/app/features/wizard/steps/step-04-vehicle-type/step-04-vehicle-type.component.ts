import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehicleType } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-04-vehicle-type',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-04-vehicle-type.component.html',
})
export class Step04VehicleTypeComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  selected: VehicleType = this.draft.draft().vehicle.type;

  options: { value: VehicleType; label: string }[] = [
    { value: 'osobowy',  label: 'samochód osobowy lub SUV' },
    { value: 'motocykl', label: 'motocykl lub quad' },
    { value: 'przyczepa', label: 'przyczepa' },
    { value: 'dostawczy', label: 'samochód dostawczy' },
  ];

  select(v: VehicleType) { this.selected = v; }

  next() {
    this.draft.patchVehicle({ type: this.selected });
    this.router.navigate(['/kalkulator/rok']);
  }
}
