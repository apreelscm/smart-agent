import { Routes } from '@angular/router';
import { OffersHomePageComponent } from './features/offers/pages/offers-home-page.component';
import { CropStepPageComponent } from './features/offer-wizard/pages/crop-step-page.component';
import { CropVariantsStepPageComponent } from './features/offer-wizard/pages/crop-variants-step-page.component';
import { CustomerStepPageComponent } from './features/offer-wizard/pages/customer-step-page.component';
import { NewOfferWizardShellComponent } from './features/offer-wizard/pages/new-offer-wizard-shell.component';
import { OfferProductSelectPageComponent } from './features/offer-wizard/pages/offer-product-select-page.component';
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
        component: OfferProductSelectPageComponent,
        title: 'smart-agent | Nowa oferta | Wybór produktu'
      },
      {
        path: 'offers/new/motor',
        component: NewOfferWizardShellComponent,
        data: {
          product: 'MOTOR'
        },
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
        path: 'offers/new/crop',
        component: NewOfferWizardShellComponent,
        data: {
          product: 'CROP'
        },
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'crop'
          },
          {
            path: 'crop',
            component: CropStepPageComponent,
            title: 'smart-agent | Nowa oferta | Uprawy'
          },
          {
            path: 'variants',
            component: CropVariantsStepPageComponent,
            title: 'smart-agent | Nowa oferta | Konfiguracja wariantu'
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
        path: 'offers/new/vehicle',
        redirectTo: 'offers/new/motor/vehicle'
      },
      {
        path: 'offers/new/variants',
        redirectTo: 'offers/new/motor/variants'
      },
      {
        path: 'offers/new/customer',
        redirectTo: 'offers/new/motor/customer'
      },
      {
        path: 'offers/new/summary',
        redirectTo: 'offers/new/motor/summary'
      },
      {
        path: 'offers/:offerId',
        component: NewOfferWizardShellComponent,
        data: {
          product: 'MOTOR'
        },
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
        path: 'offers/:offerId/crop',
        component: NewOfferWizardShellComponent,
        data: {
          product: 'CROP'
        },
        children: [
          {
            path: '',
            pathMatch: 'full',
            redirectTo: 'crop'
          },
          {
            path: 'crop',
            component: CropStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty crop | Dane do kalkulacji'
          },
          {
            path: 'variants',
            component: CropVariantsStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty crop | Konfiguracja wariantu'
          },
          {
            path: 'customer',
            component: CustomerStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty crop | Dane do umowy'
          },
          {
            path: 'summary',
            component: SummaryStepPageComponent,
            title: 'smart-agent | Kontynuacja oferty crop | Podsumowanie'
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
