import { Component, input } from '@angular/core';
import { PolicyPhotoStatus } from '../../../core/models/policy-photo.model';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  template: `
    <span class="badge" [class]="'badge--' + status()">
      {{ status() === 'do-blisko' ? 'Do blisko' : 'W Weryfikacja' }}
    </span>
  `,
  styles: [`
    .badge {
      display: inline-block;
      padding: 3px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
      white-space: nowrap;
    }
    .badge--do-blisko {
      background: transparent;
      color: #c62828;
      border: 1px solid #ef9a9a;
    }
    .badge--w-werifikacja {
      background: transparent;
      color: #2e7d32;
      border: 1px solid #a5d6a7;
    }
  `]
})
export class StatusBadgeComponent {
  readonly status = input.required<PolicyPhotoStatus>();
}
