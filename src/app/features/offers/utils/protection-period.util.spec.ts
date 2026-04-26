import { formatProtectionPeriod } from './protection-period.util';

describe('formatProtectionPeriod', () => {
  it('formats the protection period using the current local day', () => {
    const referenceDate = new Date(2025, 0, 15, 14, 35);

    expect(formatProtectionPeriod(referenceDate)).toBe('2025-01-15 – 2026-01-15');
  });

  it('keeps the same day and month across a year boundary', () => {
    const referenceDate = new Date(2025, 11, 31, 23, 59);

    expect(formatProtectionPeriod(referenceDate)).toBe('2025-12-31 – 2026-12-31');
  });

  it('handles leap-year rollover consistently', () => {
    const referenceDate = new Date(2024, 1, 29, 8, 0);

    expect(formatProtectionPeriod(referenceDate)).toBe('2024-02-29 – 2025-02-28');
  });

  it('returns a fallback for an invalid reference date', () => {
    expect(formatProtectionPeriod(new Date(Number.NaN))).toBe('—');
  });
});
