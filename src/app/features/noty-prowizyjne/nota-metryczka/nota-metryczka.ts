import { Component, inject, computed } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { NotaProwizyjnaService } from '../../../core/services/nota-prowizyjna.service';

@Component({
  selector: 'app-nota-metryczka',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  templateUrl: './nota-metryczka.html',
  styleUrl: './nota-metryczka.scss',
})
export class NotaMetryczkaComponent {
  readonly service = inject(NotaProwizyjnaService);
  private readonly route = inject(ActivatedRoute);
  readonly nota = computed(() => this.service.getById(this.route.snapshot.paramMap.get('id') ?? ''));

  lossClass(ratio: number, threshold: number): string {
    return ratio > threshold ? 'loss--high' : 'loss--ok';
  }
}
