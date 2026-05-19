export type PeselValidationError = 'required' | 'digitsOnly' | 'length' | 'checksum' | 'birthDate';

export interface PeselValidationResult {
  valid: boolean;
  error: PeselValidationError | null;
  birthDate: string | null;
}

const PESEL_WEIGHTS = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3] as const;

export function validatePesel(pesel: string): PeselValidationResult {
  if (!pesel) {
    return { valid: false, error: 'required', birthDate: null };
  }

  if (!/^\d+$/.test(pesel)) {
    return { valid: false, error: 'digitsOnly', birthDate: null };
  }

  if (pesel.length !== 11) {
    return { valid: false, error: 'length', birthDate: null };
  }

  if (!hasValidChecksum(pesel)) {
    return { valid: false, error: 'checksum', birthDate: null };
  }

  const birthDate = decodeBirthDateFromPesel(pesel);

  if (!birthDate) {
    return { valid: false, error: 'birthDate', birthDate: null };
  }

  return { valid: true, error: null, birthDate };
}

export function decodeBirthDateFromPesel(pesel: string): string | null {
  if (!/^\d{11}$/.test(pesel)) {
    return null;
  }

  const yearPart = Number(pesel.slice(0, 2));
  const encodedMonth = Number(pesel.slice(2, 4));
  const day = Number(pesel.slice(4, 6));
  const decodedDate = decodeCenturyAndMonth(yearPart, encodedMonth);

  if (!decodedDate) {
    return null;
  }

  const { year, month } = decodedDate;
  const date = new Date(Date.UTC(year, month - 1, day));

  if (
    date.getUTCFullYear() !== year ||
    date.getUTCMonth() !== month - 1 ||
    date.getUTCDate() !== day
  ) {
    return null;
  }

  return [
    year.toString().padStart(4, '0'),
    month.toString().padStart(2, '0'),
    day.toString().padStart(2, '0'),
  ].join('-');
}

function hasValidChecksum(pesel: string): boolean {
  const checksum = PESEL_WEIGHTS.reduce((sum, weight, index) => {
    return sum + Number(pesel[index]) * weight;
  }, 0);

  const controlDigit = (10 - (checksum % 10)) % 10;
  return controlDigit === Number(pesel[10]);
}

function decodeCenturyAndMonth(
  yearPart: number,
  encodedMonth: number,
): { year: number; month: number } | null {
  if (encodedMonth >= 1 && encodedMonth <= 12) {
    return { year: 1900 + yearPart, month: encodedMonth };
  }

  if (encodedMonth >= 21 && encodedMonth <= 32) {
    return { year: 2000 + yearPart, month: encodedMonth - 20 };
  }

  if (encodedMonth >= 41 && encodedMonth <= 52) {
    return { year: 2100 + yearPart, month: encodedMonth - 40 };
  }

  if (encodedMonth >= 61 && encodedMonth <= 72) {
    return { year: 2200 + yearPart, month: encodedMonth - 60 };
  }

  if (encodedMonth >= 81 && encodedMonth <= 92) {
    return { year: 1800 + yearPart, month: encodedMonth - 80 };
  }

  return null;
}
