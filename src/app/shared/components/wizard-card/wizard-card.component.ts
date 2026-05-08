import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgClass } from '@angular/common';
import { ChipBarComponent } from '../chip-bar/chip-bar.component';

@Component({
  selector: 'app-wizard-card',
  standalone: true,
  imports: [NgClass, ChipBarComponent],
  templateUrl: './wizard-card.component.html',
  styleUrl: './wizard-card.component.scss',
})
export class WizardCardComponent {
  @Input() contentBg: 'cream' | 'white' = 'white';
  @Input() showChips = false;
  @Input() nextLabel = 'Dalej';
  @Input() nextDisabled = false;
  @Input() showNext = true;
  @Output() nextClick = new EventEmitter<void>();
}
