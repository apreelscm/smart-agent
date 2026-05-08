import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-15-coverage',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-15-coverage.component.html',
  styleUrl: './step-15-coverage.component.scss',
})
export class Step15CoverageComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  cov = { ...this.draft.draft().coverages };

  mainCoverages = [
    { key: 'oc',         label: 'OC',         desc: 'Obowiązkowe ubezpieczenie odpowiedzialności cywilnej', price: 289, route: null, required: true },
    { key: 'ac',         label: 'AC',         desc: 'Pełny zakres casco',                                   price: 389, route: '/kalkulator/casco',      required: false },
    { key: 'assistance', label: 'Assistance', desc: 'Pomoc na terenie Europy',                              price:  89, route: '/kalkulator/assistance', required: false },
    { key: 'nnwKip',     label: 'NNW KiP',    desc: 'Suma ubezpieczenia do 50 000 zł',                     price: 129, route: '/kalkulator/nnw',        required: false },
  ];

  addons = [
    { key: 'szyby',  label: 'Szyby',  desc: 'Uszkodzenie, połknięcie lub wymiana szyb',               price: 150, route: '/kalkulator/szyby' },
    { key: 'bagaz',  label: 'Bagaż',  desc: 'Uszkodzenie lub kradzież przewożonego bagażu',            price:  89, route: null },
    { key: 'opony',  label: 'Opony',  desc: 'Pokrycie kosztów naprawy lub wymiany opon',               price:  89, route: null },
  ];

  promoCode = this.cov.promoCode;

  get total() {
    let sum = 289; // OC always
    if (this.cov.ac)         sum += 389;
    if (this.cov.assistance) sum += 89;
    if (this.cov.nnwKip)     sum += 129;
    if (this.cov.szyby)      sum += 150;
    if (this.cov.bagaz)      sum += 89;
    if (this.cov.opony)      sum += 89;
    return sum;
  }

  toggle(key: string) {
    (this.cov as any)[key] = !(this.cov as any)[key];
  }

  isOn(key: string): boolean {
    return !!(this.cov as any)[key];
  }

  openDetail(route: string | null) {
    if (route) this.router.navigate([route]);
  }

  next() {
    this.draft.patchCoverages({ ...this.cov, promoCode: this.promoCode, totalPremium: this.total });
    this.router.navigate(['/kalkulator/dane-polisowe']);
  }
}
