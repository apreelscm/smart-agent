import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { BirthDateInputMethod } from '../../../../core/models/policy-draft.model';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { sanitizePeselInput, validateAndDecodePesel } from '../../../../core/utils/pesel.util';

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
  inputMethod: BirthDateInputMethod = this.personalInfo.inputMethod ?? 'dateOfBirth';
  pesel = this.personalInfo.pesel ?? '';
  peselError = '';

  readonly inputMethodOptions: { value: BirthDateInputMethod; label: string }[] = [
    { value: 'dateOfBirth', label: 'Data urodzenia' },
    { value: 'pesel', label: 'PESEL' },
  ];

  constructor() {
    if (this.inputMethod === 'pesel') {
      this.applyPeselState(this.pesel);
    }
  }

  get valid(): boolean {
    const hasNames = this.firstName.trim().length > 0 && this.lastName.trim().length > 0;

    if (!hasNames) {
      return false;
    }

    if (this.inputMethod === 'dateOfBirth') {
      return this.dateOfBirth.length > 0;
    }

    return this.pesel.length === 11 && !this.peselError && this.dateOfBirth.length > 0;
  }

  selectInputMethod(method: BirthDateInputMethod): void {
    if (this.inputMethod === method) {
      return;
    }

    this.inputMethod = method;
    this.dateOfBirth = '';
    this.pesel = '';
    this.peselError = '';
  }

  onPeselChange(value: string): void {
    const sanitizedPesel = sanitizePeselInput(value).slice(0, 11);
    this.applyPeselState(sanitizedPesel);
  }

  next(): void {
    this.draft.patchPersonalInfo({
      firstName: this.firstName,
      lastName: this.lastName,
      dateOfBirth: this.dateOfBirth,
      inputMethod: this.inputMethod,
      pesel: this.inputMethod === 'pesel' ? this.pesel : '',
    });

    this.router.navigate(['/kalkulator/rejestracja']);
  }

  private applyPeselState(pesel: string): void {
    this.pesel = pesel;
    this.peselError = '';
    this.dateOfBirth = '';

    if (!pesel || pesel.length < 11) {
      return;
    }

    const decodedDateOfBirth = validateAndDecodePesel(pesel);

    if (!decodedDateOfBirth) {
      this.peselError = 'Wpisz poprawny numer PESEL.';
      return;
    }

    this.dateOfBirth = decodedDateOfBirth;
  }
}
