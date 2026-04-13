import { Component, input } from '@angular/core';

@Component({
  selector: 'app-section-card',
  templateUrl: './section-card.component.html',
  styleUrl: './section-card.component.scss'
})
export class SectionCardComponent {
  readonly title = input<string>();
  readonly subtitle = input<string>();
}
