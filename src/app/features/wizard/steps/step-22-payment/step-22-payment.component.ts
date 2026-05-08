import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { PaymentMethod } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-22-payment',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-22-payment.component.html',
  styleUrl: './step-22-payment.component.scss',
})
export class Step22PaymentComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  selected: PaymentMethod = this.draft.draft().payment.method;

  options: { value: PaymentMethod; label: string; icon: string }[] = [
    { value: 'przelew',   label: 'Przelew elektroniczny',      icon: '🏦' },
    { value: 'karta',     label: 'Karta płatnicza',            icon: '💳' },
    { value: 'applepay',  label: 'Apple Pay',                  icon: '🍎' },
    { value: 'googlepay', label: 'Google Pay',                 icon: '🔵' },
    { value: 'odroczona', label: 'Płatność z odroczoną spłatą', icon: '📅' },
  ];

  next() {
    this.draft.patch({ payment: { ...this.draft.draft().payment, method: this.selected } });
    this.router.navigate(['/kalkulator/sukces']);
  }
}
