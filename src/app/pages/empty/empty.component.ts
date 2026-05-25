import { Component } from '@angular/core';
import { WizardCardComponent } from '../../shared/components/wizard-card/wizard-card.component';

@Component({
  selector: 'app-empty',
  standalone: true,
  imports: [WizardCardComponent],
  templateUrl: './empty.component.html',
  styleUrl: './empty.component.scss',
})
export class EmptyComponent {}
