export const supportedCurrencies = ['PLN', 'EUR', 'USD'] as const;

export type SupportedCurrency = (typeof supportedCurrencies)[number];

export const foreignCurrencies = supportedCurrencies.filter((currency) => currency !== 'PLN') as Array<Exclude<SupportedCurrency, 'PLN'>>;
