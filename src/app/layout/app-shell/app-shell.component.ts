import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

type NavigationItem = {
  label: string;
  route: string;
  icon: string;
};

@Component({
  selector: 'app-app-shell',
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app-shell.component.html',
  styleUrl: './app-shell.component.scss'
})
export class AppShellComponent {
  protected readonly navigationItems: NavigationItem[] = [
    { label: 'Oferty', route: '/offers', icon: 'pi pi-briefcase' },
    { label: 'Polisy', route: '/policies', icon: 'pi pi-file' },
    { label: 'Klienci', route: '/customers', icon: 'pi pi-users' },
    { label: 'Raporty', route: '/reports', icon: 'pi pi-chart-line' }
  ];
}
