import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'kalkulator/start', pathMatch: 'full' },
  {
    path: 'kalkulator',
    children: [
      { path: 'start',         loadComponent: () => import('./features/wizard/steps/step-01-welcome/step-01-welcome.component').then(m => m.Step01WelcomeComponent) },
      { path: 'kto-ty',        loadComponent: () => import('./features/wizard/steps/step-02-personal-info/step-02-personal-info.component').then(m => m.Step02PersonalInfoComponent) },
      { path: 'rejestracja',   loadComponent: () => import('./features/wizard/steps/step-03-registration/step-03-registration.component').then(m => m.Step03RegistrationComponent) },
      { path: 'typ-pojazdu',   loadComponent: () => import('./features/wizard/steps/step-04-vehicle-type/step-04-vehicle-type.component').then(m => m.Step04VehicleTypeComponent) },
      { path: 'rok',           loadComponent: () => import('./features/wizard/steps/step-05-year/step-05-year.component').then(m => m.Step05YearComponent) },
      { path: 'marka',         loadComponent: () => import('./features/wizard/steps/step-06-make/step-06-make.component').then(m => m.Step06MakeComponent) },
      { path: 'model',         loadComponent: () => import('./features/wizard/steps/step-07-model/step-07-model.component').then(m => m.Step07ModelComponent) },
      { path: 'paliwo',        loadComponent: () => import('./features/wizard/steps/step-08-fuel/step-08-fuel.component').then(m => m.Step08FuelComponent) },
      { path: 'pojemnosc',     loadComponent: () => import('./features/wizard/steps/step-09-engine-cc/step-09-engine-cc.component').then(m => m.Step09EngineCcComponent) },
      { path: 'moc',           loadComponent: () => import('./features/wizard/steps/step-10-engine-km/step-10-engine-km.component').then(m => m.Step10EngineKmComponent) },
      { path: 'wersja',        loadComponent: () => import('./features/wizard/steps/step-11-version/step-11-version.component').then(m => m.Step11VersionComponent) },
      { path: 'wyposazenie',   loadComponent: () => import('./features/wizard/steps/step-12-equipment/step-12-equipment.component').then(m => m.Step12EquipmentComponent) },
      { path: 'przeznaczenie', loadComponent: () => import('./features/wizard/steps/step-13-usage/step-13-usage.component').then(m => m.Step13UsageComponent) },
      { path: 'prawo-jazdy',   loadComponent: () => import('./features/wizard/steps/step-14-license-zip/step-14-license-zip.component').then(m => m.Step14LicenseZipComponent) },
      { path: 'zakres',        loadComponent: () => import('./features/wizard/steps/step-15-coverage/step-15-coverage.component').then(m => m.Step15CoverageComponent) },
      { path: 'casco',         loadComponent: () => import('./features/wizard/steps/step-16-casco/step-16-casco.component').then(m => m.Step16CascoComponent) },
      { path: 'assistance',    loadComponent: () => import('./features/wizard/steps/step-17-assistance/step-17-assistance.component').then(m => m.Step17AssistanceComponent) },
      { path: 'nnw',           loadComponent: () => import('./features/wizard/steps/step-18-nnw/step-18-nnw.component').then(m => m.Step18NnwComponent) },
      { path: 'szyby',         loadComponent: () => import('./features/wizard/steps/step-18b-szyby/step-18b-szyby.component').then(m => m.Step18bSzybyComponent) },
      { path: 'dane-polisowe', loadComponent: () => import('./features/wizard/steps/step-19-policy-data/step-19-policy-data.component').then(m => m.Step19PolicyDataComponent) },
      { path: 'sprawdz-dane',  loadComponent: () => import('./features/wizard/steps/step-21-review/step-21-review.component').then(m => m.Step21ReviewComponent) },
      { path: 'platnosc',      loadComponent: () => import('./features/wizard/steps/step-22-payment/step-22-payment.component').then(m => m.Step22PaymentComponent) },
      { path: 'sukces',        loadComponent: () => import('./features/wizard/steps/step-23-success/step-23-success.component').then(m => m.Step23SuccessComponent) },
      { path: '', redirectTo: 'start', pathMatch: 'full' },
    ],
  },
];
