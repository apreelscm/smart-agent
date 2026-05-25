import { HttpErrorResponse } from '@angular/common/http';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';

const EMPTY_ROUTE = '/empty';
const INVALID_CREDENTIALS_MESSAGE = 'Nieprawidłowy login lub hasło.';
const GENERIC_SIGN_IN_FAILURE_MESSAGE = 'Nie udało się zalogować. Spróbuj ponownie później.';

type LoginFormControlName = 'username' | 'password';

export function trimmedRequiredValidator(control: AbstractControl): ValidationErrors | null {
  const value = typeof control.value === 'string' ? control.value.trim() : '';
  return value.length > 0 ? null : { required: true };
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly isSubmitting = signal(false);
  readonly authError = signal<string | null>(null);

  readonly loginForm = this.formBuilder.nonNullable.group({
    username: ['', [trimmedRequiredValidator]],
    password: ['', [trimmedRequiredValidator]],
  });

  constructor() {
    this.loginForm.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      if (this.authError()) {
        this.authError.set(null);
      }
    });
  }

  onSubmit(): void {
    if (this.isSubmitting()) {
      return;
    }

    if (this.loginForm.invalid) {
      this.authError.set(null);
      this.loginForm.markAllAsTouched();
      return;
    }

    const formValue = this.loginForm.getRawValue();

    this.isSubmitting.set(true);
    this.authError.set(null);

    this.authService
      .login({
        username: formValue.username.trim(),
        password: formValue.password.trim(),
      })
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe({
        next: () => {
          void this.router.navigateByUrl(EMPTY_ROUTE);
        },
        error: (error: unknown) => {
          if (error instanceof HttpErrorResponse && error.status === 401) {
            this.authError.set(INVALID_CREDENTIALS_MESSAGE);
            return;
          }

          this.authError.set(GENERIC_SIGN_IN_FAILURE_MESSAGE);
        },
      });
  }

  isControlInvalid(controlName: LoginFormControlName): boolean {
    const control = this.loginForm.controls[controlName];
    return control.invalid && control.touched;
  }
}
