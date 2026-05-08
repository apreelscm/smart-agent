import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

@Component({
  selector: 'app-step-23-success',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-23-success.component.html',
  styleUrl: './step-23-success.component.scss',
})
export class Step23SuccessComponent {
  policyNumber = 'EH/2024/00123456';
  constructor(private router: Router) {}
  home() { this.router.navigate(['/kalkulator/start']); }
}
