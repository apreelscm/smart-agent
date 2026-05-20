export type PeselValidationError = 'required' | 'format' | 'length' | 'checksum' | 'date';

export interface PeselValidationResult {
  isValid: boolean;
  dateOfBirth: string | null;
  errorCode: PeselValidationError | null;
}

const PESEL_LENGTH = 11;
const CHECKSUM_WEIGHTS = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3] as const;

export function isPeselNumeric(pesel: string): boolean {
  return /^\d+$/.test(pesel);
}

export function isPeselChecksumValid(pesel: string): boolean {
  if (!isPeselNumeric(pesel) || pesel.length !== PESEL_LENGTH) {
    return false;
  }

  const digits = pesel.split('').map(Number);
  const checksum = CHECKSUM_WEIGHTS.reduce(
    (sum, weight, index) => sum + digits[index] * weight,
    0
  );

  const controlDigit = (10 - (checksum % 10)) % 10;

  return controlDigit === digits[10];
}

export function decodeBirthDateFromPesel(pesel: string): string | null {
  if (!isPeselNumeric(pesel) || pesel.length !== PESEL_LENGTH) {
    return null;
  }

  const yearPart = Number(pesel.slice(0, 2));
  const encodedMonth = Number(pesel.slice(2, 4));
  const day = Number(pesel.slice(4, 6));

  const centuryData = decodeCentury(encodedMonth);

  if (!centuryData) {
    return null;
  }

  const year = centuryData.century + yearPart;
  const month = centuryData.month;
  const date = new Date(Date.UTC(year, month - 1, day));

  if (
    date.getUTCFullYear() !== year ||
    date.getUTCMonth() !== month - 1 ||
    date.getUTCDate() !== day
  ) {
    return null;
  }

  return `${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day
    .toString()
    .padStart(2, '0')}`;
}

export function validatePesel(pesel: string): PeselValidationResult {
  if (!pesel) {
    return invalidResult('required');
  }

  if (!isPeselNumeric(pesel)) {
    return invalidResult('format');
  }

  if (pesel.length !== PESEL_LENGTH) {
    return invalidResult('length');
  }

  if (!isPeselChecksumValid(pesel)) {
    return invalidResult('checksum');
  }

  const dateOfBirth = decodeBirthDateFromPesel(pesel);

  if (!dateOfBirth) {
    return invalidResult('date');
  }

  return {
    isValid: true,
    dateOfBirth,
    errorCode: null,
  };
}

function invalidResult(errorCode: PeselValidationError): PeselValidationResult {
  return {
    isValid: false,
    dateOfBirth: null,
    errorCode,
  };
}

function decodeCentury(encodedMonth: number): { century: number; month: number } | null {
  if (encodedMonth >= 1 && encodedMonth <= 12) {
    return { century: 1900, month: encodedMonth };
  }

  if (encodedMonth >= 21 && encodedMonth <= 32) {
    return { century: 2000, month: encodedMonth - 20 };
  }

  if (encodedMonth >= 41 && encodedMonth <= 52) {
    return { century: 2100, month: encodedMonth - 40 };
  }

  if (encodedMonth >= 61 && encodedMonth <= 72) {
    return { century: 2200, month: encodedMonth - 60 };
  }

  if (encodedMonth >= 81 && encodedMonth <= 92) {
    return { century: 1800, month: encodedMonth - 80 };
  }

  return null;
}
