import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';

interface SingleVisit {
  icon: string;
  title: string;
  text: string;
  price: string;
  route?: string;
}

interface MedicalPackage {
  title: string;
  text: string;
  price: string;
  bullets: string[];
}

@Component({
  selector: 'app-oferta',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './oferta.component.html',
  styleUrl: './oferta.component.scss',
})
export class OfertaComponent {
  readonly singleVisits: SingleVisit[] = [
    {
      icon: 'pi-building',
      title: 'Wizyta w placówce',
      text: 'Skonsultuj się z lekarzem w jednej z placówek Świat Zdrowia.',
      price: 'od 199 zł',
      route: '/portal/umow-wizyte',
    },
    {
      icon: 'pi-desktop',
      title: 'Konsultacja telemedyczna',
      text: 'Potrzebujesz skierowania, recepty lub szybkiej konsultacji lekarskiej.',
      price: 'od 99 zł',
    },
    {
      icon: 'pi-heart',
      title: 'Badania i szczepienia',
      text: 'Umów się na badanie diagnostyczne, zabieg ambulatoryjny lub szczepienie.',
      price: 'od 29 zł',
    },
  ];

  readonly packages: MedicalPackage[] = [
    {
      title: 'Pakiet medyczny dla Ciebie',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla klienta indywidualnego.',
      price: 'od 89 zł / mies.',
      bullets: [
        'oferta dla osoby dorosłej (od 18 do 66 r.ż.)',
        'umowa na 12 miesięcy',
        'w 4 zakresach do wyboru',
        'lekarze z zakresu nawet 30 specjalizacji',
      ],
    },
    {
      title: 'Pakiet medyczny dla Ciebie i Dziecka',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Ciebie i Twojego dziecka.',
      price: 'od 139 zł / mies.',
      bullets: [
        'oferta dla osoby dorosłej oraz dziecka do 18 r.ż.',
        'umowa na 12 miesięcy',
        'w 4 zakresach do wyboru',
        'lekarze z zakresu nawet 31 specjalizacji',
      ],
    },
    {
      title: 'Pakiet medyczny dla Ciebie i Partnera',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Ciebie i Partnera.',
      price: 'od 159 zł / mies.',
      bullets: [
        'oferta dla 2 osób dorosłych (od 18 do 66 r.ż.)',
        'umowa na 12 miesięcy',
        'w 4 zakresach do wyboru',
        'lekarze z zakresu nawet 30 specjalizacji',
      ],
    },
    {
      title: 'Pakiet medyczny dla Rodziny',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Ciebie i Twojej Rodziny.',
      price: 'od 229 zł / mies.',
      bullets: [
        'oferta dla 1 lub 2 osób dorosłych oraz maksymalnie 5 dzieci do 18 r.ż.',
        'umowa na 12 miesięcy',
        'w 3 zakresach do wyboru',
        'lekarze z zakresu nawet 22 specjalizacji',
      ],
    },
    {
      title: 'Pakiet medyczny 67+',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Seniora.',
      price: 'od 119 zł / mies.',
      bullets: [
        'oferta dla osoby dorosłej (od 67 r.ż.)',
        'umowa na 12 miesięcy',
        'w 3 zakresach do wyboru',
        'dostęp do lekarzy specjalistów bez skierowania',
      ],
    },
    {
      title: 'Pakiet medyczny dla Studenta',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Studenta.',
      price: 'od 69 zł / mies.',
      bullets: [
        'oferta dla osoby dorosłej (18 do 26 r.ż.)',
        'umowa na 12 miesięcy',
        'w 2 zakresach do wyboru',
        'atrakcyjna cena dla uczących się',
      ],
    },
    {
      title: 'Pakiet medyczny dla Kobiety w ciąży',
      text: 'Całoroczny pakiet medyczny w formie abonamentowej dla Kobiety w ciąży.',
      price: 'od 189 zł / mies.',
      bullets: [
        'kompleksowa opieka położniczo-ginekologiczna',
        'umowa na 12 miesięcy',
        'pakiet badań prenatalnych',
        'wsparcie położnej i konsultacje online',
      ],
    },
    {
      title: 'Pakiet diagnostyki i leczenia',
      text: 'Całoroczny pakiet dla pacjentów gotowych na zmianę i profilaktykę.',
      price: 'od 149 zł / mies.',
      bullets: [
        'szeroki pakiet badań diagnostycznych',
        'umowa na 12 miesięcy',
        'konsultacje dietetyczne',
        'plan leczenia prowadzony przez specjalistę',
      ],
    },
  ];
}
