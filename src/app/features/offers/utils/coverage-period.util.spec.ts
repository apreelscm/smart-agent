import {
  calculateCoveragePeriod,
  formatCoveragePeriod,
  getBusinessDateInTimeZone,
  getCoveragePeriodLabel,
  POLAND_BUSINESS_TIME_ZONE
} from './coverage-period.util';

describe('coverage-period.util', () => {
  it('formats the Jira example coverage period', () => {
    const coveragePeriod = calculateCoveragePeriod({
      year: 2025,
      month: 5,
      day: 10
    });

    expect(formatCoveragePeriod(coveragePeriod)).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  });

  it('uses Poland business date instead of the UTC calendar day', () => {
    const businessDate = getBusinessDateInTimeZone(new Date('2025-05-09T22:30:00.000Z'), POLAND_BUSINESS_TIME_ZONE);

    expect(businessDate).toEqual({
      year: 2025,
      month: 5,
      day: 10
    });
  });

  it('formats boundary times in exact 24-hour form', () => {
    const label = getCoveragePeriodLabel(new Date('2025-12-31T23:30:00.000Z'), POLAND_BUSINESS_TIME_ZONE);

    expect(label).toBe('2026/01/01 00:00 - 2026/12/31 23:59');
  });

  it('handles leap-year boundaries by subtracting one day from the next-year anniversary', () => {
    const coveragePeriod = calculateCoveragePeriod({
      year: 2024,
      month: 2,
      day: 29
    });

    expect(formatCoveragePeriod(coveragePeriod)).toBe('2024/02/29 00:00 - 2025/02/28 23:59');
  });
});
