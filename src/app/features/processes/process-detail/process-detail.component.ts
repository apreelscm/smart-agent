import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProcessListService } from '../process-list/process-list.service';

@Component({
  selector: 'app-process-detail',
  standalone: true,
  template: `
    <div class="card">
      <div class="card-body">
        <div *ngIf="loading" class="text-center text-muted py-4">Wczytywanie szczegółów procesu...</div>

        <div *ngIf="error && !loading" class="alert alert-danger">
          {{ error }}
          <div class="mt-2">
            <button class="btn btn-outline-danger btn-sm" (click)="reload()">Spróbuj ponownie</button>
            <button class="btn btn-link btn-sm" (click)="goBack()">Powrót do listy</button>
          </div>
        </div>

        <div *ngIf="process && !loading">
          <h2>Szczegóły procesu #{{ process.id }}</h2>
          <p class="text-muted mb-4">Status: <strong>{{ process.status }}</strong></p>

          <div class="row mb-3">
            <div class="col-12 col-md-6">
              <p><strong>Numer PH:</strong> {{ process.phNumber }}</p>
              <p><strong>NIP:</strong> {{ process.nip }}</p>
              <p><strong>Nazwa klienta:</strong> {{ process.clientName }}</p>
              <p><strong>Aktywność:</strong> {{ process.activity }}</p>
            </div>

            <div class="col-12 col-md-6">
              <p><strong>PH:</strong> {{ process.phName }}</p>
              <p><strong>Email PH:</strong> {{ process.phEmail }}</p>
              <p><strong>Format dokumentów:</strong> {{ formatDocuments(process.documentsFormat) }}</p>
              <p><strong>Uwagi COA:</strong> {{ process.notesFromCoa }}</p>
            </div>
          </div>

          <div class="d-flex gap-2">
            <button class="btn btn-success" (click)="accept()" [disabled]="actionLoading">Akceptuj</button>
            <button class="btn btn-danger" (click)="reject()" [disabled]="actionLoading">Odrzuć</button>
            <button class="btn btn-secondary" (click)="correct()" [disabled]="actionLoading">Popraw</button>
            <button class="btn btn-link" (click)="goBack()">Powrót</button>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class ProcessDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly processService = inject(ProcessListService);

  process: any | null = null;
  loading = true;
  actionLoading = false;
  error: string | null = null;

  constructor() {
    void this.load();
  }

  private async load(): Promise<void> {
    this.loading = true;
    this.error = null;

    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.error = 'Brak identyfikatora procesu';
      this.loading = false;
      return;
    }

    try {
      this.process = await this.processService.getProcess(id);
    } catch (e) {
      this.error = (e as Error)?.message ?? 'Wystąpił błąd podczas pobierania procesu';
      this.process = null;
    } finally {
      this.loading = false;
    }
  }

  async reload(): Promise<void> {
    await this.load();
  }

  async accept(): Promise<void> {
    if (!this.process) return;
    this.actionLoading = true;
    try {
      await this.processService.acceptProcess(this.process.id);
      await this.load();
    } catch (e) {
      this.error = (e as Error)?.message ?? 'Akceptacja nie powiodła się';
    } finally {
      this.actionLoading = false;
    }
  }

  async reject(): Promise<void> {
    if (!this.process) return;
    this.actionLoading = true;
    try {
      await this.processService.rejectProcess(this.process.id);
      await this.load();
    } catch (e) {
      this.error = (e as Error)?.message ?? 'Odrzucenie nie powiodło się';
    } finally {
      this.actionLoading = false;
    }
  }

  correct(): void {
    if (!this.process) return;
    // Navigate to a hypothetical correction route; keep behavior stable
    void this.router.navigate(['/processes', this.process.id, 'correction']);
  }

  goBack(): void {
    void this.router.navigate(['/processes']);
  }

  formatDocuments(format: any): string {
    switch (format) {
      case 'ELECTRONIC':
        return 'Elektroniczne';
      case 'PAPER':
        return 'Papierowe';
      case 'TEMPLATE':
        return 'Szablon';
      default:
        return String(format ?? '');
    }
  }
}
