import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-process-detail',
  standalone: true,
  template: `
    <h2>Szczegóły procesu #{{ id }}</h2>
    <p class="text-muted">TODO: process detail view with accept/reject/correction actions</p>
  `,
})
export class ProcessDetailComponent {
  id: string | null = null;

  constructor(private route: ActivatedRoute) {
    this.id = this.route.snapshot.paramMap.get('id');
  }
}
