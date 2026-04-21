import { InjectionToken } from '@angular/core';

export interface ExchangeRatesConfig {
  baseUrl: string;
  tableEndpoint: string;
}

export const DEFAULT_EXCHANGE_RATES_CONFIG: ExchangeRatesConfig = {
  baseUrl: 'https://api.nbp.pl/api',
  tableEndpoint: '/exchangerates/tables/A/?format=json'
};

export const EXCHANGE_RATES_CONFIG = new InjectionToken<ExchangeRatesConfig>('EXCHANGE_RATES_CONFIG', {
  providedIn: 'root',
  factory: () => DEFAULT_EXCHANGE_RATES_CONFIG
});
