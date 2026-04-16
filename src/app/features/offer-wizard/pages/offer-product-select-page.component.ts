import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';

type ProductTile = {
  key: 'MOTOR' | 'CROP';
  title: string;
  subtitle: string;
  iconClass: string;
  route: string[];
};

@Component({
  selector: 'app-offer-product-select-page',
  imports: [CommonModule, RouterLink, PageHeaderComponent, SectionCardComponent],
  templateUrl: './offer-product-select-page.component.html',
  styleUrl: './offer-product-select-page.component.scss'
})
export class OfferProductSelectPageComponent {
  protected readonly productTiles: ProductTile[] = [
    {
      key: 'MOTOR',
      title: 'Ubezpieczenie komunikacyjne',
      subtitle: 'OC / AC / Assistance / NNW',
      iconClass: 'pi pi-car',
      route: ['/offers', 'new', 'motor', 'vehicle']
    },
    {
      key: 'CROP',
      title: 'Ubezpieczenie upraw',
      subtitle: 'Uprawy, działki, ryzyka pogodowe',
      iconClass: 'pi pi-leaf',
      route: ['/offers', 'new', 'crop', 'crop']
    }
  ];
}
