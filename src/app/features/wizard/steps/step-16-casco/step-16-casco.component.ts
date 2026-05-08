import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { AcDetails } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-16-casco',
  standalone: true,
  imports: [WizardCardComponent, DecimalPipe],
  templateUrl: './step-16-casco.component.html',
  styleUrl: './step-16-casco.component.scss',
})
export class Step16CascoComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  ac: AcDetails = { ...this.draft.draft().coverages.acDetails };
  price = 389;

  confirm() {
    this.draft.patchCoverages({ acDetails: { ...this.ac } });
    this.router.navigate(['/kalkulator/zakres']);
  }

  cancel() {
    this.router.navigate(['/kalkulator/zakres']);
  }
}
