import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

export interface PortalNavItem {
  label: string;
  route?: string;
  key: string;
}

/**
 * Wspólna ramka portalu pacjenta Świat Zdrowia: pasek z logo,
 * nawigacja główna, opcjonalny okruszek (breadcrumb) i stopka.
 * Treść strony trafia przez <ng-content />.
 */
@Component({
  selector: 'app-portal-layout',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './portal-layout.component.html',
  styleUrl: './portal-layout.component.scss',
})
export class PortalLayoutComponent {
  /** Klucz aktywnej zakładki nawigacji. */
  @Input() active = 'pulpit';
  /** Tekst okruszka nad treścią (np. „UMÓW WIZYTĘ STACJONARNĄ”). */
  @Input() breadcrumb: string | null = null;

  readonly navItems: PortalNavItem[] = [
    { key: 'pulpit', label: 'Pulpit', route: '/portal/pulpit' },
    { key: 'produkty', label: 'Moje produkty' },
    { key: 'oferta', label: 'Oferta', route: '/portal/oferta' },
    { key: 'opieka', label: 'Opieka medyczna', route: '/portal/opieka' },
    { key: 'swiadczenia', label: 'Świadczenia' },
    { key: 'platnosci', label: 'Płatności' },
    { key: 'formularze', label: 'Formularze' },
  ];
}
