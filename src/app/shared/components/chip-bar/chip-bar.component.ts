import { Component, Input } from '@angular/core';

export interface Chip {
  label: string;
}

@Component({
  selector: 'app-chip-bar',
  standalone: true,
  imports: [],
  templateUrl: './chip-bar.component.html',
})
export class ChipBarComponent {
  @Input() chips: Chip[] = [];

  removeChip(index: number): void {
    this.chips = this.chips.filter((_, i) => i !== index);
  }
}
