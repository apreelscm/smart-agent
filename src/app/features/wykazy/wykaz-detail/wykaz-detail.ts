import { Component, inject, computed, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { WykazService } from '../../../core/services/wykaz.service';

@Component({
  selector: 'app-wykaz-detail',
  standalone: true,
  imports: [RouterLink, FormsModule, MatIconModule, MatButtonModule, MatTooltipModule],
  templateUrl: './wykaz-detail.html',
  styleUrl: './wykaz-detail.scss',
})
export class WykazDetailComponent {
  readonly service = inject(WykazService);
  private readonly route = inject(ActivatedRoute);

  readonly wykaz = computed(() => this.service.getById(this.route.snapshot.paramMap.get('id') ?? ''));

  readonly newNote = signal('');
  readonly showNoteForm = signal(false);
  readonly hasPayuAccess = signal(true); // mock: user has PayU permission

  addNote(): void {
    const content = this.newNote().trim();
    if (!content || !this.wykaz()) return;
    this.service.addNote(this.wykaz()!.id, content, 'p.kowalski');
    this.newNote.set('');
    this.showNoteForm.set(false);
  }

  removeItem(itemId: string): void {
    this.service.removeItem(this.wykaz()!.id, itemId);
  }

  itemTypeLabel(t: string): string {
    return ({ polisa:'Polisa', kwitariusz:'Kwitariusz', aneks:'Aneks', inny:'Inny dokument' } as Record<string, string>)[t] ?? t;
  }

  itemTypeIcon(t: string): string {
    return ({ polisa:'verified_user', kwitariusz:'receipt', aneks:'difference', inny:'description' } as Record<string, string>)[t] ?? 'description';
  }
}
