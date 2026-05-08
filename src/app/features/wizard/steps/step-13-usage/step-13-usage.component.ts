import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { VehiclePurpose } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-13-usage',
  standalone: true,
  imports: [WizardCardComponent, FormsModule, DecimalPipe],
  templateUrl: './step-13-usage.component.html',
  styleUrl: './step-13-usage.component.scss',
})
export class Step13UsageComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  purpose: VehiclePurpose = this.draft.draft().usage.purpose;
  dailyKm = this.draft.draft().usage.dailyKm;

  get yearlyKm() { return this.dailyKm * 365; }
  get sliderPct() { return ((this.dailyKm - 5) / (200 - 5) * 100).toFixed(1) + '%'; }

  purposes: { value: VehiclePurpose; label: string }[] = [
    { value: 'prywatny', label: 'Samochód prywatny' },
    { value: 'firmowy',  label: 'Samochód firmowy' },
  ];

  next() {
    this.draft.patchUsage({ purpose: this.purpose, dailyKm: this.dailyKm });
    this.router.navigate(['/kalkulator/prawo-jazdy']);
  }
}
