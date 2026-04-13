import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Tag } from 'primeng/tag';

type ShowcaseStep = {
  title: string;
  description: string;
  state: 'done' | 'active' | 'planned';
};

@Component({
  selector: 'app-foundation-page',
  imports: [CommonModule, ButtonDirective, Tag],
  templateUrl: './foundation-page.component.html',
  styleUrl: './foundation-page.component.scss'
})
export class FoundationPageComponent {
  protected readonly steps: ShowcaseStep[] = [
    {
      title: 'Fundament aplikacji',
      description: 'Shell, routing, motyw PrimeNG, polskie locale i baza wizualna dla rynku PL.',
      state: 'active'
    },
    {
      title: 'Model domeny oferty',
      description: 'Spójny model Offer -> Variant -> PolicyLine -> Cover -> CoverTerm.',
      state: 'planned'
    },
    {
      title: 'Warstwa danych',
      description: 'Repozytoria i dane aplikacyjne przygotowane pod kolejne etapy integracji.',
      state: 'planned'
    }
  ];

  protected readonly metrics = [
    { label: 'Rynek', value: 'Polska', note: 'lokalizacja pl-PL' },
    { label: 'UI Foundation', value: 'PrimeNG 20', note: 'theme preset + custom shell' },
    { label: 'Architektura', value: 'Angular Standalone', note: 'routing + feature folders' }
  ];
}
