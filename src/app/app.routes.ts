import { Routes } from '@angular/router';
import { OffersHomePageComponent } from './features/offers/pages/offers-home-page.component';
import { CustomerStepPageComponent } from './features/offer-wizard/pages/customer-step-page.component';
import { NewOfferWizardShellComponent } from './features/offer-wizard/pages/new-offer-wizard-shell.component';
import { SummaryStepPageComponent } from './features/offer-wizard/pages/summary-step-page.component';
import { VariantsStepPageComponent } from './features/offer-wizard/pages/variants-step-page.component';
import { VehicleStepPageComponent } from './features/offer-wizard/pages/vehicle-step-page.component';
import { PoliciesHomePageComponent } from './features/policies/pages/policies-home-page.component';
import { ReportsPageComponent } from './features/reports/pages/reports-page.component';
import { CustomersPageComponent } from './features/customers/pages/customers-page.component';
import { FoundationPageComponent } from './features/foundation/pages/foundation-page.component';
import { AppShellComponent } from './layout/app-shell/app-shell.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'offers'
  },
  {
    path: '',
    component: AppShellComponent,
    children: [
      {
        path: 'offers',
        component: OffersHomePageComponent,
        title: 'smart-agent | Oferty'
      },
      {
        path: 'offers/new',
        component: NewOfferWizardShellComponent,
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'vehicle'
          },
          {
            path: 'vehicle',
            component: VehicleStepPageComponent,
            title: 'smart-agent | Nowa oferta | Pojazd'
          },
          {
            path: 'variants',
            component: VariantsStepPageComponent,
            title: 'smart-agent | Nowa oferta | Warianty'
          },
          {
            path: 'customer',
            component: CustomerStepPageComponent,
            title: 'smart-agent | Nowa oferta | Dane do umowy'
          },
          {
            path: 'summary',
            component: SummaryStepPageComponent,
            title: 'smart-agent | Nowa oferta | Podsumowanie'
          }
        ]
      },
      {
        path: 'offers/:offerId',
        component: NewOfferWizardShellComponent,
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'vehicle'
          },
          {
            path: 'vehicle',
            component: VehicleStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty | Dane do kalkulacji'
          },
          {
            path: 'variants',
            component: VariantsStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty | Konfiguracja wariantu'
          },
          {
            path: 'customer',
            component: CustomerStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty | Dane do umowy'
          },
          {
            path: 'summary',
            component: SummaryStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty | Podsumowanie'
          }
        ]
      },
      {
        path: 'policies',
        component: PoliciesHomePageComponent,
        title: 'smart-agent | Polisy'
      },
      {
        path: 'customers',
        component: CustomersPageComponent,
        title: 'smart-agent | Klienci'
      },
      {
        path: 'customers/:customerKey',
        component: CustomersPageComponent,
        title: 'smart-agent | Klient'
      },
      {
        path: 'reports',
        component: ReportsPageComponent,
        title: 'smart-agent | Raporty'
      }
    ]
  }
];
