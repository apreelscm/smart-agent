import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { LicenseYears } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-14-license-zip',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-14-license-zip.component.html',
  styleUrl: './step-14-license-zip.component.scss',
})
export class Step14LicenseZipComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  licenseYears: LicenseYears = this.draft.draft().usage.licenseYears;
  zipCode = this.draft.draft().usage.zipCode;

  licenseOptions: { value: LicenseYears; label: string }[] = [
    { value: 'more3',  label: 'dłużej niż 3 lata' },
    { value: '1to3',   label: '1–3 lata' },
    { value: 'less1',  label: 'mniej niż rok' },
  ];

  get valid() { return this.zipCode.length >= 5; }

  next() {
    this.draft.patchUsage({ licenseYears: this.licenseYears, zipCode: this.zipCode });
    this.router.navigate(['/kalkulator/zakres']);
  }
}
