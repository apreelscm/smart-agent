import { CurrencyConversionService } from './currency-conversion.service';

describe('CurrencyConversionService', () => {
  let service: CurrencyConversionService;

  beforeEach(() => {
    service = new CurrencyConversionService();
  });

  it('converts PLN to foreign currency for display', () => {
    expect(service.toDisplayAmount(430, 'EUR', { EUR: 4.3 })).toBeCloseTo(100, 2);
    expect(service.toDisplayAmount(385, 'USD', { USD: 3.85 })).toBeCloseTo(100, 2);
  });

  it('converts foreign currency to persisted PLN using half-up integer rounding', () => {
    expect(service.toPersistedPln(100.4, 'PLN')).toBe(100);
    expect(service.toPersistedPln(100, 'EUR', { EUR: 4.2941 })).toBe(429);
    expect(service.toPersistedPln(100, 'USD', { USD: 3.85 })).toBe(385);
  });

  it('formats PLN with zero decimals and EUR with two decimals', () => {
    expect(service.formatAmount(1234, 'PLN')).toContain('1 234');
    expect(service.formatAmount(429.41, 'EUR', { EUR: 4.2941 })).toContain('100,00');
  });

  it('returns fallback availability when rates are unavailable', () => {
    const availability = service.getAvailability(null, true);

    expect(availability.availableCurrencies).toEqual(['PLN']);
    expect(availability.unavailableCurrencies).toEqual(['EUR', 'USD']);
    expect(availability.fallbackMessage).toBeTruthy();
  });
});
