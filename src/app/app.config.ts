import { provideHttpClient } from '@angular/common/http';
import { LOCALE_ID, ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';

import { routes } from './app.routes';
import { primeNgPolishTranslation } from './core/config/primeng-pl';
import { SmartAgentPreset } from './core/config/primeng-theme';
import { CurrencyPipe } from '@angular/common';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(),
    provideRouter(routes),
    provideAnimationsAsync(),
    providePrimeNG({
      ripple: true,
      inputVariant: 'filled',
      theme: {
        preset: SmartAgentPreset,
        options: {
          darkModeSelector: false
        }
      },
      translation: primeNgPolishTranslation
    }),
    CurrencyPipe,
    {
      provide: LOCALE_ID,
      useValue: 'pl-PL'
    }
  ]
};
