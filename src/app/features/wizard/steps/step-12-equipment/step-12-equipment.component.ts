import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-12-equipment',
  standalone: true,
  imports: [WizardCardComponent, FormsModule, DecimalPipe],
  templateUrl: './step-12-equipment.component.html',
  styleUrl: './step-12-equipment.component.scss',
})
export class Step12EquipmentComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  private v = this.draft.draft().vehicle;
  versionName = this.v.version;
  versionValue = this.v.versionValue;
  additionalValue = this.v.additionalEquipmentValue || 0;

  get total() { return this.versionValue + this.additionalValue; }
  get sliderPct() { return (this.additionalValue / 20000 * 100).toFixed(1) + '%'; }

  next() {
    this.draft.patchVehicle({ additionalEquipmentValue: this.additionalValue, totalValue: this.total });
    this.router.navigate(['/kalkulator/przeznaczenie']);
  }
}
