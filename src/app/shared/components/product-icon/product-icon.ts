import { Component, computed, input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

const ICONS: Record<string, string> = {
  car:    'directions_car',
  home:   'home',
  health: 'monitor_heart',
  other:  'description',
};

@Component({
  selector: 'app-product-icon',
  standalone: true,
  imports: [MatIconModule],
  template: `
    <span class="product-icon">
      <mat-icon class="product-icon__icon">{{ iconName() }}</mat-icon>
    </span>
  `,
  styles: [`
    .product-icon {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 36px;
      height: 36px;
      border-radius: 50%;
      border: 2px solid transparent;
      background:
        linear-gradient(white, white) padding-box,
        conic-gradient(from 270deg, #ff2e17 0%, #0155ae 50%, #ff2e17 100%) border-box;
    }
    .product-icon__icon {
      font-size: 18px;
      width: 18px;
      height: 18px;
      line-height: 18px;
      display: inline-block;
      background: conic-gradient(from 270deg, #ff2e17 0%, #0155ae 50%, #ff2e17 100%);
      -webkit-background-clip: text;
      background-clip: text;
      -webkit-text-fill-color: transparent;
      color: transparent;
    }
  `]
})
export class ProductIconComponent {
  readonly productType = input.required<string>();

  readonly iconName = computed(() => ICONS[this.productType()] ?? 'description');
}
