import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BirthInputMode } from '../../../../core/models/policy-draft.model';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { PeselValidationError, validatePesel } from '../../../../core/utils/pesel.util';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

@Component({
  selector: 'app-step-02-personal-info',
  standalone: true,
  imports: [CommonModule, WizardCardComponent, FormsModule],
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
  pesel = this.personalInfo.pesel ?? '';
  birthInputMode: BirthInputMode = this.personalInfo.birthInputMode ?? 'pesel';
  peselValidationMessage = '';

  constructor() {
    if (this.isPeselMode) {
      this.syncPeselState(this.pesel);
    }
  }

  get isPeselMode(): boolean {
    return this.birthInputMode === 'pesel';
  }

  get showPeselValidationMessage(): boolean {
    return this.isPeselMode && !!this.pesel && !!this.peselValidationMessage;
  }

  get valid(): boolean {
    const hasNames = this.isFilled(this.firstName) && this.isFilled(this.lastName);

    if (!hasNames) {
      return false;
    }

    if (this.isPeselMode) {
      return this.pesel.length === 11 && !this.peselValidationMessage && !!this.dateOfBirth;
    }

    return !!this.dateOfBirth;
  }

  setBirthInputMode(mode: BirthInputMode): void {
    if (this.birthInputMode === mode) {
      return;
    }

    this.birthInputMode = mode;
    this.pesel = '';
    this.dateOfBirth = '';
    this.peselValidationMessage = '';
  }

  onPeselChange(value: string): void {
    this.syncPeselState(value.trim());
  }

  onManualDateChange(value: string): void {
    if (this.isPeselMode) {
      this.dateOfBirth = '';
      return;
    }

    this.dateOfBirth = value;
  }

  next(): void {
    this.draft.patchPersonalInfo({
      firstName: this.firstName.trim(),
      lastName: this.lastName.trim(),
      dateOfBirth: this.dateOfBirth,
      pesel: this.isPeselMode ? this.pesel : '',
      birthInputMode: this.birthInputMode,
    });

    this.router.navigate(['/kalkulator/rejestracja']);
  }

  private syncPeselState(value: string): void {
    this.pesel = value;

    if (!this.pesel) {
      this.dateOfBirth = '';
      this.peselValidationMessage = '';
      return;
    }

    const validation = validatePesel(this.pesel);

    if (validation.isValid) {
      this.dateOfBirth = validation.dateOfBirth ?? '';
      this.peselValidationMessage = '';
      return;
    }

    this.dateOfBirth = '';
    this.peselValidationMessage = this.getPeselValidationMessage(validation.errorCode);
  }

  private getPeselValidationMessage(errorCode: PeselValidationError | null): string {
    switch (errorCode) {
      case 'format':
        return 'PESEL może zawierać wyłącznie cyfry.';
      case 'length':
        return 'PESEL musi składać się z 11 cyfr.';
      case 'checksum':
        return 'PESEL ma nieprawidłową sumę kontrolną.';
      case 'date':
        return 'PESEL zawiera nieprawidłową datę urodzenia.';
      default:
        return '';
    }
  }

  private isFilled(value: string): boolean {
    return value.trim().length > 0;
  }
}
