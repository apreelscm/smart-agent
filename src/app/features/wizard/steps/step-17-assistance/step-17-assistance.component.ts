import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { AssistanceDetails } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-17-assistance',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-17-assistance.component.html',
  styleUrl: './step-17-assistance.component.scss',
})
export class Step17AssistanceComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  details: AssistanceDetails = { ...this.draft.draft().coverages.assistanceDetails };
  price = 89;

  tiers = [
    { value: 'podstawowy',  label: 'PODSTAWOWY',  holPL: '100 km', holEU: '0 km',      carDays: '3 dni' },
    { value: 'standardowy', label: 'STANDARDOWY', holPL: '500 km', holEU: '1 000 km',  carDays: '5 dni' },
    { value: 'optymalny',   label: 'OPTYMALNY',   holPL: 'bez limitu', holEU: 'bez limitu', carDays: '7 dni' },
    { value: 'maksymalny',  label: 'MAKSYMALNY',  holPL: 'bez limitu', holEU: 'bez limitu', carDays: '7 dni' },
  ];

  confirm() {
    this.draft.patchCoverages({ assistanceDetails: { ...this.details } });
    this.router.navigate(['/kalkulator/zakres']);
  }

  cancel() { this.router.navigate(['/kalkulator/zakres']); }
}
