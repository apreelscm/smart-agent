import { Component, inject } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-18b-szyby',
  standalone: true,
  imports: [WizardCardComponent, DecimalPipe],
  templateUrl: './step-18b-szyby.component.html',
  styleUrl: './step-18b-szyby.component.scss',
})
export class Step18bSzybyComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  s = { ...this.draft.draft().coverages.szybyDetails };
  price = 150;

  confirm() {
    this.draft.patchCoverages({ szybyDetails: { ...this.s } });
    this.router.navigate(['/kalkulator/zakres']);
  }
  cancel() { this.router.navigate(['/kalkulator/zakres']); }
}
