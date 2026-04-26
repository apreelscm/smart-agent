import { getCoveragePeriodLabel } from './coverage-period.util';

describe('getCoveragePeriodLabel', () => {
  it('uses the current Europe/Warsaw business date instead of raw UTC date', () => {
    const now = new Date('2025-01-01T23:30:00.000Z');

    expect(getCoveragePeriodLabel(now)).toBe('2025/01/02 00:00 - 2026/01/01 23:59');
  });

  it('handles leap day by ending on the day before the rolled yearly anniversary', () => {
    const now = new Date('2024-02-29T10:15:00.000Z');

    expect(getCoveragePeriodLabel(now)).toBe('2024/02/29 00:00 - 2025/02/28 23:59');
  });

  it('keeps formatting correct across year rollover', () => {
    const now = new Date('2024-12-31T23:10:00.000Z');

    expect(getCoveragePeriodLabel(now)).toBe('2025/01/01 00:00 - 2025/12/31 23:59');
  });
});
