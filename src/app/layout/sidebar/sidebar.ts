import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AgentService } from '../../core/services/agent.service';
import { LayoutService } from '../../core/services/layout.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatIconModule, MatTooltipModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class SidebarComponent {
  readonly agentService = inject(AgentService);
  readonly layoutService = inject(LayoutService);

  readonly expandedRoutes = signal<Set<string>>(new Set());

  get collapsed() {
    return this.layoutService.sidebarCollapsed();
  }

  toggle() {
    this.layoutService.toggle();
  }

  toggleExpand(route: string): void {
    this.expandedRoutes.update(set => {
      const next = new Set(set);
      next.has(route) ? next.delete(route) : next.add(route);
      return next;
    });
  }

  isExpanded(route: string): boolean {
    return this.expandedRoutes().has(route);
  }
}
