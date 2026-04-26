import {
  buildOfferProtectionPeriodLabel,
  getMillisecondsUntilNextOfferProtectionPeriodRefresh
} from './offer-protection-period.util';

describe('offerProtectionPeriodUtil', () => {
  it('builds the accepted protection-period example', () => {
    const label = buildOfferProtectionPeriodLabel(new Date('2025-05-10T08:15:00+02:00'));

    expect(label).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  });

  it('uses the Europe/Warsaw business day independently from the client timezone', () => {
    const label = buildOfferProtectionPeriodLabel(new Date('2025-05-09T22:30:00Z'));

    expect(label).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  });

  it('handles the leap-day boundary with add-one-year-then-subtract-one-day logic', () => {
    const label = buildOfferProtectionPeriodLabel(new Date('2024-02-29T12:00:00Z'));

    expect(label).toBe('2024/02/29 00:00 - 2025/02/28 23:59');
  });

  it('calculates a positive delay until the next Warsaw midnight refresh', () => {
    const delay = getMillisecondsUntilNextOfferProtectionPeriodRefresh(new Date('2025-05-10T21:30:00Z'));

    expect(delay).toBe(1_800_000);
  });
});
