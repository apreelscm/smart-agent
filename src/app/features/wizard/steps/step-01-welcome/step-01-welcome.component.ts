import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

@Component({
  selector: 'app-step-01-welcome',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './step-01-welcome.component.html',
  styleUrl: './step-01-welcome.component.scss',
})
export class Step01WelcomeComponent {
  constructor(private router: Router) {}
  next() { this.router.navigate(['/kalkulator/kto-ty']); }
}
