export interface RatesMap {
  [currency: string]: number;
}

export interface RateSet {
  base: 'PLN';
  date: string; // ISO date
  rates: {
    EUR?: number;
    USD?: number;
  };
}

export interface RatesCache {
  fetchedAt: string; // ISO timestamp when fetched
  rateSet: RateSet;
}
