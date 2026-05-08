import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { emptyPerson, Person } from '../../../../core/models/policy-draft.model';

@Component({
  selector: 'app-step-19-policy-data',
  standalone: true,
  imports: [WizardCardComponent, FormsModule, DialogModule],
  templateUrl: './step-19-policy-data.component.html',
  styleUrl: './step-19-policy-data.component.scss',
})
export class Step19PolicyDataComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  ph = structuredClone(this.draft.draft().policyholder);
  ins = structuredClone(this.draft.draft().insured);
  insuredSameAsPh = signal(false);
  coOwnerDialogVisible = signal(false);
  coOwner: Person = this.draft.draft().coOwner ? structuredClone(this.draft.draft().coOwner!) : emptyPerson();

  copyPhToIns() {
    if (this.insuredSameAsPh()) {
      this.ins = structuredClone(this.ph);
    }
  }

  openCoOwner() { this.coOwnerDialogVisible.set(true); }
  closeCoOwner() { this.coOwnerDialogVisible.set(false); }
  saveCoOwner() {
    this.draft.patch({ coOwner: structuredClone(this.coOwner) });
    this.coOwnerDialogVisible.set(false);
  }

  get valid() {
    return this.ph.firstName && this.ph.lastName && this.ph.pesel && this.ph.email && this.ph.phone;
  }

  next() {
    this.draft.patchPolicyholder(this.ph);
    this.draft.patchInsured(this.insuredSameAsPh() ? this.ph : this.ins);
    this.router.navigate(['/kalkulator/sprawdz-dane']);
  }
}
