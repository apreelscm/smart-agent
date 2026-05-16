import { Injectable, signal } from '@angular/core';
import { Agent, NavItem } from '../models/agent.model';

@Injectable({ providedIn: 'root' })
export class AgentService {
  readonly currentAgent = signal<Agent>({
    id: '1',
    name: 'Piotr Kowalski',
    code: 'ZPO Modlino 99',
    packageName: 'Pakiet Mieszkanie +',
    avatarInitials: 'PK',
    email: 'p.kowalski@interrisk.pl',
    username: 'irys_tester_ZFO',
    company: 'InterRisk',
  });

  readonly navItems = signal<NavItem[]>([
    { label: 'Twój kokpit',           icon: 'grid_view',         route: '/kokpit',               section: 'sprzedaz' },
    { label: 'Oferty',                icon: 'description',       route: '/oferty',               section: 'sprzedaz' },
    { label: 'Zdjęcia do polis',      icon: 'photo_camera',      route: '/zdjecia-do-polisy',    section: 'sprzedaz' },
    { label: 'Polisy',                icon: 'verified_user',     route: '/polisy',               section: 'sprzedaz' },
    { label: 'Polisy do wznowienia',  icon: 'history',           route: '/polisy-do-wznowienia', section: 'sprzedaz' },
    { label: 'Zniżki',               icon: 'local_offer',       route: '/znizki',               section: 'sprzedaz' },
    { label: 'Panel administracyjny', icon: 'manage_accounts',   route: '/admin',                section: 'sprzedaz' },
    { label: 'Underwriting',          icon: 'assignment',        route: '/underwriting',         section: 'sprzedaz' },
    { label: 'Rozliczenia',           icon: 'receipt_long',      route: '/rozliczenia',          section: 'sprzedaz',
      children: [
        { label: 'Kwitariusze',                  route: '/rozliczenia/kwitariusze',               icon: 'receipt'          },
        { label: 'Wykazy',                        route: '/rozliczenia/wykazy',                    icon: 'list_alt'         },
        { label: 'Noty prowizyjne',               route: '/rozliczenia/noty-prowizyjne',           icon: 'request_quote'    },
        { label: 'OC',   route: '/rozliczenia/wypowiedzenia-zawiadomienia', icon: 'mark_email_read' },
      ] },
    { label: 'Klienci',               icon: 'group',             route: '/klienci',              section: 'wkrotce', disabled: true },
    { label: 'Do pobrania',           icon: 'file_download',     route: '/do-pobrania',          section: 'wkrotce', disabled: true },
    { label: 'Kontakt',               icon: 'email',             route: '/kontakt',              section: 'wkrotce', disabled: true },
  ]);

  get sprzedazItems() {
    return this.navItems().filter(i => i.section === 'sprzedaz');
  }

  get wkrotceItems() {
    return this.navItems().filter(i => i.section === 'wkrotce');
  }
}
