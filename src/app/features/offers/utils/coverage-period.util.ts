export const COVERAGE_PERIOD_BUSINESS_TIME_ZONE = 'Europe/Warsaw'

type CoveragePeriodBoundary = {
  year: number
  month: number
  day: number
  hour: number
  minute: number
}

export type CoveragePeriod = {
  start: CoveragePeriodBoundary
  end: CoveragePeriodBoundary
  label: string
}

export function getCoveragePeriod(
  now: Date = new Date(),
  timeZone: string = COVERAGE_PERIOD_BUSINESS_TIME_ZONE
): CoveragePeriod {
  const businessDate = getBusinessDate(now, timeZone)
  const endDate = new Date(Date.UTC(businessDate.year, businessDate.month - 1, businessDate.day))

  endDate.setUTCFullYear(endDate.getUTCFullYear() + 1)
  endDate.setUTCDate(endDate.getUTCDate() - 1)

  const start: CoveragePeriodBoundary = {
    ...businessDate,
    hour: 0,
    minute: 0
  }

  const end: CoveragePeriodBoundary = {
    year: endDate.getUTCFullYear(),
    month: endDate.getUTCMonth() + 1,
    day: endDate.getUTCDate(),
    hour: 23,
    minute: 59
  }

  return {
    start,
    end,
    label: `${formatBoundary(start)} - ${formatBoundary(end)}`
  }
}

export function getCoveragePeriodLabel(
  now: Date = new Date(),
  timeZone: string = COVERAGE_PERIOD_BUSINESS_TIME_ZONE
): string {
  return getCoveragePeriod(now, timeZone).label
}

function getBusinessDate(now: Date, timeZone: string): Omit<CoveragePeriodBoundary, 'hour' | 'minute'> {
  const formatter = new Intl.DateTimeFormat('en-CA', {
    timeZone,
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
  const parts = formatter.formatToParts(now)

  return {
    year: getNumericPart(parts, 'year'),
    month: getNumericPart(parts, 'month'),
    day: getNumericPart(parts, 'day')
  }
}

function getNumericPart(parts: Intl.DateTimeFormatPart[], type: Intl.DateTimeFormatPartTypes): number {
  const value = parts.find((part) => part.type === type)?.value

  if (!value) {
    throw new Error(`Missing "${type}" part while formatting coverage period`)
  }

  return Number.parseInt(value, 10)
}

function formatBoundary(boundary: CoveragePeriodBoundary): string {
  return `${boundary.year}/${pad(boundary.month)}/${pad(boundary.day)} ${pad(boundary.hour)}:${pad(boundary.minute)}`
}

function pad(value: number): string {
  return value.toString().padStart(2, '0')
}
