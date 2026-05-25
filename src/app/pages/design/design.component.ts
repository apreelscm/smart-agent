import { Component } from '@angular/core';
import { WizardCardComponent } from '../../shared/components/wizard-card/wizard-card.component';
import { ChipBarComponent } from '../../shared/components/chip-bar/chip-bar.component';

@Component({
  selector: 'app-design',
  standalone: true,
  imports: [WizardCardComponent, ChipBarComponent],
  templateUrl: './design.component.html',
  styleUrl: './design.component.scss',
})
export class DesignComponent {
  sampleChips = [
    { label: 'eService' },
    { label: 'Terminal przenośny' },
    { label: 'Ingenico Move 5000' },
    { label: '36 mies.' },
    { label: 'Warszawa' },
  ];

  selectedRadio = 'option-2';

  selectRadio(value: string): void {
    this.selectedRadio = value;
  }
}
