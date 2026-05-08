import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-21-review',
  standalone: true,
  imports: [WizardCardComponent, DatePipe, DecimalPipe, FormsModule],
  templateUrl: './step-21-review.component.html',
  styleUrl: './step-21-review.component.scss',
})
export class Step21ReviewComponent {
  private draftSvc = inject(PolicyDraftService);
  private router = inject(Router);

  draft = this.draftSvc.draft();

  installmentOptions = [1, 2, 3, 4] as const;
  selectedInstallments: 1|2|3|4 = this.draft.payment.installments;

  consentsAll = false;

  toggleAll(v: boolean) {
    this.consentsAll = v;
    this.draft.consents.owu = v;
    this.draft.consents.kid = v;
    this.draft.consents.marketing = v;
  }

  get startDate() { return new Date(); }
  get endDate() { const d = new Date(); d.setFullYear(d.getFullYear() + 1); return d; }

  get installmentPrice() {
    return Math.ceil(this.draft.coverages.totalPremium / this.selectedInstallments);
  }

  next() {
    this.draftSvc.patch({ payment: { ...this.draft.payment, installments: this.selectedInstallments }, consents: { ...this.draft.consents } });
    this.router.navigate(['/kalkulator/platnosc']);
  }

  editSection(route: string) { this.router.navigate([route]); }
}
