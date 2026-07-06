import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';

interface Shortcut {
  icon: string;
  label: string;
  route?: string;
}

@Component({
  selector: 'app-pulpit',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './pulpit.component.html',
  styleUrl: './pulpit.component.scss',
})
export class PulpitComponent {
  readonly shortcuts: Shortcut[] = [
    { icon: 'pi-calendar-plus', label: 'Umów wizytę lub badanie', route: '/portal/opieka' },
    { icon: 'pi-map-marker', label: 'Placówki medyczne' },
    { icon: 'pi-file-edit', label: 'Zgłoś świadczenie' },
    { icon: 'pi-heart', label: 'Moje pakiety' },
    { icon: 'pi-shopping-cart', label: 'Kup online' },
    { icon: 'pi-credit-card', label: 'Nadchodzące płatności' },
    { icon: 'pi-gift', label: 'Klub i rabaty' },
    { icon: 'pi-chart-line', label: 'Profilaktyka' },
    { icon: 'pi-file', label: 'Formularze' },
    { icon: 'pi-id-card', label: 'Deklaracja POZ' },
  ];
}
