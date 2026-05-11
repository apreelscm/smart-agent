import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div class="card shadow" style="width: 400px">
        <div class="card-body">
          <h3 class="card-title text-center text-primary mb-4">eUmowy</h3>

          @if (errorMessage) {
            <div class="alert alert-danger" role="alert">
              {{ errorMessage }}
            </div>
          }

          <form (ngSubmit)="onLogin()">
            <div class="mb-3">
              <label for="username" class="form-label">Login</label>
              <input
                id="username"
                type="text"
                class="form-control"
                [(ngModel)]="username"
                name="username"
                required
              />
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">Hasło</label>
              <input
                id="password"
                type="password"
                class="form-control"
                [(ngModel)]="password"
                name="password"
                required
              />
            </div>
            <button type="submit" class="btn btn-primary w-100" [disabled]="isSubmitting">
              {{ isSubmitting ? 'Logowanie...' : 'Zaloguj' }}
            </button>
          </form>
        </div>
      </div>
    </div>
  `,
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  isSubmitting = false;

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  async onLogin(): Promise<void> {
    if (this.isSubmitting) {
      return;
    }

    this.errorMessage = '';
    this.isSubmitting = true;

    try {
      await this.authService.login(this.username, this.password);
      await this.router.navigate(['/']);
    } catch {
      this.errorMessage = 'Błędny login lub hasło.';
    } finally {
      this.isSubmitting = false;
    }
  }
}
