import { formatOfferProtectionPeriod } from './offer-protection-period.util';

describe('formatOfferProtectionPeriod', () => {
  it('should return the Jira example for 2025/01/15', () => {
    const referenceDate = new Date('2025-01-15T12:00:00.000Z');

    expect(formatOfferProtectionPeriod(referenceDate)).toBe('2025/01/15 00:00 - 2026/01/14 23:59');
  });

  it('should derive the current day using the CET calendar boundary', () => {
    const referenceDate = new Date('2025-01-14T23:30:00.000Z');

    expect(formatOfferProtectionPeriod(referenceDate)).toBe('2025/01/15 00:00 - 2026/01/14 23:59');
  });

  it('should handle year rollover when the CET day is already in the next year', () => {
    const referenceDate = new Date('2024-12-31T23:30:00.000Z');

    expect(formatOfferProtectionPeriod(referenceDate)).toBe('2025/01/01 00:00 - 2025/12/31 23:59');
  });

  it('should keep the +1 year - 1 minute rule across leap-year boundaries', () => {
    const referenceDate = new Date('2024-02-29T10:00:00.000Z');

    expect(formatOfferProtectionPeriod(referenceDate)).toBe('2024/02/29 00:00 - 2025/02/28 23:59');
  });
});
