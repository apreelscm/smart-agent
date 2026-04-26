import { getCoveragePeriod, getCoveragePeriodLabel } from './coverage-period.util'

describe('coverage-period.util', () => {
  it('builds the Jira example range for the current business day', () => {
    expect(getCoveragePeriodLabel(new Date('2025-05-10T10:15:00.000Z'))).toBe('2025/05/10 00:00 - 2026/05/09 23:59')
  })

  it('uses the Europe/Warsaw business date when the UTC day differs', () => {
    expect(getCoveragePeriodLabel(new Date('2025-05-09T22:30:00.000Z'))).toBe('2025/05/10 00:00 - 2026/05/09 23:59')
  })

  it('resolves leap-day starts to the day before the rolled yearly anniversary', () => {
    const period = getCoveragePeriod(new Date('2024-02-29T12:00:00.000Z'))

    expect(period.start).toEqual({
      year: 2024,
      month: 2,
      day: 29,
      hour: 0,
      minute: 0
    })
    expect(period.end).toEqual({
      year: 2025,
      month: 2,
      day: 28,
      hour: 23,
      minute: 59
    })
    expect(period.label).toBe('2024/02/29 00:00 - 2025/02/28 23:59')
  })

  it('keeps the required format across year rollover', () => {
    const label = getCoveragePeriodLabel(new Date('2025-12-31T08:00:00.000Z'))

    expect(label).toBe('2025/12/31 00:00 - 2026/12/30 23:59')
    expect(label).toMatch(/^\d{4}\/\d{2}\/\d{2} \d{2}:\d{2} - \d{4}\/\d{2}\/\d{2} \d{2}:\d{2}$/)
  })
})
