import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-18-nnw',
  standalone: true,
  imports: [WizardCardComponent, FormsModule, DecimalPipe],
  templateUrl: './step-18-nnw.component.html',
  styleUrl: './step-18-nnw.component.scss',
})
export class Step18NnwComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  nnw = { ...this.draft.draft().coverages.nnwDetails };
  price = 129;

  get sliderPct() { return ((this.nnw.sumInsured - 10000) / (200000 - 10000) * 100).toFixed(1) + '%'; }

  confirm() {
    this.draft.patchCoverages({ nnwDetails: { ...this.nnw } });
    this.router.navigate(['/kalkulator/zakres']);
  }
  cancel() { this.router.navigate(['/kalkulator/zakres']); }
}
