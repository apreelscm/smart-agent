import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-02-personal-info',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-02-personal-info.component.html',
})
export class Step02PersonalInfoComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  firstName = this.draft.draft().personalInfo.firstName;
  lastName  = this.draft.draft().personalInfo.lastName;
  dateOfBirth = this.draft.draft().personalInfo.dateOfBirth;

  get valid() { return this.firstName && this.lastName && this.dateOfBirth; }

  next() {
    this.draft.patchPersonalInfo({ firstName: this.firstName, lastName: this.lastName, dateOfBirth: this.dateOfBirth });
    this.router.navigate(['/kalkulator/rejestracja']);
  }
}
