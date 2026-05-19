import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { BirthDateInputMode } from '../../../../core/models/policy-draft.model';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { validatePesel } from '../../../../core/utils/pesel.util';

@Component({
  selector: 'app-step-02-personal-info',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-02-personal-info.component.html',
  styleUrl: './step-02-personal-info.component.scss',
})
export class Step02PersonalInfoComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  private personalInfo = this.draft.draft().personalInfo;

  firstName = this.personalInfo.firstName;
  lastName = this.personalInfo.lastName;
  dateOfBirth = this.personalInfo.dateOfBirth;
  pesel = this.personalInfo.pesel;
  birthDateInputMode: BirthDateInputMode = this.personalInfo.birthDateInputMode;

  constructor() {
    if (this.isPeselMode) {
      this.updateDateOfBirthFromPesel();
    }
  }

  get isPeselMode(): boolean {
    return this.birthDateInputMode === 'pesel';
  }

  get peselError(): string {
    if (!this.isPeselMode) {
      return '';
    }

    switch (validatePesel(this.pesel).error) {
      case 'required':
        return 'PESEL jest wymagany.';
      case 'digitsOnly':
        return 'PESEL może zawierać wyłącznie cyfry.';
      case 'length':
        return 'PESEL musi składać się z 11 cyfr.';
      case 'checksum':
        return 'PESEL ma nieprawidłową sumę kontrolną.';
      case 'birthDate':
        return 'PESEL zawiera nieprawidłową datę urodzenia.';
      default:
        return '';
    }
  }

  get valid(): boolean {
    const hasRequiredNames = this.firstName.trim().length > 0 && this.lastName.trim().length > 0;

    if (!hasRequiredNames) {
      return false;
    }

    if (this.isPeselMode) {
      return validatePesel(this.pesel).valid && this.dateOfBirth.trim().length > 0;
    }

    return this.dateOfBirth.trim().length > 0;
  }

  setBirthDateInputMode(mode: BirthDateInputMode): void {
    if (this.birthDateInputMode === mode) {
      return;
    }

    this.birthDateInputMode = mode;

    if (mode === 'pesel') {
      this.updateDateOfBirthFromPesel();
    }
  }

  onPeselChange(value: string): void {
    this.pesel = value.slice(0, 11);
    this.updateDateOfBirthFromPesel();
  }

  next(): void {
    if (!this.valid) {
      return;
    }

    this.draft.patchPersonalInfo({
      firstName: this.firstName.trim(),
      lastName: this.lastName.trim(),
      dateOfBirth: this.dateOfBirth,
      pesel: this.pesel,
      birthDateInputMode: this.birthDateInputMode,
    });

    this.router.navigate(['/kalkulator/rejestracja']);
  }

  private updateDateOfBirthFromPesel(): void {
    if (!this.isPeselMode) {
      return;
    }

    const validation = validatePesel(this.pesel);
    this.dateOfBirth = validation.valid ? validation.birthDate ?? '' : '';
  }
}
