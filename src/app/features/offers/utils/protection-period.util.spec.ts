import { formatProtectionPeriod } from './protection-period.util';

describe('formatProtectionPeriod', () => {
  it('formats the Jira example exactly', () => {
    expect(formatProtectionPeriod(new Date('2025-05-10T08:15:00Z'))).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  });

  it('uses Warsaw civil date derivation for timezone boundary instants', () => {
    expect(formatProtectionPeriod(new Date('2025-05-09T22:30:00Z'))).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  });

  it('handles leap-year rollover with civil-date arithmetic', () => {
    expect(formatProtectionPeriod(new Date('2024-02-29T12:00:00Z'))).toBe('2024/02/29 00:00 - 2025/02/28 23:59');
  });

  it('always returns the exact required display format', () => {
    expect(formatProtectionPeriod(new Date('2026-12-31T11:45:00Z'))).toMatch(/^\d{4}\/\d{2}\/\d{2} 00:00 - \d{4}\/\d{2}\/\d{2} 23:59$/);
  });
});
