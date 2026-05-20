const PESEL_WEIGHTS = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3] as const;

function pad(value: number): string {
  return String(value).padStart(2, '0');
}

function isValidCalendarDate(year: number, month: number, day: number): boolean {
  const candidate = new Date(Date.UTC(year, month - 1, day));

  return candidate.getUTCFullYear() === year
    && candidate.getUTCMonth() === month - 1
    && candidate.getUTCDate() === day;
}

function decodeCentury(encodedMonth: number): { yearPrefix: number; month: number } | null {
  if (encodedMonth >= 1 && encodedMonth <= 12) {
    return { yearPrefix: 1900, month: encodedMonth };
  }

  if (encodedMonth >= 21 && encodedMonth <= 32) {
    return { yearPrefix: 2000, month: encodedMonth - 20 };
  }

  if (encodedMonth >= 41 && encodedMonth <= 52) {
    return { yearPrefix: 2100, month: encodedMonth - 40 };
  }

  if (encodedMonth >= 61 && encodedMonth <= 72) {
    return { yearPrefix: 2200, month: encodedMonth - 60 };
  }

  if (encodedMonth >= 81 && encodedMonth <= 92) {
    return { yearPrefix: 1800, month: encodedMonth - 80 };
  }

  return null;
}

export function sanitizePeselInput(value: string): string {
  return value.replace(/\D/g, '');
}

export function isPeselChecksumValid(pesel: string): boolean {
  if (!/^\d{11}$/.test(pesel)) {
    return false;
  }

  const checksum = PESEL_WEIGHTS.reduce(
    (sum, weight, index) => sum + Number(pesel[index]) * weight,
    0
  );

  const controlDigit = (10 - (checksum % 10)) % 10;

  return controlDigit === Number(pesel[10]);
}

export function decodeDateOfBirthFromPesel(pesel: string): string | null {
  if (!/^\d{11}$/.test(pesel)) {
    return null;
  }

  const yearSuffix = Number(pesel.slice(0, 2));
  const encodedMonth = Number(pesel.slice(2, 4));
  const day = Number(pesel.slice(4, 6));
  const decodedCentury = decodeCentury(encodedMonth);

  if (!decodedCentury) {
    return null;
  }

  const year = decodedCentury.yearPrefix + yearSuffix;
  const month = decodedCentury.month;

  if (!isValidCalendarDate(year, month, day)) {
    return null;
  }

  return `${year}-${pad(month)}-${pad(day)}`;
}

export function validateAndDecodePesel(value: string): string | null {
  const pesel = sanitizePeselInput(value);

  if (pesel.length !== 11) {
    return null;
  }

  if (!isPeselChecksumValid(pesel)) {
    return null;
  }

  return decodeDateOfBirthFromPesel(pesel);
}
