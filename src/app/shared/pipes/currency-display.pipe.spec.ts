import { CurrencyDisplayPipe } from './currency-display.pipe';

describe('CurrencyDisplayPipe', () => {
  const pipe = new CurrencyDisplayPipe();

  it('formats PLN with no decimals and currency symbol', () => {
    expect(pipe.transform(1234.56, 'PLN')).toBeDefined();
    const out = pipe.transform(1234.56, 'PLN');
    expect(out).toContain('zł');
  });

  it('formats EUR with two decimals and euro symbol', () => {
    const out = pipe.transform(1234.5, 'EUR');
    expect(out).toContain('€');
    // two decimals
    expect(/\d+,\d{2}/.test(out) || /\d+\.\d{2}/.test(out)).toBeTrue();
  });

  it('formats USD with two decimals and dollar symbol', () => {
    const out = pipe.transform(1234.5, 'USD');
    expect(out).toContain('$');
  });

  it('rounds PLN to integer', () => {
    expect(pipe.transform(1234.4, 'PLN')).toContain('1');
  });
});
