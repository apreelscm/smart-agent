import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid">
        <a class="navbar-brand fw-bold" routerLink="/">eUmowy</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/processes" routerLinkActive="active">Procesy</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/wizard" routerLinkActive="active">Kreator</a>
            </li>
          </ul>
          <span class="navbar-text text-light">
            {{ auth.user?.name }}
          </span>
        </div>
      </div>
    </nav>
    <div class="container-fluid mt-3">
      <router-outlet />
    </div>
  `,
})
export class LayoutComponent {
  constructor(public auth: AuthService) {}
}
